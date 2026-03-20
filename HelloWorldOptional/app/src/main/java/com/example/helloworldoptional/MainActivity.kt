// Esta MainActivity tem como objetivo extrair e exibir informações técnicas sobre o hardware e o
// sistema operativo do dispositivo Android onde a aplicação está a correr. Ele utiliza a classe
// Build para aceder a metadados como o fabricante, o modelo do telemóvel e a versão do SDK,
// formatando todos esses dados numa única String que é depois injetada dinamicamente num componente
// visual (TextView) da interface.
package com.example.helloworldoptional

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Obtém a referência do componente de texto (TextView) definido no ficheiro XML
        val textViewBuildInfo = findViewById<TextView>(R.id.textViewBuildInfo)

        // Cria uma String formatada (usando Raw String """) que acede a constantes da
        // classe Build para recolher dados do fabricante, modelo e versão do Android
        val info_projeto = """
            Manufacturer: ${Build.MANUFACTURER}
            Model: ${Build.MODEL}
            Brand: ${Build.BRAND}
            Type: ${Build.TYPE}
            User: ${Build.USER}
            Base: ${Build.VERSION_CODES.BASE} 
            Incremental: ${Build.VERSION.INCREMENTAL}
            SDK: ${Build.VERSION.SDK_INT}
            Version Code: ${Build.VERSION.RELEASE}
            Display: ${Build.DISPLAY}
        """.trimIndent()

        // Atribui o texto gerado ao widget no ecrã para que o utilizador o possa ler
        textViewBuildInfo.text = info_projeto
    }
}

