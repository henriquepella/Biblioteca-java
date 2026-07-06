package com.biblioteca.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Perfil <b>Atendente</b>: atendimento ao público — empréstimos, devoluções,
 * cadastro de membros e consulta ao acervo.
 */
public class Atendente extends Funcionario {

    public Atendente() {
        super();
    }

    public Atendente(int id, String nome, String endereco, String telefone,
                     BigDecimal salario, String login, String senha) {
        super(id, nome, endereco, telefone, salario, login, senha);
    }

    @Override
    public TipoFuncionario getTipo() {
        return TipoFuncionario.ATENDENTE;
    }

    @Override
    public String getPerfil() {
        return "Atendente";
    }

    @Override
    public List<String> getPermissoes() {
        return List.of(
                "Adicionar e editar livros",
                "Cadastrar novos membros",
                "Registrar empréstimos",
                "Registrar devoluções (com cálculo de multa)",
                "Consultar livros por título, autor ou categoria",
                "Emitir relatórios"
        );
    }
}
