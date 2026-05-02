package dam.a50829.coolweatherapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    // Card semi-transparente para combinar com o gradiente de fundo
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
            // Título do card a branco para contrastar com o fundo
            Text(
                text = stringResource(R.string.coordinates),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // OutlinedTextField com cores adaptadas ao fundo escuro
            // OutlinedTextFieldDefaults.colors() permite personalizar
            // todas as cores do campo de texto individualmente
            OutlinedTextField(
                value = latitude.toString(),
                onValueChange = onLatitudeChange,
                label = { Text(stringResource(R.string.latitude), color = Color.White) },
                colors = OutlinedTextFieldDefaults.colors(
                    // Cor do texto que o utilizador escreve
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    // Cor da borda quando o campo está selecionado
                    focusedBorderColor = Color.White,
                    // Cor da borda quando o campo não está selecionado
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    // Cor do label quando o campo está selecionado
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
                    // Cursor a branco
                    cursorColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = longitude.toString(),
                onValueChange = onLongitudeChange,
                label = { Text(stringResource(R.string.longitude), color = Color.White) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
                    cursorColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}