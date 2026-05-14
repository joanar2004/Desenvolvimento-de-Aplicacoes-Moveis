package dam

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * OpenAIAssistant class provides an interface to communicate with OpenAI's GPT models.
 * This class handles API authentication, request formatting, response parsing, and error handling.
 * It implements retry logic for rate-limited requests and validates JSON responses.
 *
 * @param properties Properties containing an API key for authentication with OpenAI services
 */
class AIAssistantOpenAI(override val properties: Properties) : AIAssistant {

    override fun getSystem() = "OPENAI"
    override val apiKeyName = "OPENAI_API_KEY"

    // Model selection - uncomment the desired model
    override var model = "gpt-4o"

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

        // cria o array de mensagens com as instruções do sistema e o conteúdo do utilizador
        // segue o formato esperado pela API do OpenAI para chat completions
        val messagesArray = JSONArray()
            .put(
                // mensagem de sistema define o comportamento e personalidade do assistente
                JSONObject()
                    .put("role", "system")
                    .put("content", "You are a friendly and helpful assistant.")
            )
            .put(
                // mensagem do utilizador contém a pergunta atual
                JSONObject()
                    .put("role", "user")
                    .put("content", prompt)
            )

        // constrói o corpo completo do pedido com o modelo, mensagens,
        // temperatura e max tokens lidos do config.properties
        val requestBody = JSONObject()
            .put("model", model)
            .put("messages", messagesArray)
            .put("temperature", temperature)  // valor lido do config.properties (ou 0.7 por defeito)
            .put("max_tokens", maxTokens)     // valor lido do config.properties (ou 800 por defeito)
            .toString()

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