# Monólito modular com pacotes por funcionalidade

- **Status:** Aceito
- **Data:** 2026-10-15

## Contexto

O PayFlow é uma plataforma de cobranças e pagamentos construída por uma
única pessoa, com o objetivo de aprender e aplicar práticas usadas no
mercado. O projeto pode crescer no futuro, mas hoje será publicado em
serviços gratuitos (Render ou Railway), com recursos limitados.

O sistema é dividido nos módulos `merchant`, `charge`, `ledger`, `payment`
e `shared`. Várias operações atravessam mais de um módulo. Um pagamento,
por exemplo, precisa ao mesmo tempo mudar a cobrança para PAGA, registrar
o pagamento e gravar no ledger os lançamentos de débito e crédito (de onde
o dinheiro saiu e para onde foi). Ou tudo isso acontece, ou nada acontece:
se a cobrança virar PAGA e os lançamentos falharem, o dinheiro "some".

## Decisão

O PayFlow será um único deploy (monólito), dividido em módulos por
funcionalidade. Cada módulo tem as camadas `api`, `application`, `domain`
e `infrastructure`, e todos usam o mesmo banco PostgreSQL.

Um módulo nunca acessa as classes internas de outro, como repositórios ou
entidades. A comunicação acontece pela "porta da frente": a camada
`application` do outro módulo ou eventos. Por exemplo, o `PaymentService`
não grava diretamente nas tabelas do ledger; ele pede ao `LedgerService`,
que aplica as regras do ledger (débitos iguais a créditos) antes de gravar.

A única exceção prevista é o `partner-bank` (Fase 7), um segundo serviço
que simula um banco parceiro externo e instável. Ele não é um módulo do
PayFlow separado, e sim outro sistema, criado de propósito para praticar
integração, resiliência e rastreamento de requisições entre serviços.

## Alternativas consideradas

- **Microsserviços desde o início:** para uma pessoa sozinha, multiplicaria
  o trabalho de deploy, a configuração de rede e o custo de hospedagem, e
  tornaria a depuração em produção mais lenta. O principal problema, porém,
  é a consistência: com um banco por serviço, garantir o "tudo ou nada" de
  um pagamento entre serviços exige mecanismos complexos, que não se
  justificam neste momento.
- **Monólito organizado por camada** (`controller/`, `service/`,
  `repository/`): uma mudança em uma funcionalidade obrigaria a procurar
  arquivos em várias pastas, misturados com os de outros assuntos. Além
  disso, como todas as classes ficam "no mesmo andar", é fácil um módulo
  acessar o repositório de outro, e as fronteiras se perdem com o tempo.

## Consequências

- (+) Operações que envolvem vários módulos, como um pagamento, rodam em
  uma única transação do banco, com garantia de "tudo ou nada".
- (+) Depuração mais simples: um processo só, um log só, e o erro aparece
  numa pilha de chamadas completa, sem precisar rastrear requisições entre
  serviços.
- (+) Deploy simples e de baixo custo, adequado aos serviços gratuitos.
- (+) Se um módulo precisar virar um serviço no futuro, ele já está isolado
  em uma pasta própria, o que facilita a extração.
- (−) Se as fronteiras entre módulos não forem respeitadas, o código vira
  um emaranhado difícil de manter. Para reduzir esse risco, um teste com
  ArchUnit no CI vai reprovar o build quando um módulo acessar as classes
  internas de outro.
- (−) Não é possível escalar um módulo isoladamente: se só os pagamentos
  tiverem alta demanda, é preciso subir cópias da aplicação inteira.
- (−) Uma falha grave em qualquer módulo (um vazamento de memória, por
  exemplo) pode derrubar a aplicação inteira.