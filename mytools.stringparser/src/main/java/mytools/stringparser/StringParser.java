package mytools.stringparser;

import java.util.function.Function;

/**
 * A function which takes a {@code String} and returns an object of some
 * different type.
 *
 * @param <T> the type of object returned by this function
 */
@FunctionalInterface
public interface StringParser<T> {

    T parse(String s);

    /**
     * Represent this parser as a {@code Function} which
     * returns an {@code Object}.
     * This helps using a string parser in a context
     * where the return type is irrelevant (for example,
     * in code which uses reflection).
     */
    default Function<String, Object> toFunction() {
        return s -> parse(s);
    }

}
