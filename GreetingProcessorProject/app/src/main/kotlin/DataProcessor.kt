package app

import annotations.Extract

// Classe abstrata que recebe uma string de input no construtor
// Será estendida pela classe gerada automaticamente pelo processador
abstract class DataProcessor(val input: String) {

    // A anotação @Extract indica ao processador que deve gerar
    // um método que extrai o primeiro grupo da regex "Name: (\w+)"
    // Exemplo: "Name: John" -> retorna "John"
    @Extract(regex = "Name: (\\w+)")
    abstract fun getName(): String?

    // Igual ao anterior mas para extrair o endereço
    // Exemplo: "Address: 123 Street" -> retorna "123 Street"
    @Extract(regex = "Address: (.+)")
    abstract fun getAddress(): String?
}