package db;

import java.sql.*;
import java.util.ArrayList;

import static log.LoggerInfo.*;

public class DbUtil {
    private static Connection connecion = null;
    private static Statement statment;

    public synchronized static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connecion = DriverManager.getConnection("jdbc:sqlite:sky.db");
            statment = connecion.createStatement();
        } catch (ClassNotFoundException e) {
            logException("Произошла ошибка при выполнении подключения к базе данных!", e);
        } catch (SQLException e) {
            logException("Произошла ошибка при выполнении подключения к базе данных!", e);
        }
    }

    public synchronized static void disconnect() {
        try {
            connecion.close();
        } catch (SQLException e) {
            logException("Произошла ошибка при выполнении отключения от базы данных!", e);
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
                logError("Результат в базе данных не найден");
            }
        } catch (SQLException e) {
            logException("Произошла ошибка при выполнении SELECT запроса!", e);
        } finally {
            disconnect();
        }
        return result;
    }

    public synchronized static String executeInsert(String sql) {
        connect();
        String request = String.format(sql);
        try {
            int response = statment.executeUpdate(request);
            if(response == 1) {
                logSuccess("Запрос на добавление строк в базу данных выполнен!\n" + request);
            } else {
                logError("Запрос на добавление строк в базу данных не выполнен!\n" + request);
            }
        } catch (SQLException e) {
            logException("Запрос на добавление строк в базу данных не выполнен!", e);
        } finally {
            disconnect();
        }
        return null;
    }
}
