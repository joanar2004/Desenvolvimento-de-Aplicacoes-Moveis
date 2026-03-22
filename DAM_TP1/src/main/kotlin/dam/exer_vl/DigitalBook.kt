// Esta classe representa um livro digital. Esta é uma subclasse / filha da suoperclasse / classe pai 'Book'. De forma a
// reutiliza a estrutura original do livro 'Book'
package dam.exer_vl

class DigitalBook (
    // Como a classe pai já possui o val, aqui não é necessário colocar, pois aqui apenas é para aceitar um argumento
    // temporário para ele depois subir para a classe 'Book'
    title: String,
    author: String,
    publicationYear: Int,
    availableCopiesParam: Int,

    // Propriedades próprias do 'DigitalBook' que representam, respetivamente o tamanho do ficheiro e o formato do mesmo
    val fileSize: Double,
    val format: String

    // Chamada ao construtor da classe pai para inicializar
) : Book(title, author, publicationYear, availableCopiesParam) {

    // Metodo obrigatório implementado na classe pai. Como aqui estamos perante um livro digital, o 'storageInfo' vai
    // corresponder ao tamanho, em MB do livro.
    override fun getStorageInfo(): String {
        return "Storage: Stored digitally: ($fileSize MB), Format: $format"
    }

    // A classe pai também já tem esta função, no entanto, aqui para além das informações que lá vêm, são acrescentadas
    // o formato, pois é digital e o tamanho do ficheiro.
    override fun toString(): String {
        return "${super.toString()}\n Storage: Stored digitally: $fileSize MB, Format: $format"
    }
}