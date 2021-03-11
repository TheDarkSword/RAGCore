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

    public static class Builder {
        private String host;
        private int port = -1;
        private String database;
        private String user;
        private String password;
        private String table;
        private boolean pool = true;
        private PoolSettings poolSettings;

        public String getHost() {
            return host;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public int getPort() {
            return port;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public String getDatabase() {
            return database;
        }

        public Builder setDatabase(String database) {
            this.database = database;
            return this;
        }

        public String getUser() {
            return user;
        }

        public Builder setUser(String user) {
            this.user = user;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getTable() {
            return table;
        }

        public Builder setTable(String table) {
            this.table = table;
            return this;
        }

        public boolean isPool() {
            return pool;
        }

        public Builder setPool(boolean pool) {
            this.pool = pool;
            return this;
        }

        public boolean usePool() {
            return pool;
        }

        public Builder usePool(boolean pool) {
            this.pool = pool;
            return this;
        }

        public PoolSettings getPoolSettings() {
            return poolSettings;
        }

        public Builder setPoolSettings(PoolSettings poolSettings) {
            this.poolSettings = poolSettings;
            return this;
        }

        public MySQL build() {
            if (host == null || host.trim().isEmpty() ||
                    port == -1 ||
                    database == null || database.trim().isEmpty() ||
                    user == null || user.trim().isEmpty() ||
                    password == null || password.trim().isEmpty() ||
                    table == null || table.isEmpty()) throw new IllegalArgumentException("Some required parameters are missing.");

            return new MySQL(host, port, database, user, password, table, pool, poolSettings);
        }
    }
}
