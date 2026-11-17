# 📚 Sistema de Gerenciamento de Biblioteca (Java, em construção)

Sistema de console (linha de comando) para gerenciar **livros**, **usuários**
(funcionários e membros) e **empréstimos**, com persistência em banco de dados
relacional via **JDBC puro** (sem ORM) e arquitetura em camadas **MVC + DAO**.

> 🚧 **Projeto em construção — entregue camada por camada.**
> Já concluído: estrutura Maven, entidades com POO (`model/`), classe de conexão (`util/`),
> scripts de banco (`schema.sql` + `seed.sql`), `.gitignore` e configuração externa.
> Próximas etapas: DAOs com JDBC e MySQL, serviços, menus e `Main`.

---

## 🎯 Conceitos de POO aplicados

| Pilar | Onde aparece |
|-------|--------------|
| **Encapsulamento** | Todos os atributos das entidades são `private`, acessados por getters/setters com validação. |
| **Herança** | `Pessoa` (abstrata) → `Funcionario` (abstrata) → `Administrador`, `Bibliotecario`, `Atendente`; e `Pessoa` → `Membro`. |
| **Polimorfismo** | Método `getPerfil()` / `exibirPermissoes()` sobrescrito em cada subclasse de funcionário — o menu exibido no login depende do tipo. |
| **Abstração** | Classes/métodos abstratos (`Pessoa`, `Funcionario`) e **interfaces** para os DAOs (`LivroDAO`, `MembroDAO`, `EmprestimoDAO`, `FuncionarioDAO`). |

---

## 🧰 Stack técnica (planejada)

- **Java 17+** (LTS)
- **MySQL 8+**
- **JDBC puro** com `PreparedStatement` (proteção contra SQL Injection)
- **Maven** (gerenciamento de dependências e build)
- **BCrypt** (`org.mindrot:jbcrypt`) para hash de senha
- **JUnit 5** para testes da camada de serviço

### Por que MySQL?
Escolhi **MySQL 8** por ser gratuito, extremamente difundido no meio acadêmico e
de instalação simples (MySQL Installer / XAMPP / Docker), com excelente suporte a
JDBC. Recursos como `ENUM` nativo (usado nas colunas `tipo` e `status`) e
`CHECK constraints` (MySQL 8.0.16+) deixam o schema mais expressivo. O código de
acesso usa apenas SQL padrão + `PreparedStatement`, então a migração para
PostgreSQL exigiria poucos ajustes.

---

## 🗂️ Estrutura de pastas

```
Java/
├── pom.xml
├── .gitignore
├── README.md
└── src/
    └── main/
        ├── java/com/biblioteca/
        │   ├── model/      # Entidades e hierarquia de herança
        │   ├── dao/        # Interfaces + implementações JDBC
        │   ├── service/    # Regras de negócio
        │   ├── view/       # Menus (console)
        │   ├── util/       # Conexão, validações, formatadores
        │   └── Main.java   # Ponto de entrada
        └── resources/
            ├── config.properties.example   # modelo de configuração (versionado)
            └── db/
                ├── schema.sql               # criação de tabelas/índices/FKs
                └── seed.sql                 # dados de exemplo
```

---

## ⚙️ Pré-requisitos

- **JDK 17+** — verifique com `java -version`
- **Maven 3.8+** — verifique com `mvn -version`
- **MySQL 8+** rodando localmente (porta padrão `3306`)

---

## 🛠️ Configuração do banco de dados

1. **Criar o schema e as tabelas:**
   ```bash
   mysql -u root -p < src/main/resources/db/schema.sql
   ```

2. **Popular com dados de exemplo:**
   ```bash
   mysql -u root -p < src/main/resources/db/seed.sql
   ```

3. **Configurar as credenciais da aplicação** (arquivo fora do versionamento):
   ```powershell
   Copy-Item src/main/resources/config.properties.example src/main/resources/config.properties
   ```
   Depois edite `config.properties` e ajuste `db.user` e `db.password`.

### 🔑 Credenciais de teste (do `seed.sql`)

| Perfil | Login | Senha |
|--------|-------|-------|
| Administrador | `admin` | `admin123` |
| Bibliotecário | `bibliotecario` | `biblio123` |
| Atendente | `atendente` | `atend123` |

---

## ▶️ Como executar

> _A camada de aplicação (`Main`, menus e serviços) está sendo adicionada nas
> próximas etapas. As instruções completas de execução entram na versão final
> deste README._

```bash
# (em breve) build + execução
mvn clean package
java -jar target/sistema-biblioteca-jar-with-dependencies.jar
```

---

## 📌 Regras de negócio (resumo)

- Um livro só é emprestado se `quantidade_disponivel > 0`.
- Ao emprestar, a disponibilidade **decrementa**; ao devolver, **incrementa**.
- Prazo padrão de devolução: **14 dias** (parametrizável em `config.properties`).
- Multa por atraso: **R$ 1,00/dia** (parametrizável).
- `ISBN` e `login` são **únicos**.
- Limite de **3 empréstimos ativos** por membro (parametrizável).
