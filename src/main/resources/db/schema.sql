-- =========================================================================
--  schema.sql  —  Sistema de Gerenciamento de Biblioteca
--  Banco: MySQL 8+
--
--  Cria o banco, as tabelas, chaves estrangeiras e índices.
--  Execute uma vez antes de rodar a aplicação:
--     mysql -u root -p < src/main/resources/db/schema.sql
-- =========================================================================

CREATE DATABASE IF NOT EXISTS biblioteca
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE biblioteca;

-- Remove na ordem inversa das dependências (FK) para permitir recriação limpa
DROP TABLE IF EXISTS emprestimos;
DROP TABLE IF EXISTS livros;
DROP TABLE IF EXISTS membros;
DROP TABLE IF EXISTS funcionarios;

-- -------------------------------------------------------------------------
-- funcionarios  (Administrador / Bibliotecario / Atendente)
-- A coluna "tipo" define a subclasse concreta na aplicação (polimorfismo).
-- A coluna "senha" guarda o HASH BCrypt (nunca a senha em texto puro).
-- -------------------------------------------------------------------------
CREATE TABLE funcionarios (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(120)  NOT NULL,
    endereco  VARCHAR(200),
    telefone  VARCHAR(20),
    salario   DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    tipo      ENUM('ADMIN', 'BIBLIOTECARIO', 'ATENDENTE') NOT NULL,
    login     VARCHAR(50)   NOT NULL,
    senha     VARCHAR(255)  NOT NULL,
    CONSTRAINT uq_funcionarios_login UNIQUE (login)
) ENGINE = InnoDB;

-- -------------------------------------------------------------------------
-- membros  (usuários que pegam livros emprestados)
-- -------------------------------------------------------------------------
CREATE TABLE membros (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nome          VARCHAR(120) NOT NULL,
    endereco      VARCHAR(200),
    telefone      VARCHAR(20),
    data_cadastro DATE NOT NULL DEFAULT (CURRENT_DATE)
) ENGINE = InnoDB;

-- -------------------------------------------------------------------------
-- livros  (acervo)
-- O "status" (DISPONIVEL/INDISPONIVEL) é derivado na aplicação a partir de
-- quantidade_disponivel, portanto não é armazenado aqui (evita redundância).
-- -------------------------------------------------------------------------
CREATE TABLE livros (
    id                    INT AUTO_INCREMENT PRIMARY KEY,
    titulo                VARCHAR(200) NOT NULL,
    autor                 VARCHAR(120) NOT NULL,
    isbn                  VARCHAR(20)  NOT NULL,
    categoria             VARCHAR(80),
    quantidade_total      INT NOT NULL DEFAULT 0,
    quantidade_disponivel INT NOT NULL DEFAULT 0,
    CONSTRAINT uq_livros_isbn UNIQUE (isbn),
    CONSTRAINT chk_livros_qtd CHECK (
        quantidade_total >= 0
        AND quantidade_disponivel >= 0
        AND quantidade_disponivel <= quantidade_total
    )
) ENGINE = InnoDB;

-- -------------------------------------------------------------------------
-- emprestimos
-- status: ATIVO | DEVOLVIDO | ATRASADO
-- data_devolucao_real é NULL enquanto o livro não é devolvido.
-- -------------------------------------------------------------------------
CREATE TABLE emprestimos (
    id                      INT AUTO_INCREMENT PRIMARY KEY,
    livro_id                INT  NOT NULL,
    membro_id               INT  NOT NULL,
    data_emprestimo         DATE NOT NULL,
    data_devolucao_prevista DATE NOT NULL,
    data_devolucao_real     DATE NULL,
    status                  ENUM('ATIVO', 'DEVOLVIDO', 'ATRASADO') NOT NULL DEFAULT 'ATIVO',
    CONSTRAINT fk_emp_livro  FOREIGN KEY (livro_id)  REFERENCES livros(id),
    CONSTRAINT fk_emp_membro FOREIGN KEY (membro_id) REFERENCES membros(id),
    CONSTRAINT chk_emp_datas CHECK (data_devolucao_prevista >= data_emprestimo)
) ENGINE = InnoDB;

-- -------------------------------------------------------------------------
-- Índices para acelerar buscas e relatórios
-- -------------------------------------------------------------------------
CREATE INDEX idx_livros_titulo     ON livros (titulo);
CREATE INDEX idx_livros_autor      ON livros (autor);
CREATE INDEX idx_livros_categoria  ON livros (categoria);

CREATE INDEX idx_emp_status        ON emprestimos (status);
CREATE INDEX idx_emp_livro         ON emprestimos (livro_id);
CREATE INDEX idx_emp_membro        ON emprestimos (membro_id);
CREATE INDEX idx_emp_prevista      ON emprestimos (data_devolucao_prevista);
