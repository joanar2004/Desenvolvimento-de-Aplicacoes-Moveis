package main.kotlin

import kotlin.math.sqrt

 // A 'data class' é ideal para representar conceitos matemáticos como vetores.
 // Ela já cria automaticamente métodos para comparar (equals) e imprimir (toString) os dados.
data class Vec2(val x: Double, val y: Double) : Comparable<Vec2> {

     // SOBRECARGA DO OPERADOR +
     // Quando escreves `v1 + v2`, o Kotlin chama esta função.
     // @param other: O segundo vetor (o que está à direita do sinal +).
     // Dá return de um NOVO objeto Vec2 com as coordenadas somadas
    operator fun plus(other: Vec2): Vec2 {
        // x e y referem-se ao primeiro vetor (v1)
        // other.x e other.y referem-se ao segundo vetor (v2)
        return Vec2(x + other.x, y + other.y)
    }


     // SOBRECARGA DO OPERADOR -
     // Funciona exatamente como a soma, mas retira os valores do 'other'.
    operator fun minus(other: Vec2): Vec2 {
        return Vec2(x - other.x, y - other.y)
    }
     
     // SOBRECARGA DO OPERADOR * (Multiplicação Escalar)
     // Aqui o tipo muda! Não recebemos outro Vec2, mas sim um número Double.
     // @param scalar: O fator de escala (ex: 2.0 para duplicar o tamanho).
     // Dá return de um novo vetor onde cada componente foi multiplicado pelo número.
    operator fun times(scalar: Double): Vec2 {
        return Vec2(x * scalar, y * scalar)
    }


     // SOBRECARGA DO OPERADOR UNÁRIO - (Negação)
     // Este é "unário" porque só atua sobre UM vetor (ex: -v1).
     // Não recebe nenhum parâmetro (não há 'other') porque ele age sobre si próprio.
     // @return: Um novo vetor com as direções invertidas.7
    operator fun unaryMinus(): Vec2 {
        // Simplesmente coloca um sinal de menos antes das coordenadas atuais
        return Vec2(-x, -y)
    }


     // O CompareTo compara dois vetores, quando ele é implementado o kotlin ativa automaticamente o >, <, <=, >=.
     // Retorna um número NEGATIVO se 'this' for menor do que 'other'
     // Retorna ZERO se forem iguais
     // Retorna um número POSITIVO se 'this' for maior do que 'other'
     override operator fun compareTo(other: Vec2): Int {
         // Para comparar o tamanho de dois vetores, usamos a fórmula: x^2 + y^2
         // Não é necessário a^2 > b^2, pois a > b
         val thisMag = this.x * this.x + this.y * this.y
         val otherMag = other.x * other.x + other.y * other.y

         // Fazer comparações
         if (thisMag < otherMag) {
             return -1
         } else if (thisMag > otherMag) {
             return 1
         } else {
             return 0
         }
     }

     // Calcula a magnitude
     // Usar o teorema de pitágoras
     // Fórmula: sqrt(x^2 + y^2)
     fun magnitude(): Double {
         return sqrt(x * x + y * y)
     }

     // Calcula o produto escalar entre dois vetores
     // Fórmula: (x1 * x2) + (y1 * y2)
     fun dot (other: Vec2): Double {
         return this.x * other.x + this.y * other.y
     }

     // Devolver um vetor normalizado (Vetor Unitário)
     // Um vetor normalizado tem a mesma direção que o original, mas magnitude igual a 1.0
     fun normalized(): Vec2 {
         val mag = magnitude()

         // Não podemos normalizar um vetor de comprimento zero
         if (mag == 0.0) {
             throw IllegalStateException("O vetor zero não pode ser normalizado (divisão por zero).")
         }

         // Dividimos cada com,ponente pela magnitude total para "ajustar" o tamanho para 1.0
         return Vec2(x / mag, y / mag)
     }

     // Permitir aceder às coordenadas
     // Recebe como parâmetros o índice das coordenadas (0 ou 1)
     // O valor Double corresponde à coordenada
     // Tem exceção caso o índice não seja 0 nem 1
     operator fun get (index: Int): Double {
         return when (index) {
             0 -> x // se o índice for 0, devolvemos a coordenada x
             1 -> y // se o índice for 1, devolvemos a coordenada y
             else -> throw IndexOutOfBoundsException("Índice $index inválido para Vec2. Usa 0 para x ou 1 para y.")
         }
     }

}

// É uma 'extension function' da classe Double
// Permite fazer 2.0 * vetor
operator fun Double.times(vec: Vec2): Vec2 {
    // Reutilizar a lógica que já usei dentro da classe Vec2
    return vec.times(this)
}