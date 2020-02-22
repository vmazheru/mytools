package mytools.stringparser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mytools.stringparser.impl.DateStringParser;

/**
 * This class contains functions which convert (parse) strings to objects.
 * This class also provides a way to globally register parsers for custom types.
 */
public final class StringParsers {

    private StringParsers() { }

    /**
     * Default string parsers. They are always available when used directly,
     * but may be replaced in the registration map.
     */
    public static final class Defaults {

        private Defaults() { }

        /**
         * Parse a {@code String} to a {@code Byte}.
         * This parser delegates to {@link Byte#valueOf(String)} method.
         */
        public static final StringParser<Byte> BYTE_PARSER = Byte::valueOf;

        /**
         * Parse a {@code String} to a {@code Short}.
         * This parser delegates to {@link Short#valueOf(String)} method.
         */
        public static final StringParser<Short> SHORT_PARSER = Short::valueOf;

        /**
         * Parse a {@code String} to an {@code Integer}.
         * This parser delegates to {@link Integer#valueOf(String)} method.
         */
        public static final StringParser<Integer> INT_PARSER = Integer::valueOf;

        /**
         * Parse a {@code String} to a {@code Long}.
         * This parser delegates to {@link Long#valueOf(String)} method.
         */
        public static final StringParser<Long> LONG_PARSER = Long::valueOf;

        /**
         * Parse a {@code String} to a {@code Float}.
         * This parser delegates to {@link Float#valueOf(String)} method.
         */
        public static final StringParser<Float> FLOAT_PARSER = Float::valueOf;

        /**
         * Parse a {@code String} to a {@code Double}.
         * This parser delegates to {@link Double#valueOf(String)} method.
         */
        public static final StringParser<Double> DOUBLE_PARSER =
                Double::valueOf;

        /**
         * Parse a {@code String} to a {@code Character}.
         * It returns the first character of the string.
         */
        public static final StringParser<Character> CHAR_PARSER =
                s -> Character.valueOf(s.charAt(0));

        /**
         * Parse a {@code String} to a {@code Boolean}.
         * This parser delegates to {@link Boolean#valueOf(String)} method.
         */
        public static final StringParser<Boolean> BOOLEAN_PARSER =
                Boolean::valueOf;

        /**
         * Parse a {@code String} to a {@code BigInteger}
         * This parser delegates to {@link java.math.BigInteger} constructor.
         */
        public static final StringParser<BigInteger> BIG_INTEGER_PARSER =
                BigInteger::new;

        /**
         * Parse a {@code String} to a {@code BigDecimal}
         * This parser delegates to {@link java.math.BigDecimal} constructor.
         */
        public static final StringParser<BigDecimal> BIG_DECIMAL_PARSER =
                BigDecimal::new;

        /**
         * Parse a {@code String} to a {@code LocalDateTime} object.
         * This parser delegates to
         * {@link java.time.LocalDateTime#parse(CharSequence)} method.
         */
        public static final StringParser<LocalDateTime> LOCAL_DATE_TIME_PARSER =
                LocalDateTime::parse;

        /**
         * Parse a {@code String} to a {@code LocalDate} object.
         * This parser delegates to
         * {@link java.time.LocalDate#parse(CharSequence)} method.
         */
        public static final StringParser<LocalDate> LOCAL_DATE_PARSER =
                LocalDate::parse;

        /**
         * Parse a {@code String} to a {@code LocalTime} object.
         * This parser delegates to
         * {@link java.time.LocalTime#parse(CharSequence)} method.
         */
        public static final StringParser<LocalTime> LOCAL_TIME_PARSER =
                LocalTime::parse;

        /**
         * Parse a {@code String} to a {@code ZonedDateTime} object.
         * This parser delegates to
         * {@link java.time.ZonedDateTime#parse(CharSequence)} method.
         */
        public static final StringParser<ZonedDateTime> ZONED_DATE_TIME_PARSER =
                ZonedDateTime::parse;

        /**
         * Parse a {@code String} to a {@code Date} object.
         * This parser accepts strings in the format used by
         * the {@code Date#toString()} method.
         * Note, that this parser does not read milliseconds since the
         * {@code Date#toString()} method does not dump them.
         *
         * @see cl.core.util.Dates#DATE_TO_STRING_FORMAT for
         * the format description.
         */
        public static final StringParser<Date> DATE_PARSER =
                DateStringParser::parse;

        /**
         * Parse a {@code String} as is. This parser just returns the input.
         */
        public static final StringParser<String> STRING_PARSER = s -> s;
    }

    /**
     * Register a new parser for the given type.  Note, that the new parser
     * will replace the existing parser for that type if any globally, that is
     * for the entire application.
     */
    public static <T> void register(Class<T> klass, StringParser<T> parser) {
        synchronized (PARSERS) {
            PARSERS.put(klass, parser);
        }
    }

    /**
     * Remove (unregister) a parser for the given type.
     */
    public static <T> void unregister(Class<T> klass) {
        synchronized (PARSERS) {
            PARSERS.remove(klass);
        }
    }

    /**
     * Get a parser for the given type.
     */
    public static <T> StringParser<T> get(Class<T> klass) {
        synchronized (PARSERS) {
            @SuppressWarnings("unchecked")
            StringParser<T> p = (StringParser<T>) PARSERS.get(klass);
            return p;
        }
    }

    public static Byte parseByte(String s) {
        return get(Byte.class).parse(s);
    }

    public static Short parseShort(String s) {
        return get(Short.class).parse(s);
    }

    public static Integer parseInteger(String s) {
        return get(Integer.class).parse(s);
    }

    public static Long parseLong(String s) {
        return get(Long.class).parse(s);
    }

    public static Float parseFloat(String s) {
        return get(Float.class).parse(s);
    }

    public static Double parseDouble(String s) {
        return get(Double.class).parse(s);
    }

    public static Character parseCharacter(String s) {
        return get(Character.class).parse(s);
    }

    public static Boolean parseBoolean(String s) {
        return get(Boolean.class).parse(s);
    }

    public static BigInteger parseBigInteger(String s) {
        return get(BigInteger.class).parse(s);
    }

    public static BigDecimal parseBigDecimal(String s) {
        return get(BigDecimal.class).parse(s);
    }

    public static LocalDateTime parseLocalDateTime(String s) {
        return get(LocalDateTime.class).parse(s);
    }

    public static LocalDate parseLocalDate(String s) {
        return get(LocalDate.class).parse(s);
    }

    public static LocalTime parseLocalTime(String s) {
        return get(LocalTime.class).parse(s);
    }

    public static ZonedDateTime parseZonedDateTime(String s) {
        return get(ZonedDateTime.class).parse(s);
    }

    public static Date parseDate(String s) {
        return get(Date.class).parse(s);
    }

    public static String parseString(String s) {
        return get(String.class).parse(s);
    }

    private static final Map<Class<?>, StringParser<?>> PARSERS =
            new HashMap<>();
    static {
        PARSERS.put(Byte.class, Defaults.BYTE_PARSER);
        PARSERS.put(Short.class, Defaults.SHORT_PARSER);
        PARSERS.put(Integer.class, Defaults.INT_PARSER);
        PARSERS.put(Long.class, Defaults.LONG_PARSER);
        PARSERS.put(Float.class, Defaults.FLOAT_PARSER);
        PARSERS.put(Double.class, Defaults.DOUBLE_PARSER);
        PARSERS.put(Character.class, Defaults.CHAR_PARSER);
        PARSERS.put(Boolean.class, Defaults.BOOLEAN_PARSER);
        PARSERS.put(BigInteger.class, Defaults.BIG_INTEGER_PARSER);
        PARSERS.put(BigDecimal.class, Defaults.BIG_DECIMAL_PARSER);
        PARSERS.put(LocalDateTime.class, Defaults.LOCAL_DATE_TIME_PARSER);
        PARSERS.put(LocalDate.class, Defaults.LOCAL_DATE_PARSER);
        PARSERS.put(LocalTime.class, Defaults.LOCAL_TIME_PARSER);
        PARSERS.put(ZonedDateTime.class, Defaults.ZONED_DATE_TIME_PARSER);
        PARSERS.put(Date.class, Defaults.DATE_PARSER);
        PARSERS.put(String.class, Defaults.STRING_PARSER);
    }

}
