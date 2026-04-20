package app

fun main() {
    // String de input que contém o nome e o endereço
    val input = "Name: John Address: 123 Street"

    // Usa a classe gerada automaticamente pelo RegexProcessor
    // O DataProcessorExtractor ainda vai aparecer a vermelho - é normal!
    // Só vai existir depois de compilar com kapt
    val extractor = DataProcessorExtractor(input)

    // Extrai e imprime o nome usando a regex "Name: (\w+)"
    println("Name: ${extractor.getName()}")

    // Extrai e imprime o endereço usando a regex "Address: (.+)"
    println("Address: ${extractor.getAddress()}")
}