package mytools.function;

import java.io.IOException;

@FunctionalInterface
public interface ConsumerWithIOException<T, E extends IOException> {
    void accept(T t) throws E;
}
