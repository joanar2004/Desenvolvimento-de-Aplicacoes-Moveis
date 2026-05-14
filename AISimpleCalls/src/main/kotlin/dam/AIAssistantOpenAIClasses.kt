package dam

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

/**
 * OpenAIAssistant class provides an interface to communicate with OpenAI's GPT models.
 * This class handles API authentication, request formatting, response parsing, and error handling.
 * It implements retry logic for rate-limited requests and validates JSON responses.
 *
 * @param properties Properties containing an API key for authentication with OpenAI services
 */
class AIAssistantOpenAIClasses(override val properties: Properties) : AIAssistant {

    override fun getSystem() = "OPENAI"
    override val apiKeyName = "OPENAI_API_KEY"

    override var model = "gpt-4o"

    // Data classes para estruturar o pedido à API do OpenAI
    data class Message(val role: String, val content: String)

    data class OpenAIRequest(
        val model: String,
        val messages: List<Message>,
        val temperature: Double = 0.7,        // controla a aleatoriedade (0.0 a 2.0)
        val max_tokens: Int = 800,            // comprimento máximo da resposta
        val top_p: Double = 1.0,             // alternativa à temperatura para nucleus sampling
        val frequency_penalty: Double = 0.0, // reduz a repetição de sequências de tokens
        val presence_penalty: Double = 0.0   // reduz a repetição de tópicos
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

        // cria a lista de mensagens com as instruções do sistema e o conteúdo do utilizador
        // segue o formato esperado pela API do OpenAI para chat completions
        val messages = listOf(
            Message(role = "system", content = "You are a friendly and helpful assistant."),
            Message(role = "user", content = prompt)
        )

        // cria o pedido com os valores de temperatura e max tokens lidos do config.properties
        // em vez de valores fixos no código, agora são configuráveis externamente
        val openAIRequest = OpenAIRequest(
            model = model,
            messages = messages,
            temperature = temperature, // valor lido do config.properties (ou 0.7 por defeito)
            max_tokens = maxTokens     // valor lido do config.properties (ou 800 por defeito)
        )

        // converte o pedido para JSON usando Gson
        val requestBody = gson.toJson(openAIRequest)

        // configura o pedido HTTP com os headers e autenticação corretos
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        return request
    }
}