package dam.a50829.coolweatherapp.ui

// Importamos os componentes necessários do Jetpack Compose
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// @Composable indica que esta função é um componente de UI do Jetpack Compose
// Em vez de criar Views como no XML, descrevemos a UI com funções Kotlin
// O Compose trata de desenhar tudo no ecrã automaticamente
@Composable
fun WeatherRow(
    // Recebe o nome do campo (ex: "Temperature") e o valor (ex: "14.5 °C")
    label: String,
    value: String
) {
    // Row organiza os elementos horizontalmente (lado a lado)
    Row(
        // Modifier permite configurar o comportamento e aparência do componente
        // fillMaxWidth() faz a Row ocupar toda a largura disponível
        // padding() adiciona espaço em cima e em baixo
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),

        // SpaceBetween coloca o label à esquerda e o value à direita
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Text é o componente para mostrar texto no Compose
        // FontWeight.Bold deixa o label a negrito
        Text(
            text = label,
            fontWeight = FontWeight.Bold
        )

        // O valor aparece à direita sem formatação especial
        Text(text = value)
    }
}