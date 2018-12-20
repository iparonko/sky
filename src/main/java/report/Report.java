package report;


import test.Test;
import util.StringUtil;

import java.util.ArrayList;

public class Report {
    private String numberReport; //номер отчета
    private String stand; //наименование стенда
    private String testSuite; //наименование тестового набора
    private String startTime; //время запуска прогона
    private String launchDuration; //длительность прогона
    private int totalNumberTests; //всего тестов
    private int countPassed; //количество прошедших тестов
    private int countFailed; //количество упавших тестов
    private int countSkipped; //количество пропущенных тестов
    private static ArrayList<Test> tests = new ArrayList<Test>(); //список всех тестов

    public Report(String report) {
        parseReport(report);
    }

    public void parseReport(String report) {
        parseNumberReport(report);
        parseStand(report);
        parseTestSuite(report);
        parseStartTime(report);
        parseLaunchDuration(report);
        parseNumberTests(report);
        parseCountPassed(report);
        parseCountFailed(report);
        parseCountSkipped(report);
        parsePassedTests(report);
        parseFailedTests(report);
        parseSkippedTests(report);
        printInfoAboutReport();
    }

    /**
     * Парс всех успешно пройденных тестов и запись в массив
     */
    private void parsePassedTests(String report) {
        int expectedCountPassedTest = getCountPassed();
        String reportWork = report;
        for (int i = 0; i < expectedCountPassedTest; i++) {
            try {
                String fullInfoAboutTest = StringUtil.findMatchByRegexp(reportWork, "<tr><td><img width=\"25\"(.*)success.png(.*)");
                String nameSuite = StringUtil
                        .findMatchByRegexp(fullInfoAboutTest, "logs_html/(.*)target=\"_blank\">PASSED")
                        .replaceAll("logs_html\\/", "")
                        .replaceAll("_(.*)", "");
                String nameTest = StringUtil
                        .findMatchByRegexp(fullInfoAboutTest, "logs_html/(.*)target=\"")
                        .replaceAll("logs_html\\/(.*)_", "")
                        .replaceAll(".html\" target=\"", "");
                Test passedTest = new Test(this.numberReport, nameSuite, nameTest, Test.StatusTest.PASSED);
                tests.add(passedTest);
                fullInfoAboutTest = fullInfoAboutTest
                        .replaceAll("\\.", "\\\\.")
                        .replaceAll("\\/", "\\\\/")
                        .replaceAll("\\[", "\\\\[")
                        .replaceAll("\\]", "\\\\]");

                reportWork = reportWork.replaceAll(fullInfoAboutTest, "");
            } catch (Exception e) {
                System.out.println("Успешно пройденный тест не найден! А должен быть!");
            }
        }
    }

    /**
     * Парс всех упавших тестов и запись в массив
     */
    private void parseFailedTests(String report) {
        int expectedCountFailedTest = getCountFailed();
        String reportWork = report;
        for (int i = 0; i < expectedCountFailedTest; i++) {
            try {
                String fullInfoAboutTest = StringUtil.findMatchByRegexp(reportWork, "<tr><td><img width=\"25\"(.*)fail.png\"></td><td bgcolor=#FF3300(.*)");
                String nameSuite = StringUtil
                        .findMatchByRegexp(fullInfoAboutTest, "logs_html/(.*)target=\"_blank\">FAILED")
                        .replaceAll("logs_html\\/", "")
                        .replaceAll("_(.*)", "");
                String nameTest = StringUtil
                        .findMatchByRegexp(fullInfoAboutTest, "logs_html/(.*)target=\"")
                        .replaceAll("logs_html\\/(.*)_", "")
                        .replaceAll(".html\" target=\"", "");
                Test failedTest = new Test(this.numberReport, nameSuite, nameTest, Test.StatusTest.FAILED);
                tests.add(failedTest);
                fullInfoAboutTest = fullInfoAboutTest
                        .replaceAll("\\.", "\\\\.")
                        .replaceAll("\\/", "\\\\/")
                        .replaceAll("\\[", "\\\\[")
                        .replaceAll("\\]", "\\\\]");

                reportWork = reportWork.replaceAll(fullInfoAboutTest, "");
            } catch (Exception e) {
                System.out.println("Упавший тест не найден! А должен быть!");
            }
        }
    }

    /**
     * Парс всех пропущенных тестов и запись в массив
     */
    private void parseSkippedTests(String report) {
        int expectedCountSkippedTest = getCountSkipped();
        String reportWork = report;
        for (int i = 0; i < expectedCountSkippedTest; i++) {
            try {
                String fullInfoAboutTest = StringUtil.findMatchByRegexp(reportWork, "<tr><td><img width=\"25\"(.*)fail.png\"></td><td bgcolor=#C0C0C0(.*)");
                String nameSuite = StringUtil
                        .findMatchByRegexp(fullInfoAboutTest, "logs_html/(.*)target=\"_blank\">SKIPPED")
                        .replaceAll("logs_html\\/", "")
                        .replaceAll("_(.*)", "");
                String nameTest = StringUtil
                        .findMatchByRegexp(fullInfoAboutTest, "logs_html/(.*)target=\"")
                        .replaceAll("logs_html\\/(.*)_", "")
                        .replaceAll(".html\" target=\"", "");
                Test skippedTest = new Test(this.numberReport, nameSuite, nameTest, Test.StatusTest.SKIPPED);
                tests.add(skippedTest);
                fullInfoAboutTest = fullInfoAboutTest
                        .replaceAll("\\.", "\\\\.")
                        .replaceAll("\\/", "\\\\/")
                        .replaceAll("\\[", "\\\\[")
                        .replaceAll("\\]", "\\\\]");

                reportWork = reportWork.replaceAll(fullInfoAboutTest, "");
            } catch (Exception e) {
                System.out.println("Пропущенный тест не найден! А должен быть!");
            }
        }
    }

    /**
     * Возвращает количество успешно пройденных тестов
     */
    public int getCountPassed() {
        return this.countPassed;
    }

    /**
     * Возвращает количество упавших тестов
     */
    public int getCountFailed() {
        return this.countFailed;
    }

    /**
     * Возвращает количество пропущенных тестов
     */
    public int getCountSkipped() {
        return this.countSkipped;
    }

    /**
     * Парс номера отчета
     */
    private void parseNumberReport(String report) {
        this.numberReport = StringUtil
                .findMatchByRegexp(report, "youdo_android_testing/(.*)/artifact/diagram.png")
                .replaceAll("(.*)youdo_android_testing\\/", "")
                .replaceAll("/artifact(.*)", "");
    }

    /**
     * Парс наименования тестового плана
     */
    private void parseTestSuite(String report) {
        this.testSuite = StringUtil
                .findMatchByRegexp(report, "<h4>Тестовый набор:(.*)")
                .replaceAll("<h4>Тестовый набор: ", "")
                .replaceAll("</h4>", "");
    }

    /**
     * Парс стенда
     */
    private void parseStand(String report) {
        this.stand = StringUtil
                .findMatchByRegexp(report, "<h4>Стенд:(.*)")
                .replaceAll("<h4>Стенд: ", "")
                .replaceAll("</h4>", "");
    }

    /**
     * Парс времени запуска прогона
     */
    private void parseStartTime(String report) {
        this.startTime = StringUtil
                .findMatchByRegexp(report, "<h4>Время запуска:(.*)")
                .replaceAll("<h4>Время запуска: ", "")
                .replaceAll(" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Длительность:(.*)", "");
    }

    /**
     * Парс длительности прогона
     */
    private void parseLaunchDuration(String report) {
        this.launchDuration = StringUtil
                .findMatchByRegexp(report, "Длительность:(.*)")
                .replaceAll("Длительность: ", "")
                .replaceAll("</h4>", "");
    }

    /**
     * Парс общего количества тестов
     */
    private void parseNumberTests(String report) {
        this.totalNumberTests = Integer.parseInt(StringUtil
                .findMatchByRegexp(report, "<h4>Всего тестов:(.*)")
                .replaceAll("<h4>Всего тестов: ", "")
                .replaceAll(" &nbsp; <font color=green(.*)", ""));
    }

    /**
     * Парс количества успешно пройденных тестов
     */
    private void parseCountPassed(String report) {
        this.countPassed = Integer.parseInt(StringUtil
                .findMatchByRegexp(report, "<h4>Всего тестов:(.*)")
                .replaceAll("(.*)Успешно:</font> ", "")
                .replaceAll(" &nbsp; <font color=red(.*)", ""));
    }

    /**
     * Парс количества упавших тестов
     */
    private void parseCountFailed(String report) {
        this.countFailed = Integer.parseInt(StringUtil
                .findMatchByRegexp(report, "<h4>Всего тестов:(.*)")
                .replaceAll("(.*)Ошибки:</font> ", "")
                .replaceAll(" &nbsp; <font color=gray(.*)", ""));
    }

    /**
     * Парс количества пропущенных тестов
     */
    private void parseCountSkipped(String report) {
        this.countSkipped = Integer.parseInt(StringUtil
                .findMatchByRegexp(report, "<h4>Всего тестов:(.*)")
                .replaceAll("(.*)Пропущено:</font> ", "")
                .replaceAll("</h4>", ""));
    }

    public void printInfoAboutReport() {
        System.out.println("Номер прогона: [" + numberReport + "]\n" +
                "Наименование стенда: [" + stand + "]\n" +
                "Наименование тестового набора: [" + testSuite + "]\n" +
                "Время начало прогона: [" + startTime + "]\n" +
                "Длительность прогона: [" + launchDuration + "]\n" +
                "Всего тестов: [" + totalNumberTests + "]\n" +
                "Успешные: [" + countPassed + "]\n" +
                "Упавшие: [" + countFailed + "]\n" +
                "Пропущенные: [" + countSkipped + "]\n" +
                "Успешно распарсилось [" + tests.size() + "]");
    }
}
