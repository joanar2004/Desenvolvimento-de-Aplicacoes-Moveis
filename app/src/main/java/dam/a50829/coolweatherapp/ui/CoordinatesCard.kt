package dam.a50829.coolweatherapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam.a50829.coolweatherapp.R

// Este composable é responsável por mostrar e editar as coordenadas
@Composable
fun CoordinatesCard(
    latitude: Float,
    longitude: Float,

    // Estes dois parâmetros são funções — em Kotlin, funções podem ser
    // passadas como parâmetros, tal como qualquer outro valor.
    // "(String) -> Unit" significa: uma função que recebe uma String
    // e não devolve nada (Unit = void noutras linguagens)
    // São chamadas "lambdas" ou "higher-order functions"
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título do card
            Text(
                text = stringResource(R.string.coordinates),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // OutlinedTextField é um campo de texto editável com borda
            // É o componente do Material Design para inputs de texto
            OutlinedTextField(
                // .toString() converte o Float para String para mostrar no campo
                value = latitude.toString(),

                // onValueChange é chamada automaticamente pelo Compose
                // sempre que o utilizador escreve algo no campo
                // Passamos o novo valor para o ViewModel através da lambda
                onValueChange = onLatitudeChange,

                label = { Text(stringResource(R.string.latitude)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = longitude.toString(),
                onValueChange = onLongitudeChange,
                label = { Text(stringResource(R.string.longitude)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}