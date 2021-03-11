package it.revarmygaming.commonapi.db.connectors;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector implements Connector {

    private File database;

    public SQLiteConnector(File database) {
        this.database = database;
    }

    @Override
    public Connection connect() throws SQLException {
        if (database == null) throw new NullPointerException("Database file can't be null");
        if (!database.exists() || !database.isFile()) {
            try {
                if (!database.createNewFile()) throw new SQLException("Can't create database file");
            } catch (IOException e) {
                throw new SQLException(e);
            }
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }

        //return org.sqlite.JDBC.createConnection("jdbc:sqlite:" + database, new java.util.Properties());
        return DriverManager.getConnection("jdbc:sqlite:" + database);
    }

    @Override
    public void shutdown() {

    }
}
