package tasks

import contributors.*

/*
Versão com progresso - mostra os resultados à medida que cada repositório é carregado, em vez de esperar que todos
terminem para mostrar tudo de uma vez.
É baseada no SUSPEND (sequencial), por isso ainda não é concorrente, ou seja, os pedidos são feitos um de cada vez, mas
utilizador já vê os resultados intermédios.
 */
suspend fun loadContributorsProgress(
    // updateResults: callback suspend chamado após cada repositório
    // List<User>: lista acumulada até ao momento
    // completed: Boolean: indica se já carregámos todos os repositórios
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    // Pede a lista de repositórios da organização
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    // Lista acumulada de contribuidores, começa vazia e vai crescendo a cada repositório carregado, é atualizada com os
    // novos dados agregados
    var allUsers = emptyList<User>()

    // Itera sobre os repositórioos com índice para saber qual e o último
    for ((index, repo) in repos.withIndex()) {
        // Pede os contribuidores deste repositório
        val users = service.getRepoContributors(req.org, repo.name)
            .also {logUsers(repo, it)}
            .bodyList()

        // Junta os novos contribuidores aos anteriores e agrega (soma contribuições de utilizadores repetidos e ordena)
        allUsers = (allUsers + users).aggregate()

        // Atualiza a UI com a lista acumulada até agora
        // completed = true apenas quando é o último repositório
        updateResults(allUsers, index == repos.lastIndex)
    }
}
