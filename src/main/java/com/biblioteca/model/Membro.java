package com.biblioteca.model;

import java.time.LocalDate;

/**
 * Membro da biblioteca — usuário que pega livros emprestados.
 * Herda os dados pessoais de {@link Pessoa} e acrescenta a data de cadastro.
 */
public class Membro extends Pessoa {

    private LocalDate dataCadastro;

    public Membro() {
        super();
        this.dataCadastro = LocalDate.now();
    }

    public Membro(int id, String nome, String endereco, String telefone, LocalDate dataCadastro) {
        super(id, nome, endereco, telefone);
        setDataCadastro(dataCadastro);
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        // se não informada, assume a data atual
        this.dataCadastro = (dataCadastro == null) ? LocalDate.now() : dataCadastro;
    }

    @Override
    public String getDescricaoResumida() {
        return String.format("#%d %s", getId(), getNome());
    }
}
