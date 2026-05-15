package dam.a50829.cooljetpackweatherapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dam.a50829.cooljetpackweatherapp.R

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
    // Card semi-transparente para combinar com o gradiente de fundo
    // copy(alpha = 0.2f) torna a cor branca semi-transparente
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Reutilizamos o WeatherRow que criámos antes para cada linha
            // stringResource(R.string.xxx) busca o texto do strings.xml
            // para suportar múltiplos idiomas
            WeatherRow(
                label = stringResource(R.string.temperature),
                value = "$temperature \u00B0C"
            )
            WeatherRow(
                label = stringResource(R.string.wind_speed),
                value = "$windSpeed km/h"
            )
            WeatherRow(
                label = stringResource(R.string.wind_direction),
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