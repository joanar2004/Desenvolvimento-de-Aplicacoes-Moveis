package tasks

import contributors.MockGithubService
import contributors.expectedConcurrentResults
import contributors.expectedResults
import contributors.testRequestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class Request4SuspendKtTest {

    @Test
    // "runTest" em vez de "runBlocking" usa tempo virtual em vez de tempo real —
    // delays e suspensões não esperam tempo real, tornando os testes muito mais rápidos
    fun testSuspend() = runTest {

        // "currentTime" em vez de System.currentTimeMillis()
        // é o relógio virtual do runTest — avança instantaneamente quando há delays
        val startTime = currentTime

        // MockGithubService é um serviço falso que simula as respostas da API do GitHub
        // devolve dados predefinidos com delays simulados, sem fazer pedidos reais à rede
        val result = loadContributorsSuspend(MockGithubService, testRequestData)

        Assert.assertEquals(
            "Wrong result for 'loadContributorsSuspend'",
            expectedResults.users, // resultado esperado (predefinido no teste)
            result // resultado obtido pela função
        )

        // calcula o tempo virtual total que a função demorou
        val totalTime = currentTime - startTime

        // verifica que os pedidos foram feitos SEQUENCIALMENTE —
        // na versão suspend cada pedido espera pelo anterior,
        // por isso o tempo total é a soma de todos os delays (~4400 ms virtuais)
        Assert.assertEquals(
            "The calls run sequentially, so the total virtual time should be around 4400 ms",
            expectedResults.timeFromStart, // tempo esperado
            totalTime // tempo virtual medido
        )
    }
}