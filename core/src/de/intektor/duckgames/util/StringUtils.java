package de.intektor.duckgames.util;

import com.google.common.base.Splitter;

import java.util.List;

/**
 * @author Intektor
 */
public class StringUtils {

    public static String repeat(String s, int t) {
        String repetition = s;
        for (int i = 1; i < t; i++) {
            repetition += s;
        }
        return repetition;
    }

    public static String concatWithSplit(String splitter, Object... parts) {
        return String.format("%s" + repeat(splitter + "%s", parts.length - 1), parts);
    }

    public static List<String> splitConcated(String splitOn, String code) {
        Splitter splitter = Splitter.on(splitOn);
        return splitter.splitToList(code);
    }
}
