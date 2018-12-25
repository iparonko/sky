package log;

import org.apache.log4j.Logger;

public class LoggerInfo {
    private static Logger log = Logger.getLogger(Logger.class.getName());

    public static void logException(String message, Exception e) {
        log.fatal("[Исключение] " + message, e);
    }

    public static void logSuccess(String message) {
        log("[Выполнено] " + message);
    }

    public static void log(String message) {
        log.info(message);
    }

    public static void logError(String message) {
        log.error("[Ошибка] " + message);
    }
}
