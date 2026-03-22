// Esta classe representa um membro da biblioteca. As data classes são usadas para guardar dados

package dam.exer_vl

data class LibraryMember (
    // Gurdar o nome do membro
    val name: String,

    // Guarda o número de membro da biblioteca
    val membership: String,

    // Lista de livros que o membro pediu emprestado. Usei o mutableList pois a lista pode mudar (adicionar ou remover
    // livros)
    val borrowedBooks: MutableList<String> = mutableListOf()
)