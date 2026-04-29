package dam.A50829.coolweatherapp.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import dam.A50829.coolweatherapp.data.WeatherApiClient
import dam.A50829.coolweatherapp.ui.WeatherUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(WeatherUIState())
    val uiState: StateFlow<WeatherUIState> = _uiState.asStateFlow()

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(application)

    fun updateLatitude(newLat: Float) {
        _uiState.update { it.copy(latitude = newLat) }
    }

    fun updateLongitude(newLon: Float) {
        _uiState.update { it.copy(longitude = newLon) }
    }

    fun fetchWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = WeatherApiClient.getWeather(
                lat = _uiState.value.latitude,
                lon = _uiState.value.longitude
            )

            if (result != null) {
                val cw = result.currentWeather
                val pressure = result.hourly.pressureMsl.getOrElse(12) { 0.0 }

                _uiState.update {
                    it.copy(
                        temperature = cw.temperature,
                        windspeed = cw.windspeed,
                        winddirection = cw.winddirection,
                        weathercode = cw.weathercode,
                        isDay = cw.isDay == 1,
                        seaLevelPressure = pressure,
                        time = cw.time,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Erro ao carregar dados")
                }
            }
        }
    }

    fun fetchWeatherFromLastLocation(onPermissionNeeded: () -> Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                if (loc != null) {
                    _uiState.update {
                        it.copy(
                            latitude = loc.latitude.toFloat(),
                            longitude = loc.longitude.toFloat()
                        )
                    }
                }
                fetchWeather()
            }
        } catch (e: SecurityException) {
            onPermissionNeeded()
            fetchWeather()
        }
    }
}