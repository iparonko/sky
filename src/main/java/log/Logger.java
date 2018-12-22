package log;

public class Logger {
    public static void logError(String message) {
        System.out.println("=============================================");
        System.out.println("[ОШИБКА] " + message);
        System.out.println("=============================================");
    }

    public static void logSuccess(String message) {
        System.out.println("=============================================");
        System.out.println("[ОК] " + message);
        System.out.println("=============================================");
    }

    public static void log(String message) {
        System.out.println(message);
    }
}
