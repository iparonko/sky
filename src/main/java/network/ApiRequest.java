package network;

import util.StringUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import static log.LoggerInfo.logError;
import static log.LoggerInfo.logException;
import static network.Api.*;
import static settings.Accounts.LOGIN_JENKINS;
import static settings.Accounts.PASSWORD_JENKINS;

public class ApiRequest {

    /**
     * Возвращает jenkins cookie
     */
    public static String getCookieForJenkins() {
        String cookie = "";
        int tryCount = 3;
        while (tryCount > 0) {
            try {
                cookie = generateCookieForJenkins();
                tryCount = 0;
            } catch (Exception e) {
                tryCount--;
                if(tryCount > 0) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e1) {
                        logException("Произошла ошибка при выполнении sleep метода!", e);
                    }
                    logError("Пробуем повторно отправить запрос. Попыток: " + tryCount);
                } else {
                    logException("Произошла ошибка при генерации jenkins cookie!", e);
                }
            }
        }
        return cookie;
    }

    /**
     * Генерация jenkins cookie
     */
    private static String generateCookieForJenkins() {
        //запрос 1
        HttpURLConnection response1 = Api.execute(
                "http://build.youdo.sg/",
                "GET",
                null,
                null
        );
        assertResponseCode(response1, 403);
        String response1Cookie = response1.getHeaderField("Set-Cookie").replaceAll(";(.*)", "");

        //запрос 2
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
        assertResponseCode(response2, 302);
        String response2Location = response2.getHeaderField("Location");

        //запрос 3
        HttpURLConnection response3 = Api.execute(
                response2Location,
                "GET",
                null,
                null
        );
        assertResponseCode(response3, 302);
        String response3Cookie = response3.getHeaderField("Set-Cookie").replaceAll(";(.*)", "");
        String response3Location = response3.getHeaderField("Location");

        //запрос 4
        ArrayList<String> headersNameRequest4 = new ArrayList<>();
        ArrayList<String> headersValueRequest4 = new ArrayList<>();
        headersNameRequest4.add("cookie");
        headersValueRequest4.add(response3Cookie);
        HttpURLConnection response4 = Api.execute(
                response3Location,
                "GET",
                headersNameRequest4,
                headersValueRequest4
        );
        assertResponseCode(response4, 200);
        String response4Cookie = response4.getHeaderField("Set-Cookie").replaceAll(";(.*)", "");
        String token = StringUtil
                .findMatchByRegexp(getSourcePage(response4), "<meta name=\"csrf-token\" content=\"(.*)")
                .replaceAll("<meta name=\"csrf-token\" content=\"", "")
                .replaceAll("\" />", "")
                .replaceAll("\\+", "%2B");

        //запрос 5
        ArrayList<String> headersNameRequest5 = new ArrayList<>();
        ArrayList<String> headersValueRequest5 = new ArrayList<>();
        headersNameRequest5.add("cookie");
        headersNameRequest5.add("authenticity_token");
        headersValueRequest5.add(response4Cookie);
        headersValueRequest5.add(token);
        HttpURLConnection response5 = Api.execute(
                "https://gitlab.youdo.sg/users/sign_in?" +
                        "utf8=%E2%9C%93&" +
                        "authenticity_token=" + token + "&" +
                        "user%5Blogin%5D=" + LOGIN_JENKINS + "&" +
                        "user%5Bpassword%5D=" + PASSWORD_JENKINS + "&" +
                        "user%5Bremember_me%5D=0",
                "POST",
                headersNameRequest5,
                headersValueRequest5
        );
        assertResponseCode(response5, 302);
        String response5Location = response5.getHeaderField("Location");
        String response5Cookie = response5.getHeaderField("Set-Cookie").replaceAll(";(.*)", "");

        //запрос 6
        ArrayList<String> headersNameRequest6 = new ArrayList<>();
        ArrayList<String> headersValueRequest6 = new ArrayList<>();
        headersNameRequest6.add("cookie");
        headersValueRequest6.add(response5Cookie);
        HttpURLConnection response6 = Api.execute(
                response5Location,
                "GET",
                headersNameRequest6,
                headersValueRequest6
        );
        assertResponseCode(response6, 302);
        String response6Location = response6.getHeaderField("Location");

        //запрос 7
        ArrayList<String> headersNameRequest7 = new ArrayList<>();
        ArrayList<String> headersValueRequest7 = new ArrayList<>();
        headersNameRequest7.add("Cookie");
        headersValueRequest7.add(response1Cookie); //Cookie
        HttpURLConnection response7 = Api.execute(
                response6Location,
                "GET",
                headersNameRequest7,
                headersValueRequest7
        );
        assertResponseCode(response7, 302);
        String response7Location = response7.getHeaderField("Location");

        //запрос 8
        ArrayList<String> headersNameRequest8 = new ArrayList<>();
        ArrayList<String> headersValueRequest8 = new ArrayList<>();
        headersNameRequest8.add("Cookie");
        headersValueRequest8.add(response1Cookie);
        HttpURLConnection response8 = Api.execute(
                response7Location,
                "GET",
                headersNameRequest8,
                headersValueRequest8
        );
        assertResponseCode(response8, 200);
        return response1Cookie;
    }

    /**
     * Возвращает все номера сборок андроид прогонов
     */
    public static HttpURLConnection getAllBuildsAndroid() {
        ArrayList<String> headersNameGitLabLogin = new ArrayList<>();
        headersNameGitLabLogin.add("Cookie");
        ArrayList<String> headersValueGitLabLogin = new ArrayList<>();
        headersValueGitLabLogin.add(getJenkinsCookie());
        HttpURLConnection response = Api.execute(
                "http://build.youdo.sg/view/%D0%A2%D0%B5%D1%81%D1%82%D0%BE%D0%B2%D1%8B%D0%B9%20%D1%81%D1%82%D0%B5%D0%BD%D0%B4/job/staging/job/youdo_android_testing/buildTimeTrend",
                "GET",
                headersNameGitLabLogin,
                headersValueGitLabLogin
        );
        return response;
    }

    /**
     * Возвращает код страницы отчета
     *
     * @param numberReport
     */
    public static HttpURLConnection getPageReport(int numberReport) {
        ArrayList<String> headersNameGitLabLogin = new ArrayList<>();
        headersNameGitLabLogin.add("Cookie");
        ArrayList<String> headersValueGitLabLogin = new ArrayList<>();
        headersValueGitLabLogin.add(getJenkinsCookie());
        HttpURLConnection response = Api.execute(
                "http://build.youdo.sg/job/staging/job/youdo_android_testing/" + numberReport + "/artifact/report/report.html",
                "GET",
                headersNameGitLabLogin,
                headersValueGitLabLogin
        );
        try {
            if(response.getResponseCode() == 404) {
                logError("При выполнении запроса получен 404 ответ!");
                return null;
            }
        } catch (IOException e) {
            logException("Не выполнен запрос по получению кода ответа!", e);
        }
        return response;
    }
}
