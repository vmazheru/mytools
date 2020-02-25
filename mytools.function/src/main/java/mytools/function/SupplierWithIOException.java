package mytools.function;

import java.io.IOException;

@FunctionalInterface
public interface SupplierWithIOException<R, E extends IOException> {
    R get() throws E;
}
