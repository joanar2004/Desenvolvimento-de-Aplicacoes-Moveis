package main.kotlin

// Esta classe permite ter "espaços reservados". Ao criar esta classe Cache, eu posso decidir o que ela vai guardar.
// Isto evita ter de criar uma classe diferente para cada tipo de dado
class Cache <K, V>{

    // Criar um mapa mutável privado para armazenar os dados
    // O mutableMapOf permite remover e adicionar items em tempo de execução
    private val storage: MutableMap<K, V> = mutableMapOf<K, V>()

    // Função para escrever ou reescrever uma entrada
    fun put(key: K, value: V) {
        storage[key] = value
    }

    // Devolver o valor
    fun get(key: K): V?{
        return storage[key]
    }

    // Remover uma entry da cache
    fun evict(key:K){
        storage.remove(key)
    }

    fun size(): Int{
        return storage.size
    }

    // Função que tenta obter o valor da chave. Caso não exista, executa o bloco 'default', guarda o resultado no cache
    // e devolve-o
    fun getOrPut(key: K, default: () -> V): V {
        val cachedValue = storage[key]

        if (cachedValue != null) {
            return cachedValue
        } else {
            // O programa vai "saltar" para onde o utilizadore escreveu o código e corre-lo
            val newValue = default()
            // Agora que já existe um valor newValue, guardar no mapa para que, da próxima vez, ele já lá esteja
            storage[key] = newValue
            // Devolver o valor
            return newValue
        }
    }

    // Função, que, caso a chave exista, aplica 'action' ao valor atual, guarda o novo resultado e retorna true. Caso
    // não exista, retorna false.
    fun transform(key: K, action: (V) -> V): Boolean {
        val currentValue = storage[key]

        if (currentValue != null) {
            // Aplicar a transformação ao valor que enconmtramos
            val newValue = action(currentValue)
            // Atualizar o mapa com o novo valor
            storage[key] = newValue
            return true
        }else{
            return false
        }
    }

    // Função que retorna uma cópia imutável atual do cache
    fun snapshot(): Map<K, V> {
        // o toMap() cria um novo mapa (cópia) que é o tipo 'Map'
        return storage.toMap()
    }

    // EXTRA
    // Retorna um mapa imutável contendo apenas as entradas cujo valores satisfazem a condição (predicate)
    fun filterValues(predicate: (V) -> Boolean): Map<K, V> {
        // Usamos a função filter do Kotlin que já faz este trabalho e 'it.value' refere-se ao valor de cada entrada do
        // mapa
        return storage.filter { entry -> predicate(entry.value) }
    }
}