package mytools.function;

import java.io.IOException;

@FunctionalInterface
public interface SupplierWithIOException<R> {
    R get() throws IOException;
}
