package dam.a50829.coolweatherapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dam.a50829.coolweatherapp.R

// Este composable é responsável por mostrar todos os dados meteorológicos
// Recebe os valores como parâmetros — isto chama-se "state hoisting"
// A UI recebe dados mas não os gere, quem gere é o ViewModel
@Composable
fun WeatherCard(
    temperature: Float,
    windSpeed: Float,
    windDirection: Int,
    weathercode: Int,
    seaLevelPressure: Float,
    time: String
) {
    // Card é um componente do Material Design que desenha um retângulo
    // com cantos arredondados e sombra
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        // elevation adiciona a sombra por baixo do card
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // Usamos a cor de superfície do tema atual da app
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        // Column organiza os elementos verticalmente (um por baixo do outro)
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Reutilizamos o WeatherRow que criámos antes para cada linha
            // stringResource(R.string.xxx) busca o texto do strings.xml
            // para suportar múltiplos idiomas
            WeatherRow(
                label = stringResource(R.string.temperature),
                // "$temperature" insere o valor na string
                // \u00B0 é o símbolo de grau (°)
                value = "$temperature \u00B0C"
            )
            WeatherRow(
                label = stringResource(R.string.wind_speed),
                value = "$windSpeed km/h"
            )
            WeatherRow(
                label = stringResource(R.string.wind_direction),
                // "$windDirection°" mostra a direção em graus
                value = "$windDirection\u00B0"
            )
            WeatherRow(
                label = stringResource(R.string.weather_code),
                value = "$weathercode"
            )
            WeatherRow(
                label = stringResource(R.string.pressure),
                value = "$seaLevelPressure hPa"
            )
            WeatherRow(
                label = stringResource(R.string.time),
                value = time
            )
        }
    }
}