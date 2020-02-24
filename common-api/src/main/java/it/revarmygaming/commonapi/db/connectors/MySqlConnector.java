package it.revarmygaming.commonapi.db.connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnector implements Connector {

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    public MySqlConnector(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://" +
                    host + ":" + port +
                    "/" + database +
                    "?zeroDateTimeBehavior=convertToNull&amp;characterEncoding=utf8mb4", user, password);

            if (connection == null)
                throw new SQLException("Unable to establish a connection with the database");

            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void shutdown() {
    }
}
