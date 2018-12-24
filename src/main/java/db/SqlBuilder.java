package db;

public class SqlBuilder {
    private static String INSERT_REPORT = "INSERT INTO Report ( " +
            "NumberReport, " +
            "Stand, " +
            "TestSuite, " +
            "LaunchDuraction, " +
            "StartTime, " +
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
            "'<startTime>', " +
            "'<totalCountTests>', " +
            "'<countPassed>', " +
            "'<countFailed>', " +
            "'<countSkipped>', " +
            "'<sessionKey>' " +
            ")";

    private static String GET_LAST_REPORT = "SELECT MAX(NumberReport) " +
            "FROM Report";

    private static String INSERT_TEST = "INSERT INTO Test ( " +
            "NumberReport, " +
            "NamePackageSuite, " +
            "NameTestEng, " +
            "NameTestRus, " +
            "Status, " +
            "SessionKey " +
            ") " +
            "VALUES ( " +
            "'<numberReport>', " +
            "'<namePackageSuite>', " +
            "'<nameTestEng>', " +
            "'<nameTestRus>', " +
            "'<status>', " +
            "'<sessionKey>' " +
            ");";

    public static String insertTest(int numberReport, String namePackageSuite, String nameTestEng, String nameTestRus,int status, String sessionKey) {
        return INSERT_TEST
                .replace("<numberReport>", String.valueOf(numberReport))
                .replace("<namePackageSuite>", namePackageSuite)
                .replace("<nameTestEng>", nameTestEng)
                .replace("<nameTestRus>", nameTestRus)
                .replace("<status>", String.valueOf(status))
                .replace("<sessionKey>", sessionKey);
    }

    public static String insertReport(int numberReport, String stand, String testSuite, int launchDuraction, String startTime, int totalCountTests, int countPassed, int countFailed, int countSkipped, String sessionKey) {
        return INSERT_REPORT
                .replace("<numberReport>", String.valueOf(numberReport))
                .replace("<stand>", stand)
                .replace("<testSuite>", testSuite)
                .replace("<launchDuraction>", String.valueOf(launchDuraction))
                .replace("<startTime>", String.valueOf(startTime))
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
