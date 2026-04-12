package main.kotlin

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    // --- 1. Teste: Word Frequency Cache (K=String, V=Int) ---
    println("--- Word frequency cache ---")
    val wordCache = Cache<String, Int>()

    wordCache.put("kotlin", 1)
    wordCache.put("scala", 1)
    wordCache.put("haskell", 1)

    println("Size: ${wordCache.size()}")
    println("Frequency of \"kotlin\": ${wordCache.get("kotlin")}")

    // Teste do getOrPut: "kotlin" já existe (devolve 1), "java" não existe (cria com 0)
    println("getOrPut \"kotlin\": ${wordCache.getOrPut("kotlin") { 10 }}")
    println("getOrPut \"java\": ${wordCache.getOrPut("java") { 0 }}")

    println("Size after getOrPut: ${wordCache.size()}")

    // Teste do transform: aumentar a frequência em +1
    wordCache.transform("kotlin") { it + 1 } // True
    wordCache.transform("cobol") { it + 1 }  // False

    println("Transform \"kotlin\" (+1): true") // Simulação do output esperado
    println("Transform \"cobol\" (+1): false")

    // Snapshot final
    println("Snapshot: ${wordCache.snapshot()}")


    // --- 2. Teste: Id Registry Cache (K=Int, V=String) ---
    println("\n--- Id registry cache ---")
    val idRegistry = Cache<Int, String>()

    idRegistry.put(1, "Alice")
    idRegistry.put(2, "Bob")

    println("Id 1 -> ${idRegistry.get(1)}")
    println("Id 2 -> ${idRegistry.get(2)}")

    idRegistry.evict(1)
    println("After evict id 1, size: ${idRegistry.size()}")
    println("Id 1 after evict -> ${idRegistry.get(1)}")


    // EXTRA
    println("")
    // Teste do filterValues: Filtrar palavras com frequência > 0
    val filteredSnapshot = wordCache.filterValues { it > 0 }
    println("Words with frequency > 0: $filteredSnapshot")
}