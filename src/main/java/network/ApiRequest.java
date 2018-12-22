package network;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class ApiRequest {
    public static String getCookieForJenkins() throws Exception {
        //запрос 1 (код ответа:403)
        HttpURLConnection response1 = Api.execute(
                "http://build.youdo.sg/",
                "GET",
                null,
                null
        );
        String response1Cookie = response1.getHeaderField("Set-Cookie").replaceAll(";(.*)", "");

        //запрос 2 (код ответа:302)
        ArrayList<String> headersNameRequest2 = new ArrayList<>();
        ArrayList<String> headersValueRequest2 = new ArrayList<>();
        headersNameRequest2.add("Cookie");
        headersValueRequest2.add(response1Cookie); //Cookie
        HttpURLConnection response2 = Api.execute(
                "http://build.youdo.sg/securityRealm/commenceLogin?from=%2F",
                "GET",
                headersNameRequest2,
                headersValueRequest2
        );
        String response2Location = response2.getHeaderField("Location");

        //запрос 3 (код ответа:302)
        HttpURLConnection response3 = Api.execute(
                response2Location,
                "GET",
                null,
                null
        );
        String response3Cookie = response3.getHeaderField("Set-Cookie").replaceAll(";(.*)", "");
        String response3Location = response3.getHeaderField("Location");

        //запрос 4 (код ответа:302)
        ArrayList<String> headersNameRequest4 = new ArrayList<>();
        ArrayList<String> headersValueRequest4 = new ArrayList<>();
        headersNameRequest4.add("cookie");
        headersValueRequest4.add(response3Cookie); //Cookie
        HttpURLConnection response4 = Api.execute(
                response3Location,
                "GET",
                headersNameRequest4,
                headersValueRequest4
        );
        String response4Cookie = response4.getHeaderField("Set-Cookie").replaceAll(";(.*)", "");

        //запрос 5 (код ответа:302)
        ArrayList<String> headersNameRequest5 = new ArrayList<>();
        ArrayList<String> headersValueRequest5 = new ArrayList<>();
        headersNameRequest5.add("cookie");
        headersValueRequest5.add(response4Cookie); //Cookie

        //TODO: Как передавать параметры и почему сервер отвечает 422 ошибкой вместо 302?
        HttpURLConnection response5 = Api.execute(
                "https://gitlab.youdo.sg/users/sign_in",
                "POST",
                headersNameRequest5,
                headersValueRequest5
        );

        String response5Location = response5.getHeaderField("Location");
        String response5Cookie = response5.getHeaderField("Set-Cookie").replaceAll(";(.*)", "");

        System.out.println(response4Cookie);
        System.out.println(response5Cookie);

        //запрос 6 (код ответа:302)
        ArrayList<String> headersNameRequest6 = new ArrayList<>();
        ArrayList<String> headersValueRequest6 = new ArrayList<>();
        headersNameRequest6.add("cookie");
        headersValueRequest6.add(response5Cookie); //Cookie
        HttpURLConnection response6 = Api.execute(
                response5Location,
                "GET",
                headersNameRequest6,
                headersValueRequest6
        );
        String response6Location = response6.getHeaderField("Location");

        //запрос 7 (код ответа:302)
        ArrayList<String> headersNameRequest7 = new ArrayList<>();
        ArrayList<String> headersValueRequest7 = new ArrayList<>();
        headersNameRequest7.add("cookie");
        headersValueRequest7.add(response1Cookie); //Cookie
        HttpURLConnection response7 = Api.execute(
                response6Location,
                "GET",
                headersNameRequest6,
                headersValueRequest6
        );
        String response7Location = response7.getHeaderField("Location");

        //запрос 8 (код ответа:200)
        ArrayList<String> headersNameRequest8 = new ArrayList<>();
        ArrayList<String> headersValueRequest8 = new ArrayList<>();
        headersNameRequest8.add("cookie");
        headersValueRequest8.add(response1Cookie); //Cookie
        HttpURLConnection response8 = Api.execute(
                response7Location,
                "GET",
                headersNameRequest8,
                headersValueRequest8
        );

        return response1Cookie;
    }
}
