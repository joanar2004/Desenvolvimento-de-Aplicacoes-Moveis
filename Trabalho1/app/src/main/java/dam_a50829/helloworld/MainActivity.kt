package dam_a50829.helloworld

import android.os.Build
import android.os.Bundle
import android.widget.Button // Necessário para o botão
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ativa o modo de ecrã total e define o layout
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ajusta as margens para as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. FUNCIONALIDADE EXTRA: Mensagem de boas-vindas
        Toast.makeText(this, "Bem-vindo!", Toast.LENGTH_LONG).show()

        // 2. Preparar os dados para exportação (o bloco que pediste)
        val info = """
            Manufacturer: ${Build.MANUFACTURER}
            Model: ${Build.MODEL}
            Brand: ${Build.BRAND}
            Type: ${Build.TYPE}
            User: ${Build.USER}
            Base: ${Build.VERSION.RELEASE}
            Incremental: ${Build.VERSION.INCREMENTAL}
            SDK: ${Build.VERSION.SDK_INT}
            Version Code: ${Build.VERSION.RELEASE}
            Display: ${Build.DISPLAY}
        """.trimIndent()

        // 3. FUNCIONALIDADE EXTRA: Configurar o botão de partilha para exportar os dados
        // Certifica-te que o ID no XML é btnShare
        val btnShare = findViewById<Button>(R.id.btnShare)
        btnShare.setOnClickListener {
            val intent = android.content.Intent(android.content.Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(android.content.Intent.EXTRA_TEXT, info) // Exporta a variável 'info'
            startActivity(android.content.Intent.createChooser(intent, "Exportar via:"))
        }

        // Regista nos logs a criação da atividade
        println(getString(R.string.activity_oncreate_msg, this@MainActivity.localClassName))
    }
}