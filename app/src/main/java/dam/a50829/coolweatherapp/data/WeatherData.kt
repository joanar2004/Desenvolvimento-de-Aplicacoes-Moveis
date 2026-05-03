package dam.a50829.coolweatherapp.data

// Importamos as anotações da biblioteca kotlinx.serialization
// Esta biblioteca permite converter JSON <-> objetos Kotlin automaticamente
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// @Serializable diz ao Kotlin que esta classe pode ser convertida
// de/para JSON automaticamente pela biblioteca kotlinx.serialization
@Serializable
// "data class" é um tipo especial de classe em Kotlin para guardar dados
// Gera automaticamente: equals(), hashCode(), toString(), copy()
// Os parâmetros do construtor são as propriedades da classe
data class WeatherData(
    // @SerialName diz que no JSON este campo chama-se "current_weather"
    // mas no Kotlin chamamos-lhe "currentWeather" (convenção camelCase)
    @SerialName("current_weather") val currentWeather: CurrentWeather,

    // "val" significa que é imutável (não pode ser alterado depois de criado)
    // O tipo é outra data class que definimos abaixo
    @SerialName("hourly") val hourly: HourlyData
)

@Serializable
data class CurrentWeather(
    // Float é um número decimal de precisão simples (ex: 14.5)
    val temperature: Float,
    val windspeed: Float,

    // Int é um número inteiro (ex: 296)
    val winddirection: Int,
    val weathercode: Int,

    // String é texto (ex: "2025-03-26T14:45")
    val time: String,

    // A API devolve 1 se for dia e 0 se for noite
    // "= 1" é o valor padrão caso a API não devolva este campo
    // @SerialName mapeia o campo "is_day" do JSON para "isDay" no Kotlin
    @SerialName("is_day") val isDay: Int = 1
)

@Serializable
data class HourlyData(
    // List<Float> é uma lista de números decimais
    // O JSON devolve vários valores horários, por isso é uma lista
    // Exemplo: [1013.2, 1012.8, 1011.5, ...]
    @SerialName("pressure_msl") val pressureMsl: List<Float>
)