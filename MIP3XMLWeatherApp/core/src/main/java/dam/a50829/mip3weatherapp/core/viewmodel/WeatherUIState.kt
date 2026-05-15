package dam.a50829.mip3weatherapp.core.viewmodel

data class WeatherUIState(
    val latitude: Float = 38.7223f,
    val longitude: Float = -9.1393f,
    val temperature: Float = 0f,
    val windspeed: Float = 0f,
    val winddirection: Float = 0f,
    val weathercode: Int = 0,
    val seaLevelPressure: Float = 0f,
    val time: String = "",
    val isLoading: Boolean = false,
    val isDay: Boolean = true
)
