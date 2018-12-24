package test;

public class Test {
    private int numberReport; //номер отчета
    private String namePackageSuite; //имя пакета, где находится тест
    private String nameTestEng; //имя теста Eng
    private String nameTestRus; //имя теста Rus
    private int status; //состояние: 0-упал, 1-прошел, 2-скипнут

    public static class StatusTest {
        public static final Integer FAILED = 0; //упал
        public static final Integer PASSED = 1; //прошел
        public static final Integer SKIPPED = 2; //пропущен
    }

    public Test(int numberReport, String namePackageSuite, String nameTestEng, String nameTestRus, int status) {
        this.numberReport = numberReport;
        this.namePackageSuite = namePackageSuite;
        this.nameTestEng = nameTestEng;
        this.nameTestRus = nameTestRus;
        this.status = status;
    }

    /**
     * Возвращает номер отчета
     */
    public int getNumberReport() {
        return numberReport;
    }

    /**
     * Возвращает наименование сьюта
     */
    public String getNamePackageSuite() {
        return namePackageSuite;
    }

    /**
     * Возвращает наименование теста на английском
     */
    public String getNameTestEng() {
        return nameTestEng;
    }

    /**
     * Возвращает наименование теста на русском
     */
    public String getNameTestRus() {
        return nameTestRus;
    }

    /**
     * Возвращает статус теста
     */
    public int getStatus() {
        return status;
    }
}
