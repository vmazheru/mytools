package mytools.util.string;

import mytools.util.string.impl.CaseConversions;
import mytools.util.string.impl.Capitalizations;

public interface Strings {

    ///////////////////// Case Conversions //////////////////
    
    public static String snakeToCamel(String s) {
        return CaseConversions.snakeToCamel(s);
    }
    
    public static String camelToSnake(String s) {
        return CaseConversions.camelToSnake(s); 
    }
    
    public static String dashedToCamel(String s) {
        return CaseConversions.dashedToCamel(s);
    }
    
    public static String camelToDashed(String s) {
        return CaseConversions.camelToDashed(s);
    }
    
    public static String dottedToCamel(String s) {
        return CaseConversions.dottedToCamel(s);
    }
    
    public static String camelToDotted(String s) {
        return CaseConversions.camelToDotted(s);
    }
    
    public static String spacedToCamel(String s) {
        return CaseConversions.spacedToCamel(s);
    }
    
    public static String camelToSpaced(String s) {
        return CaseConversions.camelToSpaced(s);
    }
    
    
    ///////////////////// Capitalization //////////////////
    
    public static String capitalize(CharSequence s) {
        return Capitalizations.capitalize(s);
    }
    
    public static String unCapitalize(CharSequence s) {
        return Capitalizations.unCapitalize(s);
    }
}
