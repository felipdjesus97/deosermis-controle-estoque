![Sistema](docs/prints/Imagem2.png)

# Deosérmis – Sistema de Gestão de Estoque

Sistema completo de gestão de estoque com controle financeiro, autenticação de usuários, permissões por perfil e auditoria de operações, desenvolvido em Java com integração a banco de dados MySQL.

## Sobre o Projeto
O Deosérmis é uma aplicação desktop desenvolvida como Trabalho de Conclusão de Curso (TCC) da Fatec Zona Sul, voltada para microempreendedores que realizam vendas por redes sociais.

O sistema permite o controle completo de produtos, fornecedores, pedidos, reposições e relatórios financeiros, auxiliando na organização e tomada de decisões.

---

## Tecnologias Utilizadas

- Java (Swing)
- MySQL
- JDBC
- NetBeans
- UML
- Git e GitHub

---

## Arquitetura do Sistema

O sistema foi desenvolvido utilizando o padrão MVC (Model-View-Controller), com separação em camadas:

- View: Interface gráfica em Java Swing  
- Controller: Controle de fluxo e regras de negócio  
- Model: Representação das entidades do sistema  
- DAO: Acesso e manipulação de dados via JDBC  

---

## Segurança e Integridade

- Senhas armazenadas com hash e salt  
- Soft delete (exclusão lógica)  
- Auditoria de operações  
- Controle de acesso por perfil  

---

## Funcionalidades

- Cadastro de produtos e fornecedores  
- Controle de estoque (entrada e saída)  
- Registro de pedidos e reposições  
- Alertas de estoque mínimo  
- Relatórios financeiros  
- Controle de usuários e permissões  
- Backup de dados  

---

## Imagens do Sistema

### Tela de Login
![Login](docs/prints/Imagem1.png)

### Tela Principal
![Dashboard](docs/prints/Imagem2.png)

### Balanço Financeiro
![Relatórios](docs/prints/Imagem10.png)

### Cadastro de Produto
![Produtos](docs/prints/Imagem4.png)

### Cadastro de Fornecedor
![Fornecedores](docs/prints/Imagem3.png)

### Registro de Pedido
![Pedidos](docs/prints/Imagem6.png)

---

## Como Executar o Projeto

### 1. Clonar o repositório:

Abra o terminal e execute:

git clone https://github.com/felipdjesus97/deosermis-controle-estoque

---

### 2. Configurar o MySQL
- Executar scripts da pasta `/database`

---

### 3. Ajustar conexão com banco no projeto  

---

### 4. Executar o sistema

Escolha uma das opções abaixo:

#### Opção 1 - Executar diretamente (sem NetBeans)

Após clonar o projeto, acesse a pasta:

src/DEOSERMIS

Execute o arquivo:

Deosermis.exe

O sistema será iniciado normalmente.

#### Opção 2 - Executar pelo NetBeans

Abra o projeto no NetBeans e execute (F6).

---

Caso não exista nenhum usuário cadastrado, o sistema criará automaticamente um usuário administrador.

Credenciais padrão:
- Usuário: adm  
- Senha: 123  

Recomenda-se alterar a senha após o primeiro acesso.

---

## Estrutura do Projeto

- /src → Código Java  
- /database → Scripts SQL  
- /docs → Documentação, Diagramas e Imagens  

---

## Contexto Acadêmico

Projeto desenvolvido como TCC da Fatec Zona Sul.

---

## Autor

Felipe de Jesus dos Reis  
https://www.linkedin.com/in/felipedejesus97  
https://github.com/felipdjesus97
