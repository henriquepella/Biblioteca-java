package com.biblioteca.model;

/**
 * Classe base <b>abstrata</b> da hierarquia de pessoas do sistema.
 *
 * <p>Concentra os atributos comuns a funcionários e membros. Não pode ser
 * instanciada diretamente (abstração) e todos os atributos são {@code private},
 * acessíveis apenas por getters/setters com validação (encapsulamento).</p>
 */
public abstract class Pessoa {

    private int id;
    private String nome;
    private String endereco;
    private String telefone;

    protected Pessoa() {
        // usado pelos DAOs para construir o objeto e preencher via setters
    }

    protected Pessoa(int id, String nome, String endereco, String telefone) {
        setId(id);
        setNome(nome);
        setEndereco(endereco);
        setTelefone(telefone);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("O id não pode ser negativo.");
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        this.nome = nome.trim();
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = (endereco == null) ? null : endereco.trim();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = (telefone == null) ? null : telefone.trim();
    }

    /**
     * Descrição curta usada em listagens. Cada subclasse concreta personaliza
     * o texto (polimorfismo).
     */
    public abstract String getDescricaoResumida();

    @Override
    public String toString() {
        return getDescricaoResumida();
    }
}
