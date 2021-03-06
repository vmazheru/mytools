package mytools.function.decorator.retry;

import static mytools.function.decorator.retry.RetryDecorators.retried;
import static mytools.function.decorator.retry.RetryDecorators.retriedWithException;
import static mytools.function.decorator.retry.RetryDecorators.retry;
import static mytools.function.decorator.retry.RetryDecorators.retryWithException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private static final int THREE = 3;
    private static final long TEN = 10L;
    private static final long FIVE = 5L;

    private static class Shaky {

        static final String SUM_RESULT = "8";
        static final String CONCAT_RESULT = "35";

        private final Counter c = new Counter();
        private boolean beforeSleepExecuted;
        private boolean afterSleepExecuted;
        private boolean wasExecuted;

        @SuppressWarnings("serial")
        static class RuntimeException extends java.lang.RuntimeException { }

        @SuppressWarnings("serial")
        static class Exception extends java.lang.Exception { }

        @SuppressWarnings("serial")
        static class ConcatException extends Exception { }

        @SuppressWarnings("serial")
        static class UnrelatedException extends Exception { }


        String sum(Integer i, Long l) {
            if (c.incrementAndGet() < THREE) {
                throw new RuntimeException();
            }
            c.reset();
            wasExecuted = true;
            return Long.toString(i.longValue() + l.longValue());
        }

        String concat(Integer i, Long l) throws ConcatException {
            if (c.incrementAndGet() < THREE) {
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
            () -> new LinearRetryPolicy(THREE, TEN);

    @Test
    public void examples() {
        retry(THREE, TEN, () -> System.out.println());
        retry(THREE, TEN, () -> new java.util.Random().nextInt());

        retried(THREE, TEN, () -> System.out.println()).run();
        retried(THREE, TEN,
                () -> new java.util.Random().nextInt()).get();

        assertThrows(IOException.class, () ->
            retryWithException(THREE, TEN,
                    () -> Files.delete(Path.of("some/path")))
        );

        assertThrows(IOException.class, () ->
            retryWithException(THREE, TEN,
                    () -> {
                        Files.delete(Path.of("some/path"));
                        return true;
                    })
        );

        assertThrows(IOException.class, () ->
            retriedWithException(THREE, TEN,
                    () -> Files.delete(Path.of("some/path"))).run()
        );

        assertThrows(IOException.class, () ->
            retriedWithException(THREE, TEN,
                    () -> {
                        Files.delete(Path.of("some/path"));
                        return true;
                    }).get()
        );
    }

    @Test
    public void runnable() {
        Shaky shaky = new Shaky();
        Runnable bare = () -> shaky.sum(THREE, FIVE);

        retried(THREE, TEN, bare).run();
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
        RunnableWithException<Shaky.Exception> bare =
                () -> shaky.concat(THREE, FIVE);

        retriedWithException(THREE, TEN, bare).run();
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
        Supplier<String> bare = () -> shaky.sum(THREE, FIVE);

        assertEquals(Shaky.SUM_RESULT,
                retried(THREE, TEN, bare).get());
        assertEquals(Shaky.SUM_RESULT,
                retried(THREE, TEN, bare).get());
        assertEquals(Shaky.SUM_RESULT,
                retried(threeTimes.get(), bare).get());
        assertEquals(Shaky.SUM_RESULT,
                retried(threeTimes.get(), Arrays.asList(), bare).get());
        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare).get());
        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare).get());

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).get());
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).get());
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare).get());
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
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

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
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
                () -> shaky.concat(THREE, FIVE);

        assertEquals(Shaky.CONCAT_RESULT,
                retriedWithException(THREE, TEN, bare).get());
        assertEquals(Shaky.CONCAT_RESULT,
                retriedWithException(THREE, TEN, bare).get());
        assertEquals(Shaky.CONCAT_RESULT,
                retriedWithException(threeTimes.get(), bare).get());
        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(
                threeTimes.get(), Arrays.asList(), bare).get());
        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare).get());
        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare).get());

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).get());
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).get());
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare).get());
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
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

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).get());
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }

    @Test
    public void consumer() {
        Shaky shaky = new Shaky();
        Consumer<Long> bare = arg -> shaky.sum(THREE, arg);
        Long arg = FIVE;

        retried(THREE, TEN, bare).accept(arg);
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
                arg -> shaky.concat(THREE, arg);
        Long arg = FIVE;

        retriedWithException(THREE, TEN, bare).accept(arg);
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
        Integer arg1 = THREE;
        Long arg2 = FIVE;

        retried(THREE, TEN, bare).accept(arg1, arg2);
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
        Integer arg1 = THREE;
        Long arg2 = FIVE;

        retriedWithException(THREE, TEN, bare).accept(arg1, arg2);
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
        Function<Long, String> bare = arg -> shaky.sum(THREE, arg);
        Long arg = FIVE;

        assertEquals(Shaky.SUM_RESULT,
                retried(THREE, TEN, bare).apply(arg));
        assertEquals(Shaky.SUM_RESULT,
                retried(THREE, TEN, bare).apply(arg));
        assertEquals(Shaky.SUM_RESULT,
                retried(threeTimes.get(), bare).apply(arg));
        assertEquals(Shaky.SUM_RESULT,
                retried(threeTimes.get(), Arrays.asList(), bare).apply(arg));
        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare).apply(arg));
        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare).apply(arg));

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare).apply(arg));
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
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

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
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
                arg -> shaky.concat(THREE, arg);
        Long arg = FIVE;

        assertEquals(Shaky.CONCAT_RESULT,
                retriedWithException(THREE, TEN, bare).apply(arg));
        assertEquals(Shaky.CONCAT_RESULT,
                retriedWithException(THREE, TEN, bare).apply(arg));
        assertEquals(Shaky.CONCAT_RESULT,
                retriedWithException(threeTimes.get(), bare).apply(arg));
        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(
                threeTimes.get(), Arrays.asList(), bare).apply(arg));
        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare)
                .apply(arg));
        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare).apply(arg));

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare).apply(arg));
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
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

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
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
        Integer arg1 = THREE;
        Long arg2 = FIVE;

        assertEquals(Shaky.SUM_RESULT,
                retried(THREE, TEN, bare).apply(arg1, arg2));
        assertEquals(Shaky.SUM_RESULT,
                retried(THREE, TEN, bare).apply(arg1, arg2));
        assertEquals(Shaky.SUM_RESULT,
                retried(threeTimes.get(), bare).apply(arg1, arg2));
        assertEquals(Shaky.SUM_RESULT,
                retried(threeTimes.get(), Arrays.asList(), bare)
                .apply(arg1, arg2));
        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare)
                .apply(arg1, arg2));
        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare)
                .apply(arg1, arg2));

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare).apply(arg1, arg2));
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
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

        assertEquals(Shaky.SUM_RESULT, retried(threeTimes.get(),
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
                (arg1, arg2) -> shaky.concat(THREE, FIVE);
        Integer arg1 = THREE;
        Long arg2 = FIVE;

        assertEquals(Shaky.CONCAT_RESULT,
                retriedWithException(THREE, TEN, bare)
                .apply(arg1, arg2));
        assertEquals(Shaky.CONCAT_RESULT,
                retriedWithException(THREE, TEN, bare)
                .apply(arg1, arg2));
        assertEquals(Shaky.CONCAT_RESULT,
                retriedWithException(threeTimes.get(), bare).apply(arg1, arg2));
        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(
                threeTimes.get(), Arrays.asList(), bare).apply(arg1, arg2));
        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare)
                .apply(arg1, arg2));
        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare)
                .apply(arg1, arg2));

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare).apply(arg1, arg2));
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
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

        assertEquals(Shaky.CONCAT_RESULT, retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()).apply(arg1, arg2));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }

    @Test
    public void runnableApplied() {
        Shaky shaky = new Shaky();
        Runnable bare = () -> shaky.sum(THREE, FIVE);

        retry(THREE, TEN, bare);
        assertTrue(shaky.wasExecuted());

        retry(threeTimes.get(), bare);
        assertTrue(shaky.wasExecuted());

        retry(threeTimes.get(), Arrays.asList(), bare);
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare).run());

        retry(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare);
        assertTrue(shaky.wasExecuted());

        retry(threeTimes.get(), ex -> shaky.beforeSleep(ex), bare);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        retry(threeTimes.get(), ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep());
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare).run());
        assertFalse(shaky.wasBeforeSleepExecuted());

        retry(threeTimes.get(), Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retried(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()).run());
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        retry(threeTimes.get(), Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep());
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());
    }

    @Test
    public void runnableWithExceptionApplied() throws Shaky.Exception {
        Shaky shaky = new Shaky();
        RunnableWithException<Shaky.Exception> bare =
                () -> shaky.concat(THREE, FIVE);

        retryWithException(THREE, TEN, bare);
        assertTrue(shaky.wasExecuted());

        retryWithException(threeTimes.get(), bare);
        assertTrue(shaky.wasExecuted());

        retryWithException(threeTimes.get(), Arrays.asList(), bare);
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retriedWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare).run());

        retryWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare);
        assertTrue(shaky.wasExecuted());

        retryWithException(
                threeTimes.get(), ex -> shaky.beforeSleep(ex), bare);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        retryWithException(
                threeTimes.get(), ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep());
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retryWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare));
        assertFalse(shaky.wasBeforeSleepExecuted());

        retryWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare);
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retryWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()));
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        retryWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep());
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
        assertTrue(shaky.wasExecuted());
    }

    @Test
    public void supplierApplied() {
        Shaky shaky = new Shaky();
        Supplier<String> bare = () -> shaky.sum(THREE, FIVE);

        assertEquals(Shaky.SUM_RESULT, retry(THREE, TEN, bare));
        assertEquals(Shaky.SUM_RESULT, retry(THREE, TEN, bare));
        assertEquals(Shaky.SUM_RESULT, retry(threeTimes.get(), bare));
        assertEquals(Shaky.SUM_RESULT,
                retry(threeTimes.get(), Arrays.asList(), bare));
        assertThrows(Shaky.RuntimeException.class,
                () -> retry(threeTimes.get(),
                Arrays.asList(NullPointerException.class), bare));
        assertEquals(Shaky.SUM_RESULT, retry(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class), bare));

        assertEquals(Shaky.SUM_RESULT, retry(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.SUM_RESULT, retry(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retry(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare));
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.SUM_RESULT, retry(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertThrows(Shaky.RuntimeException.class,
                () -> retry(threeTimes.get(),
                        Arrays.asList(NullPointerException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()));
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        assertEquals(Shaky.SUM_RESULT, retry(threeTimes.get(),
                Arrays.asList(Shaky.RuntimeException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }

    @Test
    public void supplierWithExceptionApplied() throws Shaky.Exception {
        Shaky shaky = new Shaky();
        SupplierWithException<String, Shaky.Exception> bare =
                () -> shaky.concat(THREE, FIVE);

        assertEquals(Shaky.CONCAT_RESULT,
                retryWithException(THREE, TEN, bare));
        assertEquals(Shaky.CONCAT_RESULT,
                retryWithException(THREE, TEN, bare));
        assertEquals(Shaky.CONCAT_RESULT,
                retryWithException(threeTimes.get(), bare));
        assertEquals(Shaky.CONCAT_RESULT, retryWithException(
                threeTimes.get(), Arrays.asList(), bare));
        assertThrows(Shaky.ConcatException.class,
                () -> retryWithException(threeTimes.get(),
                Arrays.asList(Shaky.UnrelatedException.class), bare));
        assertEquals(Shaky.CONCAT_RESULT, retryWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class), bare));

        assertEquals(Shaky.CONCAT_RESULT, retryWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.CONCAT_RESULT, retryWithException(threeTimes.get(),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retryWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare));
        assertFalse(shaky.wasBeforeSleepExecuted());

        assertEquals(Shaky.CONCAT_RESULT, retryWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare));
        assertTrue(shaky.wasBeforeSleepExecuted());

        assertThrows(Shaky.ConcatException.class,
                () -> retryWithException(threeTimes.get(),
                        Arrays.asList(Shaky.UnrelatedException.class),
                        ex -> shaky.beforeSleep(ex), bare,
                        () -> shaky.afterSleep()));
        assertFalse(shaky.wasBeforeSleepExecuted());
        assertFalse(shaky.wasAfterSleepExecuted());

        assertEquals(Shaky.CONCAT_RESULT, retryWithException(threeTimes.get(),
                Arrays.asList(Shaky.ConcatException.class),
                ex -> shaky.beforeSleep(ex), bare,
                () -> shaky.afterSleep()));
        assertTrue(shaky.wasBeforeSleepExecuted());
        assertTrue(shaky.wasAfterSleepExecuted());
    }

}
