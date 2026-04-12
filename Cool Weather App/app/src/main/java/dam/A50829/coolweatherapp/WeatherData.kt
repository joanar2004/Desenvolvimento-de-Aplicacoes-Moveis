package dam.A50829.coolweatherapp

// Classe principal que recebe o JSON da API
data class WeatherData(
    var latitude: String,
    var longitude: String,
    var timezone: String,
    var utc_offset_seconds: Int,
    var current_weather: CurrentWeather,
    var hourly: Hourly
)

// Dados do tempo atual
data class CurrentWeather(
    var temperature: Float,
    var windspeed: Float,
    var winddirection: Int,
    var weathercode: Int,
    var is_day: Int, // 1 para dia, 0 para noite (vem da API)
    var time: String // Hora local formatada (ex: "2026-04-08T18:30")
)
// Dados por hora (para irmos buscar a pressão)
data class Hourly(
    var time: ArrayList<String>,
    var temperature_2m: ArrayList<Float>,
    var weathercode: ArrayList<Int>,
    var pressure_msl: ArrayList<Double>
)

data class WeatherInfo(
    val description: String,
    val imageName: String
)

