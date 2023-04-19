package org.example;

import java.sql.*;
import java.util.Properties;

public class TDEngineManager {
    private static String host = "127.0.0.1";
    private static final String dbName = "test";
    private static final String tbName = "d7771";
    private static final String user = "root";
    private static final String password = "taosdata";

    private Connection connection;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        TDEngineManager manager = new TDEngineManager();
        //
        String sql = "insert into gas_compressor.test file '/Users/hebert/Desktop/data.csv';";
        manager.exuete(args[0]);

    }

    public TDEngineManager() throws SQLException, ClassNotFoundException {
        init();
    }

    private void init() throws ClassNotFoundException, SQLException {
        // use port 6041 in url when use JDBC-restful
        String url = "jdbc:TAOS-RS://" + host + ":6041/?user=" + user + "&password=" + password;

        Properties properties = new Properties();
        properties.setProperty("charset", "UTF-8");
        properties.setProperty("locale", "en_US.UTF-8");
        properties.setProperty("timezone", "UTC-8");
        connection = DriverManager.getConnection(url, properties);
    }

    private void close() {
        try {
            if (connection != null) {
                this.connection.close();
                System.out.println("connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void executeQuery(String sql) {
        long start = System.currentTimeMillis();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            long end = System.currentTimeMillis();
            printSql(sql, true, (end - start));
            printResult(resultSet);
        } catch (SQLException e) {
            long end = System.currentTimeMillis();
            printSql(sql, false, (end - start));
            e.printStackTrace();
        }
    }

    private void printResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnLabel = metaData.getColumnLabel(i);
                String value = resultSet.getString(i);
                System.out.printf("%s: %s\t", columnLabel, value);
            }
            System.out.println();
        }
    }

    private void printSql(String sql, boolean succeed, long cost) {
        System.out.println("[ " + (succeed ? "OK" : "ERROR!") + " ] time cost: " + cost + " ms, execute statement ====> " + sql);
    }

    private void exuete(String sql) {
        long start = System.currentTimeMillis();
        try (Statement statement = connection.createStatement()) {
            boolean execute = statement.execute(sql);
            long end = System.currentTimeMillis();
            printSql(sql, true, (end - start));
        } catch (SQLException e) {
            long end = System.currentTimeMillis();
            printSql(sql, false, (end - start));
            System.err.println("ERROR MESSAGE: " + e.getMessage());
        }
    }

}