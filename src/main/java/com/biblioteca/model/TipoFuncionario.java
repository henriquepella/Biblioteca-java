package com.biblioteca.model;

/**
 * Tipos de funcionário. O valor é persistido na coluna {@code funcionarios.tipo}
 * (ENUM no banco) e usado para instanciar a subclasse correta (polimorfismo).
 */
public enum TipoFuncionario {
    ADMIN,
    BIBLIOTECARIO,
    ATENDENTE
}
