package it.revarmygaming.commonapi.db;

import it.revarmygaming.commonapi.db.connectors.SQLiteConnector;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class SQLite extends SQLImplementation {

    private File database;

    public SQLite(@NotNull File database, @NotNull String table) {
        super(new SQLiteConnector(database), table);
        this.database = database;
    }

    public File getDatabase() {
        return database;
    }
}
