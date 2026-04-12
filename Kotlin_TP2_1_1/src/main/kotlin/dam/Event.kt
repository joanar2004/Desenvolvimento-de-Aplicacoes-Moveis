// Uma sealed class é como se fosase um "membro VIP" de uma hierarquia de classes. Ela permite-te definir um conjunto
// restrito de tipos. Ao contrário de uma classe normal, o compilador sabe exatamente quais são todas as subclasses
// possíveis.

package dam

// Definir a Sealed Class "Event"
// O modificador 'sealed' impede que outras classes fora deste ficheiro estendam a classe Event

sealed class Event {

    // Subclasse para Login
    // Usar 'val' pois os valores do login após serem criados não vão ser mudados
    data class Login(
        val username: String,
        val timestamp: Long
    ): Event()

    // Sublasse para o evento de compra (Purchase)
    data class Purchase(
        val username: String,
        val amount: Double,
        val timestamp: Long
    ): Event()

    // Subclasse para logout
    data class Logout(
        val username: String,
        val timestamp: Long
    ): Event()
}

// Extension Function é uma forma de adicionar funções a classes que já existem sem ter de modificar o código original
// da inguagem.
// Extensionh function para List<Event>
// List<Event>.filterByUser : Função pertence a qualquer lista de Eventos.
fun List<Event>.filterByUser(username: String): List<Event> {
    // Percorrer a lista evento
    return this.filter {event ->
        // Ver se o username coincide para cada evento
        when(event) {
            is Event.Login -> event.username == username
            is Event.Purchase -> event.username == username
            is Event.Logout -> event.username == username
        }
    }
}

// Função para calcular o gasto total, recebe username e retorna um double
fun List<Event>.totalSpent(username: String): Double{
    // filtra a lista de eventos e garante que daqui para a frente só haver objetos do tipo Purchase
    return this.filterIsInstance<Event.Purchase>()

        // mais um filter para que a compra de factro pertence ao utilizador correto
        .filter {it.username == username}

        // somar o campo 'amount' de cada compra encontrada
        .sumOf {it.amount}
}

// Uma função Higher-Order que recebe uma lista de eventos (que queremos processar e uma função lambda que define o que
// fazer com cada evento
fun processEvents(events: List<Event>, handler: (Event) -> Unit){
    // Percorrer os eventos da lista
    for (event in events) {
        handler(event)
    }
}