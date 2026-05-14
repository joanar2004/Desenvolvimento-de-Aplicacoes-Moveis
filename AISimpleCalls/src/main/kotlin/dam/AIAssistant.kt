package dam

import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Properties
import kotlin.math.pow

/**
 * AIAssistant interface defines the contract for different AI assistant implementations.
 * Any class implementing this interface must provide methods for processing user input
 * and retrieving model information.
 */
interface AIAssistant {

    /**
     * Representa as propriedades de configuração do assistente.
     * Contém as API keys, nível de logging, temperatura, max tokens, etc.
     * É carregado a partir do ficheiro config.properties.
     */
    val properties: Properties

    /**
     * Logger para registo de mensagens de debug, erro e informação.
     * Usa SLF4J que se liga automaticamente ao Logback em runtime.
     * O nome da classe é usado para identificar a origem das mensagens no log.
     */
    val logger: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    /**
     * Nome da propriedade no config.properties que contém a API key.
     * Cada implementação define o seu próprio nome, por exemplo:
     * - "GEMINI_API_KEY" para o Gemini
     * - "OPENAI_API_KEY" para o OpenAI
     * - "NVIDIA_KIMI_API_KEY" para o Kimi
     */
    val apiKeyName: String

    /**
     * O modelo de IA a usar para gerar respostas.
     * Cada implementação define o seu modelo por defeito, por exemplo:
     * - "gemini-2.0-flash" para o Gemini
     * - "gpt-4o" para o OpenAI
     * - "moonshotai/kimi-k2.6" para o Kimi
     */
    var model: String

    /**
     * Cliente HTTP usado para fazer os pedidos à API.
     * O OkHttpClient é reutilizável e gere as ligações HTTP de forma eficiente.
     * Algumas implementações podem substituir este cliente para definir timeouts personalizados.
     */
    val client: OkHttpClient
        get() = OkHttpClient()

    /**
     * A API key lida do config.properties usando o apiKeyName.
     * Se a key não estiver definida no ficheiro de configuração, lança uma exceção
     * para evitar pedidos não autenticados à API.
     *
     * @throws IllegalStateException Se a API key não estiver definida no config.properties
     */
    val apiKey: String
        get() = properties.getProperty(apiKeyName)
            ?: throw IllegalStateException("API key $apiKeyName not found in configuration file.")

    /**
     * Devolve o nome do sistema de IA usado, por exemplo "GEMINI", "OPENAI" ou "KIMI".
     * Usado para mostrar ao utilizador qual o sistema que está a ser usado.
     */
    fun getSystem(): String

    /**
     * Processa o input do utilizador de duas formas diferentes:
     * - Se o input começar com "sentiment:" faz análise de sentimento do texto que se segue
     * - Caso contrário, faz uma resposta normal e amigável
     *
     * Exemplo de uso para sentimento: "sentiment: I love this sunny day!"
     * Exemplo de uso normal: "What is the capital of Portugal?"
     *
     * @param input O input do utilizador
     * @return A resposta do modelo como string
     */
    suspend fun processInput(input: String): String {
        // verifica se o input começa com "sentiment:" (ignorando maiúsculas/minúsculas)
        // este prefixo é o sinal para ativar o modo de análise de sentimento
        return if (input.startsWith("sentiment:", ignoreCase = true)) {

            // remove o prefixo "sentiment:" e os espaços em branco para obter apenas o texto a analisar
            // ex: "sentiment: I love this!" -> "I love this!"
            val textToAnalyze = input.removePrefix("sentiment:").trim()

            // constrói um prompt específico para análise de sentimento
            // este prompt instrui o modelo a responder apenas em formato JSON
            val formattedPrompt = buildSentimentPrompt(textToAnalyze)

            // faz a chamada à API com o prompt de sentimento
            apiCallWithBackoff(formattedPrompt)
        } else {
            // modo normal — constrói um prompt amigável com as instruções do assistente
            val formattedPrompt = buildPrompt(input)

            // faz a chamada à API com o prompt normal
            apiCallWithBackoff(formattedPrompt)
        }
    }

    /**
     * Constrói um prompt normal com instruções de personalidade para o assistente.
     * Define o nome, idioma e tom de resposta do assistente.
     *
     * @param input O input do utilizador
     * @return Um prompt formatado com instruções do sistema e o input do utilizador
     */
    fun buildPrompt(input: String): String {
        return """
            Your name is Assistant.
            The preferred language is English.
            Respond in a friendly and helpful manner.
            The user's request is: "$input"
            """.trimIndent()
        // trimIndent() remove a indentação extra do texto multilinha
    }

    /**
     * Constrói um prompt específico para análise de sentimento.
     * Instrui o modelo a avaliar o sentimento do texto numa escala de 7 pontos
     * e a devolver o resultado APENAS em formato JSON — sem texto adicional.
     *
     * A escala de sentimento é:
     * 1 - Very Negative  (muito negativo)
     * 2 - Negative       (negativo)
     * 3 - Slightly Negative (ligeiramente negativo)
     * 4 - Neutral        (neutro)
     * 5 - Slightly Positive (ligeiramente positivo)
     * 6 - Positive       (positivo)
     * 7 - Very Positive  (muito positivo)
     *
     * O formato da resposta esperado é:
     * {
     *   "rating": <número de 1 a 7>,
     *   "justification": "<breve explicação do rating>"
     * }
     *
     * @param input O texto a analisar
     * @return Um prompt formatado com instruções para análise de sentimento
     */
    fun buildSentimentPrompt(input: String): String {
        return """
            You are a sentiment analysis assistant.
            Analyze the sentiment of the following text and rate it on a 7-point scale:
            1 - Very Negative
            2 - Negative
            3 - Slightly Negative
            4 - Neutral
            5 - Slightly Positive
            6 - Positive
            7 - Very Positive
            
            You MUST respond ONLY with a JSON object in this exact format, with no extra text:
            {
                "rating": <number from 1 to 7>,
                "justification": "<brief explanation>"
            }
            
            The text to analyze is: "$input"
            """.trimIndent()
        // trimIndent() remove a indentação extra do texto multilinha
        // garantindo que o prompt enviado à API não tem espaços desnecessários no início de cada linha
    }

    /**
     * Chama a API com um mecanismo de retry com backoff exponencial.
     * Faz retry automaticamente em caso de rate limiting (HTTP 429).
     * O tempo de espera entre tentativas aumenta exponencialmente:
     * - 1ª tentativa: 2000ms
     * - 2ª tentativa: 4000ms
     * - 3ª tentativa: 8000ms
     * - etc.
     *
     * @param input O input a enviar para a API
     * @return A resposta do modelo como string
     * @throws Exception Se o número máximo de tentativas for excedido ou outro erro ocorrer
     */
    suspend fun apiCallWithBackoff(input: String): String {
        var attempts = 0
        val maxAttempts = 5  // número máximo de tentativas antes de desistir
        val baseDelay = 1000L  // delay base em milissegundos (1 segundo)

        while (attempts < maxAttempts) {
            try {
                // tenta chamar a API — se funcionar, retorna imediatamente
                return makeApiCall(input)

            } catch (e: Exception) {
                logger.error("Error message: ${e.message}")

                // só faz retry em caso de rate limiting (HTTP 429)
                // outros erros são propagados imediatamente
                if (e.message?.contains("429") == true) {
                    logger.warn("Error 429: Too Many Requests. Will delay and retry.")
                    attempts++

                    // calcula o delay com backoff exponencial: baseDelay * 2^attempts
                    // aumenta o tempo de espera a cada falha consecutiva para não sobrecarregar a API
                    val delayTime = baseDelay * (2.0.pow(attempts.toDouble())).toLong()
                    logger.info("Attempt: $attempts failed - will delay: $delayTime ms")

                    // suspende a coroutine pelo tempo calculado sem bloquear a thread
                    delay(delayTime)
                } else {
                    // para outros erros (404, 401, etc.), propaga imediatamente sem retry
                    throw e
                }
            }
        }
        // se esgotou todas as tentativas, lança uma exceção
        throw Exception("Exceeded maximum retry attempts")
    }

    /**
     * Faz uma chamada à API com o prompt fornecido e processa a resposta.
     * Este método é comum a todas as implementações que usam o formato Gemini.
     * As implementações que usam outro formato (ex: Kimi) podem substituir este método.
     *
     * @param prompt O texto a enviar para a API
     * @return O texto da resposta extraído da API
     * @throws Exception Se a chamada falhar ou a resposta não puder ser processada
     */
    fun makeApiCall(prompt: String): String {
        // regista o prompt no log para debugging
        logger.info("Prompt:\n$prompt")

        // constrói o pedido HTTP específico para cada implementação
        val request = buildRequest(prompt)

        // envia o pedido HTTP e processa a resposta
        // o bloco "use" garante que a resposta é fechada após o processamento
        client.newCall(request).execute().use { response ->

            // em caso de erro HTTP, lança uma exceção com os detalhes do erro
            if (!response.isSuccessful) {
                val errorBody = response.body?.string()
                throw Exception("Error in API call: ${response.code} - ${response.message}\nResponse: $errorBody")
            }

            // extrai o corpo da resposta como string
            val responseBody = response.body?.string() ?: return "Error: empty response"

            try {
                // faz o parse da resposta como JSON
                val json = JSONObject(responseBody)
                logger.debug("Raw API response: {}", responseBody)

                // valida que a resposta contém o campo "candidates"
                if (!json.has("candidates") || json.getJSONArray("candidates").length() == 0) {
                    return "Error: No candidates found in the API response"
                }

                val candidates = json.getJSONArray("candidates")
                val firstCandidate = candidates.getJSONObject(0)

                // valida que o primeiro candidato tem o campo "content"
                if (!firstCandidate.has("content")) {
                    return "Error: No content found in the API response"
                }

                val content = firstCandidate.getJSONObject("content")

                // valida que o content tem o campo "parts" com pelo menos um elemento
                if (!content.has("parts") || content.getJSONArray("parts").length() == 0) {
                    return "Error: No parts found in the API response"
                }

                val parts = content.getJSONArray("parts")
                val firstPart = parts.getJSONObject(0)

                // valida que a primeira parte tem o campo "text"
                if (!firstPart.has("text")) {
                    return "Error: No text found in the API response"
                }

                // extrai e devolve o texto da resposta
                val text = firstPart.getString("text")
                return text.trim()

            } catch (e: JSONException) {
                // em caso de erro a fazer o parse do JSON, regista o erro e lança uma exceção
                val truncatedResponse = if (responseBody.length > 200)
                    "${responseBody.substring(0, 200)}..."
                else
                    responseBody

                logger.error("Error parsing JSON response: ${e.message}")
                logger.error("Response body (truncated): $truncatedResponse")
                throw Exception("Failed to parse API response: ${e.message}", e)
            }
        }
    }

    /**
     * Constrói e formata o pedido HTTP para a API.
     * Cada implementação define o formato específico do pedido conforme a API que usa.
     * Por exemplo, o Gemini usa um formato diferente do OpenAI.
     *
     * @param prompt O texto a enviar para a API
     * @return O pedido HTTP formatado e pronto a enviar
     */
    fun buildRequest(prompt: String): Request
}