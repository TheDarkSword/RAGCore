package it.revarmygaming.commonapi.db.connectors;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PoolSettings {

    private String poolName = "ragcore-hikari";
    private int maximumPoolSize = 10;
    private int minimumIdle = 5;
    private long initializationFailTimeout = 10000;
    private long connectionTimeout = 30000;
    private long idleTimeout = 100000;
    private long maxLifetime = 900000;
    private long leakDetectionThreshold = 60000;
    private HashMap<String, String> dataSourceProperties = new HashMap<>();
    private HashMap<String, String> healthCheckProperties = new HashMap<>();

    public PoolSettings() {
        dataSourceProperties.put("cachePrepStmts", "true");
        dataSourceProperties.put("alwaysSendSetIsolation", "false");
        dataSourceProperties.put("cacheServerConfiguration", "true");
        dataSourceProperties.put("elideSetAutoCommits", "true");
        dataSourceProperties.put("maintainTimeStats", "false");
        dataSourceProperties.put("useLocalSessionState", "true");
        dataSourceProperties.put("useServerPrepStmts", "true");
        dataSourceProperties.put("prepStmtCacheSize", "250");
        dataSourceProperties.put("rewriteBatchedStatements", "true");
        dataSourceProperties.put("prepStmtCacheSqlLimit", "2048");
        dataSourceProperties.put("cacheCallableStmts", "true");
        dataSourceProperties.put("cacheResultSetMetadata", "true");
        dataSourceProperties.put("characterEncoding", "utf8");
        dataSourceProperties.put("useUnicode", "true");
        dataSourceProperties.put("zeroDateTimeBehavior", "convertToNull");
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getMinimumIdle() {
        return minimumIdle;
    }

    public void setMinimumIdle(int minimumIdle) {
        this.minimumIdle = minimumIdle;
    }

    public long getInitializationFailTimeout() {
        return initializationFailTimeout;
    }

    public void setInitializationFailTimeout(long initializationFailTimeout) {
        this.initializationFailTimeout = initializationFailTimeout;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public long getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public long getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public long getLeakDetectionThreshold() {
        return leakDetectionThreshold;
    }

    public void setLeakDetectionThreshold(long leakDetectionThreshold) {
        this.leakDetectionThreshold = leakDetectionThreshold;
    }

    public HashMap<String, String> getDataSourceProperties() {
        return dataSourceProperties;
    }

    public void setDataSourceProperty(@NotNull String key, @NotNull String value) {
        dataSourceProperties.put(key, value);
    }

    public void removeDataSourceProperty(@NotNull String key) {
        dataSourceProperties.remove(key);
    }

    public HashMap<String, String> getHealthCheckProperties() {
        return healthCheckProperties;
    }

    public void setHealthCheckProperty(@NotNull String key, @NotNull String value) {
        healthCheckProperties.put(key, value);
    }

    public void removeHealthCheckProperty(@NotNull String key) {
        healthCheckProperties.remove(key);
    }
}
