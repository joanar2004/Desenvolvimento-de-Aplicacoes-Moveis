package tasks

import contributors.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/*
Vaerrsão com channels -  combina o melhor do CONCURRENT  e do PROCESS: os pedidos correm em paralelo e os resultados são
mostrados à medida que chegam.
Um channel funciona como uma "fila" entre produtores (coroutines que fazem pedidos) e o consumidor (coroutine que
atualiza a UI)
 */
suspend fun loadContributorsChannels(
    // coroutineScope garante que todas as coroutines filhas terminam antes de sair
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) = coroutineScope{

    // Pede a lista de repositórios da organização
    val repos = service
        .getOrgRepos(req.org)
        .also {logRepos(req, it)}
        .bodyList()

    // Cria um channel para comunicação entre as coroutines produtoras e o consumidor
    // As coroutines enviam os resultados para o channel com send() e o consumidor recebe os resultadps com receive()
    val channel = Channel<List<User>>()

    // Para cada repositório, lança uma coroutine independente (em paralelo)
    // Cada coroutine faz o seu pedido e envia o resultado para o channel
    for (repo in repos) {
        launch {
            val users = service.getRepoContributors(req.org, repo.name)
                .also {logUsers(repo, it)}
                .bodyList()
            channel.send(users) // Envia os contribuidores deste repo para o channel
        }
    }

    // Lista acumulada de contribuidores, começa vazia e vai crescendo
    var allUsers = emptyList<User>()

    // Repete tantas vezes quantos repositórios tem
    // receive() fica suspenso até haver dados disponíveis no channel
    // Não precisamos de sincronixação extra porque receive() é sequencial
    repeat(repos.size) {
        val users = channel.receive() // Recebe o próximo resultado disponível
        allUsers = (allUsers + users).aggregate() // Agrega com os resultados anteriores
        updateResults(allUsers, it == repos.lastIndex) // Atualiza a UI
        // it == repos.lastIndex: true apenas na última iteração (completed)
    }
}
