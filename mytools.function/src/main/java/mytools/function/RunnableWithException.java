package mytools.function;

@FunctionalInterface
public interface RunnableWithException {
    void run() throws Exception;
}
