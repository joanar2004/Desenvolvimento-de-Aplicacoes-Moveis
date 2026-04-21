# Tutorial 3 — JPCompose

**Unidade Curricular:** Desenvolvimento de Aplicações Móveis (DAM)  
**Aluno:** Joana Ramos

**Data:** Abril 2026  
**URL do Repositório:** https://github.com/joanar2004/Desenvolvimento-de-Aplicacoes-Moveis.git
---

## 1. Introdução

Este tutorial está dividido em duas partes principais. A primeira parte foca-se na criação de um processador de anotações personalizado em Kotlin, utilizando um projeto multi-módulo no IntelliJ IDEA. A segunda parte consiste em reconstruir a WeatherApp do tutorial anterior, adaptando-a para seguir a arquitetura MVVM com Jetpack Compose.

Na primeira parte foram implementados dois processadores de anotações:
- **GreetingProcessor** — gera classes wrapper que imprimem uma mensagem de saudação antes de delegar a chamada ao método original
- **RegexProcessor** — gera classes que implementam métodos abstratos usando expressões regulares para extrair partes de uma string de input

---

## 2. Visão Geral do Sistema

O projeto do processador de anotações (`GreetingProcessorProject`) está estruturado como um projeto Gradle multi-módulo com três módulos:

- **annotations** — define as anotações `@Greeting` e `@Extract`
- **processor** — implementa os processadores de anotações usando KotlinPoet e AutoService
- **app** — utiliza as anotações e as classes geradas automaticamente

---

## 3. Arquitetura e Design

### Projeto do Processador de Anotações

O projeto segue uma arquitetura multi-módulo:

```
GreetingProcessorProject/
├── annotations/         # Definição das anotações @Greeting e @Extract
├── processor/           # GreetingProcessor e RegexProcessor
└── app/                 # MyClass, DataProcessor, Main (usa as classes geradas)
```

### Anotação `@Greeting`
- `@Target(AnnotationTarget.FUNCTION)` — apenas aplicável a funções
- `@Retention(AnnotationRetention.SOURCE)` — utilizada apenas em tempo de compilação
- Parâmetro `message: String` — mensagem de saudação a imprimir

### Anotação `@Extract`
- `@Target(AnnotationTarget.FUNCTION)` — apenas aplicável a funções
- `@Retention(AnnotationRetention.SOURCE)` — utilizada apenas em tempo de compilação
- Parâmetro `regex: String` — expressão regular para extrair dados do input

### Fluxo de Geração de Código — GreetingProcessor

```
Anotação @Greeting no método
        ↓
GreetingProcessor (em tempo de compilação)
        ↓
MyClassWrapper.kt (gerado automaticamente)
        ↓
Main.kt usa o MyClassWrapper
```

### Fluxo de Geração de Código — RegexProcessor

```
Anotação @Extract no método abstrato
        ↓
RegexProcessor (em tempo de compilação)
        ↓
DataProcessorExtractor.kt (gerado automaticamente)
        ↓
MainRegex.kt usa o DataProcessorExtractor
```

---

## 4. Implementação

### Secção 1 — GreetingProcessor

#### `Greeting.kt` (módulo annotations)

```kotlin
package annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Greeting(val message: String)
```

#### `GreetingProcessor.kt` (módulo processor)

O processador estende `AbstractProcessor` e é registado com `@AutoService`. Ele:
1. Analisa todos os elementos anotados com `@Greeting`
2. Agrupa os métodos anotados pela classe que os contém
3. Gera uma classe wrapper para cada grupo usando KotlinPoet
4. Escreve o ficheiro gerado na diretoria de output do kapt

#### `MyClass.kt` (módulo app)

```kotlin
package app

import annotations.Greeting

open class MyClass {

    @Greeting("Hello from MyClass!")
    open fun sayHello() {
        println("Executing sayHello method")
    }

    @Greeting("Welcome to the compute function!")
    open fun compute() {
        println("Computing something important...")
    }
}
```

#### `MyClassWrapper.kt` (gerado automaticamente)

```kotlin
package app

public final class MyClassWrapper(
    public val original: MyClass,
) {
    public final fun sayHello() {
        println("Hello from MyClass!")
        original.sayHello()
    }

    public final fun compute() {
        println("Welcome to the compute function!")
        original.compute()
    }
}
```

#### `Main.kt` (módulo app)

```kotlin
package app

fun main() {
    val myClass = MyClass()
    val wrappedMyClass = MyClassWrapper(myClass)
    wrappedMyClass.sayHello()
    wrappedMyClass.compute()
}
```

---

### Secção 2 — RegexProcessor

#### `Extract.kt` (módulo annotations)

```kotlin
package annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Extract(val regex: String)
```

#### `RegexProcessor.kt` (módulo processor)

O processador estende `AbstractProcessor` e é registado com `@AutoService`. Ele:
1. Analisa todos os elementos anotados com `@Extract`
2. Agrupa os métodos anotados pela classe abstrata que os contém
3. Gera uma classe concreta que estende a classe abstrata original
4. Para cada método, gera uma implementação que aplica a regex ao input e retorna o primeiro grupo capturado

#### `DataProcessor.kt` (módulo app)

```kotlin
package app

import annotations.Extract

abstract class DataProcessor(val input: String) {

    @Extract(regex = "Name: (\\w+)")
    abstract fun getName(): String?

    @Extract(regex = "Address: (.+)")
    abstract fun getAddress(): String?
}
```

#### `DataProcessorExtractor.kt` (gerado automaticamente)

```kotlin
package app

public class DataProcessorExtractor(
    input: String,
) : DataProcessor(input) {

    override fun getName(): String? {
        val match = Regex("Name: (\\w+)").find(input)
        return match?.groupValues?.get(1)
    }

    override fun getAddress(): String? {
        val match = Regex("Address: (.+)").find(input)
        return match?.groupValues?.get(1)
    }
}
```

#### `MainRegex.kt` (módulo app)

```kotlin
package app

fun main() {
    val input = "Name: John Address: 123 Street"
    val extractor = DataProcessorExtractor(input)
    println("Name: ${extractor.getName()}")
    println("Address: ${extractor.getAddress()}")
}
```

---

## 5. Testes e Validação

### Secção 1 — GreetingProcessor

Output esperado ao correr `Main.kt`:

```
Hello from MyClass!
Executing sayHello method
Welcome to the compute function!
Computing something important...
```

### Secção 2 — RegexProcessor

Output esperado ao correr `MainRegex.kt`:

```
Name: John
Address: 123 Street
```

Ambos os processadores foram validados com sucesso, confirmando que a geração automática de código em tempo de compilação funciona corretamente.

---

## 6. Instruções de Utilização

### Requisitos
- IntelliJ IDEA
- JDK 23
- Gradle 8.10+

### Compilar e Executar

1. Clonar o repositório e abrir o projeto no IntelliJ IDEA
2. Aguardar a sincronização do Gradle (clicar no ícone do elefante)
3. No terminal, definir o JAVA_HOME e executar o kapt:
```
$env:JAVA_HOME = "C:\Users\limar\.gradle\jdks\eclipse_adoptium-23-amd64-windows.2"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
./gradlew :app:kaptKotlin
```
4. Executar a função `main()` no ficheiro `Main.kt` ou `MainRegex.kt`

### Notas de Configuração
- A propriedade `kapt.include.compile.classpath=false` deve estar presente no `gradle.properties`
- O processamento de anotações deve estar ativo em **File → Settings → Build → Compiler → Annotation Processors**

---

## 7. Controlo de Versões e Histórico de Commits

_(Descrever aqui o histórico de commits — ex: configuração inicial, módulo de anotações, módulo do processador, módulo app, correções)_

---

## 8. Dificuldades e Lições Aprendidas

As principais dificuldades encontradas durante este tutorial foram:

- **Compatibilidade entre Gradle e JDK** — o kapt 1.9.23 não é compatível com o Gradle 9.x, sendo necessário fazer downgrade para o Gradle 8.10. Além disso, o JDK 25 causou problemas e foi necessário configurar manualmente o JDK 23.
- **Conflito de versão JVM** — um conflito entre o Java 23 e o Kotlin JVM target 21 obrigou a definir explicitamente `jvmToolchain(23)` em todos os módulos.
- **kapt não gerava ficheiros** — foi necessário definir manualmente a variável de ambiente `JAVA_HOME` no terminal para apontar para o JDK 23 antes de correr `./gradlew :app:kaptKotlin`.

**Lições aprendidas:**
- O processamento de anotações é uma ferramenta poderosa para automatizar a geração de código repetitivo em tempo de compilação
- A estrutura de projeto multi-módulo ajuda a separar as responsabilidades de forma clara
- As versões do Gradle, Kotlin e JDK devem ser cuidadosamente compatibilizadas para evitar conflitos
- As expressões regulares com grupos de captura são uma forma eficiente de extrair dados estruturados de strings

---

## 9. Melhorias Futuras

- Estender o processador `@Greeting` para suportar tipos de retorno diferentes de `Unit`
- Adicionar suporte para anotar classes inteiras em vez de métodos individuais
- Estender o `RegexProcessor` para suportar múltiplos grupos de captura por método
- Completar a WeatherApp com MVVM e Jetpack Compose (Secção 3)

---

## 10. Declaração de Uso de IA (Obrigatório)

_(Preencher de acordo com o uso de ferramentas de IA durante este trabalho, seguindo as indicações AC/AI definidas no enunciado)_

> Nota: As Secções 1 e 2 deste tutorial estão marcadas como **[AC NO, AI NO]** — o uso de autocomplete e assistência por IA não foi permitido.
