package it.revarmygaming.commonapi.db.connectors;

import java.sql.Connection;
import java.sql.SQLException;

public interface Connector {

    Connection connect() throws SQLException;
    void shutdown();
}
