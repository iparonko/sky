package db;

public class SqlBuilder {
    private static String INSERT_REPORT = "INSERT INTO Report ( " +
            "NumberReport, " +
            "Stand, " +
            "TestSuite, " +
            "LaunchDuraction, " +
            "TotalCountTests, " +
            "CountPassed, " +
            "CountFailed, " +
            "CountSkipped, " +
            "SessionKey " +
            ") " +
            "VALUES ( " +
            "'<numberReport>', " +
            "'<stand>', " +
            "'<testSuite>', " +
            "'<launchDuraction>', " +
            "'<totalCountTests>', " +
            "'<countPassed>', " +
            "'<countFailed>', " +
            "'<countSkipped>', " +
            "'<sessionKey>' " +
            ")";

    private static String GET_LAST_REPORT = "SELECT MAX(NumberReport) " +
            "FROM Report";

    public static String insertReport(int numberReport, String stand, String testSuite, int launchDuraction, int totalCountTests, int countPassed, int countFailed, int countSkipped, String sessionKey) {
        return INSERT_REPORT
                .replace("<numberReport>", String.valueOf(numberReport))
                .replace("<stand>", stand)
                .replace("<testSuite>", testSuite)
                .replace("<launchDuraction>", String.valueOf(launchDuraction))
                .replace("<totalCountTests>", String.valueOf(totalCountTests))
                .replace("<countPassed>", String.valueOf(countPassed))
                .replace("<countFailed>", String.valueOf(countFailed))
                .replace("<countSkipped>", String.valueOf(countSkipped))
                .replace("<sessionKey>", sessionKey);
    }

    public static String getLastReport() {
        return GET_LAST_REPORT;
    }
}
