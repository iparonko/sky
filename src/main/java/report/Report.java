package report;


import log.Logger;
import test.Test;
import util.StringUtil;

import java.util.ArrayList;

public class Report {
    private int numberReport; //номер отчета

    private String stand; //наименование стенда

    private String testSuite; //наименование тестового набора
    private String startTime; //время запуска прогона
    private String launchDuration; //длительность прогона
    private int totalCountTests; //всего тестов
    private int countPassed; //количество прошедших тестов
    private int countFailed; //количество упавших тестов
    private int countSkipped; //количество пропущенных тестов

    private boolean validStand = false;
    private boolean validNumberReport = false;

    public static ArrayList<Test> tests = new ArrayList<Test>(); //список всех тестов

    public Report(String report) {
        parseReport(report);
    }

    public void parseReport(String report) {
        parseNumberReport(report);
        parseStand(report);
        parseTestSuite(report);
        parseStartTime(report);
        parseLaunchDuration(report);
        parseCountTests(report);
        parseCountPassed(report);
        parseCountFailed(report);
        parseCountSkipped(report);
        parsePassedTests(report);
        parseFailedTests(report);
        parseSkippedTests(report);
        assertValidReport();
    }

    /**
     * Проверка валидации отчета
     */
    private void assertValidReport() {
        int totalCountTests = countPassed + countFailed + countSkipped;
        if(validStand == true && totalCountTests == this.totalCountTests && validNumberReport == true) {
            printInfoAboutReport();
        } else {
            Logger.logError("Отчет не прошел валидацию!\n" +
                    "Валидация стенда: [" + this.validStand + "]\n" +
                    "Валидация количества тестов: В отчете тестов [" + this.totalCountTests + "]. Распарсено тестов [" + totalCountTests + "]\n" +
                    "Валидация номера отчета: [" + this.validNumberReport + "]");
        }
    }

    /**
     * Возвращает наименование класса, в котором находится тест
     *
     * @param fullInfoAboutTest
     */
    private String getNameSuiteByFullInfoAboutTest(String fullInfoAboutTest) {
        return fullInfoAboutTest
                .replaceAll("(.*)logs_html\\/", "")
                .replaceAll("_(.*)", "");
    }

    /**
     * Возвращает имя теста на английском языке
     *
     * @param fullInfoAboutTest
     */
    private String getNameTestEngByFullInfoAboutTest(String fullInfoAboutTest) {
        return "test" + fullInfoAboutTest
                .replaceAll("(.*)logs_html\\/(.*)_test", "")
                .replaceAll(".html\" target=\"(.*)", "");
    }

    /**
     * Возвращает имя теста на русском языке
     *
     * @param fullInfoAboutTest
     */
    private String getNameTestRusByFullInfoAboutTest(String fullInfoAboutTest) {
        return fullInfoAboutTest
                .replaceAll("(.*)</a></td><td>&nbsp;", "")
                .replaceAll("&nbsp;</td><td> (.*)", "");
    }

    /**
     * Экранирует знаки в регулярном выражении
     *
     * @param text
     */
    private String convertRegularExpressionCharacters(String text) {
        return text
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\/", "\\\\/")
                .replaceAll("\\[", "\\\\[")
                .replaceAll("\\]", "\\\\]");
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
                String nameSuite = getNameSuiteByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestEng = getNameTestEngByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestRus = getNameTestRusByFullInfoAboutTest(fullInfoAboutTest);
                Test passedTest = new Test(this.numberReport, nameSuite, nameTestEng, nameTestRus, Test.StatusTest.PASSED);
                tests.add(passedTest);
                fullInfoAboutTest = convertRegularExpressionCharacters(fullInfoAboutTest);
                reportWork = reportWork.replaceAll(fullInfoAboutTest, "");
            } catch (Exception e) {
                Logger.logError("Пропущенный тест не найден! А должен быть!");
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
                String nameSuite = getNameSuiteByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestEng = getNameTestEngByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestRus = getNameTestRusByFullInfoAboutTest(fullInfoAboutTest);
                Test failedTest = new Test(this.numberReport, nameSuite, nameTestEng, nameTestRus, Test.StatusTest.FAILED);
                tests.add(failedTest);
                fullInfoAboutTest = convertRegularExpressionCharacters(fullInfoAboutTest);
                reportWork = reportWork.replaceAll(fullInfoAboutTest, "");
            } catch (Exception e) {
                Logger.logError("Пропущенный тест не найден! А должен быть!");
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
                String nameSuite = getNameSuiteByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestEng = getNameTestEngByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestRus = getNameTestRusByFullInfoAboutTest(fullInfoAboutTest);
                Test skippedTest = new Test(this.numberReport, nameSuite, nameTestEng, nameTestRus, Test.StatusTest.SKIPPED);
                tests.add(skippedTest);
                fullInfoAboutTest = convertRegularExpressionCharacters(fullInfoAboutTest);
                reportWork = reportWork.replaceAll(fullInfoAboutTest, "");
            } catch (Exception e) {
                Logger.logError("Пропущенный тест не найден! А должен быть!");
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
        int numberReport = Integer.parseInt(StringUtil
                .findMatchByRegexp(report, "testing/(.*)/artifact/diagram.png")
                .replaceAll("(.*)testing\\/", "")
                .replaceAll("/artifact(.*)", ""));
        setNumberReport(numberReport);
    }

    /**
     * Сохраняет номер отчета
     */
    private void setNumberReport(int numberReport) {
        if(!((1 < numberReport) && (numberReport < 9999))) {
            Logger.logError("Получен ошибочный номер отчета: [" + numberReport + "]");
        } else {
            validNumberReport = true;
            this.numberReport = numberReport;
        }
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
        String stand = StringUtil
                .findMatchByRegexp(report, "<h4>Стенд:(.*)")
                .replaceAll("<h4>Стенд: ", "")
                .replaceAll("</h4>", "");
        setStand(stand);
    }

    /**
     * Сохраняет имя стенда
     */
    private void setStand(String stand) {
        if(!(stand.contains("test") && (stand.length() < 7))) {
            Logger.logError("Получен ошибочное имя стенда: [" + stand + "]");
        } else {
            validStand = true;
            this.stand = stand;
        }
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
    private void parseCountTests(String report) {
        this.totalCountTests = Integer.parseInt(StringUtil
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
                "Всего тестов: [" + totalCountTests + "]\n" +
                "Успешные: [" + countPassed + "]\n" +
                "Упавшие: [" + countFailed + "]\n" +
                "Пропущенные: [" + countSkipped + "]\n" +
                "Успешно распарсилось [" + tests.size() + "]");
    }
}
