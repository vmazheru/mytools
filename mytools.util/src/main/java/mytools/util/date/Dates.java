package mytools.util.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import mytools.util.date.impl.DateConversions;

public interface Dates {

    DateFormat DATE_TO_STRING_FORMAT =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

    static LocalDateTime localDateTime(long epochSeconds) {
        return DateConversions.localDateTime(epochSeconds);
    }

    static LocalDateTime localDateTime(Date date) {
        return DateConversions.localDateTime(date);
    }

    static LocalDate localDate(Date date) {
        return DateConversions.localDate(date);
    }

    static Date date(LocalDateTime dateTime) {
        return DateConversions.date(dateTime);
    }

    static Date date(LocalDate date) {
        return DateConversions.date(date);
    }

}
