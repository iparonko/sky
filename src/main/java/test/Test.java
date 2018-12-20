package test;

public class Test {
    int numberReport; //номер отчета
    String nameSuite; //имя пакета, где находится тест
    String nameTestEng; //имя теста Eng
    String nameTestRus; //имя теста Rus
    int status; //состояние: 0-упал, 1-прошел, 2-скипнут

    public static class StatusTest {
        public static final Integer FAILED = 0; //упал
        public static final Integer PASSED = 1; //прошел
        public static final Integer SKIPPED = 2; //пропущен
    }

    public Test(int numberReport, String nameSuite, String nameTestEng, String nameTestRus, int status) {
        this.numberReport = numberReport;
        this.nameSuite = nameSuite;
        this.nameTestEng = nameTestEng;
        this.nameTestRus = nameTestRus;
        this.status = status;
    }
}
