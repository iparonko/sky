package db;

import java.util.ArrayList;

public class SqlClient {
    public static void insertReport(int numberReport, String stand, String testSuite, int launchDuraction, int totalCountTests, int countPassed, int countFailed, int countSkipped, String sessionKey) throws Exception {
        DbUtil.connect();
        DbUtil.executeInsert(SqlBuilder.insertReport(numberReport, stand, testSuite, launchDuraction, totalCountTests, countPassed, countFailed, countSkipped, sessionKey));
        DbUtil.disconnect();
    }

    public static int getLastReport() throws Exception {
        DbUtil.connect();
        ArrayList<String> result = DbUtil.executeSelect(SqlBuilder.getLastReport());
        DbUtil.disconnect();
        if(result.size() == 0) {
            throw new Exception("Не найдено максимальное значение номера отчета");
        }
        return Integer.parseInt(result.get(0));
    }
}
