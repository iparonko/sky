import network.Api;
import network.ApiRequest;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
/*        for (int i = 547; i < 586; i++) {
            testMoreReport(i);
        }*/

        testMoreReport(585);
    }

    public synchronized static void testMoreReport(int a) throws Exception {
        String cookieJenkins = ApiRequest.getCookieForJenkins();

        ArrayList<String> headersNameGitLabLogin = new ArrayList<>();
        headersNameGitLabLogin.add("Cookie");

        ArrayList<String> headersValueGitLabLogin = new ArrayList<>();
        headersValueGitLabLogin.add(cookieJenkins); //Cookie

        HttpURLConnection responseOpenPage = Api.execute(
                "ссылка",
                "GET",
                headersNameGitLabLogin,
                headersValueGitLabLogin
        );

        //String row = responseOpenPage.getHeaderField("Set-Cookie").replaceAll(";(.*)","");
        //System.out.println(row);

        //String fullPageReport = Api.getSourcePage(responseOpenPage);

        //Report report = new Report(fullPageReport);

        //System.out.println(fullPageReport);

        // String guidSessionKey = DataGeneratorUtil.generateUUID();
        // DbReport.insertReport(report, guidSessionKey);
    }
}
