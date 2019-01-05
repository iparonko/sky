package db;

import report.Report;
import test.Test;

import java.util.ArrayList;

import static log.LoggerInfo.logError;

public class SqlClient {
    /**
     * Добавляет отчет в базу
     */
    public static boolean insertReport(int numberReport, String stand, String testSuite, int launchDuraction, String startTime, int totalCountTests, int countPassed, int countFailed, int countSkipped, String sessionKey) {
        return DbUtil.executeInsert(SqlBuilder.insertReport(numberReport, stand, testSuite, launchDuraction, startTime, totalCountTests, countPassed, countFailed, countSkipped, sessionKey), 1);
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
     * Добавляет тесты в базу
     */
    public static boolean insertAllTests(Report report, String sessionKey) {
        ArrayList<Test> tests = report.getTestsArray();
        int countTest = report.getTotalCountTests();
        String sql = "INSERT INTO Test (" +
                "NumberReport, " +
                "NamePackageSuite, " +
                "NameTestEng, " +
                "NameTestRus, " +
                "Status, " +
                "Bug, " +
                "SessionKey " +
                ") " +
                "VALUES ";
        for (int i = 0; i < countTest; i++) {
            Test test = tests.get(i);

            sql += "\n('" + test.getNumberReport() + "', " +
                    "'" + test.getNamePackageSuite() + "', " +
                    "'" + test.getNameTestEng() + "', " +
                    "'" + test.getNameTestRus() + "', " +
                    "'" + test.getStatus() + "', " +
                    "'" + test.getBug() + "', " +
                    "'" + sessionKey + "')";

            if(i < countTest - 1) {
                sql += ", ";
            }
        }
        sql += ";";
        return DbUtil.executeInsert(sql, countTest);
    }

    public static boolean insertSessionKey(String sessionKey) {
        return DbUtil.executeInsert(SqlBuilder.insertSessionKey(sessionKey), 1);
    }

    public static boolean updateSessionKeySetIsDone(String sessionKey) {
        return DbUtil.executeUpdate(SqlBuilder.updateSessionKeySetIsDone(sessionKey), 1);
    }
}
