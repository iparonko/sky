package db;

import java.util.ArrayList;

import static log.LoggerInfo.logError;

public class SqlClient {
    /**
     * Добавляет отчет в базу
     */
    public static boolean insertReport(int numberReport, String stand, String testSuite, int launchDuraction, String startTime, int totalCountTests, int countPassed, int countFailed, int countSkipped, String sessionKey) {
        return DbUtil.executeInsert(SqlBuilder.insertReport(numberReport, stand, testSuite, launchDuraction, startTime, totalCountTests, countPassed, countFailed, countSkipped, sessionKey));
    }

    /**
     * Возвращает последний номер репорта из базы
     */
    public static int getLastReport() {
        ArrayList<String> result = DbUtil.executeSelect(SqlBuilder.getLastReport());
        if(result.get(0).equals("null")) {
            logError("Не найден максимальный номер отчета");
        }
        return Integer.parseInt(result.get(0));
    }

    /**
     * Добавляет тест в базу
     */
    public static void insertTest(int numberReport, String namePackageSuite, String nameTestEng, String nameTestRus, int status, String bug, String sessionKey) {
        DbUtil.executeInsert(SqlBuilder.insertTest(numberReport, namePackageSuite, nameTestEng, nameTestRus, status, bug, sessionKey));
    }
}
