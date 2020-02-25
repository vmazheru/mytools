package mytools.function;

@FunctionalInterface
public interface BiFunctionWithException<T, U, R, E extends Exception> {
    R apply(T t, U u) throws E;
}
