package com.biblioteca.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Perfil <b>Administrador</b>: gerencia funcionários e tem visão global do
 * acervo e de todo o histórico de empréstimos.
 */
public class Administrador extends Funcionario {

    public Administrador() {
        super();
    }

    public Administrador(int id, String nome, String endereco, String telefone,
                         BigDecimal salario, String login, String senha) {
        super(id, nome, endereco, telefone, salario, login, senha);
    }

    @Override
    public TipoFuncionario getTipo() {
        return TipoFuncionario.ADMIN;
    }

    @Override
    public String getPerfil() {
        return "Administrador";
    }

    @Override
    public List<String> getPermissoes() {
        return List.of(
                "Cadastrar bibliotecários e atendentes",
                "Listar, editar e remover funcionários",
                "Visualizar todo o acervo de livros",
                "Visualizar todo o histórico de empréstimos",
                "Emitir relatórios"
        );
    }
}
