package it.revarmygaming.commonapi.db;

import it.revarmygaming.commonapi.db.connectors.HikariConnector;
import it.revarmygaming.commonapi.db.connectors.MySqlConnector;
import it.revarmygaming.commonapi.db.connectors.PoolSettings;
import org.jetbrains.annotations.NotNull;

public class MySQL extends SQLImplementation {

    private String host;
    private int port;
    private String database;
    private String user;

    public MySQL(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String password, @NotNull String table, boolean pool, @NotNull PoolSettings poolSettings) {
        super(pool ?
                        new HikariConnector(host, port, database, user, password, poolSettings) :
                        new MySqlConnector(host, port, database, user, password),
                table);

        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
    }

    public MySQL(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String password, @NotNull String table, boolean usePool) {
        this(host, port, database, user, password, table, usePool, new PoolSettings());
    }

    public MySQL(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String password, @NotNull String table) {
        this(host, port, database, user, password, table, true, new PoolSettings());
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }
}
