package dam.A50829.coolweatherapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dam.A50829.coolweatherapp.R

@Composable
fun WeatherCard(uiState: WeatherUIState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            WeatherRow(stringResource(R.string.temperature), "${uiState.temperature} ºC")
            WeatherRow(stringResource(R.string.pressure), "${uiState.seaLevelPressure} hPa")
            WeatherRow(stringResource(R.string.wind_speed), "${uiState.windspeed} km/h")
            WeatherRow(stringResource(R.string.wind_direction), "${uiState.winddirection}º")
            WeatherRow(stringResource(R.string.time_label), uiState.time)
        }
    }
}