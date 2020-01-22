package mytools.util.date.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public final class DateConversions {
    
    private DateConversions() {}

    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();
    
    public static LocalDateTime localDateTime(long epochSeconds) {
        Instant i = Instant.ofEpochSecond(epochSeconds, 0);
        ZonedDateTime utcZoned = ZonedDateTime.ofInstant(i, SYSTEM_ZONE);
        return utcZoned.toLocalDateTime();
    }

    public static LocalDateTime localDateTime(Date date) {
        return date.toInstant().atZone(SYSTEM_ZONE).toLocalDateTime();
    }

    public static LocalDate localDate(Date date) {
        return localDateTime(date).toLocalDate();
    }

    public static Date date(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(SYSTEM_ZONE).toInstant());
    }
    
    public static Date date(LocalDate date) {
        return date(date.atStartOfDay());
    }
}
