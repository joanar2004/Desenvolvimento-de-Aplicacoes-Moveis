# Tutorial 3 — JPCompose

**Unidade Curricular:** Desenvolvimento de Aplicações Móveis (DAM)  
**Aluno(s):** _______________  
**Data:** Abril 2026  
**URL do Repositório:** _______________  

---

## 1. Introdução

Este tutorial está dividido em duas partes principais. A primeira parte foca-se na criação de um processador de anotações personalizado em Kotlin, utilizando um projeto multi-módulo no IntelliJ IDEA. A segunda parte consiste em reconstruir a WeatherApp do tutorial anterior, adaptando-a para seguir a arquitetura MVVM com Jetpack Compose.

Na primeira parte, é definida uma anotação `@Greeting` que é processada em tempo de compilação para gerar automaticamente classes wrapper em torno dos métodos anotados. Estas classes wrapper imprimem uma mensagem de saudação antes de delegar a chamada ao método original.

---

## 2. Visão Geral do Sistema

O projeto do processador de anotações (`GreetingProcessorProject`) está estruturado como um projeto Gradle multi-módulo com três módulos:

- **annotations** — define a anotação `@Greeting`
- **processor** — implementa o processador de anotações usando KotlinPoet e AutoService
- **app** — utiliza a anotação e as classes wrapper geradas automaticamente

O processador de anotações analisa os métodos anotados com `@Greeting` em tempo de compilação e gera automaticamente uma classe wrapper para cada classe que contém métodos anotados.

---

## 3. Arquitetura e Design

### Projeto do Processador de Anotações

O projeto segue uma arquitetura multi-módulo:

```
GreetingProcessorProject/
├── annotations/         # Definição da anotação @Greeting
├── processor/           # GreetingProcessor (processador de anotações)
└── app/                 # MyClass, Main (usa o MyClassWrapper gerado)
```

A anotação `@Greeting` é definida com:
- `@Target(AnnotationTarget.FUNCTION)` — apenas aplicável a funções
- `@Retention(AnnotationRetention.SOURCE)` — utilizada apenas em tempo de compilação

O processador usa **KotlinPoet** para gerar código Kotlin e **AutoService** para se registar automaticamente.

### Fluxo de Geração de Código

```
Anotação @Greeting no método
        ↓
GreetingProcessor (em tempo de compilação)
        ↓
MyClassWrapper.kt (gerado automaticamente)
        ↓
Main.kt usa o MyClassWrapper
```

---

## 4. Implementação

### `Greeting.kt` (módulo annotations)

```kotlin
package annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Greeting(val message: String)
```

### `GreetingProcessor.kt` (módulo processor)

O processador estende `AbstractProcessor` e é registado com `@AutoService`. Ele:
1. Analisa todos os elementos anotados com `@Greeting`
2. Agrupa os métodos anotados pela classe que os contém
3. Gera uma classe wrapper para cada grupo usando KotlinPoet
4. Escreve o ficheiro gerado na diretoria de output do kapt

### `MyClass.kt` (módulo app)

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

### `MyClassWrapper.kt` (gerado automaticamente)

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

### `Main.kt` (módulo app)

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

## 5. Testes e Validação

O projeto foi validado executando a função `main()` e confirmando o output esperado na consola:

```
Hello from MyClass!
Executing sayHello method
Welcome to the compute function!
Computing something important...
```

A classe wrapper imprime corretamente a mensagem de saudação definida na anotação antes de delegar a chamada ao método original, confirmando que o processador de anotações funciona como esperado.

---

## 6. Instruções de Utilização

### Requisitos
- IntelliJ IDEA
- JDK 21 ou 23
- Gradle 8.10+

### Compilar e Executar

1. Clonar o repositório e abrir o projeto no IntelliJ IDEA
2. Aguardar a sincronização do Gradle (clicar no ícone do elefante)
3. No terminal, executar a tarefa kapt para gerar a classe wrapper:
```
./gradlew :app:kaptKotlin
```
4. Executar a função `main()` no ficheiro `Main.kt`

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

---

## 9. Melhorias Futuras

- Estender o processador `@Greeting` para suportar tipos de retorno diferentes de `Unit`
- Adicionar suporte para anotar classes inteiras em vez de métodos individuais
- Implementar o `RegexProcessor` da Secção 2 do tutorial
- Completar a WeatherApp com MVVM e Jetpack Compose (Secção 3)

---

## 10. Declaração de Uso de IA (Obrigatório)

_(Preencher de acordo com o uso de ferramentas de IA durante este trabalho, seguindo as indicações AC/AI definidas no enunciado)_

> Nota: A Secção 1 deste tutorial está marcada como **[AC NO, AI NO]** — o uso de autocomplete e assistência por IA não foi permitido.
