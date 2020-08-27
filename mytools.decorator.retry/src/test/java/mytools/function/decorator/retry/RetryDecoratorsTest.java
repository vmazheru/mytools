package mytools.function.decorator.retry;

import static mytools.function.decorator.retry.RetryDecorators.retried;
import static mytools.function.decorator.retry.RetryDecorators.retriedWithException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import mytools.function.BiConsumerWithException;
import mytools.function.BiFunctionWithException;
import mytools.function.ConsumerWithException;
import mytools.function.FunctionWithException;
import mytools.function.RunnableWithException;
import mytools.function.SupplierWithException;
import mytools.function.object.Counter;

public class RetryDecoratorsTest {

    private static class Shaky {
        private final Counter c = new Counter();
        private boolean beforeSleepExecuted;
        private boolean afterSleepExecuted;
        private boolean wasExecuted;

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
            wasExecuted = true;
            return Long.toString(i.longValue() + l.longValue());
        }

        String concat(Integer i, Long l) throws ConcatException {
            if (c.incrementAndGet() < 3) {
                throw new ConcatException();
            }
            c.reset();
            wasExecuted = true;
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

        boolean wasExecuted() {
            boolean itWas = wasExecuted;
            wasExecuted = false;
            return itWas;
        }
    }

    private Supplier<RetryPolicy> threeTimes =
            () -> new LinearRetryPolicy(3, 10);

    @Test
    public void runnable() {
        Shaky shaky = new Shaky();
        Runnable bare = () -> shaky.sum(3, 5L);

        retried(3, 10, bare).run();
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(), bare).run();
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(), Arrays.asList(), bare).run();
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare).run());

        retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare).run();
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(), ex -> shaky.beforeSleep(ex), bare).run();
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(), ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).run();
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare).run());
        assertFalse(shaky.wasBeforeSleepExecuted());

        retried(threeTimes.get(), Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare).run();
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

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
        assertTrue(shaky.wasExecuted());
    }

    @Test
    public void runnableWithException() throws Shaky.Exception {
        Shaky shaky = new Shaky();
        RunnableWithException<Shaky.Exception> bare = () -> shaky.concat(3, 5L);

        retriedWithException(3, 10, bare).run();
        assertTrue(shaky.wasExecuted());

        retriedWithException(threeTimes.get(), bare).run();
        assertTrue(shaky.wasExecuted());

        retriedWithException(threeTimes.get(), Arrays.asList(), bare).run();
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare).run());

        retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare).run();
        assertTrue(shaky.wasExecuted());

        retriedWithException(
                threeTimes.get(), ex -> shaky.beforeSleep(ex), bare).run();
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        retriedWithException(
                threeTimes.get(), ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).run();
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare).run());
        assertFalse(shaky.wasBeforeSleepExecuted());

        retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare).run();
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

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
        assertTrue(shaky.wasExecuted());
    }

    @Test
    public void supplier() {
        Shaky shaky = new Shaky();
        Supplier<String> bare = () -> shaky.sum(3, 5L);

        assertEquals("8", retried(3, 10, bare).get());
        assertEquals("8", retried(3, 10, bare).get());
        assertEquals("8", retried(threeTimes.get(), bare).get());
        assertEquals("8",
                retried(threeTimes.get(), Arrays.asList(), bare).get());
        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare).get());
        assertEquals("8", retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare).get());

        assertEquals("8", retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).get());
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals("8", retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).get());
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare).get());
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals("8", retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare).get());
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).get());
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        assertEquals("8", retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).get());
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }

    @Test
    public void supplierWithException() throws Shaky.Exception {
        Shaky shaky = new Shaky();
        SupplierWithException<String, Shaky.Exception> bare =
                () -> shaky.concat(3, 5L);

        assertEquals("35", retriedWithException(3, 10, bare).get());
        assertEquals("35", retriedWithException(3, 10, bare).get());
        assertEquals("35", retriedWithException(threeTimes.get(), bare).get());
        assertEquals("35", retriedWithException(
                threeTimes.get(), Arrays.asList(), bare).get());
        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare).get());
        assertEquals("35", retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare).get());

        assertEquals("35", retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).get());
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals("35", retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).get());
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare).get());
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals("35", retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare).get());
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).get());
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        assertEquals("35", retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).get());
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }

    @Test
    public void consumer() {
        Shaky shaky = new Shaky();
        Consumer<Long> bare = arg -> shaky.sum(3, arg);
        Long arg = 5L;

        retried(3, 10, bare).accept(arg);
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(), bare).accept(arg);
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(), Arrays.asList(), bare).accept(arg);
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare).accept(arg));

        retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare).accept(arg);
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).accept(arg);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(), ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).accept(arg);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare).accept(arg));
        assertFalse(shaky.wasBeforeSleepExecuted());

        retried(threeTimes.get(), Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare).accept(arg);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).accept(arg));
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        retried(threeTimes.get(), Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).accept(arg);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());
    }

    @Test
    public void consumerWithException() throws Shaky.Exception {
        Shaky shaky = new Shaky();
        ConsumerWithException<Long, Shaky.Exception> bare =
                arg -> shaky.concat(3, arg);
        Long arg = 5L;

        retriedWithException(3, 10, bare).accept(arg);
        assertTrue(shaky.wasExecuted());

        retriedWithException(threeTimes.get(), bare).accept(arg);
        assertTrue(shaky.wasExecuted());

        retriedWithException(
                threeTimes.get(), Arrays.asList(), bare).accept(arg);
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare)
                .accept(arg));

        retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare).accept(arg);
        assertTrue(shaky.wasExecuted());

        retriedWithException(
                threeTimes.get(), ex -> shaky.beforeSleep(ex), bare)
            .accept(arg);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        retriedWithException(
                threeTimes.get(), ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).accept(arg);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare).accept(arg));
        assertFalse(shaky.wasBeforeSleepExecuted());

        retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare).accept(arg);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).accept(arg));
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).accept(arg);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());
    }

    @Test
    public void biConsumer() {
        Shaky shaky = new Shaky();
        BiConsumer<Integer, Long> bare = (arg1, arg2) -> shaky.sum(arg1, arg2);
        Integer arg1 = 3;
        Long arg2 = 5L;

        retried(3, 10, bare).accept(arg1, arg2);
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(), bare).accept(arg1, arg2);
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(), Arrays.asList(), bare).accept(arg1, arg2);
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare)
                .accept(arg1, arg2));

        retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare)
            .accept(arg1, arg2);
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).accept(arg1, arg2);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        retried(threeTimes.get(), ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).accept(arg1, arg2);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare).accept(arg1, arg2));
        assertFalse(shaky.wasBeforeSleepExecuted());

        retried(threeTimes.get(), Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare).accept(arg1, arg2);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).accept(arg1, arg2));
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        retried(threeTimes.get(), Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).accept(arg1, arg2);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());
    }

    @Test
    public void biConsumerWithException() throws Shaky.Exception {
        Shaky shaky = new Shaky();
        BiConsumerWithException<Integer, Long, Shaky.Exception> bare =
                (arg1, arg2) -> shaky.concat(arg1, arg2);
        Integer arg1 = 3;
        Long arg2 = 5L;

        retriedWithException(3, 10, bare).accept(arg1, arg2);
        assertTrue(shaky.wasExecuted());

        retriedWithException(threeTimes.get(), bare).accept(arg1, arg2);
        assertTrue(shaky.wasExecuted());

        retriedWithException(
                threeTimes.get(), Arrays.asList(), bare).accept(arg1, arg2);
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare)
                .accept(arg1, arg2));

        retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare)
            .accept(arg1, arg2);
        assertTrue(shaky.wasExecuted());

        retriedWithException(
                threeTimes.get(), ex -> shaky.beforeSleep(ex), bare)
            .accept(arg1, arg2);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        retriedWithException(
                threeTimes.get(), ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).accept(arg1, arg2);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare).accept(arg1, arg2));
        assertFalse(shaky.wasBeforeSleepExecuted());

        retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare).accept(arg1, arg2);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).accept(arg1, arg2));
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).accept(arg1, arg2);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());
    }

    @Test
    public void function() {
        Shaky shaky = new Shaky();
        Function<Long, String> bare = arg -> shaky.sum(3, arg);
        Long arg = 5L;

        assertEquals("8", retried(3, 10, bare).apply(arg));
        assertEquals("8", retried(3, 10, bare).apply(arg));
        assertEquals("8", retried(threeTimes.get(), bare).apply(arg));
        assertEquals("8",
                retried(threeTimes.get(), Arrays.asList(), bare).apply(arg));
        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare).apply(arg));
        assertEquals("8", retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare).apply(arg));

        assertEquals("8", retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals("8", retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare).apply(arg));
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals("8", retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).apply(arg));
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        assertEquals("8", retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }

    @Test
    public void functionWithException() throws Shaky.Exception {
        Shaky shaky = new Shaky();
        FunctionWithException<Long, String, Shaky.Exception> bare =
                arg -> shaky.concat(3, arg);
        Long arg = 5L;

        assertEquals("35", retriedWithException(3, 10, bare).apply(arg));
        assertEquals("35", retriedWithException(3, 10, bare).apply(arg));
        assertEquals("35",
                retriedWithException(threeTimes.get(), bare).apply(arg));
        assertEquals("35", retriedWithException(
                threeTimes.get(), Arrays.asList(), bare).apply(arg));
        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare)
                .apply(arg));
        assertEquals("35", retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare).apply(arg));

        assertEquals("35", retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals("35", retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare).apply(arg));
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals("35", retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).apply(arg));
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        assertEquals("35", retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }

    @Test
    public void biFunction() {
        Shaky shaky = new Shaky();
        BiFunction<Integer, Long, String> bare =
                (arg1, arg2) -> shaky.sum(arg1, arg2);
        Integer arg1 = 3;
        Long arg2 = 5L;

        assertEquals("8", retried(3, 10, bare).apply(arg1, arg2));
        assertEquals("8", retried(3, 10, bare).apply(arg1, arg2));
        assertEquals("8", retried(threeTimes.get(), bare).apply(arg1, arg2));
        assertEquals("8",
                retried(threeTimes.get(), Arrays.asList(), bare)
                .apply(arg1, arg2));
        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare)
                .apply(arg1, arg2));
        assertEquals("8", retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare)
                .apply(arg1, arg2));

        assertEquals("8", retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals("8", retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare).apply(arg1, arg2));
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals("8", retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).apply(arg1, arg2));
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        assertEquals("8", retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }

    @Test
    public void biFunctionWithException() throws Shaky.Exception {
        Shaky shaky = new Shaky();
        BiFunctionWithException<Integer, Long, String, Shaky.Exception> bare =
                (arg1, arg2) -> shaky.concat(3, 5L);
        Integer arg1 = 3;
        Long arg2 = 5L;

        assertEquals("35", retriedWithException(3, 10, bare).apply(arg1, arg2));
        assertEquals("35", retriedWithException(3, 10, bare).apply(arg1, arg2));
        assertEquals("35",
                retriedWithException(threeTimes.get(), bare).apply(arg1, arg2));
        assertEquals("35", retriedWithException(
                threeTimes.get(), Arrays.asList(), bare).apply(arg1, arg2));
        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare)
                .apply(arg1, arg2));
        assertEquals("35", retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare)
                .apply(arg1, arg2));

        assertEquals("35", retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals("35", retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare).apply(arg1, arg2));
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals("35", retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).apply(arg1, arg2));
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        assertEquals("35", retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }

}
