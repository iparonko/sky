package log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerInfo {
    private static Logger log = Logger.getLogger(Logger.class.getName());

    public static void logException(String message, Exception e) {
        log.log(Level.SEVERE,
                "\n====================================================\n" +
                        "[Ошибка - исключение] " + message + "" +
                        "\n====================================================", e);
    }

    public static void logSuccess(String message) {
        log.log(Level.FINE,
                "\n====================================================\n" +
                        "[Выполнено] " + message +
                        "\n====================================================");
    }

    public static void logError(String message) {
        log.log(Level.SEVERE,
                "\n====================================================\n" +
                        "[Ошибка] " + message +
                        "\n====================================================");
    }

    public static void log(String message) {
        log.log(Level.FINE,
                "\n====================================================\n" +
                        message +
                        "\n====================================================");
    }
}
