# 📌 Projeto de Nivelamento NV1 — Backend Java

## 📖 Visão Geral

Este projeto consiste no desenvolvimento de uma **API RESTful** voltada para o **gerenciamento de reservas de salas**, permitindo que clientes realizem agendamentos de espaços para eventos de forma organizada, segura e eficiente.

A solução oferece recursos como:

- Cadastro e gerenciamento de usuários
- Registro e controle de salas disponíveis
- Criação, consulta e administração de reservas
- Cancelamento com regras de negócio
- Filtros e paginação nas consultas

---

## 🧾 Sumário

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura e Organização](#-arquitetura-e-organização)
- [Modelo de Dados](#-modelo-de-dados)
- [Endpoints Disponíveis](#-endpoints-disponíveis)
    - [Usuários](#-usuários)
    - [Salas](#-salas)
    - [Reservas](#-reservas)
- [Filtros de Reserva](#-filtros-de-reserva)
- [Exemplo de Cancelamento de Reserva](#-exemplo-de-cancelamento-de-reserva)
- [Como Executar com Docker Compose (Prod)](#-como-executar-com-docker-compose-prod)
- [Como Executar em Desenvolvimento (Dev)](#-como-executar-em-desenvolvimento-dev)
- [Executando Testes](#-executando-testes)
- [Aprendizados Obtidos](#-aprendizados-obtidos)

---

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.x**
- **Gradle**
- **Docker**
- **MariaDB**

---

## 🏗️ Arquitetura e Organização

O projeto segue uma estrutura inspirada no ecossistema Spring Boot, separando responsabilidades por camadas e organizando os recursos por contexto.

### 📌 `domain`
Camada responsável pelo núcleo da aplicação (entidades, regras de negócio, serviços, repositórios e mappers).

### 🌐 `web`
Camada de apresentação (controllers, DTOs, validações e handlers globais com `@RestControllerAdvice`).

### ⚙️ `config`
Pacote reservado para configurações futuras.

### ❗ `exceptions`
Exceções personalizadas utilizadas pela aplicação.

### 🌱 `seeders`
Geração de dados fictícios para facilitar desenvolvimento/testes manuais.

### 🧰 `utils`
Classes utilitárias de suporte geral.

---

## 🗂️ Modelo de Dados

```mermaid
erDiagram
USUARIO ||--o{ RESERVA : faz
SALA    ||--o{ RESERVA : possui

USUARIO {
Long id PK
TEXT cpf UK
TEXT nome
TEXT sobrenome
TEXT email UK
TEXT telefone
TIMESTAMP dataNascimento
BOOLEAN ativo
}

RESERVA {
UUID id PK
TIMESTAMP dataInicio
TIMESTAMP dataFim
ENUM situacao
TIMESTAMP dataCancelamento
TEXT motivoCancelamento
}

SALA {
Long id PK
TEXT nome
INTEGER capacidade
}
```

---

# 📌 Endpoints Disponíveis

A API segue o padrão REST e está versionada em:

```
/api/v1
```

> **Paginação/ordenação (Spring Pageable)**: `page`, `size`, `sort` (ex.: `sort=dataInicio,desc`).

---

## 👤 Usuários

Base path:

```
/api/v1/usuarios
```

- `GET /api/v1/usuarios` — lista paginada
- `POST /api/v1/usuarios` — cadastra usuário
- `GET /api/v1/usuarios/{id}` — detalha usuário
- `PATCH /api/v1/usuarios/{id}` — atualiza usuário
- `DELETE /api/v1/usuarios/{id}` — remove usuário

---

## 🏢 Salas

Base path:

```
/api/v1/salas
```

- `GET /api/v1/salas` — lista paginada
- `POST /api/v1/salas` — cadastra sala
- `GET /api/v1/salas/{id}` — detalha sala
- `DELETE /api/v1/salas/{id}` — remove sala

---

## 📅 Reservas

Base path:

```
/api/v1/reservas
```

- `GET /api/v1/reservas` — lista paginada com filtros via query params
- `POST /api/v1/reservas` — cria reserva
- `GET /api/v1/reservas/{id}` — detalha reserva
- `PATCH /api/v1/reservas/{id}/cancelar` — cancela reserva
- `DELETE /api/v1/reservas/{id}` — remove reserva

---

## 🔎 Filtros de Reserva

O endpoint `GET /api/v1/reservas` aceita filtros via query params (opcionais):

| Parâmetro | Tipo | Descrição |
|---|---|---|
| `situacao` | `SituacaoReserva` | Situação atual da reserva (enum). |
| `usuarioId` | `Long` | Filtra reservas por usuário. |
| `salaId` | `Long` | Filtra reservas por sala. |
| `inicioDe` | `LocalDateTime` | Filtra reservas com `dataInicio >= inicioDe`. |
| `inicioAte` | `LocalDateTime` | Filtra reservas com `dataInicio <= inicioAte`. |

📌 **Formato de data/hora**: use ISO-8601, por exemplo: `2026-02-10T00:00:00`.

Exemplos:

```http
GET /api/v1/reservas?situacao=ATIVA
```

```http
GET /api/v1/reservas?usuarioId=10&salaId=2
```

```http
GET /api/v1/reservas?inicioDe=2026-02-10T00:00:00&inicioAte=2026-02-20T23:59:59
```

Com paginação/ordenação:

```http
GET /api/v1/reservas?size=20&page=0&sort=dataInicio,desc
```

---

## ✋ Exemplo de Cancelamento de Reserva

Endpoint:

```http
PATCH /api/v1/reservas/{id}/cancelar
```

Body:

```json
{
  "motivoCancelamento": "Cliente desistiu do evento"
}
```

Response (exemplo):

```json
{
  "id": "2b1c2f65-1c8f-4b14-a5d5-3cbbf4c1a9a9",
  "dataCancelamento": "06/02/2026 14:22",
  "motivoCancelamento": "Cliente desistiu do evento"
}
```

---

## 🐳 Como Executar com Docker Compose (Prod)

O projeto possui um `docker-compose.yaml` com dois serviços:

- **mariadb** (MariaDB 11) com healthcheck
- **alura-app** (API) rodando com profile `prod` e variáveis de ambiente para o datasource

Subir a stack:

```bash
docker compose up --build
```

A aplicação ficará disponível em:

- API: `http://localhost:8080`
- MariaDB (porta exposta): `localhost:3307` (mapeada para `3306` no container)

> Observação: no profile `prod`, as configs do datasource são lidas de `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`.

---

## 🛠️ Como Executar em Desenvolvimento (Dev)

No profile `dev`, o projeto utiliza integração do Spring com Docker Compose (arquivo `docker-compose-dev.yaml`) para iniciar o banco automaticamente.

Opções comuns:

### Rodar com profile dev

```bash
gradle bootRun --args='--spring.profiles.active=dev'
```

Ou configure `SPRING_PROFILES_ACTIVE=dev` no ambiente/IDE.

> No `dev`, o MariaDB normalmente fica em `jdbc:mariadb://localhost:3307/alura1`.

---

## 🧪 Executando Testes

```bash
gradle test
```

---

## 📚 Aprendizados Obtidos

- Construção de APIs REST com Spring Boot
- Modelagem relacional com JPA/Hibernate
- Validações com Bean Validation
- Filtros + paginação com `Pageable`
- Infra local com Docker Compose
- Boas práticas de documentação e estruturação

---

## ✅ Conclusão

Este projeto representa uma base sólida para aplicações backend modernas, servindo como exercício prático de arquitetura, organização e desenvolvimento com Java e Spring Boot.
