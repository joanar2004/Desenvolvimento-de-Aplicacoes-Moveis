// Este código simula o comportamento de uma bola que quica, calculando a altura de cada salto sucessivo. Utilizando uma
// enerateSequence, o programa aplica uma perda de energia constante (cada salto atinge 60% da altura anterior),
// começando nos 100 metros. O uso de funções como drop(1) para ignorar a queda inicial e takeWhile para interromper o
// cálculo assim que a altura for inferior a 1 metro demonstra um uso avançado de sequências preguiçosas (lazy
// evaluation) em Kotlin. O código é finalizado com uma formatação precisa de duas casas decimais, garantindo que a
// simulação apresente no máximo 15 saltos de forma clara e controlada.

package dam.exer_3

fun main() {
    println("--- Simulação de Saltos da Bola ---")

    val listaAlturas = generateSequence(100.0) { it * 0.60 }
        .drop(1) // Ignora a queda inicial de 100m
        .takeWhile { it >= 1.0 } // Para a sequência. Teve de substituir o filter pois entrava em loop infinito
        .take(15) // Garante que não passamos dos 15 saltos
        .toList() // Converte para lista


    if (listaAlturas.isEmpty()) {
        println("A lista está vazia!")
    } else {
        listaAlturas.forEachIndexed { index, altura ->
            println("Salto ${index + 1}: %.2f metros".format(altura))
        }
    }

    println("--- Fim da Simulação ---")
}