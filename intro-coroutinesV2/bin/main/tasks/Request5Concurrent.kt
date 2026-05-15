package tasks

import contributors.*
import kotlinx.coroutines.*

/*
Versão concorrente, em vez de esperar pela resposta de cada repositório antes de pedir o próximo, lança todos os pedidos
ao mesmo tempo com "async".
Cada repositório tem a sua própria coroutine, e só no final esperamos por todos os resultados com o awaitAll().
 */

// coroutineScope cria um scope local, só termina quando todas as coroutines filhas (os "async") terminarem
suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData): List<User> = coroutineScope {
    // Pede a lista de repositórios da organização
    // A coroutine suspende até a resposta chegar, sem bloquear a thread
    val repos = service
        .getOrgRepos(req.org) // Faz o pedido HTTP à API do GitHb (versão suspend)
        .also { logRepos(req, it) } // Regista no log quantos repositórios foram carregados
        .bodyList() // Extrai a lista de repositórios do corpo da resposta HTTP

    // Para cada repositório, lança uma coroutine independente com "async"
    // Todas as coroutines são lançadas imediatamente - os pedidos correm em paralelo
    val deferred: List<Deferred<List<User>>> = repos.map { repo ->
        // Deferred<List<User>> é como se fosse um compromisso de que no futuro teremos uma List<User>
        // repos.map: para cada repositório, cria uma coroutine assíncrona


        //async: lança uma nova coroutine que corre em paralelo com as outras
        // Dispatchers.Default: usa uma pool de threads partilhada da JVM
        async{
            log("Starting loading for ${repo.name}") // Regista no log que este repo começou a carregar
            service.getRepoContributors(req.org, repo.name) // Pedido HTTP para os contribuidores deste repo
                .also {logUsers(repo, it)} // Regista no log os contribuidores carregados
                .bodyList() // Extrai a lista de contribuiores da resposta
        }
    }

    // Espera que todas as coroutines terminem e junta os resultados
    // AwaitAll() devolve List<List<User>> -> flatten() junta numa só lista
    deferred.awaitAll().flatten<User>().aggregate()
}