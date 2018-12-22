package db;

public class SqlClient {
    public static void insertReport(int numberReport, String stand, String testSuite, int launchDuraction, int totalCountTests, int countPassed, int countFailed, int countSkipped, String sessionKey) {
        DbUtil.connect();
        DbUtil.executeInsert(SqlBuilder.insertReport(numberReport, stand, testSuite, launchDuraction, totalCountTests, countPassed, countFailed, countSkipped, sessionKey));
        DbUtil.disconnect();
    }
}
