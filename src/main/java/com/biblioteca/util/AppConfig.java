package com.biblioteca.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

/**
 * Carrega as configurações externas do arquivo {@code config.properties}
 * (credenciais do banco + parâmetros de negócio).
 *
 * <p>O arquivo fica em {@code src/main/resources/config.properties} e NÃO é
 * versionado. Copie o {@code config.properties.example} e preencha os dados.</p>
 *
 * <p>Todos os acessos são estáticos e o arquivo é lido uma única vez.</p>
 */
public final class AppConfig {

    private static final String ARQUIVO = "/config.properties";
    private static final Properties PROPS = carregar();

    private AppConfig() {
        // classe utilitária: não deve ser instanciada
    }

    private static Properties carregar() {
        Properties p = new Properties();
        try (InputStream in = AppConfig.class.getResourceAsStream(ARQUIVO)) {
            if (in == null) {
                throw new IllegalStateException(
                        "Arquivo 'config.properties' não encontrado no classpath.\n" +
                        "Copie 'config.properties.example' para 'config.properties' " +
                        "em src/main/resources e preencha as credenciais do banco.");
            }
            p.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao ler config.properties: " + e.getMessage(), e);
        }
        return p;
    }

    private static String obrigatorio(String chave) {
        String valor = PROPS.getProperty(chave);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Propriedade obrigatória ausente em config.properties: " + chave);
        }
        return valor.trim();
    }

    // ---------- Conexão com o banco ----------

    public static String getDbUrl() {
        return obrigatorio("db.url");
    }

    public static String getDbUser() {
        return obrigatorio("db.user");
    }

    public static String getDbPassword() {
        // senha pode ser vazia em alguns ambientes de desenvolvimento
        return PROPS.getProperty("db.password", "");
    }

    public static String getDbDriver() {
        return PROPS.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
    }

    // ---------- Parâmetros de negócio (com valores padrão) ----------

    /** Prazo padrão de devolução, em dias. */
    public static int getPrazoDias() {
        return Integer.parseInt(PROPS.getProperty("emprestimo.prazoDias", "14"));
    }

    /** Valor da multa por dia de atraso (R$). */
    public static BigDecimal getMultaPorDia() {
        return new BigDecimal(PROPS.getProperty("emprestimo.multaPorDia", "1.00"));
    }

    /** Máximo de empréstimos ativos permitidos por membro. */
    public static int getLimiteEmprestimosAtivos() {
        return Integer.parseInt(PROPS.getProperty("emprestimo.limiteAtivosPorMembro", "3"));
    }
}
