package network;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Api {

    public static HttpURLConnection execute(String url, String requestMethod, ArrayList<String> headersName, ArrayList<String> headersValue) throws Exception {
        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestMethod(requestMethod);
            connection.setUseCaches(false); //не кешировать
            connection.setConnectTimeout(1000); //время подключения в миллисекундах
            connection.setReadTimeout(1000); //время чтения в миллисекундах
            connection.setDoOutput(true);

            if (headersName != null && headersValue != null) {
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
}