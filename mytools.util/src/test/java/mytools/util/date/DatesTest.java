package mytools.util.date;

import static org.junit.jupiter.api.Assertions.*;

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
        Instant i = LocalDate.now().atStartOfDay().toInstant(OffsetDateTime.now().getOffset());
        Clock clock = Clock.fixed(i, ZoneId.systemDefault());
                
        // Derive different time object from that instant
        LocalDateTime localDateTime = LocalDateTime.now(clock);
        LocalDate localDate = LocalDate.now(clock);
        Date date = Date.from(i);
        long epochSeconds = date.getTime() / 1000;
        
        // Verify that Dates class methods convert them to each other correctly
        assertEquals(date, Dates.fromLocalDateTime(localDateTime));
        assertEquals(date, Dates.fromLocalDate(localDate));
        assertEquals(localDate, Dates.toLocalDate(date));
        assertEquals(localDateTime, Dates.toLocalDateTime(date));
        assertEquals(localDateTime, Dates.toLocalDateTime(epochSeconds));
    }

}
