package tasks

import contributors.*

/*
Versão com coroutines e funções suspend.
Ao contrário do Blocking, esta função não bloqueia a thread da UI. Quando está à espera de uma resposta da rede, a
coroutine é suspensa e a thread fica livre para fazer outras coisas, tal como atualizar a UI.
Quando a resposta chegar, a coroutine retoma automaticamente.
 */
suspend fun loadContributorsSuspend(service: GitHubService, req: RequestData): List<User> {
    // Pede a lista de repositórios - a coroutine suspende até a resposta chegar, então já não precisamos de chamar
    // .execute() porque a função "suspend" já retorna a resposta diretamente (sem bloquear)
    val repos = service
        .getOrgRepos(req.org)
        .also {logRepos(req, it)} // Regista no log quantos repos foram carregados
        .bodyList() // Extrai a lista de repositórios da resposta

    // Para cada repositório, pede os contribuidores, também suspende à espera e os pedidos são feitos sequencialmente
    // (um de cada vez), por isso ainda não é a versão mais rápida.
    return repos.flatMap{ repo ->
        service.getRepoContributors(req.org, repo.name)
            .also {logUsers (repo, it)} // Regista no log os contribuidores do repo
            .bodyList() // Extrai a lista de contribuidores
    }.aggregate()
}