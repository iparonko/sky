package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String findMatchByRegexp(String src, String regexp) {
        String result = null;
        Pattern p = Pattern.compile(regexp);
        for (Matcher m = p.matcher(src); m.find(); result = m.group(0)) {
        }
        return result;
    }
}
