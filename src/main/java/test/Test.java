package test;

public class Test {
    String numberReport; //номер отчета
    String nameSuite; //имя пакета, где находится тест
    String nameTest; //имя теста
    int status; //состояние: 0-упал, 1-прошел, 2-скипнут

    public static class StatusTest {
        public static final Integer FAILED = 0; //упал
        public static final Integer PASSED = 1; //прошел
        public static final Integer SKIPPED = 2; //пропущен
    }

    public Test(String numberReport, String nameSuite, String nameTest, int status) {
        this.numberReport = numberReport;
        this.nameSuite = nameSuite;
        this.nameTest = nameTest;
        this.status = status;
    }
}
