package report;


import test.Test;

import java.util.ArrayList;

import static log.LoggerInfo.logError;
import static log.LoggerInfo.logSuccess;

public class Report {
    private String sourcePage; //исходная страница отчета
    private int numberReport; //номер отчета
    private String stand; //наименование стенда
    private String testSuite; //наименование тестового набора
    private String startTime; //время запуска прогона
    private int launchDuration; //длительность прогона (в секундах)
    private int totalCountTests; //всего тестов
    private int countPassed; //количество прошедших тестов
    private int countFailed; //количество упавших тестов
    private int countSkipped; //количество пропущенных тестов

    private boolean validStand = false;
    private boolean validNumberReport = false;

    private ArrayList<Test> tests = new ArrayList<>(); //список всех тестов

    public Report(String report) {
        this.sourcePage = report;
        new ParserReport(this);
    }

    /**
     * Добавляет тест в массив (ссылку на него)
     */
    public void addTestInTestsArray(Test test) {
        tests.add(test);
    }

    /**
     * Возвращает массив с тестами
     */
    public ArrayList<Test> getTestsArray() {
        return tests;
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
            logError("Получен ошибочный номер отчета: [" + numberReport + "]");
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
            logError("Получен ошибочное имя стенда: [" + stand + "]");
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
    public void setLaunchDuration(int launchDuration) {
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

    /**
     * Возвращает наименование стенда
     */
    public String getStand() {
        return this.stand;
    }

    /**
     * Возвращает наименование тестового плана
     */
    public String getTestSuite() {
        return this.testSuite;
    }

    /**
     * Возвращает длительность прогона (в секундах)
     */
    public int getLaunchDuration() {
        return this.launchDuration;
    }

    /**
     * Возвращает время начало прогона
     */
    public String getStartTime() {
        return this.startTime;
    }

    public void logInfoAboutReport() {
        logSuccess("Номер прогона: [" + numberReport + "]\n" +
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
