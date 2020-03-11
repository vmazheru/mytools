package mytools.function.decorator.retry;

import static mytools.function.decorator.retry.RetryDecorators.retried;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import mytools.function.RunnableWithException;
import mytools.function.object.Counter;

public class RetryDecoratorsTest {

    @Test
    public void retriedRunnable() {
        test(THREE, ZERO, ZERO, () ->
            retried(NUM_RETRIES, SLEEP_TIME, badRunnable).run());

        test(THREE, ZERO, ZERO, () ->
            retried(policy(), badRunnable).run());

        test(THREE, ZERO, ZERO, () ->
            retried(policy(), badRunnable).run());






//        test(THREE, ZERO, ZERO, true, () ->
//            retriedWithException(NUM_RETRIES, SLEEP_TIME,
//                badRunnableWithException).run());
    }

    private static void test(
            int expectedExecutionCount,
            int expectedBeforeCount,
            int expectedAfterCount,
            Runnable r) {
        resetCounters();

        long start = System.currentTimeMillis();
        try {
            r.run();
            fail("Must throw runtime excepiton");
        } catch (@SuppressWarnings("unused")
                 NullPointerException | IllegalArgumentException e) {
            // ok
        }
        long timeSpent = System.currentTimeMillis() - start;

        assertTrue(timeSpent >= EXPECTED_RUN_TIME);
        assertEquals(expectedExecutionCount, executionCounter.get());
        assertEquals(expectedBeforeCount, beforeCounter.get());
        assertEquals(expectedAfterCount, afterCounter.get());
    }

    private static final int NUM_RETRIES = 3;
    private static final long SLEEP_TIME = 50;
    private static final long EXPECTED_RUN_TIME =
            SLEEP_TIME * (NUM_RETRIES - 1);
    private static final int ZERO = 0;
    //private static final int ONE = 1;
    private static final int THREE = 3;

    private static Random rand = new Random();

    @SuppressWarnings("unchecked")
    private static Class<? extends RuntimeException>[] runtimeExceptions =
            new Class[] {
                    NullPointerException.class,
                    IllegalArgumentException.class };

    @SuppressWarnings("unchecked")
    private static Class<? extends IOException>[] exceptions =
            new Class[] {
                    NullPointerException.class,
                    IOException.class };

    private static Counter executionCounter = new Counter();
    private static Counter beforeCounter = new Counter();
    private static Counter afterCounter = new Counter();

    //////////// functions that always fail ////////////////////
    private static Runnable
    badRunnable = () -> incrementAndThrowRuntimeException(true);
    private static RunnableWithException<IOException>
    badRunnableWithException = () -> incrementAndThrowException(true);

    ///////// functions that succeed on second try ////////////
    private static Runnable
    shakeyRunnable = () -> incrementAndThrowRuntimeException(false);
    private static RunnableWithException<IOException>
    shakeyRunnableWithException = () -> incrementAndThrowException(false);

    private static void incrementAndThrowRuntimeException(boolean always) {
        executionCounter.increment();
        if (always || executionCounter.get() <= 1) {
            throwRandomRuntimeException();
        }
    }

    private static void incrementAndThrowException(boolean always)
            throws IOException {
        executionCounter.increment();
        if (always || executionCounter.get() <= 1) {
            throwRandomException();
        }
    }

    private static RetryPolicy policy() {
        return new LinearRetryPolicy(NUM_RETRIES, SLEEP_TIME);
    }

    private static void throwRandomException() throws IOException {
        throwException(exceptions[rand.nextInt(exceptions.length)]);
    }

    private static void throwRandomRuntimeException() {
        throwRuntimeException(
                runtimeExceptions[rand.nextInt(runtimeExceptions.length)]);
    }

    private static void throwException(
            Class<? extends IOException> klass) throws IOException {
        throw getExceptionInstance(klass);
    }

    private static void throwRuntimeException(
            Class<? extends RuntimeException> klass) {
        throw getExceptionInstance(klass);
    }

    private static <E extends Exception> E getExceptionInstance(
            Class<E> klass) {
        try {
            return klass.getConstructor().newInstance();
        } catch (InvocationTargetException |
                 IllegalAccessException |
                 NoSuchMethodException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void resetCounters() {
        executionCounter.reset();
        beforeCounter.reset();
        afterCounter.reset();
    }

}
