// Esta classe representa um livro físico dentro do sistema da biblioteca. Tal como a classe DigitalBook, esta classe
// herda da classe Book.

package dam.exer_vl

class PhysicalBook(
    // Estes parâmetros são recebidos  pelo construtor da subclasse e depois são passados para o construtor da classe
    // pai.

    title: String,
    author: String,
    publicationYear: Int,
    availableCopiesParam: Int,

    // Estas propriedade, indicam, respetivamente o peso do livro físico e se o livro tem ou não capa dura. O kotlin
    // assume que todos os livros têm capa dura até porova em contrário
    val weight: Int,
    val hasHardCover: Boolean = true

    // Chamar o construtor da classe Book que garante que todas as propriedades comuns ao livro são inicializados de
    // forma correta
) : Book (title, author, publicationYear, availableCopiesParam){

    // Este metodo tem de ser implementado por todas as subclasses da classe 'Book'. Neste caso, no livro físico, este
    // refere-se ao facto de o livro ter ou não capa rija e qual o peso do livro, em gramas.
    override fun getStorageInfo(): String {
        val type = if (hasHardCover) "Yes" else "No"
        return "Storage: Physical Storage: $weight g, Hardcover:($type)"
    }

    // Apesar da classe 'Book' já possuir este metodo desenvolvido, vai ser acrescentado a ele, no caso de um livro
    // físico, o peso do livro e qual o tipo de capa que o memso tem, para além de mostrar tudo o que está definido
    // nesta função na classe pai.
    override fun toString(): String {
        val cover = if (hasHardCover) "Yes" else "No"
        return "${super.toString()} \n Storage: Physical book: $weight g, Hardcover: $cover"
    }

}