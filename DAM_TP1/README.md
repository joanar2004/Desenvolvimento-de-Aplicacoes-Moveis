# Assignment — Kotlin Library Management System

**Course:** Licenciatura em Engenharia Informática e Multimédia  
**Student(s):** Joana Ramos  
**Date:** 08 março 2026  
**Repository URL:**(https://github.com/joanar2004/DAM_TP1.git)

---

## 1. Introduction

O objetivo deste trabalho foi desenvolver uma aplicação simples em **Kotlin** com foco na utilização de **programação orientada a objetos**. O projeto consiste na implementação de um pequeno sistema de **Biblioteca Virtual**, capaz de gerir diferentes tipos de livros e utilizadores.

A aplicação permite criar livros, adicioná-los à biblioteca e simular operações básicas como pesquisa e gestão da coleção.

---

## 2. System Overview

O sistema simula o funcionamento básico de uma biblioteca digital, incluindo:

- Gestão de livros físicos e digitais
- Registo de membros da biblioteca
- Organização e armazenamento da coleção de livros
- Operações básicas de pesquisa e listagem

A aplicação executa através de uma **interface de consola**, onde são criados exemplos de livros e operações para demonstrar o funcionamento do sistema.

---

## 3. Architecture and Design

O projeto foi desenvolvido seguindo os princípios da **Programação Orientada a Objetos (OOP)**.

### Estrutura principal do sistema

O sistema é composto pelas seguintes classes principais:

**Book**

Classe base que representa um livro genérico, contendo atributos como:

- título
- autor
- ano de publicação

**PhysicalBook**

Classe que herda de `Book` e representa livros físicos disponíveis na biblioteca.

**DigitalBook**

Classe derivada de `Book` que representa livros digitais, podendo incluir características adicionais como formato digital.

**Library**

Classe responsável por gerir a coleção de livros da biblioteca, permitindo adicionar, listar e pesquisar livros.

**LibraryMember**

Representa um utilizador da biblioteca, que pode interagir com o sistema.

---

## 4. Implementation

A implementação foi realizada em **Kotlin**, utilizando conceitos fundamentais de programação orientada a objetos.

### Herança

Utilizada para criar diferentes tipos de livros a partir da classe base `Book`.

Book
|-- PhysicalBook
|-- DigitalBook

### Encapsulamento

Os atributos das classes são protegidos e manipulados através de métodos apropriados.

### Organização Modular

O código encontra-se organizado em diferentes **packages**, facilitando a manutenção e legibilidade do projeto.

---

## 5. Testing and Validation

O funcionamento da aplicação foi testado através da execução da função `main`, onde são simuladas várias operações:

- criação de livros
- adição de livros à biblioteca
- listagem da coleção existente
- testes das classes implementadas

Estes testes permitem verificar se a estrutura de classes e a lógica do sistema funcionam corretamente.

---

## 6. Usage Instructions

Para executar o projeto:

1. Clonar o repositório.
2. Abrir o projeto no **IntelliJ IDEA**.
3. Esperar que o sistema carregue as dependências.
4. Abrir o ficheiro principal:
   src/main/kotlin/Main.kt


5. Executar o método `main`.

A aplicação irá demonstrar o funcionamento do sistema através da consola.

---

## 7. Version Control and Commit History

O controlo de versões foi realizado utilizando **Git**, permitindo acompanhar a evolução do projeto e manter histórico das alterações efetuadas.

---

## 8. Difficulties and Lessons Learned

### Dificuldades

- Organização inicial das classes e packages
- Estruturação correta da herança entre os diferentes tipos de livros

### Aprendizagens

- Aplicação prática de **programação orientada a objetos em Kotlin**
- Utilização de **classes, herança e encapsulamento**
- Organização de projetos Kotlin em diferentes packages

---

## 9. Future Improvements

Possíveis melhorias futuras incluem:

- Implementação de uma interface gráfica
- Persistência de dados em ficheiros ou base de dados
- Sistema de empréstimos de livros
- Sistema de autenticação de utilizadores

