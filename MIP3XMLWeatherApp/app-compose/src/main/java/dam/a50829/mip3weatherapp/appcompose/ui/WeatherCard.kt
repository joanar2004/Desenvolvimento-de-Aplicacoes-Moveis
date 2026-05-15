package dam.a50829.mip3weatherapp.appcompose.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dam.a50829.mip3weatherapp.appcompose.R
import dam.a50829.mip3weatherapp.core.viewmodel.WeatherUIState

@Composable
fun WeatherCard(uiState: WeatherUIState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            WeatherRow(
                label = stringResource(R.string.wind_speed_label),
                value = "%.1f km/h".format(uiState.windspeed)
            )
            WeatherRow(
                label = stringResource(R.string.wind_direction_label),
                value = "%.0f°".format(uiState.winddirection)
            )
            WeatherRow(
                label = stringResource(R.string.weather_code_label),
                value = uiState.weathercode.toString()
            )
            WeatherRow(
                label = stringResource(R.string.pressure_label),
                value = "%.1f hPa".format(uiState.seaLevelPressure)
            )
            WeatherRow(
                label = stringResource(R.string.time_label),
                value = uiState.time
            )
        }
    }
}
