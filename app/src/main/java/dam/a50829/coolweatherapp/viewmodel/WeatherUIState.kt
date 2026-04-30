package dam.a50829.coolweatherapp.viewmodel

// Esta data class representa o ESTADO COMPLETO da interface gráfica
// O ViewModel vai manter uma instância desta classe atualizada,
// e a UI vai redesenhar-se sempre que o estado mudar.

// Pensa nisto como um "snapshot" de tudo o que a UI precisa de mostrar
// num determinado momento.
data class WeatherUIState(

    // Coordenadas atuais — têm valores padrão (Lisboa)
    // "= 38.7223f" é o valor inicial se não for passado nenhum
    // O "f" no fim indica que é um Float (sem "f" seria Double)
    val latitude: Float = 38.7223f,
    val longitude: Float = -9.1393f,

    // Dados meteorológicos — começam todos a zero
    // Serão atualizados quando a API responder
    val temperature: Float = 0f,
    val windspeed: Float = 0f,
    val winddirection: Int = 0,
    val weathercode: Int = 0,
    val seaLevelPressure: Float = 0f,

    // Hora da última atualização — começa vazia
    val time: String = ""
)