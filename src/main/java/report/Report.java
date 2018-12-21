package report;


import log.Logger;
import test.Test;

import java.util.ArrayList;

public class Report {
    private String sourcePage; //исходная страница отчета
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
        this.sourcePage = report;
        new ParserReport(this);
    }

    /**
     * Возвращает количество успешно пройденных тестов
     */
    public int getCountPassed() {
        return this.countPassed;
    }

    /**
     * Возвращает исходную страницу отчета
     */
    public String getSourcePage() {
        return this.sourcePage;
    }

    /**
     * Возвращает номер отчета
     */
    public int getNumberReport() {
        return this.numberReport;
    }

    /**
     * Возвращает суммарное количество тестов
     */
    public int getTotalCountTests() {
        return this.totalCountTests;
    }

    /**
     * Сохраняет номер отчета
     */
    public void setNumberReport(int numberReport) {
        if(!((1 < numberReport) && (numberReport < 9999))) {
            Logger.logError("Получен ошибочный номер отчета: [" + numberReport + "]");
        } else {
            validNumberReport = true;
            this.numberReport = numberReport;
        }
    }

    /**
     * Сохраняет имя стенда
     */
    public void setStand(String stand) {
        if(!(stand.contains("test") && (stand.length() < 7))) {
            Logger.logError("Получен ошибочное имя стенда: [" + stand + "]");
        } else {
            validStand = true;
            this.stand = stand;
        }
    }

    /**
     * Устанавливает значение количества пройденных тестов
     */
    public void setCountPassed(int countPassed) {
        this.countPassed = countPassed;
    }

    /**
     * Устанавливает значение упавших тестов
     */
    public void setCountFailed(int countFailed) {
        this.countFailed = countFailed;
    }

    /**
     * Устанавливает значение пропущенных тестов
     */
    public void setCountSkipped(int countSkipped) {
        this.countSkipped = countSkipped;
    }

    /**
     * Устанавливает значение всех тестов
     */
    public void setTotalCountTests(int totalCountTests) {
        this.totalCountTests = totalCountTests;
    }

    /**
     * Устанавливает значение времени начала прогона
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Устанавливает значение длительности прогона
     */
    public void setLaunchDuration(String launchDuration) {
        this.launchDuration = launchDuration;
    }

    /**
     * Устанавливает значение наименования тестового набора
     */
    public void setTestSuite(String testSuite) {
        this.testSuite = testSuite;
    }

    /**
     * Возвращает состояние валидации наименования стенда
     */
    public boolean getValidStand() {
        return this.validStand;
    }

    /**
     * Возвращает состояние валидации номера отчета
     */
    public boolean getValidNumberReport() {
        return this.validNumberReport;
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
