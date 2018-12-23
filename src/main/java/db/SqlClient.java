package db;

import java.util.ArrayList;

public class SqlClient {
    public static void insertReport(int numberReport, String stand, String testSuite, int launchDuraction, int totalCountTests, int countPassed, int countFailed, int countSkipped, String sessionKey) throws Exception {
        DbUtil.executeInsert(SqlBuilder.insertReport(numberReport, stand, testSuite, launchDuraction, totalCountTests, countPassed, countFailed, countSkipped, sessionKey));
    }

    public static int getLastReport() throws Exception {
        ArrayList<String> result = DbUtil.executeSelect(SqlBuilder.getLastReport());
        if(result.get(0).equals("null")) {
            throw new Exception("Не найдено максимальный номер отчета");
        }
        return Integer.parseInt(result.get(0));
    }
}
