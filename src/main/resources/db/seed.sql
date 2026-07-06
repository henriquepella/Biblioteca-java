-- =========================================================================
--  seed.sql  —  Dados de exemplo para o Sistema de Biblioteca
--  Banco: MySQL 8+
--
--  Execute APÓS o schema.sql:
--     mysql -u root -p < src/main/resources/db/seed.sql
--
--  As senhas abaixo estão gravadas como HASH BCrypt. As senhas em texto
--  puro (apenas para você testar o login) são:
--     login: admin          senha: admin123
--     login: bibliotecario  senha: biblio123
--     login: atendente      senha: atend123
-- =========================================================================

USE biblioteca;

-- Limpa dados anteriores mantendo a estrutura (ordem respeita as FKs)
DELETE FROM emprestimos;
DELETE FROM livros;
DELETE FROM membros;
DELETE FROM funcionarios;
ALTER TABLE emprestimos  AUTO_INCREMENT = 1;
ALTER TABLE livros       AUTO_INCREMENT = 1;
ALTER TABLE membros      AUTO_INCREMENT = 1;
ALTER TABLE funcionarios AUTO_INCREMENT = 1;

-- -------------------------------------------------------------------------
-- Funcionários (um de cada tipo). Senha = hash BCrypt (custo 10).
-- -------------------------------------------------------------------------
INSERT INTO funcionarios (nome, endereco, telefone, salario, tipo, login, senha) VALUES
('Ricardo Admin',      'Rua Central, 100',   '(51) 99999-0001', 8500.00, 'ADMIN',
    'admin',          '$2a$10$0dA8SwUoA9QuPlpuufCS/uTJDARfu6zPeQtox7JdzAzSRFELg3t4m'),
('Beatriz Bibliotecária','Av. das Flores, 250','(51) 99999-0002', 4200.00, 'BIBLIOTECARIO',
    'bibliotecario',  '$2a$10$VSI9wAt5.CmRCqu/gBnnuOZv//QcDqwJUFPsze1yaPg0WMvWRKZN.'),
('André Atendente',    'Rua do Sol, 42',     '(51) 99999-0003', 2600.00, 'ATENDENTE',
    'atendente',      '$2a$10$X/mRwxMkxucGSNpizlymdezr2p7PJIXkZT3Y0w2Ar9ks7IvHULwFq');

-- -------------------------------------------------------------------------
-- Membros
-- -------------------------------------------------------------------------
INSERT INTO membros (nome, endereco, telefone, data_cadastro) VALUES
('João Silva',     'Rua A, 10',   '(51) 98888-1111', DATE_SUB(CURDATE(), INTERVAL 200 DAY)),
('Maria Oliveira', 'Rua B, 20',   '(51) 98888-2222', DATE_SUB(CURDATE(), INTERVAL 150 DAY)),
('Carlos Souza',   'Rua C, 30',   '(51) 98888-3333', DATE_SUB(CURDATE(), INTERVAL 100 DAY)),
('Ana Santos',     'Rua D, 40',   '(51) 98888-4444', DATE_SUB(CURDATE(), INTERVAL  30 DAY));

-- -------------------------------------------------------------------------
-- Livros
-- quantidade_disponivel já considera os empréstimos ATIVOS abaixo.
-- -------------------------------------------------------------------------
INSERT INTO livros (titulo, autor, isbn, categoria, quantidade_total, quantidade_disponivel) VALUES
('Dom Casmurro',            'Machado de Assis',   '978-8535910663', 'Romance',    3, 2),
('O Cortiço',               'Aluísio Azevedo',    '978-8508133392', 'Romance',    2, 2),
('Clean Code',              'Robert C. Martin',   '978-0132350884', 'Tecnologia', 4, 3),
('O Senhor dos Anéis',      'J.R.R. Tolkien',     '978-8533613379', 'Fantasia',   2, 2),
('Sapiens',                 'Yuval Noah Harari',  '978-8525432186', 'História',   5, 4),
('A Revolução dos Bichos',  'George Orwell',      '978-8535909555', 'Ficção',     1, 1);

-- -------------------------------------------------------------------------
-- Empréstimos (datas relativas a hoje para os relatórios ficarem realistas)
--
--  Livro 3 (Clean Code) aparece 3x  -> mais popular
--  Livro 5 (Sapiens)     aparece 2x
--  Empréstimo do livro 1 está VENCIDO (prevista no passado) -> relatório de atraso
-- -------------------------------------------------------------------------
INSERT INTO emprestimos
    (livro_id, membro_id, data_emprestimo, data_devolucao_prevista, data_devolucao_real, status) VALUES
-- ATIVO, dentro do prazo (Clean Code com João)
(3, 1, DATE_SUB(CURDATE(), INTERVAL  2 DAY), DATE_ADD(CURDATE(), INTERVAL 12 DAY), NULL, 'ATIVO'),
-- ATIVO, mas VENCIDO há 6 dias (Dom Casmurro com João) -> aparece no relatório de atrasos
(1, 1, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(CURDATE(), INTERVAL  6 DAY), NULL, 'ATIVO'),
-- ATIVO, dentro do prazo (Sapiens com Maria)
(5, 2, DATE_SUB(CURDATE(), INTERVAL  5 DAY), DATE_ADD(CURDATE(), INTERVAL  9 DAY), NULL, 'ATIVO'),
-- DEVOLVIDO no prazo (Clean Code com Maria)
(3, 2, DATE_SUB(CURDATE(), INTERVAL 40 DAY), DATE_SUB(CURDATE(), INTERVAL 26 DAY), DATE_SUB(CURDATE(), INTERVAL 28 DAY), 'DEVOLVIDO'),
-- DEVOLVIDO no prazo (Sapiens com Carlos)
(5, 3, DATE_SUB(CURDATE(), INTERVAL 60 DAY), DATE_SUB(CURDATE(), INTERVAL 46 DAY), DATE_SUB(CURDATE(), INTERVAL 44 DAY), 'DEVOLVIDO'),
-- DEVOLVIDO com atraso histórico (Clean Code com Carlos)
(3, 3, DATE_SUB(CURDATE(), INTERVAL 90 DAY), DATE_SUB(CURDATE(), INTERVAL 76 DAY), DATE_SUB(CURDATE(), INTERVAL 70 DAY), 'DEVOLVIDO');
