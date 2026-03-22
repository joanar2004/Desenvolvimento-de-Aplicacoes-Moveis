//Este código demonstra três formas diferentes de criar e preencher uma coleção em Kotlin com os quadrados dos números
// de 1 a 50. O ponto central é a comparação entre performance e legibilidade: a opção (a) utiliza IntArray para
// manipular tipos primitivos de forma eficiente em memória; a (b) foca no estilo funcional usando ranges e a função
// map, que é mais intuitiva mas gera uma lista de objetos; e a (c) utiliza o construtor genérico Array<Int> para uma
// abordagem mais estruturada. No final, todas as abordagens utilizam a função joinToString() para formatar e exibir os
// resultados de forma legível no console.

package dam.exer_1

fun main() {
    // --- OPÇÃO (a): Foco em Performance ---
    println("(a) - Using IntArray constructor")

    // IntArray é um array de tipos primitivos. O índice começa em 0 e acaba em 49
    // Como queremos de 1 a 50, fazemos (i + 1).
    val quadrados_a = IntArray(50) { i -> (i + 1) * (i + 1) }

    // joinToString() transforma a lista num texto
    println(quadrados_a.joinToString())
    println("-------------------")


    // --- OPÇÃO (b): Foco em Legibilidade (Estilo Funcional) ---
    println("(b) - Using a range and map()")

    // (1 .. 50) cria um "intervalo" de números de 1 a 50.
    // .map { ... } transforma cada número desse intervalo aplicando uma regra
    // 'it' é o padrão se dá ao número atual
    val quadrados_b = (1 .. 50)
        .map { it * it }

    println(quadrados_b.joinToString())
    println("-------------------")


    // --- OPÇÃO (c): Foco em Estrutura Explícita ---
    println("(c) Using Array with constructor")

    // Array<Int> cria um array de objetos (Integer)
    val quadrados_c = Array(50) { i ->
        val numero = i + 1
        numero * numero
    }

    println(quadrados_c.joinToString())
    println("------------------")
}