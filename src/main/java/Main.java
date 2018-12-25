import builds.BuildsInfo;
import db.SqlClient;
import network.Api;
import report.DbReport;
import report.Report;
import util.DataGeneratorUtil;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static log.LoggerInfo.logSuccess;
import static network.Api.setJenkinsCookie;
import static network.ApiRequest.getPageReport;

public class Main {
    public static void main(String[] args) {
        setJenkinsCookie();
        checkAndSaveNewReport();
    }

    public synchronized static void checkAndSaveNewReport() {
        int lastReportInDb = SqlClient.getLastReport();
        ArrayList<Integer> allBuildsAndroid = BuildsInfo.getAllBuildsAndroid();
        ArrayList<Integer> finalAllBuildsAndroid = new ArrayList<>();
        int countBuilds = allBuildsAndroid.size();
        for (int i = 0; i < countBuilds; i++) {
            if(lastReportInDb < allBuildsAndroid.get(i)) {
                finalAllBuildsAndroid.add(allBuildsAndroid.get(i));
            }
        }
        int finalCountBuilds = finalAllBuildsAndroid.size();
        if(finalAllBuildsAndroid.size() > 0) {
            for (int i = 0; i < finalCountBuilds; i++) {
                saveNewReport(finalAllBuildsAndroid.get(i));
            }
        }
        logSuccess("Все отчеты успешно загружены в базу данных!");
    }

    public synchronized static void saveNewReport(int numberReport) {
        HttpURLConnection response = getPageReport(numberReport);
        if(response != null) {
            String fullPageReport = Api.getSourcePage(response);
            Report report = new Report(fullPageReport);
            String guidSessionKey = DataGeneratorUtil.generateUUID();
            if(DbReport.insertReport(report, guidSessionKey)) {
                DbReport.insertAllTest(report, guidSessionKey);
            }
        }
    }
}
