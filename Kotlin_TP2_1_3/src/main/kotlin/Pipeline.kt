package main.kotlin

// 1. A Classe Pipeline (como fizemos antes)
class Pipeline {
    private val nomes = mutableListOf<String>()
    private val transformacoes = mutableListOf<(List<String>) -> List<String>>()

    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        nomes.add(name)
        transformacoes.add(transform)
    }

    // DESAFIO: Compose
    fun compose(nome1: String, nome2: String, novoNome: String) {
        // 1. Encontrar os índices das etapas originais
        val idx1 = nomes.indexOf(nome1)
        val idx2 = nomes.indexOf(nome2)

        if (idx1 != -1 && idx2 != -1) {
            val f1 = transformacoes[idx1]
            val f2 = transformacoes[idx2]

            // Criamos uma nova função que chama f1 e depois passa o resultado para f2
            // No Kotlin, (f1 andThen f2) seria: { input -> f2(f1(input)) }
            val composicao = { input: List<String> -> f2(f1(input)) }

            addStage(novoNome, composicao)
        }
    }

    // DESAFIO: Fork
    // Executa esta pipeline e outra pipeline externa com o mesmo input
    fun fork(outraPipeline: Pipeline, input: List<String>): Pair<List<String>, List<String>> {
        val resultado1 = this.execute(input)
        val resultado2 = outraPipeline.execute(input)
        return Pair(resultado1, resultado2)
    }

    fun execute(input: List<String>): List<String> {
        var resultado = input
        for (funcao in transformacoes) {
            resultado = funcao(resultado)
        }
        return resultado
    }

    fun describe() {
        println("Pipeline Stages:")
        nomes.forEachIndexed { i, nome -> println("${i + 1}. $nome") }
    }
}

// 2. A Função Top-Level
fun buildPipeline(config: Pipeline.() -> Unit): Pipeline {
    val p = Pipeline()
    p.config()
    return p
}
