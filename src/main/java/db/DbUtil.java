package db;

import log.Logger;

import java.sql.*;

public class DbUtil {
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

    public synchronized static String executeSelect(String sql) {
        String request = String.format(sql);
        try (ResultSet set = statment.executeQuery(request)) {
            if (set.next()) {
                return set.getString("Name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public synchronized static String executeInsert(String sql) {
        String request = String.format(sql);
        try {
            int response = statment.executeUpdate(request);
            if (response == 1) {
                Logger.logSuccess("Запрос выполнен! \n" + request);
            } else {
                Logger.logError("Запрос не выполнен! \n" + request);
            }
        } catch (SQLException e) {
            //Logger.logError("Запрос не выполнен! \n" + request);
            //Logger.logError(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
