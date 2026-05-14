package dam

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class AIAssistantGemini(override val properties: Properties) : AIAssistant {

    override fun getSystem() = "GEMINI"
    override val apiKeyName = "GEMINI_API_KEY"

    override var model = "gemini-2.0-flash"

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

        // cria o array de conteúdo com o prompt do utilizador
        val messagesArray = JSONArray()
            .put(
                JSONObject()
                    .put("role", "user")
                    .put("parts", JSONArray().put(JSONObject().put("text", prompt)))
            )

        // cria o objeto de configuração da geração com os valores lidos do config.properties
        val generationConfig = JSONObject()
            .put("temperature", temperature) // valor lido do config.properties (ou 0.7 por defeito)
            .put("maxOutputTokens", maxTokens) // valor lido do config.properties (ou 800 por defeito)

        // constrói o corpo completo do pedido
        val requestBody = JSONObject()
            .put("contents", messagesArray)
            .put("generationConfig", generationConfig)
            .toString()

        // configura o pedido HTTP com os headers e autenticação corretos
        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        return request
    }
}