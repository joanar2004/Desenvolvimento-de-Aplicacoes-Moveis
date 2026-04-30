package dam.a50829.coolweatherapp.data

// Ktor é a biblioteca que usamos para fazer pedidos HTTP (chamar APIs)
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// "object" em Kotlin é um Singleton — existe apenas UMA instância desta classe
// em toda a aplicação. É perfeito para clientes HTTP que devem ser partilhados.
object WeatherApiClient {

    // Criamos o cliente HTTP do Ktor
    // O bloco { } depois de HttpClient é uma "lambda" de configuração
    private val client = HttpClient {

        // "install" adiciona funcionalidades (plugins) ao cliente
        // ContentNegotiation trata da conversão automática JSON <-> Kotlin
        install(ContentNegotiation) {

            // Dizemos que vamos usar JSON como formato de dados
            json(Json {
                // Formata o JSON de forma legível (útil para debug)
                prettyPrint = true

                // Aceita JSON menos estrito (ex: strings sem aspas)
                isLenient = true

                // MUITO IMPORTANTE: ignora campos do JSON que não temos
                // nas nossas data classes. Sem isto, a app crashava se a
                // API devolvesse campos extra que não mapeámos.
                ignoreUnknownKeys = true
            })
        }
    }

    // "suspend" significa que esta função é assíncrona — pode ser pausada
    // enquanto espera pela resposta da API, sem bloquear a app.
    // Só pode ser chamada dentro de uma coroutine (ex: viewModelScope.launch)
    suspend fun getWeather(lat: Float, lon: Float): WeatherData? {

        // Construímos o URL do pedido com os parâmetros de latitude e longitude
        // "$lat" é template string do Kotlin — insere o valor da variável
        val url = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=$lat&longitude=$lon&" +
                "current_weather=true&" +
                "hourly=temperature_2m,weathercode,pressure_msl,windspeed_10m"

        // "return try/catch" — tentamos fazer o pedido, e se falhar
        // (sem internet, API em baixo, etc.) devolvemos null em vez de crashar
        return try {
            // client.get() faz um pedido HTTP GET ao URL
            // .body() converte automaticamente o JSON da resposta
            // para o nosso objeto WeatherData (graças ao ContentNegotiation)
            client.get(url).body()
        } catch (e: Exception) {
            // Se der erro, imprimimos o erro no Logcat para debug
            e.printStackTrace()
            // Devolvemos null para indicar que não foi possível obter dados
            // O "?" no tipo de retorno "WeatherData?" significa que pode ser null
            null
        }
    }
}