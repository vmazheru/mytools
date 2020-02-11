package mytools.function;

@FunctionalInterface
public interface ConsumerWithException<T> {
    void accept(T t) throws Exception;
}
