import network.Api;
import report.Report;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<String> headersNameGitLabLogin = new ArrayList<String>();
        headersNameGitLabLogin.add("Cookie");

        ArrayList<String> headersValueGitLabLogin = new ArrayList<String>();
        headersValueGitLabLogin.add("JSESSIONID.1fb7cb68=node0dol0riptjnlu9h0o6zp8rxra14457.node0"); //Cookie

        HttpURLConnection responseOpenPage = Api.execute(
                "http://build.youdo.sg/job/staging/job/youdo_android_testing/579/artifact/report/report.html",
                "GET",
                headersNameGitLabLogin,
                headersValueGitLabLogin
        );
        //Api.printSourcePage(responseOpenPage);

        String fullPageReport = Api.getSourcePage(responseOpenPage);

        Report report = new Report(fullPageReport);
        report.printInfoAboutReport();
    }
}
