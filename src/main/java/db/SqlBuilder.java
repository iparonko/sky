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

    private static String INSERT_SESSION_KEY = "INSERT INTO SessionKey (SessionKey) VALUES ('<sessionKey>');";

    private static String UPDATE_SESSION_KEY_SET_IS_DONE = "UPDATE SessionKey SET isDone = 1 " +
            "WHERE SessionKey = '<sessionKey>';";

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

    public static String insertSessionKey(String sessionKey) {
        return INSERT_SESSION_KEY
                .replace("<sessionKey>", sessionKey);
    }

    public static String updateSessionKeySetIsDone(String sessionKey) {
        return UPDATE_SESSION_KEY_SET_IS_DONE
                .replace("<sessionKey>", sessionKey);
    }
}
