package com.biblioteca.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Empréstimo de um {@link Livro} para um {@link Membro}.
 *
 * <p>Encapsula o ciclo de vida (ATIVO → DEVOLVIDO/ATRASADO), o cálculo de dias
 * de atraso e o cálculo da multa. As datas são {@link LocalDate}.</p>
 */
public class Emprestimo {

    private int id;
    private Livro livro;
    private Membro membro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal; // null enquanto não devolvido
    private StatusEmprestimo status;

    public Emprestimo() {
        // usado pelos DAOs
    }

    public Emprestimo(int id, Livro livro, Membro membro, LocalDate dataEmprestimo,
                      LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoReal,
                      StatusEmprestimo status) {
        setId(id);
        setLivro(livro);
        setMembro(membro);
        setDataEmprestimo(dataEmprestimo);
        setDataDevolucaoPrevista(dataDevolucaoPrevista);
        setDataDevolucaoReal(dataDevolucaoReal);
        setStatus(status);
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

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("O livro do empréstimo é obrigatório.");
        }
        this.livro = livro;
    }

    public Membro getMembro() {
        return membro;
    }

    public void setMembro(Membro membro) {
        if (membro == null) {
            throw new IllegalArgumentException("O membro do empréstimo é obrigatório.");
        }
        this.membro = membro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        if (dataEmprestimo == null) {
            throw new IllegalArgumentException("A data de empréstimo é obrigatória.");
        }
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        if (dataDevolucaoPrevista == null) {
            throw new IllegalArgumentException("A data prevista de devolução é obrigatória.");
        }
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal; // pode ser null
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setStatus(StatusEmprestimo status) {
        if (status == null) {
            throw new IllegalArgumentException("O status do empréstimo é obrigatório.");
        }
        this.status = status;
    }

    // ---------- Regras de atraso e multa ----------

    /**
     * Quantidade de dias de atraso, considerando a data de devolução real
     * (se já devolvido) ou a data de referência informada (normalmente hoje).
     *
     * @return dias de atraso, ou 0 se não houver atraso
     */
    public long getDiasAtraso(LocalDate referencia) {
        LocalDate fim = (dataDevolucaoReal != null) ? dataDevolucaoReal : referencia;
        if (fim.isAfter(dataDevolucaoPrevista)) {
            return ChronoUnit.DAYS.between(dataDevolucaoPrevista, fim);
        }
        return 0;
    }

    /**
     * Calcula a multa do empréstimo.
     *
     * @param valorPorDia valor cobrado por dia de atraso
     * @param referencia  data de referência para empréstimos ainda ativos
     * @return valor da multa (R$ 0,00 se não houver atraso)
     */
    public BigDecimal calcularMulta(BigDecimal valorPorDia, LocalDate referencia) {
        long dias = getDiasAtraso(referencia);
        return valorPorDia.multiply(BigDecimal.valueOf(dias));
    }

    /** Indica se um empréstimo ainda ativo já passou do prazo. */
    public boolean isAtrasado(LocalDate referencia) {
        return dataDevolucaoReal == null && referencia.isAfter(dataDevolucaoPrevista);
    }

    /**
     * Status "efetivo" para exibição: se ainda está ativo mas venceu, mostra
     * ATRASADO mesmo que a coluna no banco esteja como ATIVO.
     */
    public StatusEmprestimo getStatusEfetivo(LocalDate referencia) {
        if (status == StatusEmprestimo.ATIVO && isAtrasado(referencia)) {
            return StatusEmprestimo.ATRASADO;
        }
        return status;
    }

    @Override
    public String toString() {
        return String.format("#%d | %s → %s | emp: %s | prev: %s | %s",
                id,
                livro != null ? livro.getTitulo() : "?",
                membro != null ? membro.getNome() : "?",
                dataEmprestimo,
                dataDevolucaoPrevista,
                status);
    }
}
