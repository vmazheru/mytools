package mytools.function;

@FunctionalInterface
public interface SupplierWithException<R, E extends Exception> {
    R get() throws E;
}
