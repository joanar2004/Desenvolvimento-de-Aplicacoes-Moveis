package dam

// Sealed class é uma classe "fechada" — o compilador sabe exactamente quais são todas as subclasses possíveis.
// Isto é útil porque ao fazer um 'when' sobre um Event, o compilador avisa se esqueceres algum caso.
// Ao contrário de uma classe normal, nenhuma classe fora deste ficheiro pode estender Event.
sealed class Event {

    // data class é uma classe especial do Kotlin para guardar dados.
    // O compilador gera automaticamente equals(), hashCode(), toString() e copy() com base nos parâmetros.
    // Estende Event() — ou seja, Login é um tipo de Event.
    // 'val' porque os valores de um evento não mudam após ser criado — um login já aconteceu, não se altera.
    data class Login(
        val username: String,
        val timestamp: Long  // Long para timestamps Unix em milissegundos (número muito grande para Int)
    ) : Event()

    // Purchase tem um campo extra 'amount' em relação a Login e Logout.
    // Double para valores monetários com casas decimais.
    data class Purchase(
        val username: String,
        val amount: Double,
        val timestamp: Long
    ) : Event()

    data class Logout(
        val username: String,
        val timestamp: Long
    ) : Event()
}

// Extension function — adiciona um método novo a um tipo já existente sem modificar o código original.
// List<Event>.filterByUser significa que esta função passa a pertencer a qualquer List<Event>.
// Ou seja, podes chamar minhaLista.filterByUser("ana") como se fosse um método nativo da lista.
fun List<Event>.filterByUser(username: String): List<Event> {

    // 'this' refere-se à lista sobre a qual a função foi chamada.
    // filter percorre todos os elementos e mantém apenas os que devolvem true.
    return this.filter { event ->

        // 'when' em Kotlin é como um switch, mas mais poderoso.
        // 'is' verifica o tipo em runtime — "is Event.Login" é verdadeiro se event for um Login.
        // Como Event é sealed, o compilador sabe que Login, Purchase e Logout são os únicos casos possíveis.
        // Se esquecesses um caso, o compilador dava erro — isto não acontece com classes normais.
        when (event) {
            is Event.Login -> event.username == username
            is Event.Purchase -> event.username == username
            is Event.Logout -> event.username == username
            // Não é preciso 'else' — o compilador confirma que todos os casos estão cobertos.
        }
    }
}

// Extension function em List<Event> que calcula o total gasto por um utilizador.
// Encadeia várias operações em sequência — cada linha transforma o resultado da anterior.
fun List<Event>.totalSpent(username: String): Double {

    // filterIsInstance<Event.Purchase>() filtra a lista e faz cast automático ao mesmo tempo.
    // O resultado é List<Event.Purchase> — daqui para a frente o compilador sabe que são todos Purchase.
    // Sem isto, terias de fazer um filter com 'is' e depois um cast manual.
    return this.filterIsInstance<Event.Purchase>()

        // Segundo filtro para garantir que só ficam as compras do utilizador correcto.
        // 'it' é o nome implícito do elemento actual quando a lambda tem apenas um parâmetro.
        .filter { it.username == username }

        // sumOf percorre a lista e soma o campo indicado de cada elemento.
        // Equivalente a fazer um loop e acumular numa variável, mas numa linha.
        .sumOf { it.amount }
}

// Higher-order function — recebe outra função como argumento.
// O parâmetro 'handler' é uma lambda do tipo (Event) -> Unit:
// recebe um Event e não devolve nada (Unit é o equivalente a void noutras linguagens).
// Quem chama processEvents decide o que fazer com cada evento — a função não assume nada.
fun processEvents(events: List<Event>, handler: (Event) -> Unit) {

    // Percorre a lista e chama handler para cada evento.
    // É equivalente a events.forEach { handler(it) } — o for é só mais explícito.
    for (event in events) {
        handler(event)
    }
}