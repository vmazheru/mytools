package mytools.stringparsers.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateStringParser {
    
    private DateStringParser() {}
    
    private static DateFormat DATE_TO_STRING_FORMAT = 
            new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    
    /**
     * Parse date object from format which is produced by the {@code Date.toString()} method.
     * Note, that with this format milliseconds are always set to zero.
     */
    public static Date parse(String s) {
        try {
            return DATE_TO_STRING_FORMAT.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
