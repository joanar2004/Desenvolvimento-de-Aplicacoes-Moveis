package dam.a50829.coolweatherapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam.a50829.coolweatherapp.R
import dam.a50829.coolweatherapp.viewmodel.WeatherUIState
import dam.a50829.coolweatherapp.viewmodel.WeatherViewModel

// Composable principal — é este que o MainActivity vai chamar
// viewModel() cria ou reutiliza o ViewModel automaticamente
// Se o ecrã rodar, o mesmo ViewModel é reutilizado (não perde dados)
@Composable
fun WeatherUI(weatherViewModel: WeatherViewModel = viewModel()) {

    // collectAsState() transforma o StateFlow do ViewModel
    // numa variável que o Compose consegue observar
    // Sempre que o uiState mudar, o Compose redesenha automaticamente
    // o que for necessário — isto chama-se "recomposition"
    val uiState by weatherViewModel.uiState.collectAsState()

    // LocalConfiguration dá-nos informação sobre o dispositivo
    // como orientação, tamanho do ecrã, idioma, etc.
    val configuration = LocalConfiguration.current

    // Verificamos a orientação e mostramos o layout adequado
    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeWeatherUI(
            uiState = uiState,
            onLatitudeChange = { newValue ->
                // toFloatOrNull() tenta converter a String para Float
                // Se não conseguir (ex: texto inválido) devolve null
                // ?.let { } só executa se não for null
                newValue.toFloatOrNull()?.let {
                    weatherViewModel.updateLatitude(it)
                }
            },
            onLongitudeChange = { newValue ->
                newValue.toFloatOrNull()?.let {
                    weatherViewModel.updateLongitude(it)
                }
            },
            onUpdateButtonClick = {
                weatherViewModel.fetchWeather()
            }
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
            onUpdateButtonClick = {
                weatherViewModel.fetchWeather()
            }
        )
    }
}

// Layout para modo retrato (vertical)
@Composable
fun PortraitWeatherUI(
    uiState: WeatherUIState,

    // Recebemos as lambdas do WeatherUI e passamos para os composables filhos
    // Isto chama-se "state hoisting" — o estado e os eventos sobem para cima
    // e descem como parâmetros, mantendo os composables simples e reutilizáveis
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit
) {
    // verticalScroll permite fazer scroll se o conteúdo não couber no ecrã
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.update_weather))
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
    // Row organiza os elementos lado a lado
    // Assim em landscape aproveitamos melhor o espaço horizontal
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Coluna da esquerda — coordenadas e botão
        // "weight(1f)" faz cada coluna ocupar metade do espaço disponível
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CoordinatesCard(
                latitude = uiState.latitude,
                longitude = uiState.longitude,
                onLatitudeChange = onLatitudeChange,
                onLongitudeChange = onLongitudeChange
            )
            Button(
                onClick = onUpdateButtonClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.update_weather))
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