package tasks

import contributors.GitHubService
import contributors.RequestData
import contributors.User
import kotlin.concurrent.thread

// Em vez de correr na thread da UI (que congela a janela),
// esta versão cria uma thread separada para fazer os pedidos à rede.
// A thread da UI fica livre, mas o resultado tem de ser enviado de volta
// através do callback updateResults.
fun loadContributorsBackground(
    service: GitHubService,  // cliente HTTP que faz os pedidos à API do GitHub
    req: RequestData,        // dados do pedido: username, password e organização
    updateResults: (List<User>) -> Unit  // callback chamado no fim com os resultados
) {
    // cria uma nova thread para não bloquear a UI
    // dentro dessa thread, chama o loadContributorsBlocking (que bloqueia, mas agora
    // está numa thread separada, por isso a UI não congela)
    // quando terminar, chama o updateResults com os dados agregados
    thread {
        updateResults(loadContributorsBlocking(service, req))
    }
}