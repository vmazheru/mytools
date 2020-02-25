package mytools.function;

import java.io.IOException;

@FunctionalInterface
public interface BiFunctionWithIOException<T, U, R, E extends IOException> {
    R apply(T t, U u) throws E;
}
