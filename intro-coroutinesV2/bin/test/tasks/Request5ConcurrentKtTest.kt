package tasks

import contributors.MockGithubService
import contributors.expectedConcurrentResults
import contributors.testRequestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class Request5ConcurrentKtTest {
    @Test
    // "runTest" em vez de "runBlocking" — usa tempo virtual em vez de tempo real
    // os pedidos concorrentes são simulados instantaneamente sem esperar pela rede
    fun testConcurrent() = runTest {

        // marca o início do tempo virtual
        val startTime = currentTime

        // MockGithubService simula a API do GitHub com delays predefinidos
        // sem fazer pedidos reais à rede — torna o teste rápido e independente
        val result = loadContributorsConcurrent(MockGithubService, testRequestData)

        // verifica que os dados devolvidos são os esperados
        Assert.assertEquals(
            "Wrong result for 'loadContributorsConcurrent'",
            expectedConcurrentResults.users, // lista de utilizadores esperada
            result                           // lista de utilizadores obtida
        )

        // calcula o tempo virtual total desde o início até ao fim
        val totalTime = currentTime - startTime

        // verifica que os pedidos correram em PARALELO e não sequencialmente
        // tempo esperado = 1000ms (pedido dos repos) + max(1000, 1200, 800)ms (pedidos dos contribuidores)
        // = 1000 + 1200 = 2200ms virtuais
        // se fosse sequencial seria ~4400ms — o facto de ser 2200ms prova que a concorrência funciona
        Assert.assertEquals(
            "The calls run concurrently, so the total virtual time should be 2200 ms: " +
                    "1000 for repos request plus max(1000, 1200, 800) = 1200 for concurrent contributors requests)",
            expectedConcurrentResults.timeFromStart, // tempo virtual esperado (2200ms)
            totalTime                                // tempo virtual medido
        )
    }
}