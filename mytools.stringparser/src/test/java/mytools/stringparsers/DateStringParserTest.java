package mytools.stringparsers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import mytools.stringparser.StringParsers;

public class DateStringParserTest {

    @Test
    public void parseDate() {
        Date d = getDate();
        String str = d.toString();
        assertEquals(d, StringParsers.Defaults.DATE_PARSER.parse(str));
        assertEquals(d, StringParsers.parseDate(str));
        assertEquals(d, StringParsers.get(Date.class).parse(str));
    }

    private static Date getDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

}
