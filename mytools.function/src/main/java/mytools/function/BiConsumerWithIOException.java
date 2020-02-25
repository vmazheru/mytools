package mytools.function;

import java.io.IOException;

@FunctionalInterface
public interface BiConsumerWithIOException<T, U, E extends IOException> {
    void accept(T t, U u) throws E;
}
