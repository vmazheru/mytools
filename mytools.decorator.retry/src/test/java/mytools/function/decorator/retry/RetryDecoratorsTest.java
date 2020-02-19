package mytools.function.decorator.retry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import mytools.function.object.Counter;

public class RetryDecoratorsTest {

    private final LinearRetryPolicy p = new LinearRetryPolicy(3, 10);
    private final Runnable runnableWithRuntimeException = () -> {
        throw new RuntimeException("foo");
    };
    private final Runnable runnableWithNullPointerException = () -> {
        throw new NullPointerException("foo");
    };
    private final Runnable runnableWithFileNotFoundException = () -> {
        throw new NullPointerException("foo");
    };

    @Test
    public void run3TimesOnError() {
        Counter c = new Counter();
        RetryDecorator<?,?,?> d = new RetryDecorator<>(
                p, null, null, () -> c.increment());
        assertThrows(RuntimeException.class,
                () -> d.decorate(runnableWithRuntimeException).run());
        assertEquals(p.getNumRetries(), c.get());
    }

    @Test
    public void executionTimeDictatedByRetryPolicy() {
        RetryDecorator<?,?,?> d = new RetryDecorator<>(p, null, null, null);
        long start = System.currentTimeMillis();
        assertThrows(RuntimeException.class,
                () -> d.decorate(runnableWithRuntimeException).run());
        long timeSpent = System.currentTimeMillis() - start;
        long expectedMinimalRunningTime = p.getSleepTime() * p.getNumRetries();
        assertTrue(timeSpent >= expectedMinimalRunningTime);
    }

    @Test
    public void retryOnlyOnSpecifiedExceptionTypes() {
        Counter c = new Counter();
        RetryDecorator<?,?,?> d = new RetryDecorator<>(
                p, Arrays.asList(IOException.class, NullPointerException.class),
                null, () -> c.increment());
        assertThrows(RuntimeException.class,
                () -> d.decorate(runnableWithRuntimeException).run());
        assertEquals(0, c.get());
    }



}
