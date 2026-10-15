# payflow
O PayFlow é uma plataforma de cobranças e pagamentos para pequenos negócios (um Asaas/PagSeguro simplificado)

O lojista cria cobranças (Pix, boleto e cartão simulados), recebe pagamentos numa carteira controlada por um ledger, é 
avisado por webhooks e acompanha tudo num painel React. Ao longo do caminho entram antifraude, conciliação em batch, 
integração com um banco parceiro instável, observabilidade e IA

![CI](https://github.com/Adrian-Pardal/payflow/actions/workflows/ci.yml/badge.svg)

Esta sendo usado no projeto até o momento na data 14/10/2026 as versões mais recentes do actions/checkout e 
actions/setup-java

```
    steps:
      - name: Baixa o código
        uses: actions/checkout@v7

      - name: Instala o Java 25
        uses: actions/setup-java@v6
        with:
          distribution: temurin
          java-version: '25'
          cache: maven
```

## Integração contínua

A cada pull request e a cada push na `main`, o GitHub Actions compila o projeto
e roda todos os testes, incluindo os de integração com PostgreSQL real
(Testcontainers). Um pull request só deve entrar na `main` com o CI verde.

Para rodar a mesma verificação na sua máquina (com o Docker aberto):

./mvnw verify
