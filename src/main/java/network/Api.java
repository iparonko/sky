package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static log.LoggerInfo.*;

public class Api {
    private static String jenkinsCookie = "";

    /**
     * Устанавливает jenkins сookie
     */
    public static void setJenkinsCookie() {
        jenkinsCookie = ApiRequest.getCookieForJenkins();
    }

    /**
     * Возвращает jenkins сookie
     */
    public static String getJenkinsCookie() {
        return jenkinsCookie;
    }

    /**
     * Выполняет API запрос
     */
    public static HttpURLConnection execute(String url, String requestMethod, ArrayList<String> headersName, ArrayList<String> headersValue) {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setUseCaches(false); //не кешировать
            connection.setConnectTimeout(10000); //время подключения в миллисекундах
            connection.setReadTimeout(10000); //время чтения в миллисекундах
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            if(headersName != null && headersValue != null) {
                for (int i = 0; i < headersName.size(); i++) {
                    connection.setRequestProperty(headersName.get(i), headersValue.get(i));
                }
            }
            connection.connect();
            logSuccess("[" + requestMethod + "][" + connection.getResponseMessage() + "][" + connection.getResponseCode() + "][" + url + "]");
        } catch (IOException e) {
            logException("Произошла ошибка при выполнении API запроса", e);
        }
        return connection;
    }

    /**
     * Намечатать код страницы в консоль (для отладки)
     */
    public static void printSourcePage(HttpURLConnection connection) {
        System.out.println(getSourcePage(connection));
    }

    /**
     * Возвращает код страницы
     */
    public static String getSourcePage(HttpURLConnection connection) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            logException("Произошла ошибка при получении кода страницы", e);
        }
        return sb.toString();
    }

    /**
     * Проверяет код ответа
     */
    public static void assertResponseCode(HttpURLConnection response, int expectedStatusCode) {
        int actualStatusCode = 0;
        try {
            actualStatusCode = response.getResponseCode();
        } catch (IOException e) {
            logException("Произошла ошибка при получении актуального кода ответа API запроса", e);
        }
        if(actualStatusCode != expectedStatusCode) {
            logError("\nОжидамый код ответа [" + expectedStatusCode + "]\n" +
                    "Актуальный код ответа [" + actualStatusCode + "]\n" +
                    "URL [" + response.getURL() + "]");
        }
    }
}