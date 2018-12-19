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
    ArrayList<Test> tests; //список всех тестов

    public Report(String report){
        parseReport(report);
    }

    public void parseReport(String report) {
        this.numberReport = StringUtil
                .findMatchByRegexp(report, "testing\\/\\d{1,}\\/artifact")
                .replaceAll("testing\\/", "")
                .replaceAll("\\/artifact", "");

        this.stand = StringUtil
                .findMatchByRegexp(report, "<h4>Стенд:(.*)")
                .replaceAll("<h4>Стенд: ", "")
                .replaceAll("</h4>", "");

        this.testSuite = StringUtil
                .findMatchByRegexp(report, "<h4>Тестовый набор:(.*)")
                .replaceAll("<h4>Тестовый набор: ", "")
                .replaceAll("</h4>", "");

        this.startTime = StringUtil
                .findMatchByRegexp(report, "<h4>Время запуска:(.*)")
                .replaceAll("<h4>Время запуска: ", "")
                .replaceAll(" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Длительность:(.*)", "");

        this.launchDuration = StringUtil
                .findMatchByRegexp(report, "Длительность:(.*)")
                .replaceAll("Длительность: ", "")
                .replaceAll("</h4>", "");

        this.totalNumberTests = Integer.parseInt(StringUtil
                .findMatchByRegexp(report, "<h4>Всего тестов:(.*)")
                .replaceAll("<h4>Всего тестов: ", "")
                .replaceAll(" &nbsp; <font color=green(.*)", ""));

        this.countPassed = Integer.parseInt(StringUtil
                .findMatchByRegexp(report, "<h4>Всего тестов:(.*)")
                .replaceAll("(.*)Успешно:</font> ", "")
                .replaceAll(" &nbsp; <font color=red(.*)", ""));

        this.countFailed = Integer.parseInt(StringUtil
                .findMatchByRegexp(report, "<h4>Всего тестов:(.*)")
                .replaceAll("(.*)Ошибки:</font> ", "")
                .replaceAll(" &nbsp; <font color=gray(.*)", ""));

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
                "Пропущенные: [" + countSkipped + "]");
    }
}
