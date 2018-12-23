package report;

import log.Logger;
import test.Test;
import util.StringUtil;

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
            report.printInfoAboutReport();
        } else {
            Logger.logError("Отчет не прошел валидацию!\n" +
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
    private void parsePassedTests(Report report) {
        int expectedCountPassedTest = report.getCountPassed();
        String reportWork = report.getSourcePage();
        for (int i = 0; i < expectedCountPassedTest; i++) {
            try {
                String fullInfoAboutTest = StringUtil.findMatchByRegexp(reportWork, "<tr><td><img width=\"25\"(.*)success.png(.*)");
                String nameSuite = getNameSuiteByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestEng = getNameTestEngByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestRus = getNameTestRusByFullInfoAboutTest(fullInfoAboutTest);
                Test passedTest = new Test(report.getNumberReport(), nameSuite, nameTestEng, nameTestRus, Test.StatusTest.PASSED);
                report.addTestInTestsArray(passedTest);
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
    private void parseFailedTests(Report report) {
        int expectedCountFailedTest = report.getCountFailed();
        String reportWork = report.getSourcePage();
        for (int i = 0; i < expectedCountFailedTest; i++) {
            try {
                String fullInfoAboutTest = StringUtil.findMatchByRegexp(reportWork, "<tr><td><img width=\"25\"(.*)fail.png\"></td><td bgcolor=#FF3300(.*)");
                String nameSuite = getNameSuiteByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestEng = getNameTestEngByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestRus = getNameTestRusByFullInfoAboutTest(fullInfoAboutTest);
                Test failedTest = new Test(report.getNumberReport(), nameSuite, nameTestEng, nameTestRus, Test.StatusTest.FAILED);
                report.addTestInTestsArray(failedTest);
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
    private void parseSkippedTests(Report report) {
        int expectedCountSkippedTest = report.getCountSkipped();
        String reportWork = report.getSourcePage();
        for (int i = 0; i < expectedCountSkippedTest; i++) {
            try {
                String fullInfoAboutTest = StringUtil.findMatchByRegexp(reportWork, "<tr><td><img width=\"25\"(.*)fail.png\"></td><td bgcolor=#C0C0C0(.*)");
                String nameSuite = getNameSuiteByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestEng = getNameTestEngByFullInfoAboutTest(fullInfoAboutTest);
                String nameTestRus = getNameTestRusByFullInfoAboutTest(fullInfoAboutTest);
                Test skippedTest = new Test(report.getNumberReport(), nameSuite, nameTestEng, nameTestRus, Test.StatusTest.SKIPPED);
                report.addTestInTestsArray(skippedTest);
                fullInfoAboutTest = convertRegularExpressionCharacters(fullInfoAboutTest);
                reportWork = reportWork.replaceAll(fullInfoAboutTest, "");
            } catch (Exception e) {
                Logger.logError("Пропущенный тест не найден! А должен быть!");
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
        report.setStartTime(StringUtil
                .findMatchByRegexp(report.getSourcePage(), "<h4>Время запуска:(.*)")
                .replaceAll("<h4>Время запуска: ", "")
                .replaceAll(" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Длительность:(.*)", ""));
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
