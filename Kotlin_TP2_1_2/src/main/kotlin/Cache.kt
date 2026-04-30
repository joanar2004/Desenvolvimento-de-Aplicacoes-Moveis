package main.kotlin

// Cache é uma classe genérica de armazenamento chave-valor.
// Os <K, V> são "type parameters" — espaços reservados para os tipos reais.
// Ao instanciar, decides o que K e V são: Cache<String, Int>, Cache<Int, List<String>>, etc.
// Isto evita duplicar código para cada combinação de tipos possível.
class Cache<K, V> {

    // O storage é o único estado da classe — tudo passa por aqui.
    // MutableMap<K, V> é uma interface do Kotlin para mapas que permitem alterações em runtime.
    // É private para que ninguém de fora manipule os dados directamente — só através dos métodos abaixo.
    // mutableMapOf() cria uma implementação concreta (LinkedHashMap) que mantém a ordem de inserção.
    private val storage: MutableMap<K, V> = mutableMapOf()

    // Escreve ou sobrescreve um valor no mapa.
    // storage[key] = value é syntactic sugar do Kotlin para storage.put(key, value).
    // Se a chave já existir, o valor anterior é substituído silenciosamente.
    fun put(key: K, value: V) {
        storage[key] = value
    }

    // Devolve o valor associado à chave, ou null se a chave não existir.
    // O '?' no tipo de retorno V? indica que o valor pode ser null — é o sistema de null safety do Kotlin.
    // Quem chama get() é obrigado a lidar com a possibilidade de receber null antes de usar o valor.
    fun get(key: K): V? {
        return storage[key]
    }

    // Remove uma entrada do mapa pelo key.
    // remove() é um método do MutableMap — não existe em Map normal (imutável).
    // Se a chave não existir, não faz nada (não lança excepção).
    fun evict(key: K) {
        storage.remove(key)
    }

    // Devolve o número de entradas actualmente no cache.
    // .size é uma propriedade do Map — em Kotlin acede-se sem parênteses (não é um método).
    fun size(): Int {
        return storage.size
    }

    // A função principal do padrão cache: tenta ler, e só calcula se não existir.
    // O parâmetro 'default' é uma lambda sem argumentos que devolve V — representada por () -> V.
    // Isto chama-se "lazy evaluation": o cálculo dentro de default() só acontece se mesmo necessário.
    fun getOrPut(key: K, default: () -> V): V {
        val cachedValue = storage[key]

        // Se o valor já existir no mapa, devolve-o imediatamente sem executar default.
        if (cachedValue != null) {
            return cachedValue
        } else {
            // default() executa o bloco que o utilizador passou como argumento.
            // O programa "salta" para esse código, corre-o, e o resultado volta para cá.
            val newValue = default()
            // Guardamos o resultado para que na próxima chamada com a mesma chave não seja recalculado.
            storage[key] = newValue
            return newValue
        }
    }

    // Aplica uma função de transformação ao valor existente e guarda o novo resultado.
    // O parâmetro 'action' é uma higher-order function: recebe o valor actual e devolve o novo valor.
    // O tipo (V) -> V significa: função que recebe V e devolve V (o tipo não muda, só o valor).
    // Devolve Boolean para indicar se a operação foi feita ou não — útil para quem chama saber o que aconteceu.
    fun transform(key: K, action: (V) -> V): Boolean {
        val currentValue = storage[key]

        if (currentValue != null) {
            // action(currentValue) chama a lambda passada pelo utilizador com o valor actual.
            val newValue = action(currentValue)
            // Substituímos o valor antigo pelo novo no mapa.
            storage[key] = newValue
            return true
        } else {
            // A chave não existia — não fizemos nada, avisamos com false.
            return false
        }
    }

    // Devolve uma fotografia imutável do estado actual do cache.
    // toMap() cria um novo Map independente — é uma cópia, não uma referência ao storage.
    // Isto significa que alterações posteriores ao cache não afectam o snapshot, e vice-versa.
    // Map (sem Mutable) é imutável — quem recebe não consegue chamar put, remove, etc.
    fun snapshot(): Map<K, V> {
        return storage.toMap()
    }

    // Devolve um novo mapa apenas com as entradas cujos valores passam na condição.
    // O parâmetro 'predicate' é uma função (V) -> Boolean — recebe um valor e diz se passa ou não.
    // filter do Kotlin itera sobre todas as entradas e mantém só as que devolvem true no predicate.
    // 'entry' representa cada par chave-valor — entry.key é a chave, entry.value é o valor.
    fun filterValues(predicate: (V) -> Boolean): Map<K, V> {
        return storage.filter { entry -> predicate(entry.value) }
    }
}