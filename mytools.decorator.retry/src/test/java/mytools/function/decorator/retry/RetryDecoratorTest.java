package mytools.function.decorator.retry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import mytools.function.RunnableWithException;
import mytools.function.object.Counter;

public class RetryDecoratorTest {

    private final TestLinearRetryPolicy p = new TestLinearRetryPolicy(3, 5);
    private final Runnable runnableWithRuntimeException = () -> {
        throw new RuntimeException("foo");
    };

    @Test
    public void run3TimesOnError() {
        verifyNumberOfRetries(new RetryDecorator<>(p, null, null, null));
    }

    @Test
    public void executionTimeDictatedByRetryPolicy() {
        RetryDecorator<?, ?, ?, ?> d =
                new RetryDecorator<>(p, null, null, null);
        verifyExecutionTime(d);
    }

    @Test
    public void retryOnlyOnSpecifiedExceptionTypes() {
        RetryDecorator<?, ?, ?, Exception> d = new RetryDecorator<>(
                p, Arrays.asList(IOException.class, NullPointerException.class),
                null, null);

        @SuppressWarnings("unchecked")
        Class<? extends Exception>[] exceptionTypes =
                new Class[] {
                        IOException.class, NullPointerException.class
                };

        verifyRetryingOnSpecificExceptions(d, exceptionTypes);
    }

    private void verifyExecutionTime(RetryDecorator<?, ?, ?, ?> d) {
        long start = System.currentTimeMillis();
        assertThrows(RuntimeException.class,
                () -> d.decorate(runnableWithRuntimeException).run());
        long timeSpent = System.currentTimeMillis() - start;
        long expectedMinimalRunningTime = p.getSleepTime() *
                (p.getNumRetries() - 1);
        assertTrue(timeSpent >= expectedMinimalRunningTime);
    }

    private void verifyNumberOfRetries(RetryDecorator<?, ?, ?, ?> d) {
        Counter c = new Counter();
        try {
            d.decorate((Runnable) () -> {
                c.increment();
                throw new RuntimeException();
            }).run();
        } catch (@SuppressWarnings("unused") Exception e) { }
        assertEquals(p.getNumRetries(), c.get());
    }

    private void verifyRetryingOnSpecificExceptions(
            RetryDecorator<?, ?, ?, Exception> d,
            Class<? extends Exception>[] exceptionTypes) {

        // Verify that retries are executed on the specified exception types
        for (Class<? extends Exception> exceptionType : exceptionTypes) {
            Counter c = new Counter();
            p.reset();
            try {
                RunnableWithException<Exception> r = () -> {
                    c.increment();
                    throwException(exceptionType);
                };
                d.decorate(r).run();
            } catch (@SuppressWarnings("unused") Exception e) { }
            assertEquals(p.getNumRetries(), c.get());
        }

        // Verify that retries are not executed on a different exception type
        Counter c = new Counter();
        p.reset();

        try {
            RunnableWithException<Exception> r = () -> {
                c.increment();
                throwException(IllegalArgumentException.class);
            };
            d.decorate(r).run();
        } catch (@SuppressWarnings("unused") Exception e) { }

        final int expectedRuns = 1;
        assertEquals(expectedRuns, c.get());
    }

    private static void throwException(Class<? extends Exception> klass)
        throws Exception {
        try {
            throw klass.getConstructor().newInstance();
        } catch (
                @SuppressWarnings("unused")
                InvocationTargetException | IllegalAccessException |
                NoSuchMethodException | InstantiationException e) { }
    }

}

// Same as LinearRetryPolicy, but with reset() function
final class TestLinearRetryPolicy implements RetryPolicy {

    private LinearRetryPolicy p;

    TestLinearRetryPolicy(int numRetries, long sleepTime) {
        this.p = new LinearRetryPolicy(numRetries, sleepTime);
    }

    @Override
    public long nextRetryIn() {
        return p.nextRetryIn();
    }

    int getNumRetries() {
        return p.getNumRetries();
    }

    long getSleepTime() {
        return p.getSleepTime();
    }

    void reset() {
        p = new LinearRetryPolicy(p.getNumRetries(), p.getSleepTime());
    }

}
