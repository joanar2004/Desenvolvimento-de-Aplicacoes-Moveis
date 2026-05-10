package main.kotlin

// A Pipeline é basicamente uma lista de transformações de texto que se executam em sequência.
// Cada transformação recebe uma lista de strings e devolve outra lista de strings.
class Pipeline {

    // Duas listas que andam sempre a par — o índice 0 de nomes corresponde ao índice 0 de transformacoes.
    // mutableListOf cria uma lista que pode crescer/encolher em runtime (ao contrário de listOf que é fixa).
    private val nomes = mutableListOf<String>()

    // O tipo (List<String>) -> List<String> descreve uma função — não um valor normal.
    // Ou seja, esta lista não guarda strings nem números, guarda funções em si mesmas.
    // Em Kotlin, funções são "first-class citizens": podem ser guardadas em variáveis e passadas como argumentos.
    private val transformacoes = mutableListOf<(List<String>) -> List<String>>()

    // Adiciona uma nova etapa à pipeline.
    // O parâmetro 'transform' é uma função — quem chamar addStage decide o que ela faz.
    // Isto chama-se "higher-order function": uma função que recebe outra função como argumento.
    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        nomes.add(name)
        transformacoes.add(transform)
        // As duas listas crescem sempre juntas, por isso o índice de um corresponde sempre ao outro.
    }

    // Cria uma nova etapa que é a combinação de duas etapas já existentes.
    // Em vez de aplicar f1 e f2 separadamente, criamos uma função única que faz os dois passos.
    fun compose(nome1: String, nome2: String, novoNome: String) {

        // indexOf devolve a posição do elemento na lista, ou -1 se não existir.
        val idx1 = nomes.indexOf(nome1)
        val idx2 = nomes.indexOf(nome2)

        // Verificamos se ambos os nomes existem antes de tentar aceder às listas.
        // Sem esta verificação, aceder com índice -1 causaria um IndexOutOfBoundsException.
        if (idx1 != -1 && idx2 != -1) {
            val f1 = transformacoes[idx1]
            val f2 = transformacoes[idx2]

            // Criamos uma nova função (lambda) que encadeia f1 e f2.
            // A sintaxe { input: List<String> -> ... } é uma lambda — uma função anónima definida no momento.
            // 'input' é o argumento, e o que está depois do '->' é o corpo (o que ela executa e devolve).
            // f1(input) corre primeiro, e o resultado é passado directamente a f2.
            // Isto chama-se composição de funções: f2(f1(x)) — equivalente a (f1 andThen f2).
            val composicao = { input: List<String> -> f2(f1(input)) }

            // A função composta é adicionada como qualquer outra etapa normal.
            addStage(novoNome, composicao)
        }
    }

    // Executa a mesma pipeline em paralelo com outra, usando o mesmo input para as duas.
    // Devolve um Pair — uma estrutura do Kotlin que agrupa dois valores relacionados.
    // Pair<List<String>, List<String>> significa que o primeiro e segundo elemento são ambos listas de strings.
    fun fork(outraPipeline: Pipeline, input: List<String>): Pair<List<String>, List<String>> {
        val resultado1 = this.execute(input)       // corre esta pipeline
        val resultado2 = outraPipeline.execute(input) // corre a outra com o mesmo input original
        // Pair(a, b) cria o par. Acede-se depois com .first e .second.
        return Pair(resultado1, resultado2)
    }

    // Executa todas as transformações em sequência.
    // O output de cada função é o input da seguinte — é o coração da pipeline.
    fun execute(input: List<String>): List<String> {
        // 'var' em vez de 'val' porque este valor vai ser substituído a cada iteração.
        // 'val' seria imutável — não podíamos reatribuir. 'var' permite reatribuição.
        var resultado = input

        // for..in itera directamente sobre os elementos da lista (não sobre índices).
        // A cada iteração, 'resultado' é substituído pelo output da função actual.
        for (funcao in transformacoes) {
            resultado = funcao(resultado)
        }

        return resultado
    }

    // Imprime os nomes das etapas por ordem, útil para perceber o que está configurado.
    fun describe() {
        println("Pipeline Stages:")
        // forEachIndexed é como um forEach mas dá-nos também o índice de cada elemento.
        // O 'i' é o índice (começa em 0), o 'nome' é o elemento actual.
        // ${i + 1} usa string template do Kotlin — qualquer expressão dentro de ${} é avaliada e inserida na string.
        nomes.forEachIndexed { i, nome -> println("${i + 1}. $nome") }
    }
}


 //Cria e configura uma nova instância de Pipeline.
 //param config Uma função (lambda) que recebe um Pipeline como argumento e não devolve nada (Unit).
 // É aqui que são definidas as etapas (stages) da pipeline.
 // return O objeto Pipeline já configurado.

fun buildPipeline(config: (Pipeline) -> Unit): Pipeline {
    // 1. Instanciação: Criamos o objeto que vai ser configurado.
    val p = Pipeline()

    // 2. Configuração: Chamamos a função 'config' passando o nosso objeto 'p'.
    // O código que escreves dentro das chavetas { ... } no main é executado aqui.
    // O 'it' que usas lá no main representa este objeto 'p' que estamos a passar.
    config(p)

    // 3. Entrega: Depois de correr todas as instruções (addStage, compose, etc.),
    // devolvemos o objeto pronto para ser usado.
    return p
}