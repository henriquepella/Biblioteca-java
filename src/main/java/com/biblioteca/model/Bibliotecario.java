package com.biblioteca.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Perfil <b>Bibliotecário</b>: opera o dia a dia do acervo — livros, membros,
 * empréstimos e devoluções.
 */
public class Bibliotecario extends Funcionario {

    public Bibliotecario() {
        super();
    }

    public Bibliotecario(int id, String nome, String endereco, String telefone,
                         BigDecimal salario, String login, String senha) {
        super(id, nome, endereco, telefone, salario, login, senha);
    }

    @Override
    public TipoFuncionario getTipo() {
        return TipoFuncionario.BIBLIOTECARIO;
    }

    @Override
    public String getPerfil() {
        return "Bibliotecário";
    }

    @Override
    public List<String> getPermissoes() {
        return List.of(
                "Adicionar, editar e remover livros",
                "Cadastrar novos membros",
                "Registrar empréstimos",
                "Registrar devoluções (com cálculo de multa)",
                "Consultar livros por título, autor ou categoria",
                "Emitir relatórios"
        );
    }
}
