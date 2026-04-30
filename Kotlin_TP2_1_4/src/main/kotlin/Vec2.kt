package main.kotlin

import kotlin.math.sqrt

// data class é ideal para representar dados puros como um vector matemático.
// O compilador gera automaticamente equals(), hashCode(), toString() e copy() com base nos parâmetros.
// Comparable<Vec2> é uma interface do Kotlin — ao implementá-la, activamos os operadores >, <, >=, <= entre Vec2s.
data class Vec2(val x: Double, val y: Double) : Comparable<Vec2> {

    // Sobrecarga do operador +
    // Em Kotlin, operadores são funções com nomes especiais marcadas com 'operator'.
    // Quando escreves v1 + v2, o Kotlin traduz isso para v1.plus(v2) automaticamente.
    // Devolve um NOVO Vec2 — não modifica os originais (val não pode ser reatribuído).
    operator fun plus(other: Vec2): Vec2 {
        // 'x' e 'y' referem-se ao vector da esquerda (this), other.x e other.y ao da direita.
        return Vec2(x + other.x, y + other.y)
    }

    // Sobrecarga do operador -
    // Mesma lógica que o plus, mas subtrai as componentes.
    // v1 - v2 → v1.minus(v2)
    operator fun minus(other: Vec2): Vec2 {
        return Vec2(x - other.x, y - other.y)
    }

    // Sobrecarga do operador * para multiplicação escalar.
    // Aqui o parâmetro não é outro Vec2 mas sim um Double — o tipo pode mudar.
    // v1 * 2.0 → v1.times(2.0) — escala o vector pelo factor dado.
    operator fun times(scalar: Double): Vec2 {
        return Vec2(x * scalar, y * scalar)
    }

    // Sobrecarga do operador unário - (negação).
    // "Unário" significa que actua sobre um único valor, sem segundo operando.
    // -v1 → v1.unaryMinus() — inverte a direcção do vector.
    // Não recebe parâmetros porque age sobre si próprio (this).
    operator fun unaryMinus(): Vec2 {
        return Vec2(-x, -y)
    }

    // Implementação obrigatória da interface Comparable<Vec2>.
    // 'override' porque estamos a substituir o comportamento definido na interface.
    // Comparamos vectores pela sua magnitude (tamanho), não pelas coordenadas directamente.
    // Contrato do compareTo:
    //   negativo → this é menor que other
    //   zero     → são iguais
    //   positivo → this é maior que other
    override operator fun compareTo(other: Vec2): Int {
        // Comparamos x² + y² em vez de sqrt(x² + y²) porque:
        // se a² > b², então a > b — a raiz quadrada não muda a ordem, só adiciona custo computacional.
        val thisMag = this.x * this.x + this.y * this.y
        val otherMag = other.x * other.x + other.y * other.y

        return when {
            thisMag < otherMag -> -1
            thisMag > otherMag -> 1
            else -> 0
        }
    }

    // Calcula o comprimento (magnitude) do vector.
    // Fórmula de Pitágoras: √(x² + y²)
    // sqrt() vem do import kotlin.math.sqrt no topo do ficheiro.
    fun magnitude(): Double {
        return sqrt(x * x + y * y)
    }

    // Calcula o produto escalar (dot product) entre dois vectores.
    // Fórmula: (x1 * x2) + (y1 * y2)
    // Devolve um Double, não um Vec2 — o produto escalar é um número, não um vector.
    // Útil para calcular ângulos entre vectores ou projecções.
    fun dot(other: Vec2): Double {
        return this.x * other.x + this.y * other.y
    }

    // Devolve o vector normalizado — mesma direcção, mas com magnitude igual a 1.0.
    // Um vector unitário é útil quando só importa a direcção, não o tamanho.
    fun normalized(): Vec2 {
        val mag = magnitude()

        // Normalizar um vector zero causaria divisão por zero — lançamos excepção explícita.
        // IllegalStateException indica que o objecto está num estado inválido para esta operação.
        if (mag == 0.0) {
            throw IllegalStateException("O vetor zero não pode ser normalizado (divisão por zero).")
        }

        // Dividir cada componente pela magnitude "ajusta" o tamanho para 1.0 sem mudar a direcção.
        return Vec2(x / mag, y / mag)
    }

    // Sobrecarga do operador [] (indexação).
    // Permite aceder às componentes como se Vec2 fosse uma lista: v[0] → x, v[1] → y.
    // v[0] → v.get(0) — o Kotlin traduz os [] para uma chamada a get().
    operator fun get(index: Int): Double {
        return when (index) {
            0 -> x
            1 -> y
            // 'else' obrigatório aqui porque Int tem infinitos valores possíveis — o compilador não consegue
            // garantir que todos os casos estão cobertos sem ele (ao contrário da sealed class).
            else -> throw IndexOutOfBoundsException("Índice $index inválido para Vec2. Usa 0 para x ou 1 para y.")
        }
    }
}

// Extension function em Double — adiciona um metodo à classe Double sem modificar o código original.
// Permite a sintaxe 2.0 * vetor (escalar à esquerda), que sem isto não funcionaria.
// Sem esta função, só funcionaria vetor * 2.0, porque times() está definido no Vec2.
// 'this' aqui refere-se ao Double (o escalar), não ao Vec2.
operator fun Double.times(vec: Vec2): Vec2 {
    // Reutiliza a lógica já definida dentro de Vec2 — sem duplicar código.
    return vec.times(this)
}