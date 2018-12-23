package db;

import log.Logger;

import java.sql.*;
import java.util.ArrayList;

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

    public synchronized static ArrayList<String> executeSelect(String sql) {
        connect();
        ArrayList<String> result = new ArrayList<>();
        String request = String.format(sql);
        try (ResultSet resultset = statment.executeQuery(request)) {
            if(resultset.next()) {
                ResultSetMetaData rsmd = resultset.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                    String value = resultset.getString(i);
                    if(value == null) {
                        value = "null";
                    }
                    result.add(value);
                }
            } else {
                Logger.logError("Результат не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }
        return result;
    }

    public synchronized static String executeInsert(String sql) throws Exception {
        connect();
        String request = String.format(sql);
        try {
            int response = statment.executeUpdate(request);
            if(response == 1) {
                Logger.logSuccess("Запрос выполнен! \n" + request);
            } else {
                Logger.logError("Запрос не выполнен! \n" + request);
            }
        } catch (SQLException e) {
            throw new Exception("Добавление строк в таблицу не выполнено!\n" +
                    e.getMessage());
        } finally {
            disconnect();
        }
        return null;
    }
}
