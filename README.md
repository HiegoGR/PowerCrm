
# Teste PowerCrm

Este projeto foi desenvolvido com Java 17 e Spring Boot para gerenciar usuários, veículos e integração com a API da Tabela FIPE(https://deividfortuna.github.io/fipe/)
.
A aplicação realiza processamento assíncrono usando RabbitMQ e versionamento de banco com Liquibase

## Tecnologias Utilizadas

- Java 17 + Spring Boot
- PostgreSQL (banco de dados)
- RabbitMQ (mensageria)
- Liquibase (versionamento de banco)
- Caffeine Cache
- Docker

## Estrutura do Projeto

| Pacote           | Descrição                                                                 |
|------------------|---------------------------------------------------------------------------|
| controller       | Responsável por receber e responder requisições HTTP (camada de entrada) |
| entity           | Representa as tabelas do banco (modelo de dados da aplicação)            |
| repository       | Interface com o banco de dados, estendendo JpaRepository para operações CRUD automáticas |
| service          | Contém a lógica de negócio da aplicação. Inclui a classe `CrudPadraoService` para reaproveitar operações comuns |
| service/event    | Serviços responsáveis por enviar e consumir mensagens da fila (RabbitMQ)  |
| mapper           | Converte entre DTO e Entity usando MapStruct                              |
| config           | Arquivos de configuração |
| dto              | Objetos de transferência de dados, usados entre as camadas                |
| exception        | Classes para tratamento de erros personalizados                           |


## Instalação

- Instale JDK do java, versão 17

  Clique aqui para baixar [Java17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

- Instale o Docker desktop na maquina

  Clique aqui para baixar [Docker](https://www.docker.com)


## Clone o repositório

```git clone https://github.com/HiegoGR/PowerCrm.git```

- Depois que realizar o clone do repositório em sua maquina. Abra o projeto na sua IDE execute o comando

  ```mvn clean package```

### Observações Importantes
- Certifique-se de que seu ambiente está configurado corretamente:
    - Java 17 instalado ou selecionado via IDE (como IntelliJ).
    - Docker instalado e em execução na máquina.

- Para configurar os containers do PostgreSQL e RabbitMQ, localize o arquivo executarDocker.bat na raiz do projeto e dê dois cliques para executá-lo. Esse script iniciará automaticamente os serviços necessários via Docker.

    - Após isso, você poderá:
    - Executar o projeto normalmente pela sua IDE ou linha de comando.

    - Acessar o Swagger UI para testar a API: http://localhost:8080/swagger-ui.html

    - Acessar o painel do RabbitMQ: http://localhost:15672

        - Usuário: rabbitmq
        - Senha: rabbitmq


## Funcionalidades

#### Cadastro de Veículos

- Valida marca, modelo e ano do veículo com base na API FIPE.

- Relaciona veículo com usuário, marca e modelo.

- Impede cadastros com placa duplicada.

- Processa o preço FIPE de forma assíncrona.

- Integração com API FIPE

- Consulta marca, modelo e anos disponíveis via HTTP.

- Busca o preço atualizado do veículo (fipe_price).

- Utiliza cache para evitar chamadas repetidas à API.

#### Cadastro de Usuarios
- Permite criação e listagem de usuários.

- Cada usuário pode estar vinculado a um ou mais veículos.

- Valida existência do usuário ao cadastrar veículos.

  #### Validações aplicadas:

    - CPF: deve conter exatamente 11 dígitos numéricos (@Pattern). Não permite letras nem formatação (ex: pontos ou traços).

    - Telefone: aceita 10 ou 11 dígitos numéricos (com ou sem DDD). Não permite letras nem caracteres especiais.

    - E-mail: validado com base no formato padrão de e-mails (@Email).

    - Nome: obrigatório. Não pode estar em branco.


#### Mensageria com RabbitMQ

- Ao salvar um veículo, é enviado um evento com os dados via fila RabbitMQ.

- Um consumidor processa o evento e atualiza o preço FIPE do veículo.

#### Sincronização de Marcas e Modelos

- Ao iniciar a aplicação, sincroniza marcas e modelos se o banco estiver vazio(Banco vazio consome dados de marca e modelo da api é insere no banco)


### Documentação da API
- Para visualizar a documentação da API e testar os endpoints, acesse o Swagger:
    - Ao executar o projeto, vá ate o navegador é digite:
      ```http://localhost:8080/swagger-ui/index.html```  


