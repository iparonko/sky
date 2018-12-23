import network.Api;
import network.ApiRequest;
import report.DbReport;
import report.Report;
import util.DataGeneratorUtil;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        String cookieJenkins = ApiRequest.getCookieForJenkins();

       /* for (int i = 547; i < 586; i++) {
            testMoreReport(i,cookieJenkins);
        }*/

        testMoreReport(585, cookieJenkins);
    }

    public synchronized static void testMoreReport(int numberReport, String cookieJenkins ) throws Exception {
        ArrayList<String> headersNameGitLabLogin = new ArrayList<>();
        headersNameGitLabLogin.add("Cookie");
        ArrayList<String> headersValueGitLabLogin = new ArrayList<>();
        headersValueGitLabLogin.add(cookieJenkins);
        HttpURLConnection responseOpenPage = Api.execute(
                "http://build.youdo.sg/job/staging/job/youdo_android_testing/" + numberReport + "/artifact/report/report.html",
                "GET",
                headersNameGitLabLogin,
                headersValueGitLabLogin
        );

        String fullPageReport = Api.getSourcePage(responseOpenPage);

        Report report = new Report(fullPageReport);

        String guidSessionKey = DataGeneratorUtil.generateUUID();
        DbReport.insertReport(report, guidSessionKey);
    }
}
