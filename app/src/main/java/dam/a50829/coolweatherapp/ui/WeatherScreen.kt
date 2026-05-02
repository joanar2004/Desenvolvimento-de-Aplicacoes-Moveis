package dam.a50829.coolweatherapp.ui

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam.a50829.coolweatherapp.R
import dam.a50829.coolweatherapp.viewmodel.WeatherUIState
import dam.a50829.coolweatherapp.viewmodel.WeatherViewModel

// Função que converte o weathercode num emoji representativo
// Os códigos seguem o padrão WMO (World Meteorological Organization)
fun getWeatherEmoji(weathercode: Int): String {
    return when (weathercode) {
        0 -> "☀️"           // Céu limpo
        1, 2 -> "🌤️"       // Principalmente limpo / parcialmente nublado
        3 -> "☁️"           // Nublado
        45, 48 -> "🌫️"     // Nevoeiro
        51, 53, 55 -> "🌦️" // Chuvisco
        61, 63, 65 -> "🌧️" // Chuva
        71, 73, 75 -> "❄️"  // Neve
        80, 81, 82 -> "🌨️" // Aguaceiros
        95 -> "⛈️"          // Trovoada
        96, 99 -> "⛈️"      // Trovoada com granizo
        else -> "🌡️"        // Desconhecido
    }
}

// Função que escolhe as cores do gradiente consoante o weathercode
// Devolve uma lista de duas cores para o gradiente de fundo
fun getGradientColors(weathercode: Int): List<Color> {
    return when (weathercode) {
        0 -> listOf(Color(0xFF1E88E5), Color(0xFF64B5F6))      // Azul claro — sol
        1, 2 -> listOf(Color(0xFF1976D2), Color(0xFF90CAF9))   // Azul médio — parcialmente nublado
        3 -> listOf(Color(0xFF546E7A), Color(0xFFB0BEC5))      // Cinzento — nublado
        45, 48 -> listOf(Color(0xFF607D8B), Color(0xFFCFD8DC)) // Cinzento claro — nevoeiro
        51, 53, 55,
        61, 63, 65,
        80, 81, 82 -> listOf(Color(0xFF1565C0), Color(0xFF5E92F3)) // Azul escuro — chuva
        71, 73, 75 -> listOf(Color(0xFF4FC3F7), Color(0xFFE1F5FE)) // Azul muito claro — neve
        95, 96, 99 -> listOf(Color(0xFF37474F), Color(0xFF78909C)) // Cinzento escuro — trovoada
        else -> listOf(Color(0xFF1E88E5), Color(0xFF64B5F6))   // Default — azul
    }
}

// Composable principal — é este que o MainActivity vai chamar
@Composable
fun WeatherUI(weatherViewModel: WeatherViewModel = viewModel()) {

    // collectAsState() transforma o StateFlow do ViewModel
    // numa variável que o Compose consegue observar
    val uiState by weatherViewModel.uiState.collectAsState()

    // Obtemos as cores do gradiente baseadas no weathercode atual
    val gradientColors = getGradientColors(uiState.weathercode)

    // LocalConfiguration dá-nos informação sobre o dispositivo
    val configuration = LocalConfiguration.current

    // Box permite sobrepor elementos — usamos para o gradiente de fundo
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Brush.verticalGradient cria um gradiente de cima para baixo
            // com as cores que escolhemos baseadas no tempo
            .background(
                brush = Brush.verticalGradient(colors = gradientColors)
            )
    ) {
        // Se estiver a carregar mostramos o indicador no centro
        // Caso contrário mostramos o conteúdo normal
        if (uiState.isLoading) {
            // CircularProgressIndicator é o "spinner" de loading do Material Design
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else {
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LandscapeWeatherUI(
                    uiState = uiState,
                    onLatitudeChange = { newValue ->
                        newValue.toFloatOrNull()?.let {
                            weatherViewModel.updateLatitude(it)
                        }
                    },
                    onLongitudeChange = { newValue ->
                        newValue.toFloatOrNull()?.let {
                            weatherViewModel.updateLongitude(it)
                        }
                    },
                    onUpdateButtonClick = { weatherViewModel.fetchWeather() }
                )
            } else {
                PortraitWeatherUI(
                    uiState = uiState,
                    onLatitudeChange = { newValue ->
                        newValue.toFloatOrNull()?.let {
                            weatherViewModel.updateLatitude(it)
                        }
                    },
                    onLongitudeChange = { newValue ->
                        newValue.toFloatOrNull()?.let {
                            weatherViewModel.updateLongitude(it)
                        }
                    },
                    onUpdateButtonClick = { weatherViewModel.fetchWeather() }
                )
            }
        }
    }
}

// Composable para mostrar o ícone do tempo e a temperatura em grande
@Composable
fun WeatherHero(weathercode: Int, temperature: Float) {

    // animateFloatAsState anima suavemente a temperatura quando muda
    // tween(600) significa que a animação dura 600 milissegundos
    val animatedTemp by animateFloatAsState(
        targetValue = temperature,
        animationSpec = tween(durationMillis = 600),
        label = "temperature_animation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        // Emoji do tempo em tamanho grande
        Text(
            text = getWeatherEmoji(weathercode),
            fontSize = 80.sp
        )

        // Temperatura em destaque com valor animado
        // "%.1f" formata o número com 1 casa decimal (ex: 14.5)
        Text(
            text = "%.1f°C".format(animatedTemp),
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// Layout para modo retrato (vertical)
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
        // Ícone do tempo e temperatura em grande no topo
        WeatherHero(
            weathercode = uiState.weathercode,
            temperature = uiState.temperature
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
            // Botão semi-transparente branco para combinar com o gradiente
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

// Layout para modo paisagem (horizontal)
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
        // Coluna da esquerda — ícone, temperatura, coordenadas e botão
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherHero(
                weathercode = uiState.weathercode,
                temperature = uiState.temperature
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

        // Coluna da direita — dados meteorológicos
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