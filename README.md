# 📚 Sistema de Biblioteca

Sistema de gerenciamento de biblioteca desenvolvido em Java, utilizando JDBC para conexão com banco de dados MySQL. O sistema permite o cadastro e gerenciamento de livros, usuários, categorias e empréstimos, com tratamento de erros e validações de entrada.

---

## 🔧 Tecnologias Utilizadas

- **Java 21**
- **JDBC**
- **MySQL**
- **IntelliJ IDEA**
- **MySQL Workbench**
- **Paradigma de Programação Orientada a Objetos (POO)**

---

## ⚙️ Funcionalidades

- 📁 **Gerenciar Categorias**  
  - Cadastrar, listar, buscar por ID, atualizar e deletar.

- 📚 **Gerenciar Livros**  
  - Cadastrar livros com categorias, listar, buscar, atualizar e deletar.

- 👤 **Gerenciar Usuários**  
  - Cadastrar, listar, buscar por ID, atualizar e deletar.

- 🔄 **Empréstimos**  
  - Registrar empréstimos e devoluções.
  - Listar e buscar empréstimos.
  - Excluir empréstimos (se já devolvidos).

---

## 💾 Como Executar o Projeto

1. Clone o repositório:
   ```bash
   https://github.com/leoferreira9/sistema-biblioteca
   ```
2. **Importe o projeto em uma IDE.**

3. Configure seu banco MySQL:
- Execute o script **db.sql** que está na pasta **/database** para criar o banco e as tabelas.
- Altere o arquivo **db.properties** com suas credenciais:
  ```bash
    db.url=jdbc:mysql://localhost:3306/biblioteca
    db.user=SEU_USUARIO
    db.password=SUA_SENHA
  ```
4. Execute a classe **Main** (pacote app).

