package it.revarmygaming.commonapi.db;

import it.revarmygaming.commonapi.db.connectors.Connector;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class SQLImplementation implements SQL {
    private String table;
    private boolean printQuery = false;
    private Connector connector;

    SQLImplementation(Connector connector, String table) {
        this.connector = connector;
        this.table = table;
    }

    /**
     * Return the table name.
     *
     * @return The name of the table
     */
    public String getTable() {
        return table;
    }

    /**
     * Sets this to true to print queries.
     *
     * @param printQuery Set this to true to print queries
     */
    public void setPrintQuery(boolean printQuery) {
        this.printQuery = printQuery;
    }

    /**
     * Terminates the connection pool.
     */
    public void shutdown() {
        connector.shutdown();
    }

    /**
     * Return the ping with the database.
     *
     * @return The ping in milliseconds with the database
     */
    public long ping() {
        long start = System.currentTimeMillis();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connector.connect();
            statement = connection.prepareStatement("SELECT 1");
            statement.execute();
            return System.currentTimeMillis() - start;
        } catch (SQLException ignored) {
            return -1;
        } finally {
            DBUtils.closeQuietly(statement);
            DBUtils.closeQuietly(connection);
        }
    }

    /**
     * Executes a given MySQL query.
     *
     * @param query the query to be executed
     * @return the CompositeResult of the query
     * @throws SQLException SQLException
     */
    public CompositeResult executeQuery(@NotNull String query) throws SQLException {
        if (query.trim().isEmpty())
            throw new IllegalArgumentException("Query cannot be empty");

        Connection connection = connector.connect();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();

        if (printQuery) System.out.println(query);

        return new CompositeResult(connection, statement, result, query);
    }

    /**
     * Executes an update given a MySQL query.
     *
     * @param query the query to be executed
     * @throws SQLException SQLException
     */
    public void executeUpdate(@NotNull String query) throws SQLException {
        if (query.trim().isEmpty())
            throw new IllegalArgumentException("Query cannot be empty");

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connector.connect();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();

            if (printQuery) System.out.println(query);
        } finally {
            DBUtils.closeQuietly(statement);
            DBUtils.closeQuietly(connection);
        }
    }

    /**
     * Creates a new table if it is not present in the database.
     *
     * @param args    the list of columns with their type (ex. `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY)
     * @param charset the default character set
     * @throws SQLException SQLException
     */
    public void createTable(@NotNull String[] args, String charset) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS `")
                .append(this.table)
                .append("` (");
        for (int i = 0; i < args.length; i++) {
            query.append(args[i]);
            if (i != args.length - 1) query.append(", ");
        }
        if (charset == null || charset.isEmpty()) {
            query.append(");");
        } else {
            query.append(") DEFAULT CHARACTER SET ").append(charset).append(";");
        }

        executeUpdate(query.toString());
    }

    /**
     * Creates a new table if it is not present in the database.
     *
     * @param args the list of columns with their type (ex. `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY)
     * @throws SQLException SQLException
     */
    public void createTable(@NotNull String[] args) throws SQLException {
        createTable(args, null);
    }

    /**
     * Adds a new line to the table assigning the given values to the given columns.
     *
     * @param columns the list of columns to edit
     * @param values  the list of values to be added to the columns
     * @throws SQLException SQLException
     */
    public void addLine(@NotNull String[] columns, Object[] values) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO `")
                .append(this.table)
                .append("` (");
        for (int i = 0; i < columns.length; i++) {
            query.append("`")
                    .append(columns[i])
                    .append("`");
            if (i != columns.length - 1) query.append(", ");
        }
        query.append(") VALUES (");
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Boolean) {
                if ((boolean) values[i]) values[i] = 1;
                else values[i] = 0;
            }
            query.append("'")
                    .append(values[i])
                    .append("'");
            if (i != values.length - 1) query.append(", ");
        }
        query.append(");");

        executeUpdate(query.toString());
    }

    /**
     * Adds a new line to the table assigning the given value to the given column.
     *
     * @param column the column to edit
     * @param value  the value to be added to the column
     * @throws SQLException SQLException
     */
    public void addLine(@NotNull String column, Object value) throws SQLException {
        addLine(new String[]{column}, new Object[]{value});
    }

    /**
     * Removes a line from the table where the given columns have the given values.
     *
     * @param columns the list of columns for the research
     * @param values  the values to be searched in the columns
     * @throws SQLException SQLException
     */
    public void removeLine(@NotNull String[] columns, Object[] values) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");

        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`").append(columns[i]).append("`").append(" IS NULL");
            else query.append("`").append(columns[i]).append("`").append(" = '").append(values[i]).append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        executeUpdate(query.toString());
    }

    /**
     * Removes a line from the table where the given columns have the given values.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @throws SQLException SQLException
     */
    public void removeLine(@NotNull String column, Object value) throws SQLException {
        removeLine(new String[]{column}, new Object[]{value});
    }

    /**
     * Checks if a line exists with the given values in the given columns.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @throws SQLException SQLException
     */
    public boolean lineExists(@NotNull String[] columns, Object[] values) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(this.table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        boolean b = result.getResult().next();
        result.close();
        return b;
    }

    /**
     * Checks if a line exists with the given value in the given column.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @throws SQLException SQLException
     */
    public boolean lineExists(@NotNull String column, Object value) throws SQLException {
        return lineExists(new String[]{column}, new Object[]{value});
    }

    /**
     * Gets a Byte from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Byte getByte(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        Byte b = null;
        if (result.getResult().absolute(row)) {
            b = result.getResult().getByte(search);
        }
        result.close();
        return b;
    }

    /**
     * Gets a Byte from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Byte getByte(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getByte(columns, values, search, 1);
    }

    /**
     * Gets a Byte from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Byte getByte(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getByte(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a Byte from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Byte getByte(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getByte(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a Short from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Short getShort(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        Short s = null;
        if (result.getResult().absolute(row)) {
            s = result.getResult().getShort(search);
        }
        result.close();
        return s;
    }

    /**
     * Gets a Short from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Short getShort(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getShort(columns, values, search, 1);
    }

    /**
     * Gets a Short from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Short getShort(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getShort(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a Short from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Short getShort(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getShort(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a Integer from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Integer getInteger(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        Integer i = null;
        if (result.getResult().absolute(row)) {
            i = result.getResult().getInt(search);
        }
        result.close();
        return i;
    }

    /**
     * Gets a Integer from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Integer getInteger(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getInteger(columns, values, search, 1);
    }

    /**
     * Gets a Integer from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Integer getInteger(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getInteger(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a Integer from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Integer getInteger(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getInteger(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a Long from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Long getLong(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        Long l = null;
        if (result.getResult().absolute(row)) {
            l = result.getResult().getLong(search);
        }
        result.close();
        return l;
    }

    /**
     * Gets a Long from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Long getLong(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getLong(columns, values, search, 1);
    }

    /**
     * Gets a Long from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Long getLong(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getLong(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a Long from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Long getLong(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLong(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a Float from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Float getFloat(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        Float f = null;
        if (result.getResult().absolute(row)) {
            f = result.getResult().getFloat(search);
        }
        result.close();
        return f;
    }

    /**
     * Gets a Float from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Float getFloat(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getFloat(columns, values, search, 1);
    }

    /**
     * Gets a Float from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Float getFloat(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getFloat(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a Float from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Float getFloat(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getFloat(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a Double from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Double getDouble(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        Double d = null;
        if (result.getResult().absolute(row)) {
            d = result.getResult().getDouble(search);
        }
        result.close();
        return d;
    }

    /**
     * Gets a Double from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Double getDouble(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getDouble(columns, values, search, 1);
    }

    /**
     * Gets a Double from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Double getDouble(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getDouble(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a Double from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Double getDouble(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getDouble(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a String from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public String getString(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        String s = null;
        if (result.getResult().absolute(row)) {
            s = result.getResult().getString(search);
        }
        result.close();
        return s;
    }

    /**
     * Gets a String from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public String getString(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getString(columns, values, search, 1);
    }

    /**
     * Gets a String from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public String getString(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getString(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a String from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public String getString(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getString(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a Boolean from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Boolean getBoolean(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        Boolean b = null;
        if (result.getResult().absolute(row)) {
            b = result.getResult().getBoolean(search);
        }
        result.close();
        return b;
    }

    /**
     * Gets a Boolean from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Boolean getBoolean(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getBoolean(columns, values, search, 1);
    }

    /**
     * Gets a Boolean from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Boolean getBoolean(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getBoolean(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a Boolean from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Boolean getBoolean(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getBoolean(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a Timestamp from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Timestamp getTimestamp(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        Timestamp t = null;
        if (result.getResult().absolute(row)) {
            t = result.getResult().getTimestamp(search);
        }
        result.close();
        return t;
    }

    /**
     * Gets a Timestamp from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Timestamp getTimestamp(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getTimestamp(columns, values, search, 1);
    }

    /**
     * Gets a Timestamp from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Timestamp getTimestamp(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getTimestamp(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a Timestamp from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Timestamp getTimestamp(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getTimestamp(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a Date from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Date getDate(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        Date d = null;
        if (result.getResult().absolute(row)) {
            d = result.getResult().getDate(search);
        }
        result.close();
        return d;
    }

    /**
     * Gets a Date from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Date getDate(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getDate(columns, values, search, 1);
    }

    /**
     * Gets a Date from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Date getDate(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getDate(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a Date from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Date getDate(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getDate(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a Time from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Time getTime(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        Time t = null;
        if (result.getResult().absolute(row)) {
            t = result.getResult().getTime(search);
        }
        result.close();
        return t;
    }

    /**
     * Gets a Time from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Time getTime(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getTime(columns, values, search, 1);
    }

    /**
     * Gets a Time from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Time getTime(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getTime(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a Time from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Time getTime(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getTime(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a Object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Object getObject(@NotNull String[] columns, Object[] values, @NotNull String search, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        Object o = null;
        if (result.getResult().absolute(row)) {
            o = result.getResult().getObject(search);
        }
        result.close();
        return o;
    }

    /**
     * Gets a Object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Object getObject(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        return getObject(columns, values, search, 1);
    }

    /**
     * Gets a Object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public Object getObject(@NotNull String column, Object value, @NotNull String search, int row) throws SQLException {
        return getObject(new String[]{column}, new Object[]{value}, search, row);
    }

    /**
     * Gets a Object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Object getObject(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getObject(new String[]{column}, new Object[]{value}, search, 1);
    }

    /**
     * Gets a T object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param type    the type of the object you want to get
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public <T> T getObject(@NotNull String[] columns, Object[] values, @NotNull String search, @NotNull Class<T> type, int row) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        T t = null;
        if (result.getResult().absolute(row)) {
            try {
                t = type.cast(result.getResult().getObject(search));
            } catch (ClassCastException e) {
                throw new SQLException(e);
            }
        }
        result.close();
        return t;
    }

    /**
     * Gets a T object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param type    the type of the object you want to get
     * @throws SQLException SQLException
     */
    public <T> T getObject(@NotNull String[] columns, Object[] values, @NotNull String search, @NotNull Class<T> type) throws SQLException {
        return getObject(columns, values, search, type, 1);
    }

    /**
     * Gets a T object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param type   the type of the object you want to get
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     */
    public <T> T getObject(@NotNull String column, Object value, @NotNull String search, @NotNull Class<T> type, int row) throws SQLException {
        return getObject(new String[]{column}, new Object[]{value}, search, type, row);
    }

    /**
     * Gets a T object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param type   the type of the object you want to get
     * @throws SQLException SQLException
     */
    public <T> T getObject(@NotNull String column, Object value, @NotNull String search, @NotNull Class<T> type) throws SQLException {
        return getObject(new String[]{column}, new Object[]{value}, search, type, 1);
    }

    /**
     * Gets a ArrayList of Object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param type    the type of the ArrayList
     * @throws SQLException SQLException | ClassCastException
     */
    public <T> List<T> getList(@NotNull String[] columns, Object[] values, @NotNull String search, @NotNull Class<T> type) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString());
        List<T> list = new ArrayList<>();
        while (result.getResult().next()) {
            list.add(type.cast(result.getResult().getObject(search)));
        }
        result.close();
        return list;
    }

    /**
     * Gets a ArrayList of Object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param type   the type of the ArrayList
     * @throws SQLException SQLException | ClassCastException
     */
    public <T> List<T> getList(@NotNull String column, Object value, @NotNull String search, @NotNull Class<T> type) throws SQLException {
        return getList(new String[]{column}, new Object[]{value}, search, type);
    }

    /**
     * Gets the last Byte from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException | ClassCastException
     */
    public Byte getLastByte(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<Byte> list = getList(columns, values, search, Byte.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Byte from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Byte getLastByte(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastByte(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last Short from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException | ClassCastException
     */
    public Short getLastShort(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<Short> list = getList(columns, values, search, Short.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Short from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Short getLastShort(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastShort(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last Integer from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Integer getLastInteger(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<Integer> list = getList(columns, values, search, Integer.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Integer from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Integer getLastInteger(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastInteger(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last Long from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Long getLastLong(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<Long> list = getList(columns, values, search, Long.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Long from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Long getLastLong(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastLong(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last Float from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Float getLastFloat(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<Float> list = getList(columns, values, search, Float.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Float from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Float getLastFloat(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastFloat(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last Double from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Double getLastDouble(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<Double> list = getList(columns, values, search, Double.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Double from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Double getLastDouble(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastDouble(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last String from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public String getLastString(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<String> list = getList(columns, values, search, String.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last String from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public String getLastString(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastString(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last Boolean from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Boolean getLastBoolean(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<Boolean> list = getList(columns, values, search, Boolean.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Boolean from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Boolean getLastBoolean(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastBoolean(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last Timestamp from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Timestamp getLastTimestamp(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<Timestamp> list = getList(columns, values, search, Timestamp.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Timestamp from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Timestamp getLastTimestamp(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastTimestamp(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last Date from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException | ClassCastException
     */
    public Date getLastDate(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<Date> list = getList(columns, values, search, Date.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Date from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Date getLastDate(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastDate(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last Time from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException | ClassCastException
     */
    public Time getLastTime(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<Time> list = getList(columns, values, search, Time.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Time from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Time getLastTime(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastTime(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last Object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Object getLastObject(@NotNull String[] columns, Object[] values, @NotNull String search) throws SQLException {
        List<Object> list = getList(columns, values, search, Object.class);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     */
    public Object getLastObject(@NotNull String column, Object value, @NotNull String search) throws SQLException {
        return getLastObject(new String[]{column}, new Object[]{value}, search);
    }

    /**
     * Gets the last T object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param type    the type of the object you want to get
     * @throws SQLException SQLException
     */
    public <T> T getLastObject(@NotNull String[] columns, Object[] values, @NotNull String search, @NotNull Class<T> type) throws SQLException {
        List<Object> list = getList(columns, values, search, Object.class);
        return type.cast(list.get(list.size() - 1));
    }

    /**
     * Gets the last T object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param type   the type of the object you want to get
     * @throws SQLException SQLException
     */
    public <T> T getLastObject(@NotNull String column, Object value, @NotNull String search, @NotNull Class<T> type) throws SQLException {
        return getLastObject(new String[]{column}, new Object[]{value}, search, type);
    }

    /**
     * Update a list of columns with new values.
     *
     * @param columnsToEdit the list of columns to edit
     * @param newValues     the list of new values
     * @param columns       the list of columns for the research
     * @param values        the list of values to be searched in the columns
     * @throws SQLException SQLException
     */
    public void set(@NotNull String[] columnsToEdit, Object[] newValues, @NotNull String[] columns, Object[] values) throws SQLException {
        if ((columns.length != values.length) || (columnsToEdit.length != newValues.length))
            throw new IllegalArgumentException("Columns and values length must have the same value");

        StringBuilder query = new StringBuilder();
        query.append("UPDATE `")
                .append(this.table)
                .append("` SET ");
        for (int i = 0; i < columnsToEdit.length; i++) {
            query.append("`")
                    .append(columnsToEdit[i])
                    .append("`")
                    .append(" ='");
            Object object = newValues[i];
            if (object instanceof Boolean) {
                if ((Boolean) object) query.append(1)
                        .append("'");
                else query.append(0)
                        .append("'");
            } else {
                query.append(object)
                        .append("'");
            }
            if (i != columnsToEdit.length - 1) query.append(", ");
        }
        query.append("WHERE (");
        for (int i = 0; i < columns.length; i++) {
            query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(values[i])
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        executeUpdate(query.toString());
    }

    /**
     * Update a list of columns with new values.
     *
     * @param columnToEdit the column to edit
     * @param newValue     the new value
     * @param columns      the list of columns for the research
     * @param values       the list of values to be searched in the columns
     * @throws SQLException SQLException
     */
    public void set(@NotNull String columnToEdit, Object newValue, @NotNull String[] columns, Object[] values) throws SQLException {
        set(new String[]{columnToEdit}, new Object[]{newValue}, columns, values);
    }

    /**
     * Update a list of columns with new values.
     *
     * @param columnsToEdit the list of columns to edit
     * @param newValues     the list of new values
     * @param column        the column for the research
     * @param value         the value to be searched in the column
     * @throws SQLException SQLException
     */
    public void set(@NotNull String[] columnsToEdit, Object[] newValues, @NotNull String column, Object value) throws SQLException {
        set(columnsToEdit, newValues, new String[]{column}, new Object[]{value});
    }

    /**
     * Update a list of columns with new values.
     *
     * @param columnToEdit the column to edit
     * @param newValues    the new value
     * @param column       the column for the research
     * @param value        the value to be searched in the column
     * @throws SQLException SQLException
     */
    public void set(@NotNull String columnToEdit, Object newValues, @NotNull String column, Object value) throws SQLException {
        set(new String[]{columnToEdit}, new Object[]{newValues}, new String[]{column}, new Object[]{value});
    }
}
