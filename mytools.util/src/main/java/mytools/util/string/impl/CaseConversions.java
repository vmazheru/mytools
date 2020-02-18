package mytools.util.string.impl;

import java.util.function.Consumer;
import java.util.function.IntPredicate;

public final class CaseConversions {

    private CaseConversions() { }

    public static String snakeToCamel(String s) {
        return delimitedToCamel(s, ch -> ch == '_', false);
    }

    public static String camelToSnake(String s) {
        return camelToDelimited(s, '_');
    }

    public static String dashedToCamel(String s) {
        return delimitedToCamel(s, ch -> ch == '-', false);
    }

    public static String camelToDashed(String s) {
        return camelToDelimited(s, '-');
    }

    public static String dottedToCamel(String s) {
        return delimitedToCamel(s, ch -> ch == '.', false);
    }

    public static String camelToDotted(String s) {
        return camelToDelimited(s, '.');
    }

    public static String spacedToCamel(String s) {
        return delimitedToCamel(s, ch -> Character.isWhitespace(ch), true);
    }

    public static String camelToSpaced(String s) {
        return camelToDelimited(s, ' ');
    }

    public static String pascalToCamel(String s) {
        return Capitalizations.unCapitalize(s);
    }

    private static String delimitedToCamel(
            String s, IntPredicate delimiterTest, boolean forceLowercase) {
        return checkIfEmptyAndProcess(s, sb -> {
            boolean lastWasDelimiter = false;
            for (char ch : s.toString().toCharArray()) {
                if (delimiterTest.test(ch)) {
                    lastWasDelimiter = true;
                } else {
                    if (lastWasDelimiter) {
                        sb.append(Character.toUpperCase(ch));
                        lastWasDelimiter = false;
                    } else {
                        sb.append(forceLowercase ?
                                Character.toLowerCase(ch) : ch);
                    }
                }
            }
        });
    }

    private static String camelToDelimited(String s, char delimiter) {
        return checkIfEmptyAndProcess(s, sb -> {
            for (char ch : s.toString().toCharArray()) {
                if (Character.isUpperCase(ch)) {
                    sb.append(delimiter);
                    sb.append(Character.toLowerCase(ch));
                } else {
                    sb.append(ch);
                }
            }
        });
    }

    private static String checkIfEmptyAndProcess(
            String s, Consumer<StringBuilder> f) {
        if (s == null) return null;
        if (s.length() == 0) return "";

        StringBuilder sb = new StringBuilder();
        f.accept(sb);
        return sb.toString();
    }

}
