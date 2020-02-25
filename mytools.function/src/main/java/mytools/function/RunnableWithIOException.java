package mytools.function;

import java.io.IOException;

@FunctionalInterface
public interface RunnableWithIOException <E extends IOException> {
    void run() throws E;
}
