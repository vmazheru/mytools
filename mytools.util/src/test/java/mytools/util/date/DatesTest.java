package mytools.util.date;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class DatesTest {

    @Test
    public void convertDates() {
        // Get instant for the beginning of the day
        Instant i = LocalDate.now().atStartOfDay()
                .toInstant(OffsetDateTime.now().getOffset());
        Clock clock = Clock.fixed(i, ZoneId.systemDefault());

        // Derive different time objects from that instant
        LocalDateTime localDateTime = LocalDateTime.now(clock);
        LocalDate localDate = LocalDate.now(clock);
        Date date = Date.from(i);
        long epochSeconds = date.getTime() / 1000;

        // Verify that Dates class methods convert them to each other correctly
        assertEquals(date, Dates.date(localDateTime));
        assertEquals(date, Dates.date(localDate));
        assertEquals(localDate, Dates.localDate(date));
        assertEquals(localDateTime, Dates.localDateTime(date));
        assertEquals(localDateTime, Dates.localDateTime(epochSeconds));
    }

}
