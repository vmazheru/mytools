package mytools.function;

import java.io.IOException;

@FunctionalInterface
public interface FunctionWithIOException<T, R, E extends IOException> {
    R apply(T t) throws E;
}
