# PayFlow

![CI](https://github.com/Adrian-Pardal/payflow/actions/workflows/ci.yml/badge.svg)

O PayFlow é uma plataforma de cobranças e pagamentos para pequenos negócios
(um Asaas/PagSeguro simplificado).

O lojista cria cobranças (Pix, boleto e cartão simulados), recebe pagamentos
numa carteira controlada por um ledger, é avisado por webhooks e acompanha
tudo num painel React. Ao longo do caminho entram antifraude, conciliação em
batch, integração com um banco parceiro instável, observabilidade e IA.

## Tecnologias

Java 25, Spring Boot 4, Maven, PostgreSQL 17, Flyway, JPA/Hibernate,
Docker Compose, Testcontainers e GitHub Actions.

## Como rodar

### Pré-requisitos

- JDK 25
- Docker Desktop (aberto)
- Git

### Passo a passo

1. Clone o repositório e entre na pasta:

```bash
   git clone https://github.com/Adrian-Pardal/payflow.git
   cd payflow
```

2. Crie o arquivo `.env` a partir do modelo e preencha os valores:

```bash
   cp .env.example .env
```

3. Suba a aplicação com o perfil de desenvolvimento. O PostgreSQL é
   iniciado automaticamente pelo Docker Compose:

```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

4. Confira se está tudo de pé em http://localhost:8080/actuator/health.

## Testes

```bash
./mvnw test     # só os testes unitários (rápidos)
./mvnw verify   # todos os testes, incluindo os de integração (precisa do Docker)
```

Antes de cada commit, formate o código:

```bash
./mvnw spotless:apply
```

## Integração contínua

A cada pull request e a cada push na `main`, o GitHub Actions confere a
formatação, procura segredos commitados (Gitleaks), compila o projeto e roda
todos os testes, incluindo os de integração com PostgreSQL real
(Testcontainers). Um pull request só deve entrar na `main` com o CI verde.

## Decisões de arquitetura

As decisões importantes estão registradas em [`docs/adr/`](docs/adr/):

- [0001 — Monólito modular](docs/adr/0001-monolito-modular.md)