package dam

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

/**
 * GeminiAIAssistant class provides an interface to communicate with Google's Gemini AI models.
 * This class handles API authentication, request formatting, response parsing, and error handling.
 * It implements retry logic for rate-limited requests and validates JSON responses.
 *
 * @param properties Properties containing the API key for authentication with Gemini services
 */
class AIAssistantGeminiClasses(override val properties: Properties) : AIAssistant {

    override fun getSystem() = "GEMINI"
    override val apiKeyName = "GEMINI_API_KEY"

    override var model = "gemini-2.0-flash"

    // Data classes para estruturar o pedido à API do Gemini
    data class Part(val text: String)

    data class Content(val role: String, val parts: List<Part>)

    data class GeminiRequest(
        val contents: List<Content>,
        val generationConfig: GenerationConfig? = null
    )

    data class GenerationConfig(
        val temperature: Double? = 0.4,      // controla a criatividade das respostas
        val topK: Int? = 40,                 // limita a seleção aos K tokens mais prováveis
        val topP: Double? = 0.95,            // nucleus sampling — cobre 95% da massa de probabilidade
        val maxOutputTokens: Int? = 800,     // controla o tamanho máximo da resposta
        val candidateCount: Int? = 1         // número de respostas alternativas a gerar
    )

    // instância do Gson para serialização JSON
    private val gson = Gson()

    override fun buildRequest(prompt: String): Request {

        // lê a temperatura do ficheiro config.properties
        // a temperatura controla a criatividade das respostas:
        //   - valor baixo (0.0-0.3): respostas mais determinísticas e previsíveis
        //   - valor médio (0.4-0.7): equilíbrio entre determinismo e criatividade
        //   - valor alto (0.8-1.0): respostas mais criativas e variadas
        // toDoubleOrNull() converte a string para Double — se não estiver definida no
        // config.properties ou for inválida, devolve null e usamos 0.7 como valor por defeito
        val temperature = properties.getProperty("TEMPERATURE")?.toDoubleOrNull() ?: 0.7

        // lê o max tokens do ficheiro config.properties
        // max tokens controla o tamanho máximo da resposta
        // toIntOrNull() converte a string para Int — se não estiver definida no
        // config.properties ou for inválida, devolve null e usamos 800 como valor por defeito
        val maxTokens = properties.getProperty("MAX_TOKENS")?.toIntOrNull() ?: 800

        // cria a estrutura do pedido usando data classes
        val part = Part(text = prompt)
        val content = Content(role = "user", parts = listOf(part))

        // cria o pedido com os valores de temperatura e max tokens lidos do config.properties
        // em vez de valores fixos no código, agora são configuráveis externamente
        val geminiRequest = GeminiRequest(
            contents = listOf(content),
            generationConfig = GenerationConfig(
                temperature = temperature,   // valor lido do config.properties (ou 0.7 por defeito)
                maxOutputTokens = maxTokens  // valor lido do config.properties (ou 800 por defeito)
            )
        )

        // converte o pedido para JSON usando Gson
        val requestBody = gson.toJson(geminiRequest)

        // configura o pedido HTTP com os headers e autenticação corretos
        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        return request
    }
}