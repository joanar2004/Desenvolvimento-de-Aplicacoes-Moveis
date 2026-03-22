//O programa corre dentro de um ciclo infinito (while (true)), o que significa que não se desliga após uma conta,
// permitindo várias operações seguidas. A escolha do when em vez de múltiplos if/else torna o código muito mais legível
// fácil de expandir.
// Além disso, o uso de tratamento de exceções (try-catch) garante que, se o utilizador escrever uma letra onde deveria
// estar um número, o programa não "morre", mas sim avisa do erro e volta ao início.

package dam.exer_2

fun main() {
    println("Bem Vindo à Calculadora!")

    // O loop 'while' mantém o programa ativo
    while (true) {
        try {
            executarCalculadora()
        } catch (e: Exception) {
            // O catch 'apanha' qualquer erro (como letras em vez de números)
            println("Erro: ${e.message}. Tente novamente.")
        }
    }
}

fun executarCalculadora(){
    println("\n Escolha o tipo de operação que quer realizar:")
    println("1. Aritmética (+, -, *, /)")
    println("2. Booleana (&&, ||, !)")
    println("3. Bitwise (shl, shr)")

    val opcao_utilizador = readln()

    // O 'when' decide qual função chamar com base na escolha do utilizador
    when (opcao_utilizador) {
        "1" -> operacoesAritmeticas()
        "2" -> operacoesBooleanas()
        "3" -> operacoesBitwise()
        else -> println ("Operação Inválida.")
    }
}

fun operacoesAritmeticas() {
    println("Escreva o primeiro número:")
    // .replace transforma a vírgula em ponto para o Kotlin aceitar decimais
    // .toDouble() permite números com casas decimais
    val n1 = readln().replace(",", ".").toDouble()

    println("Escreva a operação (+, -, *, /):")
    val operacao = readln().trim()

    println("Escreva o segundo número:")
    val n2 = readln().replace(",", ".").toDouble()

    // O when retorna o valor do cálculo diretamente para a variável resultado_final
    val resultado_final = when (operacao) {
        "+" -> n1 + n2
        "-" -> n1 - n2
        "*" -> n1 * n2
        "/" -> {
            // Verificação para evitar erro matemático de divisão por zero
            if (n2 == 0.0) throw ArithmeticException("Divisão por zero!")
            n1 / n2
        }
        else -> throw Exception("Operador Inválido!")
    }

    val hexadecimal = Integer.toHexString(resultado_final.toInt()).uppercase()

    println("Resultado: $resultado_final")
    println("Hexadecimal (parte inteira): 0x$hexadecimal")
}

fun operacoesBooleanas(){
    print("Digite o primeiro valor (true/false): ")
    // .toBoolean() converte o texto do utilizador no tipo lógico Boolean
    val b1 = readln().trim().toBoolean()

    print("Escreva a operação (&&, ||, !): ")
    val op = readln().trim()

    // O operador '!' (NOT) só precisa de um valor
    if (op == "!") {
        println("Resultado: ${!b1}")
        return // O 'return' sai da função e não pede o segundo valor
    }

    print("Escreva o segundo valor (true/false): ")
    val b2 = readln().trim().toBoolean()

    val resultado = when (op) {
        "&&" -> b1 && b2 // AND: só é true se ambos forem true
        "||" -> b1 || b2 // OR: é true se pelo menos um for true
        else -> "Operação Inválida"
    }

    println("Resultado Booleano: $resultado")
}

fun operacoesBitwise(){
    println("Escreva um número inteiro:")
    // Operações de bits exigem números inteiros (Int)
    val numero1 = readln().trim().toInt()

    println("Escolha a operação (shl / shr):")
    val operacao_bitwise = readln().trim()

    println("Quantidade de bits a deslocar:")
    val bits = readln().trim().toInt()

    val resultado_bitwise = when (operacao_bitwise) {
        "shl" -> numero1 shl bits // Shift Left: empurra bits para a esquerda
        "shr" -> numero1 shr bits // Shift Right: empurra bits para a direita
        else -> throw Exception("Operação bitwise inválida.")
    }

    println("Resultado: $resultado_bitwise")
    println("Hexadecimal: 0x${Integer.toHexString(resultado_bitwise).uppercase()}")
}