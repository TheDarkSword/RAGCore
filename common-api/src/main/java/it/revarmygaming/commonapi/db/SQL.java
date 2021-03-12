package it.revarmygaming.commonapi.db;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

interface SQL {

    /**
     * Return the table name.
     *
     * @return The name of the table
     */
    String getTable();

    /**
     * Sets this to true to print queries.
     *
     * @param printQuery Set this to true to print queries
     */
    void setPrintQuery(boolean printQuery);

    /**
     * Return the ping with the database.
     *
     * @return The ping in milliseconds with the database
     */
    long ping();

    /**
     * Executes a given MySQL query.
     *
     * @param query the query to be executed
     * @return the CompositeResult of the query
     * @throws SQLException SQLException
     */
    CompositeResult executeQuery(String query) throws SQLException;

    /**
     * Executes an update given a MySQL query.
     *
     * @param query the query to be executed
     * @throws SQLException SQLException
     */
    void executeUpdate(String query) throws SQLException;

    /**
     * Creates a new table if it is not present in the database.
     *
     * @param args    the list of columns with their type (ex. `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY)
     * @param charset the default character set
     * @throws SQLException SQLException
     */
    void createTable(String[] args, String charset) throws SQLException;

    /**
     * Creates a new table if it is not present in the database.
     *
     * @param args the list of columns with their type (ex. `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY)
     * @throws SQLException SQLException
     */
    void createTable(String[] args) throws SQLException;

    /**
     * Adds a new line to the table assigning the given values to the given columns.
     *
     * @param columns the list of columns to edit
     * @param values  the list of values to be added to the columns
     * @throws SQLException SQLException
     */
    void addLine(String[] columns, Object[] values) throws SQLException;

    /**
     * Adds a new line to the table assigning the given value to the given column.
     *
     * @param column the column to edit
     * @param value  the value to be added to the column
     * @throws SQLException SQLException
     */
    void addLine(String column, Object value) throws SQLException;

    /**
     * Removes a line from the table where the given columns have the given values.
     *
     * @param columns the list of columns for the research
     * @param values  the values to be searched in the columns
     * @throws SQLException SQLException
     */
    void removeLine(String[] columns, Object[] values) throws SQLException;

    /**
     * Removes a line from the table where the given columns have the given values.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @throws SQLException SQLException
     */
    void removeLine(String column, Object value) throws SQLException;

    /**
     * Checks if a line exists with the given values in the given columns.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @throws SQLException SQLException
     * @return boolean
     */
    boolean lineExists(String[] columns, Object[] values) throws SQLException;

    /**
     * Checks if a line exists with the given value in the given column.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @throws SQLException SQLException
     * @return boolean
     */
    boolean lineExists(String column, Object value) throws SQLException;

    /**
     * Gets a Byte from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Byte
     */
    Byte getByte(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets a Byte from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Byte
     */
    Byte getByte(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets a Byte from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Byte
     */
    Byte getByte(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets a Byte from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Byte
     */
    Byte getByte(String column, Object value, String search) throws SQLException;

    /**
     * Gets a Short from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Short
     */
    Short getShort(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets a Short from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Short
     */
    Short getShort(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets a Short from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Short
     */
    Short getShort(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets a Short from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Short
     */
    Short getShort(String column, Object value, String search) throws SQLException;

    /**
     * Gets a Integer from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Integer
     */
    Integer getInteger(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets a Integer from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Integer
     */
    Integer getInteger(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets a Integer from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Integer
     */
    Integer getInteger(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets a Integer from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Integer
     */
    Integer getInteger(String column, Object value, String search) throws SQLException;

    /**
     * Gets a Long from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Long
     */
    Long getLong(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets a Long from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Long
     */
    Long getLong(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets a Long from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Long
     */
    Long getLong(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets a Long from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Long
     */
    Long getLong(String column, Object value, String search) throws SQLException;

    /**
     * Gets a Float from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Float
     */
    Float getFloat(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets a Float from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Float
     */
    Float getFloat(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets a Float from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Float
     */
    Float getFloat(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets a Float from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Float
     */
    Float getFloat(String column, Object value, String search) throws SQLException;

    /**
     * Gets a Double from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Double
     */
    Double getDouble(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets a Double from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Double
     */
    Double getDouble(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets a Double from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Double
     */
    Double getDouble(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets a Double from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Double
     */
    Double getDouble(String column, Object value, String search) throws SQLException;

    /**
     * Gets a String from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return String
     */
    String getString(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets a String from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return String
     */
    String getString(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets a String from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return String
     */
    String getString(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets a String from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return String
     */
    String getString(String column, Object value, String search) throws SQLException;

    /**
     * Gets a Boolean from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Boolean
     */
    Boolean getBoolean(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets a Boolean from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Boolean
     */
    Boolean getBoolean(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets a Boolean from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Boolean
     */
    Boolean getBoolean(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets a Boolean from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Boolean
     */
    Boolean getBoolean(String column, Object value, String search) throws SQLException;

    /**
     * Gets a Timestamp from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Timestamp
     */
    Timestamp getTimestamp(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets a Timestamp from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Timestamp
     */
    Timestamp getTimestamp(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets a Timestamp from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Timestamp
     */
    Timestamp getTimestamp(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets a Timestamp from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Timestamp
     */
    Timestamp getTimestamp(String column, Object value, String search) throws SQLException;

    /**
     * Gets a Date from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Date
     */
    Date getDate(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets a Date from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Date
     */
    Date getDate(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets a Date from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Date
     */
    Date getDate(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets a Date from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Date
     */
    Date getDate(String column, Object value, String search) throws SQLException;

    /**
     * Gets a Time from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Time
     */
    Time getTime(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets a Time from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Time
     */
    Time getTime(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets a Time from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Time
     */
    Time getTime(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets a Time from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Time
     */
    Time getTime(String column, Object value, String search) throws SQLException;

    /**
     * Gets an object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Object
     */
    Object getObject(String[] columns, Object[] values, String search, int row) throws SQLException;

    /**
     * Gets an object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Object
     */
    Object getObject(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets an object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return Object
     */
    Object getObject(String column, Object value, String search, int row) throws SQLException;

    /**
     * Gets an object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Object
     */
    Object getObject(String column, Object value, String search) throws SQLException;

    /**
     * Gets a T object from the database.
     *
     * @param <T> custom type
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param type    the type of the object you want to get
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return T
     */
    <T> T getObject(String[] columns, Object[] values, String search, Class<T> type, int row) throws SQLException;

    /**
     * Gets a T object from the database.
     *
     * @param <T> custom type
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param type    the type of the object you want to get
     * @throws SQLException SQLException
     * @return T
     */
    <T> T getObject(String[] columns, Object[] values, String search, Class<T> type) throws SQLException;

    /**
     * Gets a T object from the database.
     *
     * @param <T> custom type
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param type   the type of the object you want to get
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @throws SQLException SQLException
     * @return T
     */
    <T> T getObject(String column, Object value, String search, Class<T> type, int row) throws SQLException;

    /**
     * Gets a T object from the database.
     *
     * @param <T> custom type
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param type   the type of the object you want to get
     * @throws SQLException SQLException
     * @return T
     */
    <T> T getObject(String column, Object value, String search, Class<T> type) throws SQLException;

    /**
     * Gets a ArrayList of Object from the database.
     *
     * @param <T> custom type
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param type    the type of the ArrayList
     * @throws SQLException SQLException | ClassCastException
     * @return List
     */
    <T> List<T> getList(String[] columns, Object[] values, String search, Class<T> type) throws SQLException;

    /**
     * Gets a ArrayList of Object from the database.
     *
     * @param <T> custom type
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param type   the type of the ArrayList
     * @throws SQLException SQLException | ClassCastException
     * @return List
     */
    <T> List<T> getList(String column, Object value, String search, Class<T> type) throws SQLException;

    /**
     * Gets the last Byte from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException | ClassCastException
     * @return Byte
     */
    Byte getLastByte(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets the last Byte from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Byte
     */
    Byte getLastByte(String column, Object value, String search) throws SQLException;

    /**
     * Gets the last Short from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException | ClassCastException
     * @return Short
     */
    Short getLastShort(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets the last Short from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Short
     */
    Short getLastShort(String column, Object value, String search) throws SQLException;

    /**
     * Gets the last Integer from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Integer
     */
    Integer getLastInteger(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets the last Integer from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Integer
     */
    Integer getLastInteger(String column, Object value, String search) throws SQLException;

    /**
     * Gets the last Long from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Long
     */
    Long getLastLong(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets the last Long from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Long
     */
    Long getLastLong(String column, Object value, String search) throws SQLException;

    /**
     * Gets the last Float from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Float
     */
    Float getLastFloat(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets the last Float from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Float
     */
    Float getLastFloat(String column, Object value, String search) throws SQLException;

    /**
     * Gets the last Double from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Double
     */
    Double getLastDouble(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets the last Double from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Double
     */
    Double getLastDouble(String column, Object value, String search) throws SQLException;

    /**
     * Gets the last String from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return String
     */
    String getLastString(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets the last String from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return String
     */
    String getLastString(String column, Object value, String search) throws SQLException;

    /**
     * Gets the last Timestamp from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Timestamp
     */
    Timestamp getLastTimestamp(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets the last Timestamp from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Timestamp
     */
    Timestamp getLastTimestamp(String column, Object value, String search) throws SQLException;

    /**
     * Gets the last Date from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException | ClassCastException
     * @return Date
     */
    Date getLastDate(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets the last Date from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Date
     */
    Date getLastDate(String column, Object value, String search) throws SQLException;

    /**
     * Gets the last Time from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException | ClassCastException
     * @return Time
     */
    Time getLastTime(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets the last Time from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Time
     */
    Time getLastTime(String column, Object value, String search) throws SQLException;

    /**
     * Gets the last Object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Object
     */
    Object getLastObject(String[] columns, Object[] values, String search) throws SQLException;

    /**
     * Gets the last Object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @throws SQLException SQLException
     * @return Object
     */
    Object getLastObject(String column, Object value, String search) throws SQLException;

    /**
     * Gets the last T object from the database.
     *
     * @param <T> custom type
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param type    the type of the object you want to get
     * @throws SQLException SQLException
     * @return T
     */
    <T> T getLastObject(String[] columns, Object[] values, String search, Class<T> type) throws SQLException;

    /**
     * Gets the last T object from the database.
     *
     * @param <T> custom type
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param type   the type of the object you want to get
     * @throws SQLException SQLException
     * @return T
     */
    <T> T getLastObject(String column, Object value, String search, Class<T> type) throws SQLException;

    /**
     * Update a list of columns with new values.
     *
     * @param columnsToEdit the list of columns to edit
     * @param newObjects    the list of new values
     * @param columns       the list of columns for the research
     * @param values        the list of values to be searched in the columns
     * @throws SQLException SQLException
     */
    void set(String[] columnsToEdit, Object[] newObjects, String[] columns, Object[] values) throws SQLException;

    /**
     * Update a list of columns with new values.
     *
     * @param columnToEdit the column to edit
     * @param newObject    the new value
     * @param columns      the list of columns for the research
     * @param values       the list of values to be searched in the columns
     * @throws SQLException SQLException
     */
    void set(String columnToEdit, Object newObject, String[] columns, Object[] values) throws SQLException;

    /**
     * Update a list of columns with new values.
     *
     * @param columnsToEdit the list of columns to edit
     * @param newObjects    the list of new values
     * @param column        the column for the research
     * @param value         the value to be searched in the column
     * @throws SQLException SQLException
     */
    void set(String[] columnsToEdit, Object[] newObjects, String column, Object value) throws SQLException;

    /**
     * Update a list of columns with new values.
     *
     * @param columnToEdit the column to edit
     * @param newObject    the new value
     * @param column       the column for the research
     * @param value        the value to be searched in the column
     * @throws SQLException SQLException
     */
    void set(String columnToEdit, Object newObject, String column, Object value) throws SQLException;
}
