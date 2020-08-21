package mytools.function.decorator.retry;

import static mytools.function.decorator.retry.RetryDecorators.retried;
import static mytools.function.decorator.retry.RetryDecorators.retriedWithException;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import mytools.function.RunnableWithException;
import mytools.function.object.Counter;

public class RetryDecoratorsTest {

    private static class Shaky {
        private final Counter c = new Counter();
        private boolean beforeSleepExecuted;
        private boolean afterSleepExecuted;

        @SuppressWarnings("serial")
        static class RuntimeException extends java.lang.RuntimeException {}

        @SuppressWarnings("serial")
        static class Exception extends java.lang.Exception {}

        @SuppressWarnings("serial")
        static class ConcatException extends Exception {}

        @SuppressWarnings("serial")
        static class UnrelatedException extends Exception {}


        String sum(Integer i, Long l) {
            if (c.incrementAndGet() < 3) {
                throw new RuntimeException();
            }
            c.reset();
            return Long.toString(i.longValue() + l.longValue());
        }

        String concat(Integer i, Long l) throws ConcatException {
            if (c.incrementAndGet() < 3) {
                throw new ConcatException();
            }
            c.reset();
            return i.toString() + l.toString();
        }

        boolean wasBeforeSleepExecuted() {
            boolean itWas = beforeSleepExecuted;
            beforeSleepExecuted = false;
            return itWas;
        }

        void beforeSleep(java.lang.RuntimeException e) {
            assertTrue(e instanceof RuntimeException);
            beforeSleepExecuted = true;
        }

        void beforeSleep(Exception e) {
            assertTrue(e instanceof ConcatException);
            beforeSleepExecuted = true;
        }

        boolean wasAfterSleepExecuted() {
            boolean itWas = afterSleepExecuted;
            afterSleepExecuted = false;
            return itWas;
        }

        void afterSleep() {
            afterSleepExecuted = true;
        }
    }

    private Supplier<RetryPolicy> threeTimes =
            () -> new LinearRetryPolicy(3, 10);

    @Test
    public void runnable() {
        Shaky shaky = new Shaky();
        Runnable bare = () -> shaky.sum(3, 5L);

        retried(3, 10, bare).run();
        retried(threeTimes.get(), bare).run();
        retried(threeTimes.get(), Arrays.asList(), bare).run();
        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare).run());
        retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare).run();

        retried(threeTimes.get(), ex -> shaky.beforeSleep(ex), bare).run();
        assertTrue(shaky.wasBeforeSleepExecuted());

        retried(threeTimes.get(), ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).run();
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare).run());
        assertFalse(shaky.wasBeforeSleepExecuted());

        retried(threeTimes.get(), Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare).run();
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).run());
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        retried(threeTimes.get(), Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).run();
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }

    @Test
    public void runnableWithException() throws Shaky.Exception {
        Shaky shaky = new Shaky();
        RunnableWithException<Shaky.Exception> bare = () -> shaky.concat(3, 5L);

        retriedWithException(3, 10, bare).run();
        retriedWithException(threeTimes.get(), bare).run();
        retriedWithException(threeTimes.get(), Arrays.asList(), bare).run();
        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare).run());

        retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare).run();

        retriedWithException(threeTimes.get(), ex -> shaky.beforeSleep(ex), bare).run();
        assertTrue(shaky.wasBeforeSleepExecuted());

        retriedWithException(threeTimes.get(), ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).run();
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare).run());
        assertFalse(shaky.wasBeforeSleepExecuted());

        retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare).run();
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).run());
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).run();
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }




}
