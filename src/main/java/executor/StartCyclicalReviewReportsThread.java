package executor;

import log.LoggerInfo;

import static executor.ExecutorMethods.checkAndSaveNewReport;
import static log.LoggerInfo.logException;

public class StartCyclicalReviewReportsThread extends Thread {
    public void run() {
        long seconds = 60;
        LoggerInfo.log("Старт циклической проверки новых отчетов. Проверка будет выполняться каждые [" + seconds + "] секунд");
        while (true) {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                logException("Произошла ошибка при выполнении sleep метода!", e);
            }
            LoggerInfo.log("Выполнение циклической проверки отчетов");
            checkAndSaveNewReport();
        }
    }
}
