package report;

import db.SqlClient;
import test.Test;

import java.util.ArrayList;

public class DbReport {
    public static boolean insertReport(Report report, String sessionKey) {
        int numberReport = report.getNumberReport();
        String stand = report.getStand();
        String testSuite = report.getTestSuite();
        int launchDuraction = report.getLaunchDuration();
        String startTime = report.getStartTime();
        int totalCountTests = report.getTotalCountTests();
        int countPassed = report.getCountPassed();
        int countFailed = report.getCountFailed();
        int countSkipped = report.getCountSkipped();
        return SqlClient.insertReport(numberReport, stand, testSuite, launchDuraction, startTime, totalCountTests, countPassed, countFailed, countSkipped, sessionKey);
    }

    public static void insertTest(int numberReport, String namePackageSuite, String nameTestEng, String nameTestRus, int status, String sessionKey) {
        SqlClient.insertTest(numberReport, namePackageSuite, nameTestEng, nameTestRus, status, sessionKey);
    }

    public static void insertAllTest(Report report, String sessionKey) {
        ArrayList<Test> tests = report.getTestsArray();
        int countTest = report.getTotalCountTests();
        for (int i = 0; i < countTest; i++) {
            Test test = tests.get(i);
            DbReport.insertTest(test.getNumberReport(), test.getNamePackageSuite(), test.getNameTestEng(), test.getNameTestRus(), test.getStatus(), sessionKey);
        }
    }
}
