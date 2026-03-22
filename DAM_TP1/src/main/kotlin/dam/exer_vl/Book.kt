// Esta classe, criada para a virtual library representa a estrutura de qualquer objeto do tipo livro.
// O abstract serve para que esta classe não possa criar objetos diretamente através dela.

package dam.exer_vl // Definir o caminho e o package da classe

// Apenas podem ser criadas instâncias de subclasses que estendam esta, neste caso, DigitalBook e RealBook
abstract class Book (
    // 'val' cria propriedades apenas de leitura após a criação do objeto
    // Neste caso o val permite que, ao registar um livro este já não seja alterado, pois estas informações mantêm-se
    // sempre as mesmas
    val title: String,
    val author: String,

    // Estes parâmetros são apenas argumentos do construtor. Eles existem apenas durante a inicialização do objeto e
    // depois servem para calcular outras propriedades do objeto
    publicationYear: Int,
    availableCopiesParam: Int
){

    // Esta propriedade calcula a era do livro.
    // Ela usa um "switch" em kotlin, ou seja, um "when". Segundo a informação dada pelo professor, é classificadoi como
    // moderno se estiver entre 1980 e 2010 e clássico caso seja antes de 1980
    val era: String = when {
        publicationYear < 1980 -> "Classic"
        publicationYear in 1980..2010 -> "Modern"
        else -> "Contemporary"
    }

    // Esta propriedade representa o número de cópias disponíveis do sistema
    // O custom setter faz com que o valor nunca fique negativo (inválido no inventário). O field, no kotlin é criado
    // automaticamente para armazenar um valor de uma propriedade. Se usássemos 'availableCopies' iria ficar uma chamada
    // recursiva dele.
    var availableCopies: Int = availableCopiesParam
        set(value) {
            // Impede que o número de cópias seja negativo
            if (value < 0) return
            field = value
            // Feedback visual: avisa se o stock chegar a zero
            if (field == 0) println("Warning: O livro '$title' já não existe em stock!")
        }

    // No Kotlin, o init pode ser usado para inicializações adicionais ou criação de objetos. Neste caso é apenas usado
    // para mostrar no terminal quando qualquer livro é registado.
    init {
        println("Book '$title' by $author has been added to the library.")
    }

    // Metodo a ser implementado pelos filhos desta classe. Existe, pois os livros podem estar em em formato físicoo ou
    // digital ou físico
    abstract fun getStorageInfo(): String


    override fun toString() : String {
        return "Title: $title, Author: $author, Era: $era, Available: $availableCopies copies"
    }
}