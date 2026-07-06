package com.biblioteca.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexões JDBC implementada como <b>Singleton</b>.
 *
 * <p>A instância única carrega o driver e lê a configuração apenas uma vez.
 * Cada chamada a {@link #getConnection()} devolve uma <b>nova</b> conexão, que
 * deve ser fechada pelo chamador — de preferência com <i>try-with-resources</i>:</p>
 *
 * <pre>{@code
 * try (Connection conn = ConnectionFactory.getInstance().getConnection();
 *      PreparedStatement ps = conn.prepareStatement(sql)) {
 *     ...
 * }
 * }</pre>
 */
public final class ConnectionFactory {

    private static ConnectionFactory instancia;

    private final String url;
    private final String usuario;
    private final String senha;

    private ConnectionFactory() {
        this.url = AppConfig.getDbUrl();
        this.usuario = AppConfig.getDbUser();
        this.senha = AppConfig.getDbPassword();
        try {
            // Registra o driver JDBC (opcional no JDBC 4+, mantido por clareza)
            Class.forName(AppConfig.getDbDriver());
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "Driver JDBC não encontrado: " + AppConfig.getDbDriver() +
                    ". Verifique a dependência do conector no pom.xml.");
        }
    }

    /** Retorna a instância única da fábrica (thread-safe). */
    public static synchronized ConnectionFactory getInstance() {
        if (instancia == null) {
            instancia = new ConnectionFactory();
        }
        return instancia;
    }

    /**
     * Abre uma nova conexão com o banco.
     *
     * @return conexão pronta para uso (o chamador é responsável por fechá-la)
     * @throws SQLException se não for possível conectar (banco fora do ar,
     *                      credenciais inválidas, etc.)
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, senha);
    }
}
