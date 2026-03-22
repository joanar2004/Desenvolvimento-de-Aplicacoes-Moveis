// Esta classe representa a própria biblioteca. Ela funciona como um "gestor" de livros, guardando todos os objetos Book
// numa lista e fornece métodos para interagfir com esses livros, tal como adicionar, procurar, requisitar ou devolver.

package dam.exer_vl

class Library (val name: String){
    // Lista de livros. O mutableListOf é usado pois é necessário o poder de adicionar ou remover livros dinamicamente
    // durante a execução do programa.
    private val books = mutableListOf<Book>()

    // Companion object serve para guardar coisas que pertencem à classe toda e não a um objeto específico
    companion object {
        // Variável que guarda o número total de livros criados
        private var totalBooksCreated = 0

        // Função que devolve o total de livros criados
        fun getTotalBooksCreated(): Int {
            return totalBooksCreated
        }
    }

    // Metodo que é responsável por por adicionar um livro à biblioteca. Este apenas recebe um objeto Book e adiciona-o
    // à lista
    fun addBook(book: Book){
        totalBooksCreated ++
        books.add(book)
    }

    // Este metodo é para requisitar um livro através do seu título
    fun borrowBook(title: String){
        // A função find percorre toda a lista e devolve o primeiro elemento que satisfaça a condição do utilizador.
        // O ignoreCase, serve para ignorar se as letras são maiúsculas
        val book = books.find{it.title.equals(title, ignoreCase = true)}

        // Caso o livro não exista na biblioteca
        if (book == null){
            println("Erro: O livro '$title' não foi encontrado.")

        // Caso o livro exista e tenha cópias disponíveis
        } else if (book.availableCopies > 0){
            // Guardar o nújmero atual para moistrar na mensagem
            val remainingAfter = book.availableCopies - 1

            // Imprimir o sucesso antes de alterar o valor
            println("Sucesso: '${book.title}' requisitado com sucesso. Cópias em stock: ${remainingAfter}")

            // Alterar o valor que depois vai ativar o warning
            book.availableCopies--
        }
    }

    // Este metodo permite devolver qualquer livro à biblioteca. Processo semelhante ao do 'borrowBook'
    fun returnBook (title:String) {
        // Procurar o livro pelo título
        val book = books.find { it.title.equals(title, ignoreCase = true)}

        // Caso o livro exista, aumenta o número de cópias disponíveis do mesmo
        if (book != null) {
            book.availableCopies ++
            println("Confirmação: Livro devolvido com sucesso. Cópias em stock: ${book.availableCopies}")
        } else {
            println("Erro: Este livro não pertence à biblioteca.")
        }
    }

    // Este metodo mostra todos os livros que estão guardados na biblioteca
    fun showBooks() {
        println("\n--- Library Catalog ---")

        // Verificar se a lista está vazia
        if (books.isEmpty()) {
            println("A biblioteca está vazia.")

        // Caso existam livros, percorre a lista e imprime cada um. Graças ao toString das outras classses, o livro vai
        // já aparecer formatado
        } else {
            books.forEach { println(it) }
        }
    }

    // Metodo que permite pesquisar livros por autor
    fun searchByAuthor(author: String) {
        println("Livros do autor $author:")

        // A função filter devolve todos os elementos da lista que satisfazem a condição definida. Aqui não podia ser
        // usado o find, pois enquanto que n
        val foundBooks = books.filter { it.author.equals(author, ignoreCase = true) }

        // Se nmenhum for encontrado
        if (foundBooks.isEmpty()) {
            println("Nenhum livro encontrado para este autor.")

        // Caso contrário, imprime de todos os resultados
        } else {
            foundBooks.forEach { book ->
                println("- ${book.title} (${book.era}, ${book.availableCopies} cópias em stock)")
            }
        }
    }
}