package mytools.function.decorator.retry;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import mytools.function.BiConsumerWithException;
import mytools.function.BiFunctionWithException;
import mytools.function.ConsumerWithException;
import mytools.function.FunctionWithException;
import mytools.function.RunnableWithException;
import mytools.function.SupplierWithException;


/**
 * Different overloaded static methods which decorate a function call with retry
 * logic.
 *
 * <p>
 * The methods in this class could be divided into two groups.
 *
 * <p>
 * If a method name is "retried", it simply transforms a function into a similar
 * function with retry logic applied. These methods could be used whenever a
 * resulting function needs to be further decorated with some other decorators.
 *
 * <p>
 * If a method name is "retry", it will execute the given function with retry
 * logic applied.
 *
 * @see RetryPolicy
 */
public interface RetryDecorators {

    ///////////////// decorators //////////////////////

    // -------------- Runnable ----------------- //

    static Runnable retried(int numRetries, long sleep, Runnable f) {
        return retried(new LinearRetryPolicy(numRetries, sleep),
                null, null, f, null);
    }

    static Runnable retried(
            RetryPolicy p,
            Runnable f) {
        return retried(p, null, null, f, null);
    }

    static Runnable retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Runnable f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static Runnable retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            Runnable f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static Runnable retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            Runnable f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static Runnable retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Runnable f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static Runnable retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Runnable f,
            Runnable afterSleep) {
        return new RetryDecorator<>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    // -------------- Runnable With Exception ----------------- //

    static <E extends Exception> RunnableWithException<E> retriedWithException(
            int numRetries, long sleep, RunnableWithException<E> f) {
        return retriedWithException(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <E extends Exception> RunnableWithException<E> retriedWithException(
            RetryPolicy p,
            RunnableWithException<E> f) {
        return retriedWithException(p, null, null, f, null);
    }

    static <E extends Exception> RunnableWithException<E> retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            RunnableWithException<E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <E extends Exception> RunnableWithException<E> retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            RunnableWithException<E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <E extends Exception> RunnableWithException<E> retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            RunnableWithException<E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <E extends Exception> RunnableWithException<E> retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            RunnableWithException<E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <E extends Exception> RunnableWithException<E> retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            RunnableWithException<E> f,
            Runnable afterSleep) {
        return new RetryDecorator<Object, Object, Object, E>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    // -------------- Supplier ----------------- //

    static <R> Supplier<R> retried(int numRetries, long sleep, Supplier<R> f) {
        return retried(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <R> Supplier<R> retried(
            RetryPolicy p,
            Supplier<R> f) {
        return retried(p, null, null, f, null);
    }

    static <R> Supplier<R> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Supplier<R> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static <R> Supplier<R> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            Supplier<R> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static <R> Supplier<R> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            Supplier<R> f, Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static <R> Supplier<R> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep, Supplier<R> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static <R> Supplier<R> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Supplier<R> f,
            Runnable afterSleep) {
        return new RetryDecorator<Object, Object, R, Exception>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    static <R, E extends Exception> SupplierWithException<R, E>
    retriedWithException(
            int numRetries, long sleep, SupplierWithException<R, E> f) {
        return retriedWithException(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <R, E extends Exception> SupplierWithException<R, E>
    retriedWithException(
            RetryPolicy p,
            SupplierWithException<R, E> f) {
        return retriedWithException(p, null, null, f, null);
    }

    static <R, E extends Exception> SupplierWithException<R, E> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            SupplierWithException<R, E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <R, E extends Exception> SupplierWithException<R, E> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            SupplierWithException<R, E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <R, E extends Exception> SupplierWithException<R, E> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            SupplierWithException<R, E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <R, E extends Exception> SupplierWithException<R, E> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            SupplierWithException<R, E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <R, E extends Exception> SupplierWithException<R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            SupplierWithException<R, E> f,
            Runnable afterSleep) {
        return new RetryDecorator<Object, Object, R, E>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    // -------------- Consumer ----------------- //

    static <T> Consumer<T> retried(int numRetries, long sleep, Consumer<T> f) {
        return retried(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <T> Consumer<T> retried(
            RetryPolicy p,
            Consumer<T> f) {
        return retried(p, null, null, f, null);
    }

    static <T> Consumer<T> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<T> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static <T> Consumer<T> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            Consumer<T> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static <T> Consumer<T> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            Consumer<T> f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static <T> Consumer<T> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Consumer<T> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T> Consumer<T> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Consumer<T> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, Object, Object, Exception>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    static <T, E extends Exception> ConsumerWithException<T, E> retried(
            int numRetries, long sleep, ConsumerWithException<T, E> f) {
        return retriedWithException(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <T, E extends Exception> ConsumerWithException<T, E>
    retriedWithException(
            RetryPolicy p,
            ConsumerWithException<T, E> f) {
        return retriedWithException(p, null, null, f, null);
    }

    static <T, E extends Exception> ConsumerWithException<T, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            ConsumerWithException<T, E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <T, E extends Exception> ConsumerWithException<T, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            ConsumerWithException<T, E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <T, E extends Exception> ConsumerWithException<T, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            ConsumerWithException<T, E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <T, E extends Exception> ConsumerWithException<T, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            ConsumerWithException<T, E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, E extends Exception> ConsumerWithException<T, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            ConsumerWithException<T, E> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, Object, Object, E>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    // -------------- BiConsumer ----------------- //

    static <T, U> BiConsumer<T, U> retried(
            int numRetries, long sleep, BiConsumer<T, U> f) {
        return retried(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <T, U> BiConsumer<T, U> retried(
            RetryPolicy p,
            BiConsumer<T, U> f) {
        return retried(p, null, null, f, null);
    }

    static <T, U> BiConsumer<T, U> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            BiConsumer<T, U> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static <T, U> BiConsumer<T, U> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            BiConsumer<T, U> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static <T, U> BiConsumer<T, U> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            BiConsumer<T, U> f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static <T, U> BiConsumer<T, U> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            BiConsumer<T, U> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, U> BiConsumer<T, U> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            BiConsumer<T, U> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, U, Object, Exception>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    retriedWithException(
            int numRetries, long sleep, BiConsumerWithException<T, U, E> f) {
        return retriedWithException(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    retriedWithException(
            RetryPolicy p,
            BiConsumerWithException<T, U, E> f) {
        return retriedWithException(p, null, null, f, null);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            BiConsumerWithException<T, U, E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            BiConsumerWithException<T, U, E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            BiConsumerWithException<T, U, E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            BiConsumerWithException<T, U, E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            BiConsumerWithException<T, U, E> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, U, Object, E>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }


    // -------------- Function ----------------- //

    static <T, R> Function<T, R> retried(
            int numRetries, long sleep, Function<T, R> f) {
        return retried(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <T, R> Function<T, R> retried(
            RetryPolicy p,
            Function<T, R> f) {
        return retried(p, null, null, f, null);
    }

    static <T, R> Function<T, R> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Function<T, R> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static <T, R> Function<T, R> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            Function<T, R> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static <T, R> Function<T, R> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            Function<T, R> f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static <T, R> Function<T, R> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Function<T, R> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, R> Function<T, R> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Function<T, R> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, Object, R, Exception>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    retriedWithException(
            int numRetries, long sleep, FunctionWithException<T, R, E> f) {
        return retriedWithException(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    retriedWithException(
            RetryPolicy p,
            FunctionWithException<T, R, E> f) {
        return retriedWithException(p, null, null, f, null);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            FunctionWithException<T, R, E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            FunctionWithException<T, R, E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            FunctionWithException<T, R, E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            FunctionWithException<T, R, E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            FunctionWithException<T, R, E> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, Object, R, E>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }


    // -------------- BiFunction ----------------- //

    static <T, U, R> BiFunction<T, U, R> retried(
            int numRetries, long sleep, BiFunction<T, U, R> f) {
        return retried(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <T, U, R> BiFunction<T, U, R> retried(
            RetryPolicy p,
            BiFunction<T, U, R> f) {
        return retried(p, null, null, f, null);
    }

    static <T, U, R> BiFunction<T, U, R> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            BiFunction<T, U, R> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static <T, U, R> BiFunction<T, U, R> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            BiFunction<T, U, R> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static <T, U, R> BiFunction<T, U, R> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            BiFunction<T, U, R> f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static <T, U, R> BiFunction<T, U, R> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            BiFunction<T, U, R> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, U, R> BiFunction<T, U, R> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            BiFunction<T, U, R> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, U, R, Exception>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    retriedWithException(
            int numRetries, long sleep, BiFunctionWithException<T, U, R, E> f) {
        return retriedWithException(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    retriedWithException(
            RetryPolicy p,
            BiFunctionWithException<T, U, R, E> f) {
        return retriedWithException(p, null, null, f, null);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            BiFunctionWithException<T, U, R, E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            BiFunctionWithException<T, U, R, E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            BiFunctionWithException<T, U, R, E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            BiFunctionWithException<T, U, R, E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            BiFunctionWithException<T, U, R, E> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, U, R, E>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }


    ///////////////// decorator applications //////////////////////

    // -------------- Runnable ----------------- //

    static void retry(int numRetries, long sleep, Runnable f) {
        retried(numRetries, sleep, f).run();
    }

    static void retry(
            RetryPolicy p,
            Runnable f) {
        retried(p, f).run();
    }

    static void retry(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Runnable f) {
        retried(p, exceptionClasses, f).run();
    }

    static void retry(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            Runnable f) {
        retried(p, beforeSleep, f).run();
    }

    static void retry(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            Runnable f,
            Runnable afterSleep) {
        retried(p, beforeSleep, f, afterSleep).run();
    }

    static void retry(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Runnable f) {
        retried(p, exceptionClasses, beforeSleep, f).run();
    }

    static void retry(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Runnable f,
            Runnable afterSleep) {
        retried(p, exceptionClasses, beforeSleep, f, afterSleep).run();
    }


    // -------------- Supplier ----------------- //

    /**
     * Apply {@link LinearRetryPolicy} to a {@code Supplier} and execute it.
     *
     * @param numRetries how many times to retry
     * @param sleep      for how long to sleep between retries
     * @param f          code to retry
     */
    static <R> R retry(int numRetries, long sleep, Supplier<R> f) {
        return retried(numRetries, sleep, f).get();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Supplier} and execute it.
     *
     * @param p retry policy
     * @param f code to retry
     */
    static <R> R retry(RetryPolicy p, Supplier<R> f) {
        return retried(p, f).get();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Supplier} and execute it.
     *
     * @param p                 retry policy
     * @param exceptionClasses  exception types on which execute retries
     * @param f                 code to retry
     */
    static <R> R retry(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses, Supplier<R> f) {
        return retried(p, exceptionClasses, f).get();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Supplier} and execute it.
     *
     * @param p           retry policy
     * @param beforeSleep code to execute before going to sleep
     * @param f           code to retry
     */
    static <R> R retry(RetryPolicy p,
            Consumer<Exception> beforeSleep, Supplier<R> f) {
        return retried(p, beforeSleep, f).get();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Supplier} and execute it.
     *
     * @param p             retry policy
     * @param beforeSleep   code to execute before going to sleep
     * @param f             code to retry
     * @param afterSleep    code to execute after sleeping
     */
    static <R> R retry(RetryPolicy p,
            Consumer<Exception> beforeSleep,
            Supplier<R> f, Runnable afterSleep) {
        return retried(p, beforeSleep, f, afterSleep).get();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Supplier} and execute it.
     *
     * @param p                retry policy
     * @param exceptionClasses exception types on which to execute the retries
     * @param beforeSleep      code to execute before sleeping
     * @param f                code to retry
     */
    static <R> R retry(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Supplier<R> f) {
        return retried(p, exceptionClasses, beforeSleep, f).get();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Supplier} and execute it.
     *
     * @param p                  retry policy
     * @param exceptionClasses   exception types on which to execute the retries
     * @param beforeSleep        code to execute before sleeping
     * @param f                  code to retry
     * @param afterSleep         code to execute after sleeping
     */
    static <R> R retry(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Supplier<R> f, Runnable afterSleep) {
        return retried(p, exceptionClasses, beforeSleep, f, afterSleep).get();
    }

}
