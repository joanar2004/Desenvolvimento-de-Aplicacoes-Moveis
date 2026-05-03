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

    // Int não precisa de "f" porque é um número inteiro
    val winddirection: Int = 0,
    val weathercode: Int = 0,
    val seaLevelPressure: Float = 0f,

    // Hora da última atualização — começa vazia
    // String vazia ("") é o valor padrão em Kotlin
    val time: String = "",

    // Controla se a app está a carregar dados da API
    // Boolean só pode ser true ou false
    // Quando true — mostramos o indicador de carregamento na UI
    // Quando false — mostramos os dados normalmente
    val isLoading: Boolean = false,

    // Controla se é dia ou noite no local pesquisado
    // true = dia, false = noite
    // Usado para mostrar ícones e cores de fundo diferentes
    // Começa a true porque Lisboa de manhã é dia por defeito
    val isDay: Boolean = true
)