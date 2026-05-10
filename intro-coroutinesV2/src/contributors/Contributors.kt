package contributors

import contributors.Contributors.LoadingStatus.*
import contributors.Variant.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import tasks.*
import java.awt.event.ActionListener
import javax.swing.SwingUtilities
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

enum class Variant {
    BLOCKING, // Request1Blocking
    BACKGROUND, // Request2Background
    CALLBACKS, // Request3Callbacks
    SUSPEND, // Request4Coroutine
    CONCURRENT, // Request5Concurrent
    NOT_CANCELLABLE, // Request6NotCancellable
    PROGRESS, // Request6Progress
    CHANNELS // Request7Channels
}

interface Contributors: CoroutineScope {

    val job: Job

    /*
    Propriedade imutável que expõe o estado atual do carregamento como um Flow.
    Qualquer componente pode observar este flow mas não pode modificá-lo diretamente, assim, o StateFlow é um flow
    "quente" - mantém sempre o último valor emitido.
     */
    val loadingState: StateFlow<LoadingStateData>

    /*
    Metodo abstrato que as subclasses têm de implementar e é chamado sempre que o estado de carregamento muda. Depois
    emite um novo valor para o StateFlow.
     */
    fun updateLoadingStatus(newStatus: LoadingStateData)

    /*
    Metodo abstrato que as subclasses têm de implementar que observa o StateFlow e atualiza a UI sempre que o estado
    muda.
     */
    fun observeLoadingStatus()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun init() {
        // Inicia a observação do StateFlow para atualizar a UI
        observeLoadingStatus()

        // Start a new loading on 'load' click
        addLoadListener {
            saveParams()
            loadContributors()
        }

        // Save preferences and exit on closing the window
        addOnWindowClosingListener {
            job.cancel()
            saveParams()
            exitProcess(0)
        }

        // Load stored params (user & password values)
        loadInitialParams()
    }

    fun loadContributors() {
        val (username, password, org, _) = getParams()
        val req = RequestData(username, password, org)

        clearResults()
        val service = createGitHubService(req.username, req.password)

        val startTime = System.currentTimeMillis()
        when (getSelectedVariant()) {
            BLOCKING -> { // Blocking UI thread
                val users = loadContributorsBlocking(service, req)
                updateResults(users, startTime)
            }
            BACKGROUND -> { // Blocking a background thread
                loadContributorsBackground(service, req) { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users, startTime)
                    }
                }
            }
            CALLBACKS -> { // Using callbacks
                loadContributorsCallbacks(service, req) { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users, startTime)
                    }
                }
            }
            SUSPEND -> { // Using coroutines
                launch {
                    val users = loadContributorsSuspend(service, req)
                    updateResults(users, startTime)
                }.setUpCancellation()
            }
            CONCURRENT -> {
                launch(Dispatchers.Default) { // Dispatchers.Default fica aqui, no chamador
                    val users = loadContributorsConcurrent(service, req)
                    withContext(Dispatchers.Main) {
                        updateResults(users, startTime)
                    }
                }.setUpCancellation()
            }
            NOT_CANCELLABLE -> { // Performing requests in a non-cancellable way
                launch {
                    val users = loadContributorsNotCancellable(service, req)
                    updateResults(users, startTime)
                }.setUpCancellation()
            }
            PROGRESS -> { // Showing progress
                launch(Dispatchers.Default) {
                    loadContributorsProgress(service, req) { users, completed ->
                        withContext(Dispatchers.Main) {
                            updateResults(users, startTime, completed)
                        }
                    }
                }.setUpCancellation()
            }
            CHANNELS -> { // Performing requests concurrently and showing progress
                launch(Dispatchers.Default) {
                    loadContributorsChannels(service, req) { users, completed ->
                        withContext(Dispatchers.Main) {
                            updateResults(users, startTime, completed)
                        }
                    }
                }.setUpCancellation()
            }
        }
    }

    /*
    Enum que representa os possíveis estados do carregamento
    INIT - estado inicial, antes de qualquer carregamento começar
    IN_PROGRESS - carregamento em curso
    COMPLETED - carregamento concluído com sucesso
    CANCELED - carregamento cancelado pelo utilizador
     */
    enum class LoadingStatus { INIT, COMPLETED, CANCELED, IN_PROGRESS }

    /*
    Data class que agrupa toda a informação sobre o estado atual do carregamento, em vez de passar parâmetros soltos,
    agrupamos tudo num único objeto.
    status - o estado atual (INIT, IN_PROGRESS, COMPLETED, CANCELED)
    startTime - o momento em que o carregamento começou (null se ainda não começou)
    elapsedTime - o tempo decorrido formatado como string
     */
    data class LoadingStateData(
        val status: LoadingStatus = LoadingStatus.INIT, // por defeito começa no estado INIT
        val startTime: Long? = null, // null porque ainda não começou
        val elapsedTime: String = "" // vazio porque ainda não há tempo
    )

    private fun clearResults() {
        updateContributors(listOf())
        // em vez de chamar updateLoadingStatus(IN_PROGRESS) diretamente, agora criamos um LoadingStateData com o
        // estado IN_PROGRESS — isto emite um novo valor para o StateFlow
        updateLoadingStatus(LoadingStateData(status = IN_PROGRESS))
        setActionsStatus(newLoadingEnabled = false)
    }

    /*
    Calcula o tempo decorrido desde o startTime até ao momento atual e devolve uma string formatada como "3.2 sec"
     */
    private fun calculateElapsedTime(startTime: Long): String {
        val time = System.currentTimeMillis() - startTime
        return "${time / 1000}.${time % 1000 / 100} sec"
    }

    private fun updateResults(
        users: List<User>,
        startTime: Long,
        completed: Boolean = true
    ) {
        updateContributors(users) // atualiza a tabela com os novos dados
        val status = if (completed) COMPLETED else IN_PROGRESS
        val elapsedTime = calculateElapsedTime(startTime)
        // cria um LoadingStateData com o estado atual e o tempo decorrido
        // isto emite um novo valor para o StateFlow que a UI irá receber
        updateLoadingStatus(LoadingStateData(status = status, startTime = startTime, elapsedTime = elapsedTime))
        if (completed) {
            setActionsStatus(newLoadingEnabled = true)
        }
    }

    private fun Job.setUpCancellation() {
        // make active the 'cancel' button
        setActionsStatus(newLoadingEnabled = false, cancellationEnabled = true)

        val loadingJob = this

        // cancel the loading job if the 'cancel' button was clicked
        val listener = ActionListener {
            loadingJob.cancel()
            // em vez de chamar updateLoadingStatus(CANCELED) diretamente, agora criamos um LoadingStateData com o
            // estado CANCELED — isto emite um novo valor para o StateFlow
            updateLoadingStatus(LoadingStateData(status = CANCELED))
        }
        addCancelListener(listener)

        // update the status and remove the listener after the loading job is completed
        launch {
            loadingJob.join()
            setActionsStatus(newLoadingEnabled = true)
            removeCancelListener(listener)
        }
    }

    fun loadInitialParams() {
        setParams(loadStoredParams())
    }

    fun saveParams() {
        val params = getParams()
        if (params.username.isEmpty() && params.password.isEmpty()) {
            removeStoredParams()
        } else {
            saveParams(params)
        }
    }

    fun getSelectedVariant(): Variant

    fun updateContributors(users: List<User>)

    fun setLoadingStatus(text: String, iconRunning: Boolean)

    fun setActionsStatus(newLoadingEnabled: Boolean, cancellationEnabled: Boolean = false)

    fun addCancelListener(listener: ActionListener)

    fun removeCancelListener(listener: ActionListener)

    fun addLoadListener(listener: () -> Unit)

    fun addOnWindowClosingListener(listener: () -> Unit)

    fun setParams(params: Params)

    fun getParams(): Params
}