package tasks

import contributors.MockGithubService
import contributors.progressResults
import contributors.testRequestData
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class Request6ProgressKtTest {
    @Test
    // "runTest" permite verificar os momentos exatos em que os resultados intermédios chegam
    // sem esperar tempo real — os delays são simulados virtualmente
    fun testProgress() = runTest {

        // marca o início do tempo virtual
        val startTime = currentTime

        // índice para percorrer os resultados intermédios esperados um a um
        var index = 0

        // loadContributorsProgress chama o callback após cada repositório ser carregado
        // como os pedidos são SEQUENCIAIS, cada resultado chega depois do anterior
        loadContributorsProgress(MockGithubService, testRequestData) { users, _ ->

            // obtém o resultado intermédio esperado para este passo
            // progressResults contém os estados esperados após cada repositório:
            // ex: após repo1 -> [userA: 10], após repo2 -> [userA: 15, userB: 5], etc.
            val expected = progressResults[index++]

            // tempo virtual decorrido desde o início até este resultado intermédio chegar
            val time = currentTime - startTime

            // verifica que o resultado intermédio chegou no momento virtual correto
            // como os pedidos são sequenciais, os momentos são cumulativos:
            // 1º resultado aos 1000ms, 2º aos 2000ms, 3º aos 3000ms, etc.
            Assert.assertEquals(
                "Expected intermediate results after ${expected.timeFromStart} ms:",
                expected.timeFromStart, // momento virtual esperado para este resultado
                time                    // momento virtual em que o resultado chegou
            )

            // verifica que os dados acumulados até este momento estão corretos
            // a lista deve estar sempre no estado agregado (sem duplicados, ordenada)
            Assert.assertEquals(
                "Wrong intermediate results after $time:",
                expected.users, // lista acumulada esperada até este ponto
                users           // lista acumulada recebida
            )
        }
    }
}
