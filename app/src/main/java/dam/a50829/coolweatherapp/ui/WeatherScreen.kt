package dam.a50829.coolweatherapp.ui

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam.a50829.coolweatherapp.R
import dam.a50829.coolweatherapp.viewmodel.WeatherUIState
import dam.a50829.coolweatherapp.viewmodel.WeatherViewModel

// Função que converte o weathercode num ID de recurso drawable
// Agora utiliza TODOS os drawables disponíveis para maior precisão
fun getWeatherIcon(weathercode: Int, isDay: Boolean): Int {
    return when (weathercode) {
        0 -> if (isDay) R.drawable.clear_day else R.drawable.clear_night
        1 -> if (isDay) R.drawable.mostly_clear_day else R.drawable.mostly_clear_night
        2 -> if (isDay) R.drawable.partly_cloudy_day else R.drawable.partly_cloudy_night
        3 -> R.drawable.mostly_cloudy
        45 -> R.drawable.fog
        48 -> R.drawable.fog_light
        51 -> R.drawable.drizzle
        53, 55 -> R.drawable.rain_light
        56, 57 -> R.drawable.freezing_drizzle
        61 -> R.drawable.rain_light
        63 -> R.drawable.rain
        65 -> R.drawable.rain_heavy
        66 -> R.drawable.freezing_rain_light
        67 -> R.drawable.freezing_rain_heavy
        71 -> R.drawable.snow_light
        73 -> R.drawable.snow
        75 -> R.drawable.snow_heavy
        77 -> R.drawable.flurries
        79 -> R.drawable.ice_pellets // Grãos de gelo
        80 -> R.drawable.rain_light
        81 -> R.drawable.rain
        82 -> R.drawable.rain_heavy
        85 -> R.drawable.snow_light
        86 -> R.drawable.snow_heavy
        95, 96, 99 -> R.drawable.tstorm
        else -> R.drawable.cloudy // Fallback para nublado se for desconhecido
    }
}

// Função que escolhe as cores do gradiente consoante o weathercode e hora do dia
fun getGradientColors(weathercode: Int, isDay: Boolean): List<Color> {
    if (!isDay) return listOf(Color(0xFF0D1B2A), Color(0xFF1B2A4A))

    return when (weathercode) {
        0 -> listOf(Color(0xFF1E88E5), Color(0xFF64B5F6))          // Sol
        1, 2 -> listOf(Color(0xFF1976D2), Color(0xFF90CAF9))       // Parcialmente nublado
        3 -> listOf(Color(0xFF546E7A), Color(0xFFB0BEC5))          // Nublado
        45, 48 -> listOf(Color(0xFF607D8B), Color(0xFFCFD8DC))     // Nevoeiro
        51, 53, 55, 61, 63, 65, 80, 81, 82 -> listOf(Color(0xFF1565C0), Color(0xFF5E92F3)) // Chuva
        71, 73, 75, 85, 86 -> listOf(Color(0xFF4FC3F7), Color(0xFFE1F5FE)) // Neve
        95, 96, 99 -> listOf(Color(0xFF37474F), Color(0xFF78909C)) // Trovoada
        else -> listOf(Color(0xFF1E88E5), Color(0xFF64B5F6))
    }
}

@Composable
fun WeatherUI(weatherViewModel: WeatherViewModel = viewModel()) {
    val uiState by weatherViewModel.uiState.collectAsState()
    val gradientColors = getGradientColors(uiState.weathercode, uiState.isDay)
    val configuration = LocalConfiguration.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors = gradientColors)
            )
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else {
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LandscapeWeatherUI(
                    uiState = uiState,
                    onLatitudeChange = { newValue ->
                        newValue.toFloatOrNull()?.let { weatherViewModel.updateLatitude(it) }
                    },
                    onLongitudeChange = { newValue ->
                        newValue.toFloatOrNull()?.let { weatherViewModel.updateLongitude(it) }
                    },
                    onUpdateButtonClick = { weatherViewModel.fetchWeather() }
                )
            } else {
                PortraitWeatherUI(
                    uiState = uiState,
                    onLatitudeChange = { newValue ->
                        newValue.toFloatOrNull()?.let { weatherViewModel.updateLatitude(it) }
                    },
                    onLongitudeChange = { newValue ->
                        newValue.toFloatOrNull()?.let { weatherViewModel.updateLongitude(it) }
                    },
                    onUpdateButtonClick = { weatherViewModel.fetchWeather() }
                )
            }
        }
    }
}

@Composable
fun WeatherHero(weathercode: Int, temperature: Float, isDay: Boolean) {
    val animatedTemp by animateFloatAsState(
        targetValue = temperature,
        animationSpec = tween(durationMillis = 600),
        label = "temperature_animation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = getWeatherIcon(weathercode, isDay)),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "%.1f°C".format(animatedTemp),
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun PortraitWeatherUI(
    uiState: WeatherUIState,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WeatherHero(
            weathercode = uiState.weathercode,
            temperature = uiState.temperature,
            isDay = uiState.isDay
        )

        CoordinatesCard(
            latitude = uiState.latitude,
            longitude = uiState.longitude,
            onLatitudeChange = onLatitudeChange,
            onLongitudeChange = onLongitudeChange
        )

        WeatherCard(
            temperature = uiState.temperature,
            windSpeed = uiState.windspeed,
            windDirection = uiState.winddirection,
            weathercode = uiState.weathercode,
            seaLevelPressure = uiState.seaLevelPressure,
            time = uiState.time
        )

        Button(
            onClick = onUpdateButtonClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.3f),
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.update_weather),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun LandscapeWeatherUI(
    uiState: WeatherUIState,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherHero(
                weathercode = uiState.weathercode,
                temperature = uiState.temperature,
                isDay = uiState.isDay
            )

            CoordinatesCard(
                latitude = uiState.latitude,
                longitude = uiState.longitude,
                onLatitudeChange = onLatitudeChange,
                onLongitudeChange = onLongitudeChange
            )

            Button(
                onClick = onUpdateButtonClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.3f),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.update_weather),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            WeatherCard(
                temperature = uiState.temperature,
                windSpeed = uiState.windspeed,
                windDirection = uiState.winddirection,
                weathercode = uiState.weathercode,
                seaLevelPressure = uiState.seaLevelPressure,
                time = uiState.time
            )
        }
    }
}
