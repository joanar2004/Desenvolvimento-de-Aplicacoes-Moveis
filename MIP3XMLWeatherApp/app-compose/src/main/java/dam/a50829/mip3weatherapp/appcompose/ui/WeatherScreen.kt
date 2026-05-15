package dam.a50829.mip3weatherapp.appcompose.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam.a50829.mip3weatherapp.appcompose.R
import dam.a50829.mip3weatherapp.core.viewmodel.WeatherUIState
import dam.a50829.mip3weatherapp.core.viewmodel.WeatherViewModel

// ── Root composable ───────────────────────────────────────────────────────────

@Composable
fun WeatherUI(vm: WeatherViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()
    val (topColor, bottomColor) = getBackgroundColors(uiState)
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(Unit) { vm.fetchWeather() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(topColor, bottomColor)))
    ) {
        if (isLandscape) {
            LandscapeWeatherUI(uiState = uiState, vm = vm)
        } else {
            PortraitWeatherUI(uiState = uiState, vm = vm)
        }
    }
}

// ── Portrait layout ───────────────────────────────────────────────────────────

@Composable
fun PortraitWeatherUI(uiState: WeatherUIState, vm: WeatherViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        WeatherHero(uiState = uiState)
        Spacer(Modifier.height(16.dp))
        CoordinatesCard(
            latitude = uiState.latitude,
            longitude = uiState.longitude,
            onLatitudeChange = { vm.updateLatitude(it.toFloatOrNull() ?: uiState.latitude) },
            onLongitudeChange = { vm.updateLongitude(it.toFloatOrNull() ?: uiState.longitude) }
        )
        Spacer(Modifier.height(12.dp))
        WeatherCard(uiState = uiState)
        Spacer(Modifier.height(16.dp))
        FetchButton(isLoading = uiState.isLoading, onClick = { vm.fetchWeather() })
        Spacer(Modifier.height(24.dp))
    }
}

// ── Landscape layout ──────────────────────────────────────────────────────────

@Composable
fun LandscapeWeatherUI(uiState: WeatherUIState, vm: WeatherViewModel) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Left column
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeatherHero(uiState = uiState)
            Spacer(Modifier.height(12.dp))
            CoordinatesCard(
                latitude = uiState.latitude,
                longitude = uiState.longitude,
                onLatitudeChange = { vm.updateLatitude(it.toFloatOrNull() ?: uiState.latitude) },
                onLongitudeChange = { vm.updateLongitude(it.toFloatOrNull() ?: uiState.longitude) }
            )
            Spacer(Modifier.height(12.dp))
            FetchButton(isLoading = uiState.isLoading, onClick = { vm.fetchWeather() })
        }

        Spacer(Modifier.width(16.dp))

        // Right column
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            WeatherCard(uiState = uiState)
        }
    }
}

// ── Shared fetch button / loading indicator ───────────────────────────────────

@Composable
private fun FetchButton(isLoading: Boolean, onClick: () -> Unit) {
    if (isLoading) {
        CircularProgressIndicator(color = Color.White)
    } else {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.25f),
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(R.string.update_weather))
        }
    }
}
