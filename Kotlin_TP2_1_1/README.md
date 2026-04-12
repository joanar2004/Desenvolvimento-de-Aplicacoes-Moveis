# Assignment 2 — Kotlin: Sealed Classes, Extension Functions & Higher-Order Functions

**Course:** LEIM — Licenciatura em Engenharia Informática e Multimédia
**Student(s):** Joana Ramos
**Date:** 12/04/2026
**Repository URL:** [https://github.com/joanar2004/Desenvolvimento-de-Aplicacoes-Moveis.git](https://github.com/joanar2004/Desenvolvimento-de-Aplicacoes-Moveis.git)

---

## 1. Introduction

This assignment is part of the practical work (TP2) for the Mobile Application Development unit. The goal is to explore and apply advanced Kotlin language features, specifically:

- **Sealed Classes** — to model a closed, type-safe hierarchy of events.
- **Data Classes** — to represent structured, immutable data with built-in equality and formatting.
- **Extension Functions** — to add new behaviour to existing types without modifying their source.
- **Higher-Order Functions** — to pass functions as parameters and compose reusable logic.

The practical scenario simulates a simple event-tracking system for an application, where users can log in, make purchases, and log out. These events are processed, filtered, and aggregated using idiomatic Kotlin code.

---

## 2. System Overview

The system models a sequence of application events associated with different users. Each event belongs to one of three categories: `Login`, `Purchase`, or `Logout`. The program:

1. Creates a list of mixed events for multiple users (`alice` and `bob`).
2. Processes each event and prints a formatted log to the console.
3. Calculates the total amount spent by each user.
4. Filters all events belonging to a specific user.

The output is purely console-based, with no external dependencies beyond the Kotlin standard library.

---

## 3. Architecture and Design

The project is structured as a standard Kotlin/Maven project with two source files inside the `dam` package:

```
Kotlin_TP2_1_1/
├── pom.xml                          # Maven build configuration
└── src/
    └── main/
        └── kotlin/
            └── dam/
                ├── Event.kt         # Sealed class definition + extension functions
                └── Main.kt          # Entry point / program execution
```

### Design Decisions

**Sealed Class `Event`**
The `Event` sealed class acts as a restricted type hierarchy. By declaring it `sealed`, the Kotlin compiler guarantees that all possible subtypes are known at compile time, within the same file. This enables exhaustive `when` expressions without an `else` branch, which improves safety and readability.

**Data Classes as Subclasses**
Each event subtype (`Login`, `Purchase`, `Logout`) is declared as a `data class`. This automatically provides `equals()`, `hashCode()`, `toString()`, and `copy()` implementations — ideal for immutable event records.

**Extension Functions on `List<Event>`**
Rather than creating a dedicated wrapper class, extension functions are added directly to `List<Event>`. This keeps the design lightweight and the API natural to use (`events.filterByUser("alice")`).

**Higher-Order Function `processEvents`**
The `processEvents` function receives a list of events and a lambda `(Event) -> Unit`, decoupling the iteration logic from the event-handling logic. The caller decides what to do with each event, which makes the function reusable across different contexts.

---

## 4. Implementation

### `Event.kt`

#### Sealed Class

```kotlin
sealed class Event {
    data class Login(val username: String, val timestamp: Long) : Event()
    data class Purchase(val username: String, val amount: Double, val timestamp: Long) : Event()
    data class Logout(val username: String, val timestamp: Long) : Event()
}
```

The `sealed` modifier ensures no class outside this file can extend `Event`. The three subtypes are `data class`es, making them immutable value objects. Properties are declared with `val` since event data should not change after creation.

#### Extension Function — `filterByUser`

```kotlin
fun List<Event>.filterByUser(username: String): List<Event> {
    return this.filter { event ->
        when (event) {
            is Event.Login    -> event.username == username
            is Event.Purchase -> event.username == username
            is Event.Logout   -> event.username == username
        }
    }
}
```

This extension adds `filterByUser` to any `List<Event>`. The `when` expression performs a smart cast: after `is Event.Login`, the compiler knows `event` is of type `Event.Login`, so `event.username` is accessible without an explicit cast. Because `Event` is sealed, the `when` is exhaustive and requires no `else`.

#### Extension Function — `totalSpent`

```kotlin
fun List<Event>.totalSpent(username: String): Double {
    return this.filterIsInstance<Event.Purchase>()
               .filter { it.username == username }
               .sumOf { it.amount }
}
```

This chains three standard library operations: first, it isolates only `Purchase` events using `filterIsInstance`; then filters by the target user; finally it sums the `amount` field using `sumOf`. The result is concise and reads like a data pipeline.

#### Higher-Order Function — `processEvents`

```kotlin
fun processEvents(events: List<Event>, handler: (Event) -> Unit) {
    for (event in events) {
        handler(event)
    }
}
```

The second parameter, `handler: (Event) -> Unit`, is a function type. Any lambda with the matching signature can be passed in. This pattern is common in functional and reactive programming to separate iteration from behaviour.

---

### `Main.kt`

The entry point sets up the event list and calls the functions defined in `Event.kt`:

```kotlin
fun main() {
    val events = listOf(
        Event.Login("alice", 1_000),
        Event.Purchase("alice", 49.99, 1_100),
        Event.Purchase("bob", 19.99, 1_200),
        Event.Login("bob", 1_050),
        Event.Purchase("alice", 15.00, 1_300),
        Event.Logout("alice", 1_400),
        Event.Logout("bob", 1_500)
    )

    processEvents(events) { event ->
        when (event) {
            is Event.Login    -> println("[LOGIN] ${event.username} logged in at t=${event.timestamp}")
            is Event.Purchase -> println("[PURCHASE] ${event.username} spent €${event.amount} at t=${event.timestamp}")
            is Event.Logout   -> println("[LOGOUT] ${event.username} logged out at t=${event.timestamp}")
        }
    }

    println("")
    println("Total spent by alice: €${events.totalSpent("alice")}")
    println("Total spent by bob: €${events.totalSpent("bob")}")

    println("\nEvents for alice:")
    events.filterByUser("alice").forEach { println(it) }
}
```

Timestamps use underscore notation (`1_000`) for readability, which is valid Kotlin syntax for numeric literals.

---

## 5. Testing and Validation

No automated unit tests were implemented in this assignment. Validation was performed manually by running the program and comparing the console output against the expected results.

**Expected Output:**

```
[LOGIN] alice logged in at t=1000
[PURCHASE] alice spent €49.99 at t=1100
[PURCHASE] bob spent €19.99 at t=1200
[LOGIN] bob logged in at t=1050
[PURCHASE] alice spent €15.0 at t=1300
[LOGOUT] alice logged out at t=1400
[LOGOUT] bob logged out at t=1500

Total spent by alice: €64.99
Total spent by bob: €19.99

Events for alice:
Event.Login(username=alice, timestamp=1000)
Event.Purchase(username=alice, amount=49.99, timestamp=1100)
Event.Purchase(username=alice, amount=15.0, timestamp=1300)
Event.Logout(username=alice, timestamp=1400)
```

All results matched the expected values, confirming the correctness of the implementation.

---

## 6. Usage Instructions

### Prerequisites

- **JDK 8** or higher
- **Maven 3.6+**
- **IntelliJ IDEA** (recommended) or any IDE with Kotlin support

### Running the Project

**Option A — IntelliJ IDEA:**
1. Open the project folder (`Kotlin_TP2_1_1/`) in IntelliJ IDEA.
2. Let IntelliJ import the Maven project automatically.
3. Open `Main.kt` and click the green ▶ Run button next to `fun main()`.

**Option B — Maven CLI:**
```bash
cd Kotlin_TP2_1_1
mvn compile exec:java
```

**Option C — Manual Kotlin compiler:**
```bash
kotlinc src/main/kotlin/dam/Event.kt src/main/kotlin/dam/Main.kt -include-runtime -d output.jar
java -jar output.jar
```

---

## 12. Version Control and Commit History

The project was versioned with Git from the start. The commit history reflects the incremental development of each feature:

| Commit Hash | Message | Description |
|-------------|---------|-------------|
| `97c2b55` | Sealed Class Event criada com os data classes Login e Purchase | Initial `sealed class Event` with `Login` and `Purchase` subtypes |
| `c06865a` | Subclasse de Event (Logout) criada | Added the `Logout` data class subtype |
| `7ef46a0` | extension function (filterByUser) escrita | Implemented the `filterByUser` extension function |
| `665060c` | exercícios kotlin incluindo pontos 4 e 5 | Added `totalSpent` and `processEvents`; completed `Main.kt` |
| `a68336d` | section 1 finished, tudo resolvido de forma a bater certo com o output e tudo comentado | Final polish, output verification, and all code commented |
| `6bc881d` | correção nos comentários | Minor comment corrections and cleanup |

The commits follow a logical progression: sealed class → subclasses → extension functions → higher-order functions → final validation. This step-by-step approach made it easier to test each concept in isolation before moving on.

---

## 13. Difficulties and Lessons Learned

**Sealed Classes and `when` exhaustiveness**
One of the first challenges was understanding why `when` on a sealed class does not require an `else` branch. After testing, it became clear that the compiler tracks all known subtypes at compile time, making the expression exhaustive by design. This is a key advantage of sealed classes over regular open class hierarchies.

**Smart Casts inside `when`**
Inside each `when` branch, the compiler automatically narrows the type (e.g., after `is Event.Login`, the variable is treated as `Event.Login`). Initially, accessing `event.username` felt unintuitive, but understanding smart casts clarified why no explicit casting is needed.

**Extension Functions on Generic Types**
Writing `fun List<Event>.filterByUser(...)` required understanding how Kotlin resolves extension functions. Since `List` is a standard library type, adding functions to it felt unfamiliar at first, but it demonstrated one of Kotlin's most powerful features — extending third-party types without inheritance.

**Higher-Order Functions and Lambda Syntax**
Passing a lambda to `processEvents` and then using a `when` block inside it required careful attention to syntax. The trailing lambda syntax (`processEvents(events) { event -> ... }`) was initially confusing but quickly became natural after a few iterations.

**Lesson learned:** Kotlin's type system and functional features (sealed classes, extension functions, higher-order functions) work together as a cohesive unit. Understanding one concept in depth makes the others much easier to adopt.

---

## 14. Future Improvements

Several enhancements could be made to extend this project in a real-world direction:

- **Unit Tests:** Add JUnit 5 / kotlin-test tests to verify `totalSpent`, `filterByUser`, and `processEvents` with edge cases (empty list, unknown user, zero-amount purchases).
- **Timestamp Formatting:** Replace raw `Long` timestamps with `java.time.Instant` or a human-readable date format for better usability.
- **Persistence:** Serialize the event list to JSON (using `kotlinx.serialization`) and write it to a file, enabling event replay and audit logging.
- **Additional Event Types:** Extend the sealed class with new subtypes such as `PasswordChange`, `SessionExpiry`, or `ItemAdded`, without breaking existing `when` expressions (the compiler will flag unhandled cases).
- **Aggregation Functions:** Add more extension functions, such as `mostActiveUser()`, `eventCountByType()`, or `averageSessionDuration()`, to explore the full power of Kotlin's collection API.
- **Android Integration:** Adapt the event model for use in an Android ViewModel, where events can be emitted via `StateFlow` or `LiveData` and observed by the UI layer.
