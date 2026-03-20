# Assignment 1 — EcoSnapshot: Índice de Biodiversidade
Course: Licenciatura em Engenharia Informática e Multimédia
Student(s): Joana Ramos
Date: 08 março 2026
Repository URL: ___________
---
## 1. Introduction
O **EcoSnapshot** é uma aplicação Android concebida para entusiastas da natureza e investigadores registarem observações da biodiversidade local. O objetivo principal é fornecer um guia de campo digital onde o utilizador pode catalogar espécies de fauna e flora, escrever descrições detalhadas e anexar fotografias capturadas no momento da observação.

## 2. System Overview
As principais funcionalidades da aplicação incluem:
- **Índice Categorizado**: Visualização de espécies separadas por "Animais" e "Plantas" através de abas (Tabs).
- **Registo de Espécies**: Adição de novos registos com escolha imediata de categoria.
- **Edição Flexível**: Alteração de nomes comuns, científicos, habitat, raridade e descrições.
- **Captura de Imagem**: Integração com a câmara do dispositivo para fotos reais.
- **Persistência de Dados**: Armazenamento local em JSON para que os dados não se percam ao fechar a app.
- **Modo Dual de Visualização**: Ecrã de detalhes com separação entre modo de leitura (estético) e modo de edição (funcional).

## 3. Architecture and Design
A aplicação segue o padrão de design **MVVM (Model-View-ViewModel)**:
- **Model**: Data class `Species` que define a estrutura da entidade.
- **View**: Fragments (`BiodiversityListFragment`, `SpeciesDetailFragment`) que gerem a UI.
- **ViewModel**: `BiodiversityListViewModel` e `SpeciesDetailViewModel` que mantêm o estado da UI e comunicam com o repositório.
- **Repository**: `SpeciesRepository` que centraliza a lógica de dados, gerindo a lista em memória e a gravação em ficheiro.
- **Navegação**: Utilização do *Navigation Component* para gerir o fluxo entre a lista e os detalhes.

## 4. Implementation
- **Categorização**: Implementada com `TabLayout` e filtros dinâmicos na lista.
- **Câmara**: Uso de `ActivityResultContracts.TakePicture` e `FileProvider` para captura segura de imagens.
- **Persistência**: Serialização de dados em formato JSON guardado no armazenamento interno do dispositivo (`species_data.json`).
- **UI/UX**: Uso de Material Design 3, com cartões (Cards) em tons de cinza claro e botões de ação flutuantes (FAB).

## 5. Testing and Validation
- **CRUD**: Testes manuais de criação, leitura, atualização e eliminação de espécies.
- **Permissões**: Validação do pedido de permissão de câmara em tempo de execução.
- **Robustez**: Verificação da recuperação de dados após o encerramento forçado da aplicação.

## 6. Usage Instructions
1. Abra a app para ver o **ÍNDICE DE BIODIVERSIDADE**.
2. Navegue entre as abas **Animais** e **Plantas**.
3. Use o botão **"+"** para adicionar um novo registo, escolhendo a categoria desejada.
4. Clique num item para ver os detalhes. Use **"Editar"** para alterar textos ou o ícone da câmara para tirar uma foto.
5. Clique em **"Guardar Alterações"** para persistir os dados.
---
# Autonomous Software Engineering Sections
## 7. Prompting Strategy
As prompts evoluíram de pedidos simples de edição de listas para solicitações técnicas complexas, como "integrar câmara com gestão de permissões" e "implementar persistência de dados em JSON". Foram usadas prompts iterativas para corrigir erros de permissão de segurança detetados no Logcat.

## 8. Autonomous Agent Workflow
O agente IA contribuiu no planeamento da estrutura de dados, geração de layouts XML seguindo normas Material 3 e na resolução de bugs críticos de segurança (Runtime Permissions). O fluxo seguiu a ordem: Model -> Persistence -> ViewModel -> UI -> UX Refinement.

## 9. Verification of AI-Generated Artifacts
O código gerado foi verificado manualmente no Android Studio, utilizando inspeções de código para adotar extensões Kotlin KTX (como `.isVisible`) e garantindo que o `FileProvider` estava corretamente configurado no `AndroidManifest.xml`.

## 10. Human vs AI Contribution
- **Humano**: Definição de requisitos funcionais, preferências estéticas (cores, títulos) e fluxo de navegação pretendido.
- **IA**: Implementação técnica de APIs complexas (Câmara, Navegação, JSON), lógica de persistência e *debugging*.

## 11. Ethical and Responsible Use
A aplicação gere permissões de privacidade (Câmara) de forma transparente e armazena os dados localmente no dispositivo, respeitando a privacidade do utilizador.
---
# Development Process
## 12. Version Control and Commit History
O histórico reflete o desenvolvimento incremental: 1. Setup inicial, 2. Modo de edição, 3. Integração de Câmara, 4. Categorização por Tabs, 5. Persistência de Dados.

## 13. Difficulties and Lessons Learned
Inicialmente, tinham sido criados vários projetos e com o JDK25 no AntiGravity estavam a haver alguns problemas. Tnetei resolver com o próprio AI do AntiGravity mas ele não conseguiu ajudar muito. Eventualmente apenas corri a aplicação no Android Studio e este
apenas ignorou os erros que davam no AntiGravity. Sei isto, pois a app funciona bem mesmo com aqueles erros presentes.
O maior desafio foi a gestão de permissões da câmara em versões recentes do Android e a garantia de que as URIs das imagens eram persistidas corretamente. Aprendi a importância de separar a lógica de negócio (Repository) da UI.

## 14. Future Improvements
- Integração com mapas (GPS) para marcar o local exato da observação.
- Sincronização com a Cloud.
- Reconhecimento automático de espécies via IA.

---
## 15. AI Usage Disclosure (Mandatory)
Utilização do assistente de IA do Android Studio para geração de código, auxílio no design de interfaces e correção de erros. Confirmo que revi e sou responsável por todo o conteúdo final.
