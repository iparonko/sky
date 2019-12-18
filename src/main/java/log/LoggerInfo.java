package log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import settings.Accounts;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class LoggerInfo {
    private static Logger log = getLogger();

    private static Logger getLogger() {
        Logger logger = Logger.getLogger(Logger.class.getName());
        Properties prop = new Properties();
        try {
            prop.load(new FileReader("/root/sky/target/classes/log4j.properties"));
            //prop.load(new FileReader("/home/aparonko/IdeaProjects/sky/src/main/resources/log4j.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prop.put("log4j.appender.db.user", "iparonko_notes");
        prop.put("log4j.appender.db.password", Accounts.PASSWORD_SQL);
        PropertyConfigurator.configure(prop);
        return logger;
    }

    public static void logException(String message, Exception e) {
        message = message.replaceAll("'", "%27");
        log.fatal("[Исключение] " + message, e);
    }

    public static void logSuccess(String message) {
        log("[Выполнено] " + message);
    }

    public static void log(String message) {
        message = message.replaceAll("'", "%27");
        log.info(message);
    }

    public static void logError(String message) {
        message = message.replaceAll("'", "%27");
        log.error("[Ошибка] " + message);
    }
}
