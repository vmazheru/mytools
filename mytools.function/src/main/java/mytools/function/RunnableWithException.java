package mytools.function;

@FunctionalInterface
public interface RunnableWithException<E extends Exception> {
    void run() throws E;
}
