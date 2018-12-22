package util;

import java.util.UUID;

public class DataGeneratorUtil {
    public static synchronized String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
