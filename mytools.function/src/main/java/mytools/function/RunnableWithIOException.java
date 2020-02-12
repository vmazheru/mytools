package mytools.function;

import java.io.IOException;

@FunctionalInterface
public interface RunnableWithIOException {
    void run() throws IOException;
}