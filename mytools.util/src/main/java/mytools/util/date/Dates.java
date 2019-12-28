package mytools.util.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public final class Dates {
    
    public static DateFormat DATE_TO_STRING_FORMAT =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    
    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();
    
    public static long toEpochSeconds(LocalDateTime t) {
        return t.atZone(SYSTEM_ZONE).toEpochSecond();
    }
    
    public static LocalDateTime toLocalDateTime(long epochSeconds) {
        Instant i = Instant.ofEpochSecond(epochSeconds, 0);
        ZonedDateTime utcZoned = ZonedDateTime.ofInstant(i, SYSTEM_ZONE);
        return utcZoned.toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(SYSTEM_ZONE).toLocalDateTime();
    }

    public static LocalDate toLocalDate(Date date) {
        return toLocalDateTime(date).toLocalDate();
    }

    public static Date fromLocalDateTime(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(SYSTEM_ZONE).toInstant());
    }
    
    public static Date fromLocalDate(LocalDate date) {
        return fromLocalDateTime(date.atStartOfDay());
    }


}
