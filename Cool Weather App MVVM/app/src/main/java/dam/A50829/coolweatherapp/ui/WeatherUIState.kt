package dam.A50829.coolweatherapp.ui

data class WeatherUIState(
    val latitude: Float = 38.7071f,
    val longitude: Float = -9.1355f,
    val temperature: Float = 0f,
    val windspeed: Float = 0f,
    val winddirection: Int = 0,
    val weathercode: Int = 0,
    val isDay: Boolean = true,
    val seaLevelPressure: Double = 0.0,
    val time: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)