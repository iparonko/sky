package report;

import test.Test;
import util.StringUtil;

import static log.LoggerInfo.logError;
import static log.LoggerInfo.logException;

public class ParserReport {
    ParserReport(Report report) {
        parseReport(report);
    }

    public void parseReport(Report report) {
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
        assertValidReport(report);
    }

    /**
     * Проверка валидации отчета
     */
    private void assertValidReport(Report report) {
        int totalCountTests = report.getCountPassed() + report.getCountFailed() + report.getCountSkipped();
        if(report.getValidStand() == true && totalCountTests == report.getTotalCountTests() && report.getValidNumberReport() == true) {
            report.logInfoAboutReport();
        } else {
            logError("Отчет не прошел валидацию!\n" +
                    "Валидация стенда: [" + report.getValidStand() + "]\n" +
                    "Валидация количества тестов: В отчете тестов [" + report.getTotalCountTests() + "]. Распарсено тестов [" + totalCountTests + "]\n" +
                    "Валидация номера отчета: [" + report.getValidNumberReport() + "]");
        }
    }

    /**
     * Возвращает наименование класса, в котором находится тест
     *
     * @param fullInfoAboutTest
     */
    private String getNamePackageSuiteByFullInfoAboutTest(String fullInfoAboutTest) {
        return fullInfoAboutTest
                .replaceAll("(.*)logs_html\\/", "")
                .replaceAll("_(.*)", "");
    }

    private String getBug(String fullInfoAboutTest) {
        String bug = StringUtil.findMatchByRegexp(fullInfoAboutTest, "myjetbrains\\.com\\/youtrack\\/issue\\/.*?\"");
        if(bug != null) {
            bug = bug.replaceAll("myjetbrains.com/youtrack/issue/", "")
                    .replaceAll("\"", "");
            String numberBug = StringUtil.findMatchByRegexp(bug, "\\d{1,}");
            bug = bug.replaceAll("\\d{1,}", "");
            bug += "-";
            bug += numberBug;
        } else {
            bug = "0";
        }
        return bug;
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
    private void parsePassedTests(Report report) {
        String regexpTest = "<tr><td><img width=\"25\"(.*)success.png(.*)";
        parseTestsByStatus(report, regexpTest, Test.StatusTest.PASSED);
    }

    /**
     * Парс всех упавших тестов и запись в массив
     */
    private void parseFailedTests(Report report) {
        String regexpTest = "<tr><td><img width=\"25\"(.*)fail.png\"></td><td bgcolor=#FF3300(.*)";
        parseTestsByStatus(report, regexpTest, Test.StatusTest.FAILED);
    }

    /**
     * Парс всех пропущенных тестов и запись в массив
     */
    private void parseSkippedTests(Report report) {
        String regexpTest = "<tr><td><img width=\"25\"(.*)fail.png\"></td><td bgcolor=#C0C0C0(.*)";
        parseTestsByStatus(report, regexpTest, Test.StatusTest.SKIPPED);
    }

    /**
     * Парс тестов с определенным статусом
     *
     * @param report     - номер отчета
     * @param regexpTest - регулярка, где находится тест
     * @param statusTest - статус теста
     */
    private void parseTestsByStatus(Report report, String regexpTest, int statusTest) {
        int expectedCountTest = -1;
        if(statusTest == Test.StatusTest.FAILED) {
            expectedCountTest = report.getCountFailed();
        } else if(statusTest == Test.StatusTest.PASSED) {
            expectedCountTest = report.getCountPassed();
        } else if(statusTest == Test.StatusTest.SKIPPED) {
            expectedCountTest = report.getCountSkipped();
        }
        String reportWork = report.getSourcePage()
                .replaceAll("\\+", "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replaceAll("-", "");
        for (int i = 0; i < expectedCountTest; i++) {
            try {
                String fullInfoAboutTest = StringUtil.findMatchByRegexp(reportWork, regexpTest);
                String namePackageSuite = getNamePackageSuiteByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestEng = getNameTestEngByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestRus = getNameTestRusByFullInfoAboutTest(fullInfoAboutTest);
                String bug = getBug(fullInfoAboutTest);
                Test failedTest = new Test(report.getNumberReport(), namePackageSuite, nameTestEng, nameTestRus, statusTest, bug);
                report.addTestInTestsArray(failedTest);
                fullInfoAboutTest = convertRegularExpressionCharacters(fullInfoAboutTest);
                reportWork = reportWork.replaceAll(fullInfoAboutTest, "");
            } catch (Exception e) {
                logException("Пропущенный тест со статусом [" + statusTest + "]", e);
            }
        }
    }

    /**
     * Парс номера отчета
     */
    private void parseNumberReport(Report report) {
        int numberReport = Integer.parseInt(StringUtil
                .findMatchByRegexp(report.getSourcePage(), "testing/(.*)/artifact/diagram.png")
                .replaceAll("(.*)testing\\/", "")
                .replaceAll("/artifact(.*)", ""));
        report.setNumberReport(numberReport);
    }

    /**
     * Парс наименования тестового плана
     */
    private void parseTestSuite(Report report) {
        report.setTestSuite(StringUtil
                .findMatchByRegexp(report.getSourcePage(), "<h4>Тестовый набор:(.*)")
                .replaceAll("<h4>Тестовый набор: ", "")
                .replaceAll("</h4>", ""));
    }

    /**
     * Парс стенда
     */
    private void parseStand(Report report) {
        String stand = StringUtil
                .findMatchByRegexp(report.getSourcePage(), "<h4>Стенд:(.*)")
                .replaceAll("<h4>Стенд: ", "")
                .replaceAll("</h4>", "");
        report.setStand(stand);
    }

    /**
     * Парс времени запуска прогона
     */
    private void parseStartTime(Report report) {
        String fullInfoStartTime = StringUtil
                .findMatchByRegexp(report.getSourcePage(), "<h4>Время запуска:(.*)")
                .replaceAll("<h4>Время запуска: ", "")
                .replaceAll(" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Длительность:(.*)", "");
        String year = StringUtil.findMatchByRegexp(fullInfoStartTime, "\\d{4}");
        String month = StringUtil.findMatchByRegexp(fullInfoStartTime, "\\.\\d{1,2}\\.")
                .replaceAll("\\.", "");
        String day = StringUtil.findMatchByRegexp(fullInfoStartTime, "^\\d{1,2}");
        String time = StringUtil.findMatchByRegexp(fullInfoStartTime, " (.*)")
                .replaceAll(" ", "")
                .replaceAll("\\.", ":");
        String finalFullInfoStartTime = year + "-" + month + "-" + day + " " + time;
        report.setStartTime(finalFullInfoStartTime);
    }

    /**
     * Парс длительности прогона
     */
    private void parseLaunchDuration(Report report) {
        String launchDurationFullInfo = StringUtil
                .findMatchByRegexp(report.getSourcePage(), "Длительность:(.*)")
                .replaceAll("Длительность: ", "")
                .replaceAll("</h4>", "");
        int hour = 0;
        int minute = 0;
        int second = 0;

        try {
            hour = Integer.valueOf(StringUtil.findMatchByRegexp(launchDurationFullInfo, "\\d{1,2} ча").replaceAll(" ча", "")) * 60 * 60;
        } catch (Exception e) {

        }

        try {
            minute = Integer.valueOf(StringUtil.findMatchByRegexp(launchDurationFullInfo, "\\d{1,2} мин.").replaceAll(" мин.", "")) * 60;
        } catch (Exception e) {

        }

        try {
            second = Integer.valueOf(StringUtil.findMatchByRegexp(launchDurationFullInfo, "\\d{1,2} сек.").replaceAll(" сек.", ""));
        } catch (Exception e) {

        }

        int totalTime = hour + minute + second;
        report.setLaunchDuration(totalTime);
    }

    /**
     * Парс общего количества тестов
     */
    private void parseCountTests(Report report) {
        report.setTotalCountTests(Integer.parseInt(StringUtil
                .findMatchByRegexp(report.getSourcePage(), "<h4>Всего тестов:(.*)")
                .replaceAll("<h4>Всего тестов: ", "")
                .replaceAll(" &nbsp; <font color=green(.*)", "")));
    }

    /**
     * Парс количества успешно пройденных тестов
     */
    private void parseCountPassed(Report report) {
        report.setCountPassed(Integer.parseInt(StringUtil
                .findMatchByRegexp(report.getSourcePage(), "<h4>Всего тестов:(.*)")
                .replaceAll("(.*)Успешно:</font> ", "")
                .replaceAll(" &nbsp; <font color=red(.*)", "")));
    }

    /**
     * Парс количества упавших тестов
     */
    private void parseCountFailed(Report report) {
        report.setCountFailed(Integer.parseInt(StringUtil
                .findMatchByRegexp(report.getSourcePage(), "<h4>Всего тестов:(.*)")
                .replaceAll("(.*)Ошибки:</font> ", "")
                .replaceAll(" &nbsp; <font color=gray(.*)", "")));
    }

    /**
     * Парс количества пропущенных тестов
     */
    private void parseCountSkipped(Report report) {
        report.setCountSkipped(Integer.parseInt(StringUtil
                .findMatchByRegexp(report.getSourcePage(), "<h4>Всего тестов:(.*)")
                .replaceAll("(.*)Пропущено:</font> ", "")
                .replaceAll("</h4>", "")));
    }
}
