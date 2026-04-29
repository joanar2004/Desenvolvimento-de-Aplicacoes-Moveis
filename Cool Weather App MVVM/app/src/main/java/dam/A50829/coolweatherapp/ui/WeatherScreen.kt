package dam.A50829.coolweatherapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam.A50829.coolweatherapp.viewmodel.WeatherViewModel

@Composable
fun WeatherUI(weatherViewModel: WeatherViewModel = viewModel()) {

    val uiState by weatherViewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    val codesArray = context.resources.getStringArray(
        context.resources.getIdentifier("weather_codes_array", "array", context.packageName)
    )

    var imageName = "clear_"
    for (item in codesArray) {
        val parts = item.split("|")
        if (parts[0].toInt() == uiState.weathercode) {
            imageName = parts[2]
            break
        }
    }

    val suffix = if (uiState.isDay) "day" else "night"
    val finalImg = if (imageName.endsWith("_")) "${imageName}${suffix}" else imageName
    val resId = context.resources.getIdentifier(finalImg, "drawable", context.packageName)

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeWeatherUI(resId, uiState, weatherViewModel)
    } else {
        PortraitWeatherUI(resId, uiState, weatherViewModel)
    }
}

@Composable
fun PortraitWeatherUI(
    iconResId: Int,
    uiState: WeatherUIState,
    viewModel: WeatherViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (iconResId != 0) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(120.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        CoordinatesCard(
            latitude = uiState.latitude,
            longitude = uiState.longitude,
            onLatitudeChange = { viewModel.updateLatitude(it.toFloatOrNull() ?: return@CoordinatesCard) },
            onLongitudeChange = { viewModel.updateLongitude(it.toFloatOrNull() ?: return@CoordinatesCard) },
            onUpdateClick = { viewModel.fetchWeather() }
        )
        WeatherCard(uiState)
        uiState.errorMessage?.let {
            Text(text = it, color = Color.Red)
        }
    }
}

@Composable
fun LandscapeWeatherUI(
    iconResId: Int,
    uiState: WeatherUIState,
    viewModel: WeatherViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (iconResId != 0) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(80.dp)
                )
            }
            CoordinatesCard(
                latitude = uiState.latitude,
                longitude = uiState.longitude,
                onLatitudeChange = { viewModel.updateLatitude(it.toFloatOrNull() ?: return@CoordinatesCard) },
                onLongitudeChange = { viewModel.updateLongitude(it.toFloatOrNull() ?: return@CoordinatesCard) },
                onUpdateClick = { viewModel.fetchWeather() }
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            WeatherCard(uiState)
        }
    }
}