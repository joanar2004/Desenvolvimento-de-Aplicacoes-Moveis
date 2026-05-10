package tasks

import contributors.MockGithubService
import contributors.concurrentProgressResults
import contributors.testRequestData
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class Request7ChannelsKtTest {
    @Test
    // "runTest" permite verificar os momentos exatos em que os resultados chegam pelo channel
    // como os pedidos são concorrentes, os resultados chegam muito mais cedo que no PROGRESS
    fun testChannels() = runTest {

        // marca o início do tempo virtual
        val startTime = currentTime

        // índice para percorrer os resultados intermédios esperados
        var index = 0

        // loadContributorsChannels chama o callback sempre que um resultado chega pelo channel
        // como os pedidos correm em PARALELO, os resultados chegam assim que ficam prontos
        // não necessariamente pela ordem dos repositórios!
        loadContributorsChannels(MockGithubService, testRequestData) { users, _ ->

            // obtém o resultado intermédio esperado para este passo
            // concurrentProgressResults é DIFERENTE de progressResults —
            // os momentos são mais cedo porque os pedidos correm em paralelo
            // ex: o primeiro resultado pode chegar aos 800ms em vez de 1000ms
            val expected = concurrentProgressResults[index++]

            // tempo virtual decorrido desde o início até este resultado chegar pelo channel
            val time = currentTime - startTime

            // verifica que o resultado chegou no momento virtual correto
            // com channels e concorrência, os primeiros resultados chegam mais cedo
            // porque não precisam de esperar pelos pedidos anteriores terminarem
            Assert.assertEquals(
                "Expected intermediate results after ${expected.timeFromStart} ms:",
                expected.timeFromStart, // momento virtual esperado (mais cedo que no PROGRESS)
                time                    // momento virtual em que chegou pelo channel
            )

            // verifica que os dados acumulados até este momento estão corretos
            Assert.assertEquals(
                "Wrong intermediate results after $time:",
                expected.users, // lista acumulada esperada
                users           // lista acumulada recebida pelo channel
            )
        }
    }
}
