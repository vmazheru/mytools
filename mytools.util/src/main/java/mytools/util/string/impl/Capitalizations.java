package mytools.util.string.impl;

public class Capitalizations {
    
    private Capitalizations() {}

    public static String capitalize(CharSequence s) {
        return changeFirstLetterCase(s, true);
    }
    
    public static String unCapitalize(CharSequence s) {
        return changeFirstLetterCase(s, false);
    }
    
    private static String changeFirstLetterCase(CharSequence s, boolean upper) {
        if (s == null) return null;
        if (s.length() == 0) return "";
        
        StringBuilder sb = new StringBuilder();
        if (upper) {
            sb.append(Character.toUpperCase(s.charAt(0)));
        } else {
            sb.append(Character.toLowerCase(s.charAt(0)));
        }
        sb.append(s.subSequence(1, s.length()));
        return sb.toString();
    }

}
