package mytools.util.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import mytools.util.date.impl.DateConversions;

public interface Dates {
    
    public static DateFormat DATE_TO_STRING_FORMAT =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    
    public static LocalDateTime localDateTime(long epochSeconds) {
        return DateConversions.localDateTime(epochSeconds);
    }

    public static LocalDateTime localDateTime(Date date) {
        return DateConversions.localDateTime(date);
    }

    public static LocalDate localDate(Date date) {
        return DateConversions.localDate(date);
    }

    public static Date date(LocalDateTime dateTime) {
        return DateConversions.date(dateTime);
    }
    
    public static Date date(LocalDate date) {
        return DateConversions.date(date);
    }

}
