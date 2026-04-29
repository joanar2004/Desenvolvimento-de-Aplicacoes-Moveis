package dam.A50829.coolweatherapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    val latitude: Float = 0f,
    val longitude: Float = 0f,
    val timezone: String = "",
    @SerialName("utc_offset_seconds")
    val utcOffsetSeconds: Int = 0,
    @SerialName("current_weather")
    val currentWeather: CurrentWeather = CurrentWeather(),
    val hourly: Hourly = Hourly()
)

@Serializable
data class CurrentWeather(
    val temperature: Float = 0f,
    val windspeed: Float = 0f,
    val winddirection: Int = 0,
    val weathercode: Int = 0,
    @SerialName("is_day")
    val isDay: Int = 1,
    val time: String = ""
)

@Serializable
data class Hourly(
    val time: List<String> = emptyList(),
    @SerialName("temperature_2m")
    val temperature2m: List<Float> = emptyList(),
    val weathercode: List<Int> = emptyList(),
    @SerialName("pressure_msl")
    val pressureMsl: List<Double> = emptyList()
)