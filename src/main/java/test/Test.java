package test;

public class Test {
    int numberReport; //номер отчета
    String nameSuite; //имя пакета, где находится тест
    String nameTest; //имя теста
    int status; //состояние: 0-упал, 1-прошел, 2-скипнут

    Test(int numberReport, String nameSuite, String nameTest, int status) {
        this.numberReport = numberReport;
        this.nameSuite = nameSuite;
        this.nameTest = nameTest;
        this.status = status;
    }
}
