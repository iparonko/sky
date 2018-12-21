import network.Api;
import report.Report;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<String> headersNameGitLabLogin = new ArrayList<String>();
        headersNameGitLabLogin.add("Cookie");

        ArrayList<String> headersValueGitLabLogin = new ArrayList<String>();
        headersValueGitLabLogin.add("кука"); //Cookie

        HttpURLConnection responseOpenPage = Api.execute(
                "ссылка на отчет",
                "GET",
                headersNameGitLabLogin,
                headersValueGitLabLogin
        );

        String fullPageReport = Api.getSourcePage(responseOpenPage);

        new Report(fullPageReport);

        Api.printSourcePage(responseOpenPage);
    }
}
