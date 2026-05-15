package tasks
/*
Com o "UpdateResults" no fim do loop iria falhar, pois,, é chamado imediatamente após o ciclo "for", antes de qualquer
callback ter terminado. Os pedidos são assíncronos, ou seja, o ciclo inicia todos os pedidos e avança imediatamente sem
esperar pelas respostas, pelo que "allUsers" está vazia quando "aggregate()" é chamado.

Já se verificar se estamos no último índice, ela tenta resolver o problema de esperar por todas as respostas, através da
verificação de que se o índice atual é o último repositório, mas falha na mesma, pois os callbacks não são chamados pela
ordem em que os pedidos foram feitos, ou seja, a resposta do último repositório ode chegar antes das anteriores, fazendo
com que "updateResults()" seja chamado com dados incompletos e os resultados dos pedidos mais lentos sejam perdidos.

O "countDownLatch" resolve ambos os problemas porque não depende da ordem das respostas nem do timing, simplesmente
conta quantos pedidos ainda faltam terminar e só avança quando todos tiverem concluído, independentemente da ordem em
que chegaram.
 */

import contributors.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.CountDownLatch

/*
Nesta versão usamos Callbacks para evitar bloqueuar a UI.
Os pedidos são feitos em paralelo (um por repositório), mas isso cria um problema:

Não sabemos quando todos os pedidos terminaram para poder chamar "updateResults()".
A solução é usar "CountDonLatch()" - um contador que começa com o número de repositórios e vai decrementando à medida
que cada pedido termina. Quando chega a zero sabemos que todos os pedidos terminaram e podemos atualizar a UI com os
resultados completos.
 */


fun loadContributorsCallbacks(
    service: GitHubService, // Cliente HTTP que faz os pedidos à API do GitHub
    req: RequestData, // Dados do pedido: username, password e organização
    updateResults: (List<User>) -> Unit // Callback chamado no fim com os resultados
) {

    service.getOrgReposCall(req.org).onResponse { responseRepos ->
        logRepos(req, responseRepos) // Regista no log quantos repos foram carregados
        val repos = responseRepos.bodyList() // Extrai a lista de repositórios da resposta

        // Lista thread-safe porque os callbacks podem correr em threads diferentes
        val allUsers = Collections.synchronizedList(mutableListOf<User>())

        // Inicializa o contador com o número total de repositórios a processar
        val countDownLatch = CountDownLatch(repos.size)

        // Para cada repositório, inicia um pedido de contribuidores em paralelo
        for (repo in repos) {
            service.getRepoContributorsCall(req.org, repo.name).onResponse { responseUsers ->
                logUsers(repo, responseUsers) // Regista no log os contribuidores
                val users = responseUsers.bodyList() // Extrai a lista de contribuidores
                allUsers += users // Adiciona à lista de contribuidores
                countDownLatch.countDown() // decrementa o contador
            }
        }
        // Bloqueia até o contador chegar a zero, oou seja, até todos os repositórios terem sido processados - só depois
        // então chama "updateResults" com os dados completos
        countDownLatch.await()
        updateResults(allUsers.aggregate())
    }
}

inline fun <T> Call<T>.onResponse(crossinline callback: (Response<T>) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            callback(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            log.error("Call failed", t)
        }
    })
}
