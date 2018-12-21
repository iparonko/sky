package db;

import java.sql.*;

public class SqlClient {
    private static Connection connecion = null;
    private static Statement statment;

    public synchronized static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connecion = DriverManager.getConnection("jdbc:sqlite:sky.db");
            statment = connecion.createStatement();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static void disconnect() {
        try {
            connecion.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static String execute() {
        String request = String.format("SELECT * FROM Test");
        try (ResultSet set = statment.executeQuery(request)) {
            if(set.next()) {
                return set.getString("Name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
