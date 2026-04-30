package dam.a50829.coolweatherapp.viewmodel

// ViewModel é uma classe do Android Jetpack que sobrevive a rotações do ecrã
// Sem ViewModel, ao rodar o telemóvel a Activity reiniciava e perdias os dados
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dam.a50829.coolweatherapp.data.WeatherApiClient

// kotlinx.coroutines permite fazer operações assíncronas (ex: chamar APIs)
// sem bloquear a interface gráfica
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Herdamos de ViewModel() para ter acesso ao viewModelScope e
// à sobrevivência a rotações de ecrã
class WeatherViewModel : ViewModel() {

    // MutableStateFlow é um "contentor" de estado que:
    // 1. Guarda o valor atual do estado
    // 2. Notifica automaticamente quem o estiver a observar quando muda
    // "private" significa que só o ViewModel pode modificar este valor
    private val _uiState = MutableStateFlow(WeatherUIState())

    // StateFlow é a versão "só de leitura" do MutableStateFlow
    // Exposta para a UI — a UI pode LER mas não pode ESCREVER
    // Isto é uma boa prática: só o ViewModel controla o estado
    val uiState: StateFlow<WeatherUIState> = _uiState

    // Função chamada pela UI quando o utilizador muda a latitude
    // Usamos .copy() para criar uma NOVA instância do estado
    // com apenas a latitude alterada — os outros campos ficam iguais
    // Em Kotlin, data classes são imutáveis, por isso criamos sempre
    // uma cópia em vez de modificar diretamente
    fun updateLatitude(lat: Float) {
        _uiState.value = _uiState.value.copy(latitude = lat)
    }

    // Mesmo conceito para a longitude
    fun updateLongitude(lon: Float) {
        _uiState.value = _uiState.value.copy(longitude = lon)
    }

    // Função chamada quando o utilizador carrega no botão "Update Weather"
    fun fetchWeather() {

        // viewModelScope.launch cria uma coroutine — um bloco de código
        // que corre de forma assíncrona sem bloquear a UI
        // Quando a app fechar, o viewModelScope cancela a coroutine automaticamente
        viewModelScope.launch {

            // Chamamos a API com as coordenadas atuais do estado
            // Esta chamada é "suspend" — pausa aqui até ter resposta,
            // mas sem bloquear a thread principal (UI continua responsiva)
            val data = WeatherApiClient.getWeather(
                _uiState.value.latitude,
                _uiState.value.longitude
            )

            // "data?.let { }" só executa o bloco se "data" não for null
            // É a forma segura de trabalhar com valores que podem ser null em Kotlin
            data?.let {

                // Atualizamos o estado com os novos dados da API
                // Usamos .copy() novamente para manter os campos que não mudaram
                // (latitude e longitude ficam iguais, só os dados meteo mudam)
                _uiState.value = _uiState.value.copy(
                    temperature = it.currentWeather.temperature,
                    windspeed = it.currentWeather.windspeed,
                    winddirection = it.currentWeather.winddirection,
                    weathercode = it.currentWeather.weathercode,
                    time = it.currentWeather.time,
                    // firstOrNull() devolve o primeiro elemento da lista,
                    // ou null se a lista estiver vazia
                    // ?: é o "Elvis operator" — se for null usa 0f
                    seaLevelPressure = it.hourly.pressureMsl.firstOrNull() ?: 0f
                )
            }
        }
    }
}