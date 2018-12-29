package executor;

import builds.BuildsInfo;
import db.SqlClient;
import network.Api;
import report.DbReport;
import report.Report;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static log.LoggerInfo.logSuccess;
import static network.ApiRequest.getPageReport;

public class ExecutorMethods {
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
        boolean resultInsertAllTests = false;
        HttpURLConnection response = getPageReport(numberReport);
        if(response != null) {
            String fullPageReport = Api.getSourcePage(response);
            Report report = new Report(fullPageReport);
            String sessionKey = DbReport.createSessionKey();
            if(!sessionKey.equals("key_not_created")) {
                if(DbReport.insertReport(report, sessionKey)) {
                    resultInsertAllTests = DbReport.insertAllTests(report, sessionKey);
                }
                if(resultInsertAllTests) {
                    DbReport.updateSessionKeySetIsDone(sessionKey);
                }
            }
        }
    }
}
