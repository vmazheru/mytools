package mytools.util.string;

import mytools.util.string.impl.Capitalizations;
import mytools.util.string.impl.CaseConversions;
import mytools.util.string.impl.Trimmer;

public interface Strings {

    ///////////////////// Case Conversions //////////////////

    static String snakeToCamel(String s) {
        return CaseConversions.snakeToCamel(s);
    }

    static String camelToSnake(String s) {
        return CaseConversions.camelToSnake(s);
    }

    static String dashedToCamel(String s) {
        return CaseConversions.dashedToCamel(s);
    }

    static String camelToDashed(String s) {
        return CaseConversions.camelToDashed(s);
    }

    static String dottedToCamel(String s) {
        return CaseConversions.dottedToCamel(s);
    }

    static String camelToDotted(String s) {
        return CaseConversions.camelToDotted(s);
    }

    static String spacedToCamel(String s) {
        return CaseConversions.spacedToCamel(s);
    }

    static String camelToSpaced(String s) {
        return CaseConversions.camelToSpaced(s);
    }


    ///////////////////// Capitalization //////////////////

    static String capitalize(CharSequence s) {
        return Capitalizations.capitalize(s);
    }

    static String unCapitalize(CharSequence s) {
        return Capitalizations.unCapitalize(s);
    }


    ///////////////////// Trim ///////////////////////////

    static String trimToNull(String s) {
        return Trimmer.trimToNull(s);
    }
}
