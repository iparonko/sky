package db;

import settings.Accounts;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import static log.LoggerInfo.*;

public class DbUtil {
    private static Connection connecion = null;
    private static Statement statment;

    public synchronized static void connect() {
        try {
            String url = "jdbc:postgresql://postgresql.iparonko.myjino.ru/iparonko_sky";
            Properties props = new Properties();
            props.setProperty("user", Accounts.LOGIN_SQL);
            props.setProperty("password",Accounts.PASSWORD_SQL);
            connecion = DriverManager.getConnection(url, props);
            statment = connecion.createStatement();
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

    public synchronized static boolean executeInsert(String sql, int expectedCountRow) {
        connect();
        String request = String.format(sql);
        try {
            int response = statment.executeUpdate(request);
            if(response == expectedCountRow) {
                logSuccess("Запрос на добавление [" + expectedCountRow + "] строк в базу данных выполнен!\n" + request);
                return true;
            } else {
                logError("Запрос на добавление [" + expectedCountRow + "] строк в базу данных не выполнен!\n" +
                        "Добавлено [" + response + "] строк!\n" + request);
                return false;
            }
        } catch (SQLException e) {
            logException("Запрос на добавление строк в базу данных не выполнен!", e);
        } finally {
            disconnect();
        }
        return false;
    }

    public synchronized static boolean executeUpdate(String sql, int expectedCountRow) {
        connect();
        String request = String.format(sql);
        try {
            int response = statment.executeUpdate(request);
            if(response == expectedCountRow) {
                logSuccess("Запрос на обновление [" + expectedCountRow + "] строк в базе данных выполнен!\n" + request);
                return true;
            } else {
                logError("Запрос на обновление [" + expectedCountRow + "] строк в базе данных не выполнен!\n" +
                        "Обновлено [" + response + "] строк!\n" + request);
                return false;
            }
        } catch (SQLException e) {
            logException("Запрос на обновление строк в базе данных не выполнен!", e);
        } finally {
            disconnect();
        }
        return false;
    }
}
