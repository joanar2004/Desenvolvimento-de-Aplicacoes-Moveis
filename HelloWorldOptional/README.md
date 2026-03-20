# Assignment 1 — Hello Kotlin. Hello Android World!

**Course:** Licenciatura em Engenharia Informática e Multimédia 
**Student(s):** Joana Ramos 
**Date:** 08 de março de 2026
**Repository URL:** https://github.com/joanar2004/HelloWorldOptional.git

---

## 1. Introduction
Este trabalho marca o primeiro contacto com o desenvolvimento nativo para Android utilizando a linguagem Kotlin. O foco principal foi a criação de uma aplicação simples que interage com o sistema operativo para extrair e exibir informações técnicas do hardware.

## 2. System Overview
A aplicação consiste num ecrã único que apresenta dinamicamente os dados do dispositivo. Ao iniciar, a aplicação recolhe metadados do sistema e injeta-os num componente de texto, permitindo ao utilizador visualizar detalhes como:
- Fabricante e Modelo.
- Versão do SDK e código da versão Android.
- Informações de Build (Brand, Type, User, Display, etc.).

## 3. Architecture and Design
O projeto segue a estrutura fundamental do Android:
- **UI:** Definida de forma declarativa no ficheiro `activity_main.xml` usando `ConstraintLayout` para um posicionamento preciso dos elementos.
- **Lógica:** Implementada na `MainActivity.kt`, onde é feita a ponte entre os dados do sistema e a interface.
- **Recursos:** Utilização de `strings.xml` para a gestão de constantes de texto e `colors.xml` para a paleta de cores.

## 4. Implementation   
A implementação destaca-se pelos seguintes pontos:
- **Extração Dinâmica:** Utilização extensiva da classe `android.os.Build` para aceder às propriedades de hardware e software do dispositivo em tempo de execução.
- **Formatação de Dados:** Uso de *Raw Strings* em Kotlin (`"""`) para organizar a informação de forma legível sem a necessidade de múltiplas concatenações.
- **Edge-to-Edge:** Integração com as APIs mais recentes (`enableEdgeToEdge`) para permitir que a interface utilize toda a área do ecrã, incluindo as barras de sistema.

## 5. Testing and Validation
- **Verificação de Dados:** Testes realizados em emuladores com diferentes versões de API para garantir que a classe `Build` devolve os valores corretos.
- **UI:** Validação da legibilidade do `TextView` e ajuste de margens (padding) para garantir que o texto não é cortado pelas extremidades do ecrã.

## 6. Usage Instructions
1. Clonar o repositório do GitHub.
2. Abrir o projeto no **Android Studio**.
3. Aguardar a sincronização do Gradle.
4. Executar num dispositivo físico ou emulador com suporte para o SDK configurado.

---

## 7. Version Control and Commit History
O controlo de versões foi gerido via Git, com commits focados na configuração inicial do projeto, definição do layout XML e implementação da lógica de extração de dados em Kotlin.

## 8. Difficulties and Lessons Learned
- **Dificuldades:** Compreender a hierarquia de insets do sistema para garantir que o texto não ficasse sobreposto à barra de status (Edge-to-edge).
- **Aprendizagens:** Manipulação básica de widgets em Kotlin, compreensão da estrutura de ficheiros de um projeto Android e utilização da classe `Build`.

## 9. Future Improvements
- Adicionar um botão para copiar os dados para a área de transferência ou partilhar.
- Implementar um layout mais visual (ex: usando Cards para cada categoria de informação).
- Suporte para modo escuro (Dark Mode) personalizado.
