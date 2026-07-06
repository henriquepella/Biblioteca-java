package com.biblioteca.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Funcionário da biblioteca — classe <b>abstrata</b> intermediária da hierarquia
 * {@code Pessoa → Funcionario → (Administrador | Bibliotecario | Atendente)}.
 *
 * <p>Adiciona os atributos {@code salario}, {@code login} e {@code senha}
 * (armazenada como <b>hash BCrypt</b>). Define métodos abstratos que cada
 * perfil concreto sobrescreve, fornecendo suas próprias permissões — é isso que
 * permite exibir menus diferentes conforme o tipo de funcionário logado.</p>
 */
public abstract class Funcionario extends Pessoa {

    private BigDecimal salario;
    private String login;
    private String senha; // hash BCrypt, nunca texto puro

    protected Funcionario() {
        super();
    }

    protected Funcionario(int id, String nome, String endereco, String telefone,
                          BigDecimal salario, String login, String senha) {
        super(id, nome, endereco, telefone);
        setSalario(salario);
        setLogin(login);
        setSenha(senha);
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        if (salario == null || salario.signum() < 0) {
            throw new IllegalArgumentException("O salário não pode ser nulo ou negativo.");
        }
        this.salario = salario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("O login é obrigatório.");
        }
        this.login = login.trim();
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("A senha (hash) é obrigatória.");
        }
        this.senha = senha;
    }

    // ---------- Contrato polimórfico dos perfis ----------

    /** Tipo persistido na coluna {@code funcionarios.tipo}. */
    public abstract TipoFuncionario getTipo();

    /** Nome do perfil exibido ao usuário (ex.: "Administrador"). */
    public abstract String getPerfil();

    /** Lista de permissões/ações do perfil, usada para montar o menu. */
    public abstract List<String> getPermissoes();

    /** Impressão amigável das permissões do perfil (polimorfismo em ação). */
    public String exibirPermissoes() {
        StringBuilder sb = new StringBuilder("Permissões do perfil " + getPerfil() + ":\n");
        for (String permissao : getPermissoes()) {
            sb.append("  • ").append(permissao).append('\n');
        }
        return sb.toString();
    }

    @Override
    public String getDescricaoResumida() {
        return String.format("#%d %s (%s)", getId(), getNome(), getPerfil());
    }
}
