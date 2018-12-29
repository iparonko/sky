package report;

import db.SqlClient;
import log.LoggerInfo;
import util.DataGeneratorUtil;

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

    public static boolean insertAllTests(Report report, String sessionKey) {
        return SqlClient.insertAllTests(report, sessionKey);
    }

    public static String createSessionKey() {
        String sessionKey = DataGeneratorUtil.generateUUID();
        boolean result = SqlClient.insertSessionKey(sessionKey);
        if(result) {
            return sessionKey;
        } else {
            LoggerInfo.logError("SessionKey не сгенерирован!");
            return "key_not_created";
        }
    }

    public static boolean updateSessionKeySetIsDone(String sessionKey) {
        return SqlClient.updateSessionKeySetIsDone(sessionKey);
    }
}
