package dam

// Importante: Se o Event.kt estiver na mesma pasta 'dam', não precisas de import.
// Mas se o IntelliJ reclamar, ele vai sugerir o import automaticamente.

fun main() {
    val events = listOf (
        Event . Login ("alice", 1_000) ,
        Event . Purchase ("alice", 49.99 , 1_100) ,
        Event . Purchase ("bob", 19.99 , 1_200) ,
        Event . Login ("bob", 1_050) ,
        Event . Purchase ("alice", 15.00 , 1_300) ,
        Event . Logout ("alice", 1_400) ,
        Event . Logout ("bob", 1_500)
    )

    processEvents(events) { event ->
        when (event) {
            is Event.Login -> println("[LOGIN] ${event.username} logged in at t=${event.timestamp}")
            is Event.Purchase -> println("[PURCHASE] ${event.username} spent €${event.amount} at t=${event.timestamp}")
            is Event.Logout -> println("[LOGOUT] ${event.username} logged out at t=${event.timestamp}")
        }
    }

    // Print dos gastos totais
    println("")
    val gastoAlice = events.totalSpent("alice")
    println("Total spent by alice: €$gastoAlice")

    val gastoBob = events.totalSpent("bob")
    println("Total spent by bob: €$gastoBob")

    // Eventos da Alice
    val eventosDaAlice = events.filterByUser("alice")
    println("\nEvents for alice:")
    //Percorrer a lista filtrada
    eventosDaAlice.forEach { evento ->
        println(evento)
    }
}