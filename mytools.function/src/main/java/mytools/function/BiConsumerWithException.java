package mytools.function;

@FunctionalInterface
public interface BiConsumerWithException<T, U> {
    void accept(T t, U u) throws Exception;
}
