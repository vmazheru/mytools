package mytools.function;

@FunctionalInterface
public interface BiConsumerWithException<T, U, E extends Exception> {
    void accept(T t, U u) throws E;
}
