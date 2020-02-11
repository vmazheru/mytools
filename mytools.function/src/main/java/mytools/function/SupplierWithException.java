package mytools.function;

@FunctionalInterface
public interface SupplierWithException<R> {
    R get() throws Exception;
}
