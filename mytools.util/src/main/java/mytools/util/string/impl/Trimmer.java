package mytools.util.string.impl;

public final class Trimmer {
    
    private Trimmer() {}
    
    public static String trimToNull(String s) {
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

}
