package de.intektor.duckgames.util;

/**
 * @author Intektor
 */
public class StringUtils {

    public static String repeat(String s, int t) {
        for (int i = 1; i < t; i++) {
            s += s;
        }
        return s;
    }
}
