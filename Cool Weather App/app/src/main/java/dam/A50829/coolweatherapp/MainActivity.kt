package dam.A50829.coolweatherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.gson.Gson
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Variáveis para os componentes da UI
    private lateinit var txtBigTemp: TextView
    private lateinit var txtPressure: TextView
    private lateinit var txtWindSpeed: TextView
    private lateinit var txtWindDir: TextView
    private lateinit var txtTime: TextView
    private lateinit var editLatitude: EditText
    private lateinit var editLongitude: EditText
    private lateinit var imgWeatherIcon: ImageView
    private lateinit var btnUpdate: Button

    companion object {
        var day: Boolean = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        applyWeatherTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar componentes com findViewById
        txtBigTemp = findViewById(R.id.txtBigTemp)
        txtPressure = findViewById(R.id.txtPressure)
        txtWindSpeed = findViewById(R.id.txtWindSpeed)
        txtWindDir = findViewById(R.id.txtWindDir)
        txtTime = findViewById(R.id.txtTime)
        editLatitude = findViewById(R.id.editLatitude)
        editLongitude = findViewById(R.id.editLongitude)
        imgWeatherIcon = findViewById(R.id.imgWeatherIcon)
        btnUpdate = findViewById(R.id.btnUpdate)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Recuperar dados se a Activity foi reiniciada por causa do tema
        val jsonWeather = intent.getStringExtra("weather_data")
        if (jsonWeather != null) {
            editLatitude.setText(intent.getStringExtra("lat_persist"))
            editLongitude.setText(intent.getStringExtra("long_persist"))
            val weather = Gson().fromJson(jsonWeather, WeatherData::class.java)
            updateUI(weather)
        } else {
            getLastLocation()
        }

        btnUpdate.setOnClickListener {
            val lat = editLatitude.text.toString().toFloatOrNull()
            val lon = editLongitude.text.toString().toFloatOrNull()
            if (lat != null && lon != null) {
                fetchWeather(lat, lon)
            }
        }
    }

    // Lógica da API que estava no ViewModel agora volta para aqui
    private fun fetchWeather(lat: Float, long: Float) {
        thread {
            try {
                val urlString = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$long&current_weather=true&hourly=temperature_2m,weathercode,pressure_msl,windspeed_10m&timezone=auto"
                val jsonString = URL(urlString).readText()
                val weather = Gson().fromJson(jsonString, WeatherData::class.java)

                runOnUiThread {
                    val isDayNow = weather.current_weather.is_day == 1
                    if (isDayNow != day) {
                        day = isDayNow
                        restartActivityWithNewTheme(weather)
                    } else {
                        updateUI(weather)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUI(request: WeatherData) {
        txtBigTemp.text = "${request.current_weather.temperature} ºC"
        txtPressure.text = "${request.hourly.pressure_msl[12]} hPa"
        txtWindSpeed.text = "${request.current_weather.windspeed} km/h"
        txtWindDir.text = "${request.current_weather.winddirection}º"

        val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        timeFormat.timeZone = TimeZone.getTimeZone(request.timezone)
        txtTime.text = timeFormat.format(Date())

        updateWeatherIcon(request.current_weather.weathercode)
    }

    private fun updateWeatherIcon(code: Int) {
        val codesArray = resources.getStringArray(R.array.weather_codes_array)
        var imageName = "clear_"
        for (item in codesArray) {
            val parts = item.split("|")
            if (parts[0].toInt() == code) {
                imageName = parts[2]
                break
            }
        }
        val suffix = if (day) "day" else "night"
        val finalImg = if (imageName.endsWith("_")) "$imageName$suffix" else imageName
        val resID = resources.getIdentifier(finalImg, "drawable", packageName)
        if (resID != 0) imgWeatherIcon.setImageResource(resID)
    }

    private fun applyWeatherTheme() {
        val isLand = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLand) {
            setTheme(if (day) R.style.Theme_Day_Land else R.style.Theme_Night_Land)
        } else {
            setTheme(if (day) R.style.Theme_Day else R.style.Theme_Night)
        }
    }

    private fun restartActivityWithNewTheme(weather: WeatherData) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("weather_data", Gson().toJson(weather))
            putExtra("lat_persist", editLatitude.text.toString())
            putExtra("long_persist", editLongitude.text.toString())
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(intent)
        finish()
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            fetchWeather(38.7071f, -9.1355f)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                editLatitude.setText(loc.latitude.toString())
                editLongitude.setText(loc.longitude.toString())
                fetchWeather(loc.latitude.toFloat(), loc.longitude.toFloat())
            } else {
                fetchWeather(38.7071f, -9.1355f)
            }
        }
    }
}