# 🏦 Arquitetura de Microsserviços Bancários Orientada a Eventos

Este projeto é um ecossistema bancário distribuído que simula o funcionamento central de um banco digital. Ele foi construído com foco em **alta disponibilidade, resiliência e consistência eventual**, utilizando o Apache Kafka como espinha dorsal da comunicação assíncrona entre os microsserviços.

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 17 / 21
* **Framework:** Spring Boot 3.x
* **Mensageria:** Apache Kafka (com Spring Kafka)
* **Segurança:** Spring Security + JWT (Stateless)
* **Banco de Dados:** PostgreSQL (Bancos independentes por serviço)
* **Infraestrutura:** Docker e Docker Compose

## 🧩 Estrutura dos Microsserviços

O ecossistema é composto por três microsserviços totalmente desacoplados:

### 1. 🔐 MS-Autenticacao
Responsável pela porta de entrada do usuário.
* Gerencia o cadastro de novos clientes.
* Autentica credenciais e emite Tokens JWT.
* **Producer:** Ao criar um usuário com sucesso, emite um evento no tópico `usuario-cadastrado-topic`.

### 2. 💸 MS-Transacoes
Responsável pelo motor financeiro. Protegido por Spring Security.
* **Consumer:** Lê o tópico de cadastros e cria contas bancárias locais (Sincronização de Dados).
* Valida regras de negócio (saldo suficiente, contas válidas, prevenção de auto-transferência).
* Audita falhas financeiras utilizando transações isoladas (`REQUIRES_NEW`).
* **Producer:** Após sucesso, emite os dados da operação no tópico `transferencia-realizada-topic`.

### 3. 📩 MS-Notificacoes
Responsável pela comunicação com o cliente. Desenvolvido sob o padrão **Event-Carried State Transfer**.
* **Consumer (Passado):** Lê o tópico de usuários desde o início (`earliest`) e constrói uma base de dados local apenas com IDs e E-mails.
* **Consumer (Presente):** Lê o tópico de transferências, cruza os IDs com o banco local e dispara e-mails de comprovante.
* **Resiliência:** Capaz de notificar clientes mesmo se o `ms-autenticacao` estiver fora do ar.

## 🧠 Decisões Arquiteturais e Desafios Resolvidos

* **Segurança Zero-Trust e "Anti-Cavalo de Troia":** A API de transações não confia no ID de origem enviado pelo JSON. A identidade do remetente é extraída  do Token JWT interceptado pelo `SecurityFilter`, impossibilitando fraudes de manipulação de *payload*.
* **Resiliência com Event-Carried State Transfer:** Para evitar gargalos síncronos (HTTP/Feign Client), o serviço de notificações mantém uma cópia eventual dos e-mails dos usuários. Se o serviço de autenticação cair, as notificações continuam operando 100%.
* **Garantia de Auditoria em Rollbacks:** Utilização profunda do Spring AOP e propagação transacional. A auditoria de falhas de transferência (ex: saldo insuficiente) sobrevive ao `Rollback` do JPA por ser executada em um serviço segregado anotado com `@Transactional(propagation = Propagation.REQUIRES_NEW)`.

## ⚙️ Como Executar o Projeto

### Pré-requisitos
* Docker e Docker Compose instalados.
* Java JDK (17 ou 21) instalado.
* Maven instalado (ou utilizar o *wrapper* `./mvnw`).

### Passo a Passo

1. **Subir a Infraestrutura (Bancos e Kafka):**
   Na raiz do projeto (onde está o `docker-compose.yml`), execute o comando:
   ```bash
   docker-compose up -d

2. **Iniciar os Microsserviços:**
    Abra os três projetos na sua IDE de preferência e inicie-os na seguinte ordem (ou inicie todos simultaneamente):

    * ms-autenticacao (Ex: porta 8081)
    * ms-transacoes (Ex: porta 8082)
    * ms-notificacoes (Ex: porta 8083)

3. **Testando o Fluxo (Postman / Insomnia):**

    * Passo 1: Faça um POST no ms-autenticacao criando um usuário (O Kafka criará a conta e o contato automaticamente).
    * Passo 2: Faça o Login no ms-autenticacao e copie o Token JWT.
    * Passo 3: Faça um POST na rota de transferência do ms-transacoes passando o Token no header Authorization: Bearer <token>.
    * Passo 4: Verifique o log do ms-notificacoes simulando o envio perfeito do E-mail!