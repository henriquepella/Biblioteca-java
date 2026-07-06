package com.biblioteca.model;

/**
 * Situação de um livro no acervo. É <b>derivada</b> de
 * {@code quantidadeDisponivel} (não é armazenada no banco).
 */
public enum StatusLivro {
    DISPONIVEL,
    INDISPONIVEL
}
