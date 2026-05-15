# Trabalho Prático — CoolWeatherApp
Course: Desenvolvimento de Aplicações Móveis (LEIM)
Student(s): Joana Ramos (a50829)
Date: 3 de Maio de 2026
Repository URL: [Link para o teu repositório GitHub]

---

## 1. Introdução
Este trabalho foi desenvolvido no âmbito da unidade curricular de Desenvolvimento de Aplicações Móveis. O objetivo principal é a criação de uma aplicação Android moderna que permita consultar informações meteorológicas em tempo real através da integração com uma API externa (Open-Meteo). A aplicação foca-se na utilização de tecnologias declarativas (Jetpack Compose) e na gestão de estado eficiente.

## 2. Visão Geral do Sistema
A CoolWeatherApp é uma aplicação de meteorologia que permite aos utilizadores visualizarem o estado do tempo atual para coordenadas específicas (Latitude e Longitude).
**Principais Funcionalidades:**
- Visualização de temperatura, velocidade e direção do vento, pressão atmosférica e hora da leitura.
- Interface dinâmica que altera o fundo (gradiente) e os ícones consoante o estado do tempo e o período do dia (dia/noite).
- Suporte para modo Retrato (Portrait) e Paisagem (Landscape).
- Internacionalização com suporte para Português e Inglês.
- Introdução manual de coordenadas para consulta global.

## 3. Arquitetura e Design
A aplicação segue o padrão de arquitetura **MVVM (Model-View-ViewModel)**, recomendado pela Google para garantir a separação de conceitos e testabilidade:

- **UI Layer (View):** Implementada com Jetpack Compose. Ficheiros como `WeatherScreen.kt`, `WeatherCard.kt` e `CoordinatesCard.kt` definem a interface de forma declarativa.
- **State Management:** Utilização de `StateFlow` no ViewModel para emitir estados de UI (`WeatherUIState`) que a View observa e reage.
- **Data Layer:** Responsável pelo consumo da API utilizando o cliente HTTP **Ktor** e serialização de dados com **Kotlinx Serialization**.
- **Pasta Structure:**
    - `ui/`: Componentes visuais e ecrãs principais.
    - `viewmodel/`: Lógica de negócio e gestão de estado.
    - `data/`: Clientes de rede e modelos de dados da API.

## 4. Implementação
A implementação destaca-se pelo uso de:
- **Corrotinas:** Para chamadas de rede assíncronas sem bloquear a interface.
- **State Hoisting:** Os componentes de UI (como o `WeatherCard`) são "stateless", recebendo dados via parâmetros, o que facilita a sua reutilização.
- **Lógica de Ícones Dinâmica:** Implementação de uma função mapeadora (`getWeatherIcon`) que traduz códigos WMO para recursos gráficos (`drawables`) específicos, diferenciando inclusive entre dia e noite.
- **Animações:** Utilização de `animateFloatAsState` para suavizar a transição dos valores de temperatura no ecrã.

## 5. Testes e Validação
A validação da aplicação foi feita através de:
- **Testes de UI:** Verificação da adaptabilidade do layout ao rodar o dispositivo (Landscape vs Portrait).
- **Cenários de Erro:** Tratamento de inputs inválidos nas coordenadas (campos vazios ou texto em vez de números).
- **Validação de Dados:** Comparação dos dados mostrados na aplicação com os resultados diretos da API Open-Meteo.
- **Limitações Conhecidas:** A aplicação requer uma ligação ativa à internet e não utiliza, de momento, o GPS do dispositivo para localização automática.

## 6. Instruções de Utilização
**Requisitos:** Android Studio Ladybug ou superior, Android SDK 34+.
1. Clonar o repositório.
2. Abrir o projeto no Android Studio.
3. Sincronizar o Gradle e esperar pelo download das dependências (Ktor, Compose, etc.).
4. Executar num Emulador ou dispositivo físico com Android 8.0+.
5. Na aplicação, introduzir a Latitude e Longitude desejadas e clicar em "Atualizar Tempo".

# Processo de Desenvolvimento

## 12. Controlo de Versões e Histórico de Commits
Foi utilizado o Git para o controlo de versões. O histórico de commits reflete o desenvolvimento incremental, começando pela estrutura base do projeto, integração da API, criação dos componentes de UI e, finalmente, o polimento visual com drawables personalizados e suporte para diferentes orientações.

## 13. Dificuldades e Lições Aprendidas
- **Dificuldade:** Mapear corretamente os códigos meteorológicos da API para os ícones drawables, garantindo que a lógica de dia/noite funcionava corretamente.
- **Lição:** A importância de usar o `LocalConfiguration` do Compose para criar interfaces verdadeiramente responsivas que se adaptam à orientação do ecrã sem perder o estado da UI.
- **Insight:** O uso de gradientes dinâmicos melhora significativamente a experiência do utilizador, tornando a app mais orgânica.

## 14. Melhorias Futuras
- Implementação da API de Location do Android para detetar automaticamente a localização do utilizador.
- Adição de uma funcionalidade de "Favoritos" para guardar localizações frequentes.
- Implementação de uma previsão para os próximos 7 dias (atualmente apenas mostra o tempo atual).

