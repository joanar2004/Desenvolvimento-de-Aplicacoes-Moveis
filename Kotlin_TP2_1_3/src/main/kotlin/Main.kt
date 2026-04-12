package main.kotlin


fun main() {
    val logs = listOf(
        " INFO : server started ",
        " ERROR : disk full ",
        " DEBUG : checking config ",
        " ERROR : out of memory ",
        " INFO : request received ",
        " ERROR : connection timeout "
    )

    // Construção da Pipeline conforme os requisitos do Passo 3
    val myPipeline = buildPipeline {
        addStage("Trim") { lista ->
            lista.map { it.trim() }
        }
        addStage("Filter errors") { lista ->
            lista.filter { it.contains("ERROR") }
        }
        addStage("Uppercase") { lista ->
            lista.map { it.uppercase() }
        }
        addStage("Add index") { lista ->
            lista.mapIndexed { i, s -> "${i + 1}. $s" }
        }
    }

    // 4. Imprimir a descrição
    myPipeline.describe()

    // Executar e imprimir o resultado final
    println("\nFinal Result:")
    val result = myPipeline.execute(logs)
    result.forEach { println(it) }

    // Testar o Desafio
    val p1 = buildPipeline {
        addStage("Trim") { it.map { s -> s.trim() } }
        addStage("Upper") { it.map { s -> s.uppercase() } }

        // Compor Trim + Upper numa única etapa chamada "LimpezaTotal"
        compose("Trim", "Upper", "LimpezaTotal")
    }


    val p2 = buildPipeline {
        addStage("ApenasErros") { lista ->
            lista.filter { it.contains("error", ignoreCase = true) }
        }
    }

    val logs_desafio = listOf("  info  ", "  error  ")

    // Testando o Fork (Executa p1 e p2 ao mesmo tempo)
    val (res1, res2) = p1.fork(p2, logs_desafio)

    println("Resultado P1: $res1")
    println("Resultado P2: $res2")
}