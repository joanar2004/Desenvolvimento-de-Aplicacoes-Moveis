# Assignment 1 — Hello Kotlin. Hello Android World!

**Course:** Licenciatura em Engenharia Informática e Multimédia 
**Student(s):** Joana Ramos 
**Date:** 08 março 2026 
**Repository URL:** https://github.com/joanar2004/HelloWorld.git

---

## 1. Introduction
O objetivo deste trabalho foi realizar o primeiro contacto com o desenvolvimento Android utilizando Kotlin. A aplicação foca-se na criação de uma interface básica, manipulação de recursos (strings, cores e imagens) e interação com APIs do sistema para obter informações do hardware.

## 2. System Overview
A aplicação é composta por um ecrã principal que apresenta:
- Mensagem de boas-vindas via `Toast`.
- Elementos visuais como `TextView`, `ImageView` e um `CalendarView`.
- Um botão de partilha que extrai e exporta os dados técnicos do dispositivo (Modelo, Fabricante, Versão SDK, etc.).

## 3. Architecture and Design
O projeto segue a arquitetura padrão do Android para aplicações simples:
- **UI:** Definida em XML (`activity_main.xml`) utilizando `ConstraintLayout` para garantir responsividade.
- **Lógica:** Implementada na `MainActivity.kt` em Kotlin.
- **Recursos:** Separação de strings, cores e dimensões nos ficheiros respetivos em `res/values`.

## 4. Implementation   
Os pontos principais da implementação incluem:
- **Extração de Dados:** Utilização da classe `android.os.Build` para obter detalhes do dispositivo.
- **Intents:** Uso de `ACTION_SEND` para permitir que o utilizador exporte os dados do sistema para outras aplicações (Email, Mensagens, etc.).
- **Edge-to-Edge:** Implementação de suporte para ecrã total, ajustando os insets das barras do sistema.


## 5. Testing and Validation
- **Testes Manuais:** Verificação da interface em modo Portrait (Vertical) e Landscape (Horizontal).
- **Funcionalidade:** Confirmação de que o botão de partilha abre corretamente o seletor do sistema com os dados do dispositivo preenchidos.
- **UI:** Verificação do contraste de cores e legibilidade dos textos.

## 6. Usage Instructions
1. Clonar o repositório.
2. Abrir o projeto no **Android Studio (Koala ou superior)**.
3. Sincronizar o projeto com o Gradle.
4. Executar num Emulador ou Dispositivo Físico com API 24+.

---

## 7. Version Control and Commit History
Utilizou-se o Git para o controlo de versões, com commits regulares à medida que as funcionalidades (UI, lógica de extração e partilha) eram implementadas.

## 8. Difficulties and Lessons Learned
- **Dificuldades:** Ajustar o layout para que o `CalendarView` e a `ImageView` coexistissem sem sobreposição em diferentes tamanhos de ecrã.
- **Aprendizagens:** Sintaxe básica de Kotlin, ciclo de vida da Activity e como utilizar Intents para comunicar com outras apps.

## 9. Future Improvements
- Adicionar suporte para múltiplos idiomas.
- Guardar o histórico de exportações localmente.
- Melhorar o design visual com Material Design 3.
