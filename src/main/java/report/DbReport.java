package report;

import db.SqlClient;

public class DbReport {
    public static void insertReport(Report report, String sessionKey) {
        int numberReport = report.getNumberReport();
        String stand = report.getStand();
        String testSuite = report.getTestSuite();
        int launchDuraction = report.getLaunchDuration();
        int totalCountTests = report.getTotalCountTests();
        int countPassed = report.getCountPassed();
        int countFailed = report.getCountFailed();
        int countSkipped = report.getCountSkipped();
        SqlClient.insertReport(numberReport, stand, testSuite, launchDuraction, totalCountTests, countPassed, countFailed, countSkipped, sessionKey);
    }

}
