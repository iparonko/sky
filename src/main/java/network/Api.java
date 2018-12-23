package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Api {
    private static String jenkinsCookie = "";

    public static void setJenkinsCookie() throws Exception {
        jenkinsCookie = ApiRequest.getCookieForJenkins();
    }

    public static String getJenkinsCookie() {
        return jenkinsCookie;
    }

    public static HttpURLConnection execute(String url, String requestMethod, ArrayList<String> headersName, ArrayList<String> headersValue) throws Exception {
        HttpURLConnection connection;

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

            System.out.println("[" + requestMethod + "][" + connection.getResponseMessage() + "][" + connection.getResponseCode() + "][" + url + "]");
        } catch (IOException e) {
            throw new Exception("Запрос не выполнен!");
        }
        return connection;
    }

    public static void printSourcePage(HttpURLConnection connection) throws IOException {
        System.out.println(getSourcePage(connection));
    }

    public static String getSourcePage(HttpURLConnection connection) throws IOException {
        StringBuilder sb = new StringBuilder();
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
        return sb.toString();
    }

    public static void assertResponseCode(HttpURLConnection response, int expectedStatusCode) throws Exception {
        int actualStatusCode = response.getResponseCode();
        if(actualStatusCode != expectedStatusCode) {
            throw new Exception("\nОжидамый код ответа [" + expectedStatusCode + "]\n" +
                    "Актуальный код ответа [" + actualStatusCode + "]\n" +
                    "URL [" + response.getURL() + "]");
        }
    }
}