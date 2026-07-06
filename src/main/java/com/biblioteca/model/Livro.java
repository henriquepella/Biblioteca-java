package com.biblioteca.model;

/**
 * Livro do acervo.
 *
 * <p>O {@link StatusLivro} é <b>derivado</b> de {@code quantidadeDisponivel}
 * (não é armazenado no banco). Os métodos {@link #registrarSaida()} e
 * {@link #registrarEntrada()} encapsulam a variação de estoque mantendo o
 * objeto sempre em um estado válido.</p>
 */
public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private String categoria;
    private int quantidadeTotal;
    private int quantidadeDisponivel;

    public Livro() {
        // usado pelos DAOs
    }

    public Livro(int id, String titulo, String autor, String isbn, String categoria,
                 int quantidadeTotal, int quantidadeDisponivel) {
        setId(id);
        setTitulo(titulo);
        setAutor(autor);
        setIsbn(isbn);
        setCategoria(categoria);
        // total antes de disponível para a validação de faixa funcionar
        setQuantidadeTotal(quantidadeTotal);
        setQuantidadeDisponivel(quantidadeDisponivel);
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título é obrigatório.");
        }
        this.titulo = titulo.trim();
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("O autor é obrigatório.");
        }
        this.autor = autor.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("O ISBN é obrigatório.");
        }
        this.isbn = isbn.trim();
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = (categoria == null) ? null : categoria.trim();
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(int quantidadeTotal) {
        if (quantidadeTotal < 0) {
            throw new IllegalArgumentException("A quantidade total não pode ser negativa.");
        }
        this.quantidadeTotal = quantidadeTotal;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        if (quantidadeDisponivel < 0) {
            throw new IllegalArgumentException("A quantidade disponível não pode ser negativa.");
        }
        if (quantidadeDisponivel > quantidadeTotal) {
            throw new IllegalArgumentException(
                    "A quantidade disponível não pode ser maior que a total.");
        }
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    /** Status derivado do estoque. */
    public StatusLivro getStatus() {
        return quantidadeDisponivel > 0 ? StatusLivro.DISPONIVEL : StatusLivro.INDISPONIVEL;
    }

    public boolean isDisponivel() {
        return quantidadeDisponivel > 0;
    }

    /** Baixa um exemplar do estoque ao emprestar. */
    public void registrarSaida() {
        if (quantidadeDisponivel <= 0) {
            throw new IllegalStateException("Não há exemplares disponíveis de \"" + titulo + "\".");
        }
        quantidadeDisponivel--;
    }

    /** Devolve um exemplar ao estoque. */
    public void registrarEntrada() {
        if (quantidadeDisponivel >= quantidadeTotal) {
            throw new IllegalStateException("Todos os exemplares de \"" + titulo + "\" já estão no acervo.");
        }
        quantidadeDisponivel++;
    }

    @Override
    public String toString() {
        return String.format("#%d %s — %s [%s] (%d/%d disp.)",
                id, titulo, autor, categoria, quantidadeDisponivel, quantidadeTotal);
    }
}
