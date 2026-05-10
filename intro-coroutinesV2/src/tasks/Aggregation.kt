package tasks

import contributors.User

/*
// A API do GitHub retorna os contribuidores separados por repositório,
// ou seja, o mesmo utilizador pode aparecer várias vezes na lista —
// uma vez por cada repositório em que contribuiu.
//
// Precisamos de agregar esses dados para obter uma lista final onde:
//   - cada utilizador aparece apenas UMA vez
//   - com o TOTAL das suas contribuições em todos os repositórios
//   - ordenada do utilizador com mais contribuições para o com menos
//
// Exemplo do problema:
//   zarechenskiy - 100 (kotlin-examples)
//   zarechenskiy -  50 (kotlin-koans)
//   goodwinnk    -  29 (kotlin-examples)
//
// Resultado esperado após aggregate():
//   zarechenskiy - 150
//   goodwinnk    -  29
 */

// Função de extensão sobre List<User> — pode ser chamada em qualquer lista de utilizadores
fun List<User>.aggregate(): List<User> =

// Agrupar os utilizadores pelo seu login (nome de utilizador)
// Resultado: Map<String, List<User>>
    // ex: { "zarechenskiy" -> [User(100), User(50)], "goodwinnk" -> [User(29)] }
    groupBy { it.login }

        // Para cada grupo (mesmo utilizador, várias entradas), cria um único User
        // (login, group): login = nome do utilizador, group = lista de todas as suas entradas
        .map { (login, group) ->
            User(
                login = login,  // mantém o nome do utilizador
                contributions = group.sumOf { it.contributions }  // soma todas as suas contribuições
                // ex: zarechenskiy: 100 + 50 = 150 contribuições no total
            )
        }

        // Ordena a lista final por número de contribuições, do maior para o menor
        // ex: zarechenskiy(150), goodwinnk(29), ...
        .sortedByDescending { it.contributions }