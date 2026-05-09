# OutForce - Coupon API

## Descrição do Projeto

Esta aplicação consiste em uma API REST desenvolvida em **Java com Spring Boot**, responsável pelo gerenciamento de **cupons**, seguindo as regras de negócio propostas no desafio técnico.

O projeto foi desenvolvido com foco em:
- Clareza de código
- Boa modelagem de domínio
- Arquitetura hexagonal
- Separação de responsabilidades
- Validação correta das regras de negócio
- Tratamento padronizado de erros
- Testes unitários
- Facilidade de execução e avaliação

A API está documentada com **Swagger / OpenAPI** e preparada para execução **localmente ou via Docker**.

---

## Modelagem de Domínio

### Coupon

Um cupom possui os seguintes atributos:
- **Id** - identificador único do cupom
- **Code** - código do cupom
- **Description** - descrição do cupom
- **Discount Value** - valor de desconto
- **Expiration Date** - data de expiração
- **Status** - estado atual do cupom
- **Published** - indica se o cupom foi publicado
- **Redeemed** - indica se o cupom foi resgatado
- **Deleted At** - data de exclusão lógica

### Status do Cupom

Os status implementados são:
- `ACTIVE`
- `DELETED`

---

## Regras de Negócio Implementadas

- Todo cupom deve possuir `code`, `description`, `discountValue` e `expirationDate`
- O campo `code` aceita caracteres especiais na entrada, mas eles são removidos antes da persistência
- O `code` final precisa conter exatamente **6 caracteres alfanuméricos**
- O `discountValue` deve ser **maior ou igual a 0.5**
- O `expirationDate` não pode estar no passado no momento da criação
- O cupom pode ser criado já com `published = true`
- Todo cupom criado inicia com:
  - `status = ACTIVE`
  - `redeemed = false`
- O delete do cupom é feito com **soft delete**
- Um cupom que já foi deletado não pode ser deletado novamente

As regras de negócio principais foram encapsuladas no **domínio**, evitando depender apenas da camada HTTP para garantir consistência.

---

## Arquitetura

O projeto foi estruturado seguindo os princípios da **arquitetura hexagonal (Ports and Adapters)**.

Essa abordagem foi adotada para:
- Isolar o domínio das dependências externas
- Separar claramente regras de negócio de detalhes de infraestrutura
- Facilitar testes unitários
- Permitir evolução da aplicação com menor acoplamento

### Como isso está representado no projeto

- **Domínio**
  Contém as entidades, enums e exceções de negócio.
- **Application**
  Contém os casos de uso e as portas de entrada e saída da aplicação.
- **Infrastructure**
  Contém os adapters responsáveis por controller HTTP, persistência, tratamento de exceções e configurações.

### Fluxo da aplicação

- O controller recebe a requisição HTTP
- A entrada é convertida para um comando de aplicação
- O caso de uso executa a regra de negócio
- As portas de saída abstraem o acesso à persistência
- Os adapters concretos implementam essas portas

Com isso, o núcleo da aplicação permanece desacoplado de framework web, banco de dados e detalhes de infraestrutura.

### Justificativa da escolha arquitetural

Uma arquitetura em camadas tradicional também seria suficiente para resolver o desafio. Ainda assim, foi escolhida a **arquitetura hexagonal** porque o enunciado exige atenção especial ao encapsulamento das regras de negócio no domínio.

Com essa abordagem, foi possível:
- Centralizar invariantes do cupom no núcleo da aplicação
- Evitar que a criação do domínio dependesse diretamente do controller ou do JPA
- Facilitar testes unitários isolados
- Tornar a aplicação mais preparada para evolução, caso novas formas de entrada ou persistência fossem adicionadas no futuro

Para o escopo do desafio, a opção foi usar uma estrutura hexagonal enxuta, sem abstrações desnecessárias, buscando equilíbrio entre organização, clareza e complexidade.

---

## Estrutura da Aplicação

O projeto foi organizado em camadas:

- `domain`
  Contém entidades, enums e exceções de negócio.
- `application`
  Contém os casos de uso e comandos de entrada da aplicação.
- `infrastructure`
  Contém controller, persistência, tratamento global de exceções e configurações.

Essa divisão ajuda a manter baixo acoplamento e deixa as responsabilidades mais claras para manutenção e evolução.

---

## Funcionalidades Implementadas

### Cupons

- Cadastro de cupom
- Busca de cupom por id
- Exclusão lógica de cupom
- Validação de request com Bean Validation
- Tratamento padronizado de erros

---

## Endpoints

### Criar cupom

`POST /coupon`

Exemplo:

```bash
curl --location 'http://localhost:8081/coupon' \
--header 'Content-Type: application/json' \
--data '{
  "code": "ABC-123",
  "description": "Cupom de teste",
  "discountValue": 0.8,
  "expirationDate": "2026-11-04T17:14:45.180Z",
  "published": false
}'
```

### Buscar cupom por id

`GET /coupon/{id}`

Exemplo:

```bash
curl --location 'http://localhost:8081/coupon/{id}'
```

### Deletar cupom

`DELETE /coupon/{id}`

Exemplo:

```bash
curl --location --request DELETE 'http://localhost:8081/coupon/{id}'
```

---

## Documentação da API

A documentação da API foi implementada com **Swagger / OpenAPI**.

Após subir a aplicação, acesse:

`http://localhost:8081/swagger-ui.html`

OpenAPI JSON:

`http://localhost:8081/v3/api-docs`

Todos os endpoints, payloads e respostas de erro relevantes estão documentados.

---

## Tratamento de Erros

Foi implementado um tratamento global de exceções para padronizar as respostas da API.

Exemplo de erro de validação:

```json
{
  "timestamp": "2026-05-09T15:10:00Z",
  "status": 400,
  "message": "Validation failed",
  "path": "/coupon",
  "fieldErrors": [
    {
      "field": "code",
      "message": "Code is required"
    }
  ]
}
```

Situações tratadas:
- violação de regra de negócio
- recurso não encontrado
- erro de validação de campos de entrada

---

## Persistência e Banco de Dados

O projeto utiliza **H2 em memória**, conforme solicitado no desafio.

Configuração principal:
- Banco em memória
- Schema gerado automaticamente pelo Hibernate
- Console H2 habilitado

H2 Console:

`http://localhost:8081/h2-console`

Configuração:
- JDBC URL: `jdbc:h2:mem:outforcedb`
- Rodando a aplicação sem configurar variáveis de ambiente:
  - User Name: `sa`
  - Password: `sa`
- Rodando com `docker compose`:
  - User Name: `admin`
  - Password: `admin`

---

## Testes

Foram implementados testes unitários cobrindo:

- Regras de criação do cupom
- Sanitização do código
- Validação de campos obrigatórios
- Validação de desconto mínimo
- Validação de data de expiração
- Soft delete
- Tentativa de deletar cupom já deletado
- Caso de uso de criação, busca e delete
- Validação do request DTO
- Tratamento global de exceções
- Adapter de persistência

Ferramentas utilizadas:
- **JUnit 5**
- **Mockito**
- **Bean Validation Test API**

Para executar os testes:

```bash
mvn test
```

---

## Como Executar o Projeto

### Executando sem Docker

Pré-requisitos:
- Java 21+
- Maven 3+

Execute:

```bash
mvn spring-boot:run
```

Ou com Maven Wrapper:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

---

## Executando com Docker

Pré-requisitos:
- Docker
- Docker Compose

### Subir com Docker Compose

```bash
docker compose up
```

A aplicação será exposta em:

`http://localhost:8081`

### Variáveis utilizadas no container

- `SERVER_PORT=8081`
- `DB_USERNAME=admin`
- `DB_PASSWORD=admin`

Mesmo utilizando H2 em memória, essas variáveis foram mantidas para padronizar a configuração da aplicação.

---

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 Database
- Springdoc OpenAPI
- JUnit 5
- Mockito
- Docker
- Docker Compose

---

## Boas Práticas Aplicadas

- Arquitetura hexagonal com Ports and Adapters
- Separação clara de responsabilidades
- Regras de negócio encapsuladas no domínio
- Validação em camadas
- Tratamento global de exceções
- Soft delete para preservação de histórico
- Testes unitários para regras críticas
- Documentação via Swagger
- Estrutura organizada para leitura e avaliação técnica
