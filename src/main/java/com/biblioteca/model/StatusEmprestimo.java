package com.biblioteca.model;

/**
 * Situação de um empréstimo, persistida na coluna {@code emprestimos.status}.
 */
public enum StatusEmprestimo {
    ATIVO,
    DEVOLVIDO,
    ATRASADO
}
