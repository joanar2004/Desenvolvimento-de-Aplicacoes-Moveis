package dam.a50829.cooljetpackweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dam.a50829.cooljetpackweatherapp.ui.WeatherUI

// ComponentActivity é a classe base para Activities que usam Jetpack Compose
class MainActivity : ComponentActivity() {

    // onCreate é chamado quando a Activity é criada
    // É o ponto de entrada da app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enableEdgeToEdge permite que a app ocupe todo o ecrã
        // incluindo por baixo das barras do sistema
        enableEdgeToEdge()

        // setContent substitui o setContentView do XML
        // Aqui definimos a UI com Jetpack Compose
        setContent {
            // MaterialTheme aplica as cores e estilos do Material Design
            // sem precisar de um tema personalizado
            MaterialTheme {
                // Surface é um contentor base do Material Design
                // Garante que o fundo tem a cor correta do tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Chamamos o nosso composable principal
                    // que gere todo o resto da UI
                    WeatherUI()
                }
            }
        }
    }
}