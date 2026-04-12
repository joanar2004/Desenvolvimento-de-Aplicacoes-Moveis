# Assignment 2 — Kotlin: Text Transformation Pipeline with DSL, Compose & Fork

**Course:** LEIM — Licenciatura em Engenharia Informática e Multimédia
**Student(s):** Joana Ramos
**Date:** 12/04/2026
**Repository URL:** [https://github.com/joanar2004/Desenvolvimento-de-Aplicacoes-Moveis.git](https://github.com/joanar2004/Desenvolvimento-de-Aplicacoes-Moveis.git)

---

## 1. Introduction

This exercise is part of the practical work (TP2) for the Mobile Application Development unit. The goal is to build a **text processing pipeline** system in Kotlin, exploring the following advanced language concepts:

- **Classes with mutable state** — internal management of stages and transformations.
- **Function types as values** — storing and composing lambdas in a list.
- **DSL with receiver lambdas** (`Pipeline.() -> Unit`) — building pipelines with declarative syntax.
- **Function composition** (`compose`) — merging two existing stages into one.
- **Simplified parallel execution** (`fork`) — splitting the same input across two distinct pipelines and obtaining both results.

The practical scenario simulates the processing of application logs, filtering, cleaning, and formatting lines of text through a sequence of chained transformations.

---

## 2. System Overview

The system works as a **data pipeline** — similar to the `|` operator in a Unix terminal. Each stage receives a list of strings, applies a transformation, and passes the result to the next stage.

The main program flow:

1. Defines a list of server logs with extra whitespace and different levels (`INFO`, `ERROR`, `DEBUG`).
2. Builds a pipeline with 4 stages: Trim → Filter errors → Uppercase → Add index.
3. Prints the pipeline stage descriptions to the console.
4. Executes the pipeline and prints the final result.
5. Demonstrates the challenge features: `compose` (stage merging) and `fork` (execution across two branches simultaneously).

---

## 3. Architecture and Design

```
Kotlin_TP2_1_3/
├── pom.xml                            # Maven build configuration
└── src/
    └── main/
        └── kotlin/
            ├── Pipeline.kt            # Pipeline class + buildPipeline function
            └── Main.kt                # Entry point and demonstration
```

### Design Decisions

**Separation of concerns**
The `Pipeline` class lives in `Pipeline.kt` and encapsulates all stage management logic. `Main.kt` only handles data creation and pipeline invocation, keeping both files cohesive and independent.

**Parallel storage of names and functions**
The pipeline maintains two parallel lists: `nomes` (for description) and `transformacoes` (for execution). This approach allows `describe()` to print stage names without accessing the functions themselves, and `execute()` to run the functions without needing the names.

**Function types as first-class values**
The type `(List<String>) -> List<String>` is stored directly in a `MutableList`. This is idiomatic Kotlin — functions are values like any other, and can be stored, passed, and composed.

**DSL with receiver lambda**
The `buildPipeline` function receives `config: Pipeline.() -> Unit`, meaning the passed block runs in the context of a `Pipeline` instance. Inside the block, `addStage(...)` and `compose(...)` are called directly without a prefix — creating clean, declarative syntax resembling a DSL.

---

## 4. Implementation

### `Pipeline.kt`

#### Class structure

```kotlin
class Pipeline {
    private val nomes = mutableListOf<String>()
    private val transformacoes = mutableListOf<(List<String>) -> List<String>>()

    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        nomes.add(name)
        transformacoes.add(transform)
    }
    ...
}
```

Both lists are `private`, ensuring the pipeline's internal state can only be modified through public methods (`addStage`, `compose`). The type of the second list, `MutableList<(List<String>) -> List<String>>`, demonstrates how Kotlin treats functions as first-class citizens — they are stored exactly like any other value.

#### `execute` method

```kotlin
fun execute(input: List<String>): List<String> {
    var resultado = input
    for (funcao in transformacoes) {
        resultado = funcao(resultado)
    }
    return resultado
}
```

Iterates through the transformations sequentially, passing the output of each stage as the input of the next. Using `var resultado` allows updating the reference at each iteration without mutating the string lists themselves (each `map`/`filter` creates a new immutable list).

#### `describe` method

```kotlin
fun describe() {
    println("Pipeline Stages:")
    nomes.forEachIndexed { i, nome -> println("${i + 1}. $nome") }
}
```

`forEachIndexed` automatically provides the index and value at each iteration, avoiding manual counter management. The index starts at 0, so `i + 1` produces human-readable numbering from 1.

#### Challenge — `compose` method

```kotlin
fun compose(nome1: String, nome2: String, novoNome: String) {
    val idx1 = nomes.indexOf(nome1)
    val idx2 = nomes.indexOf(nome2)

    if (idx1 != -1 && idx2 != -1) {
        val f1 = transformacoes[idx1]
        val f2 = transformacoes[idx2]

        val composicao = { input: List<String> -> f2(f1(input)) }
        addStage(novoNome, composicao)
    }
}
```

`compose` implements the mathematical pattern of function composition: `(f2 ∘ f1)(x) = f2(f1(x))`. Functions `f1` and `f2` are retrieved by their positions in the parallel lists. The new composed function is a lambda that closes over `f1` and `f2` — this is an example of a **closure**: the lambda captures the variables from the enclosing scope and retains a reference to them even after `compose` has returned.

If either name is not found, `indexOf` returns `-1` and the `if` block does not execute, guarding against silent failures.

#### Challenge — `fork` method

```kotlin
fun fork(outraPipeline: Pipeline, input: List<String>): Pair<List<String>, List<String>> {
    val resultado1 = this.execute(input)
    val resultado2 = outraPipeline.execute(input)
    return Pair(resultado1, resultado2)
}
```

`fork` runs the same `input` through two separate pipelines and returns both results as a `Pair`. The return type `Pair<List<String>, List<String>>` enables destructuring in `Main.kt` with `val (res1, res2) = p1.fork(p2, logs)` — concise and expressive syntax characteristic of Kotlin.

#### DSL — `buildPipeline` function

```kotlin
fun buildPipeline(config: Pipeline.() -> Unit): Pipeline {
    val p = Pipeline()
    p.config()
    return p
}
```

This top-level function creates a `Pipeline` instance, invokes the `config` block in the context of that instance (`p.config()`), and returns the configured object. The parameter `Pipeline.() -> Unit` is a **lambda with receiver**: inside the block, `this` refers to instance `p`, so `addStage` and `compose` are called directly. This technique is the foundation of DSLs in Kotlin (such as Gradle KTS or Jetpack Compose).

---

### `Main.kt`

#### Main Pipeline

```kotlin
val myPipeline = buildPipeline {
    addStage("Trim")         { lista -> lista.map { it.trim() } }
    addStage("Filter errors"){ lista -> lista.filter { it.contains("ERROR") } }
    addStage("Uppercase")    { lista -> lista.map { it.uppercase() } }
    addStage("Add index")    { lista -> lista.mapIndexed { i, s -> "${i + 1}. $s" } }
}
```

The four stages process the logs in sequence:
1. **Trim** — removes leading and trailing whitespace from each line.
2. **Filter errors** — discards all lines that do not contain `"ERROR"`.
3. **Uppercase** — converts the remaining text to uppercase.
4. **Add index** — numbers each resulting line.

#### `compose` demonstration

```kotlin
val p1 = buildPipeline {
    addStage("Trim")  { it.map { s -> s.trim() } }
    addStage("Upper") { it.map { s -> s.uppercase() } }
    compose("Trim", "Upper", "LimpezaTotal")
}
```

Creates a pipeline with two stages and then composes them into a third stage called `"LimpezaTotal"`. This new stage runs Trim followed by Upper in a single call.

#### `fork` demonstration

```kotlin
val p2 = buildPipeline {
    addStage("ApenasErros") { lista ->
        lista.filter { it.contains("error", ignoreCase = true) }
    }
}

val logs_desafio = listOf("  info  ", "  error  ")
val (res1, res2) = p1.fork(p2, logs_desafio)
```

`p1` (Trim + Upper + LimpezaTotal) and `p2` (ApenasErros) process the same input list in logical parallel. The result is destructured directly in the declaration, demonstrating `Pair` destructuring in Kotlin.

---

## 5. Testing and Validation

Validation was performed manually by running the program and verifying the console output.

**Expected output — Main pipeline:**

```
Pipeline Stages:
1. Trim
2. Filter errors
3. Uppercase
4. Add index

Final Result:
1. ERROR : DISK FULL
2. ERROR : OUT OF MEMORY
3. ERROR : CONNECTION TIMEOUT
```

**Expected output — Challenges:**

```
Resultado P1: [INFO, ERROR]
Resultado P2: [  error  ]
```

> Note: `p1` applies Trim and Uppercase to the entire list; `p2` filters only lines containing `"error"` (case insensitive), keeping the original whitespace because `p2` has no Trim stage.

---

## 6. Usage Instructions

### Prerequisites

- **JDK 8** or higher
- **Maven 3.6+**
- **IntelliJ IDEA** (recommended) or any IDE with Kotlin support

### Running the Project

**Option A — IntelliJ IDEA:**
1. Open the `Kotlin_TP2_1_3/` folder in IntelliJ IDEA.
2. Wait for IntelliJ to automatically import the Maven project.
3. Open `Main.kt` and click the ▶ Run button next to `fun main()`.

**Option B — Maven CLI:**
```bash
cd Kotlin_TP2_1_3
mvn compile exec:java
```

**Option C — Manual Kotlin compiler:**
```bash
kotlinc src/main/kotlin/Pipeline.kt src/main/kotlin/Main.kt -include-runtime -d output.jar
java -jar output.jar
```

---

## 12. Version Control and Commit History

The project was versioned with Git. The commit history reflects the incremental development by steps:

| Commit Hash | Message | Description |
|-------------|---------|-------------|
| `01e391a` | exercicio 1.3 até ao passo 4 incluindo, tentar challenge depois | Implementation of the `Pipeline` class, `buildPipeline` function, and main pipeline with 4 stages |
| `f1dd7c7` | exercicio 1.4 pronto a testar | Implementation of the `compose` and `fork` challenges, and tests in `Main.kt` |

The commit strategy reflects a two-phase approach: first consolidating the base requirements (steps 1 to 4), then tackling the additional challenges. This separation makes it easy to identify where each feature was introduced.

---

## 13. Difficulties and Lessons Learned

**Function types in lists**
Storing `(List<String>) -> List<String>` in a `MutableList` was initially unintuitive. Understanding that, in Kotlin, a function type is like any other type (it can be stored, passed, and returned) was an important moment of clarity.

**Receiver lambda vs. regular lambda**
The difference between `(Pipeline) -> Unit` and `Pipeline.() -> Unit` is subtle but powerful. With a receiver lambda, `this` inside the block refers to the `Pipeline` instance, which allows calling `addStage` without a prefix. Understanding this was essential to grasping how DSLs work in Kotlin.

**Function composition with closures**
In `compose`, the lambda `{ input -> f2(f1(input)) }` captures `f1` and `f2` from the enclosing scope. It was initially unclear that these variables would remain accessible after `compose` returned. Understanding that the lambda retains a reference to the captured variables — the concept of a **closure** — clarified the behaviour.

**Parallel lists vs. Map**
Choosing two parallel lists (`nomes` and `transformacoes`) instead of a `LinkedHashMap<String, (List<String>) -> List<String>>` was an interesting implementation decision. A Map would be more elegant for name-based lookups, but the lists guarantee insertion order and index-based access, which simplifies sequential execution in `execute`.

**Lesson learned:** DSLs in Kotlin are not magic — they are simply receiver lambdas combined with straightforward top-level functions. Once the mechanism is understood, reading code like Gradle KTS or Jetpack Compose becomes much more accessible.

---

## 14. Future Improvements

- **Per-stage error handling:** Wrap each transformation in a `try-catch` and log the name of the stage that failed, instead of letting the exception propagate without context.
- **True parallel execution with `fork`:** Use `kotlinx.coroutines` (`async`/`await`) to run the two branches of `fork` in concurrent coroutines, instead of sequentially.
- **Generic typed pipeline:** Generalise `Pipeline` to `Pipeline<T>` to process lists of any type, not just `List<String>`.
- **Stage removal:** Add a `removeStage(name: String)` method to allow modifying the pipeline after construction.
- **Per-stage metrics:** Record the execution time of each stage and print it in `describe()`, useful for identifying bottlenecks in pipelines with large volumes of data.
- **Unit tests:** Write JUnit 5 tests to verify the behaviour of `compose` with non-existent stages, `fork` with empty pipelines, and `execute` with empty input.
