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
 * Contains different overloaded static methods which decorate a function call
 * with retry logic.
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
            List<Class<? extends RuntimeException>> exceptionClasses,
            Runnable f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static Runnable retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Runnable f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static Runnable retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Runnable f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static Runnable retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            Runnable f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static Runnable retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
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
            List<Class<? extends E>> exceptionClasses,
            RunnableWithException<E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <E extends Exception> RunnableWithException<E> retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            RunnableWithException<E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <E extends Exception> RunnableWithException<E> retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            RunnableWithException<E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <E extends Exception> RunnableWithException<E> retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            RunnableWithException<E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <E extends Exception> RunnableWithException<E> retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            RunnableWithException<E> f,
            Runnable afterSleep) {
        return new RetryDecorator<>(
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
            List<Class<? extends RuntimeException>> exceptionClasses,
            Supplier<R> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static <R> Supplier<R> retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Supplier<R> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static <R> Supplier<R> retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Supplier<R> f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static <R> Supplier<R> retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            Supplier<R> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static <R> Supplier<R> retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            Supplier<R> f,
            Runnable afterSleep) {
        return new RetryDecorator<Object, Object, R, RuntimeException>(
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

    static <R, E extends Exception> SupplierWithException<R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            SupplierWithException<R, E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <R, E extends Exception> SupplierWithException<R, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            SupplierWithException<R, E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <R, E extends Exception> SupplierWithException<R, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            SupplierWithException<R, E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <R, E extends Exception> SupplierWithException<R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            SupplierWithException<R, E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <R, E extends Exception> SupplierWithException<R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            SupplierWithException<R, E> f,
            Runnable afterSleep) {
        return new RetryDecorator<Object, Object, R, E>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    // -------------- Consumer ----------------- //

    static <T> Consumer<T> retried(
            int numRetries, long sleep, Consumer<T> f) {
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
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<T> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static <T> Consumer<T> retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Consumer<T> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static <T> Consumer<T> retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Consumer<T> f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static <T> Consumer<T> retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            Consumer<T> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T> Consumer<T> retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            Consumer<T> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, Object, Object, RuntimeException>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    static <T, E extends Exception> ConsumerWithException<T, E>
    retriedWithException(
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
            List<Class<? extends E>> exceptionClasses,
            ConsumerWithException<T, E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <T, E extends Exception> ConsumerWithException<T, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            ConsumerWithException<T, E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <T, E extends Exception> ConsumerWithException<T, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            ConsumerWithException<T, E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <T, E extends Exception> ConsumerWithException<T, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            ConsumerWithException<T, E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, E extends Exception> ConsumerWithException<T, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
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
            List<Class<? extends RuntimeException>> exceptionClasses,
            BiConsumer<T, U> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static <T, U> BiConsumer<T, U> retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            BiConsumer<T, U> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static <T, U> BiConsumer<T, U> retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            BiConsumer<T, U> f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static <T, U> BiConsumer<T, U> retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            BiConsumer<T, U> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, U> BiConsumer<T, U> retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            BiConsumer<T, U> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, U, Object, RuntimeException>(
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
            List<Class<? extends E>> exceptionClasses,
            BiConsumerWithException<T, U, E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            BiConsumerWithException<T, U, E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            BiConsumerWithException<T, U, E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            BiConsumerWithException<T, U, E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
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
            List<Class<? extends RuntimeException>> exceptionClasses,
            Function<T, R> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static <T, R> Function<T, R> retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Function<T, R> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static <T, R> Function<T, R> retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Function<T, R> f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static <T, R> Function<T, R> retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            Function<T, R> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, R> Function<T, R> retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            Function<T, R> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, Object, R, RuntimeException>(
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
            List<Class<? extends E>> exceptionClasses,
            FunctionWithException<T, R, E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            FunctionWithException<T, R, E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            FunctionWithException<T, R, E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            FunctionWithException<T, R, E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
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
            List<Class<? extends RuntimeException>> exceptionClasses,
            BiFunction<T, U, R> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    static <T, U, R> BiFunction<T, U, R> retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            BiFunction<T, U, R> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    static <T, U, R> BiFunction<T, U, R> retried(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            BiFunction<T, U, R> f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    static <T, U, R> BiFunction<T, U, R> retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            BiFunction<T, U, R> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, U, R> BiFunction<T, U, R> retried(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            BiFunction<T, U, R> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, U, R, RuntimeException>(
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
            List<Class<? extends E>> exceptionClasses,
            BiFunctionWithException<T, U, R, E> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            BiFunctionWithException<T, U, R, E> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    retriedWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            BiFunctionWithException<T, U, R, E> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            BiFunctionWithException<T, U, R, E> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    retriedWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
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
            List<Class<? extends RuntimeException>> exceptionClasses,
            Runnable f) {
        retried(p, exceptionClasses, f).run();
    }

    static void retry(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Runnable f) {
        retried(p, beforeSleep, f).run();
    }

    static void retry(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Runnable f,
            Runnable afterSleep) {
        retried(p, beforeSleep, f, afterSleep).run();
    }

    static void retry(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            Runnable f) {
        retried(p, exceptionClasses, beforeSleep, f).run();
    }

    static void retry(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            Runnable f,
            Runnable afterSleep) {
        retried(p, exceptionClasses, beforeSleep, f, afterSleep).run();
    }

    static <E extends Exception> void retryWithException(
            int numRetries, long sleep, RunnableWithException<E> f) throws E {
        retriedWithException(numRetries, sleep, f).run();
    }

    static <E extends Exception> void retryWithException(
            RetryPolicy p,
            RunnableWithException<E> f) throws E {
        retriedWithException(p, f).run();
    }

    static <E extends Exception> void retryWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            RunnableWithException<E> f) throws E {
        retriedWithException(p, exceptionClasses, f).run();
    }

    static <E extends Exception> void retryWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            RunnableWithException<E> f) throws E {
        retriedWithException(p, beforeSleep, f).run();
    }

    static <E extends Exception> void retryWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            RunnableWithException<E> f,
            Runnable afterSleep) throws  E {
        retriedWithException(p, beforeSleep, f, afterSleep).run();
    }

    static <E extends Exception> void retryWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            RunnableWithException<E> f) throws E {
        retriedWithException(p, exceptionClasses, beforeSleep, f).run();
    }

    static <E extends Exception> void retryWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            RunnableWithException<E> f,
            Runnable afterSleep) throws E {
        retriedWithException(
                p, exceptionClasses, beforeSleep, f, afterSleep).run();
    }


    // -------------- Supplier ----------------- //

    static <R> R retry(int numRetries, long sleep, Supplier<R> f) {
        return retried(numRetries, sleep, f).get();
    }

    static <R> R retry(
            RetryPolicy p,
            Supplier<R> f) {
        return retried(p, f).get();
    }

    static <R> R retry(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Supplier<R> f) {
        return retried(p, exceptionClasses, f).get();
    }

    static <R> R retry(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Supplier<R> f) {
        return retried(p, beforeSleep, f).get();
    }

    static <R> R retry(
            RetryPolicy p,
            Consumer<RuntimeException> beforeSleep,
            Supplier<R> f,
            Runnable afterSleep) {
        return retried(p, beforeSleep, f, afterSleep).get();
    }

    static <R> R retry(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            Supplier<R> f) {
        return retried(p, exceptionClasses, beforeSleep, f).get();
    }

    static <R> R retry(
            RetryPolicy p,
            List<Class<? extends RuntimeException>> exceptionClasses,
            Consumer<RuntimeException> beforeSleep,
            Supplier<R> f,
            Runnable afterSleep) {
        return retried(p, exceptionClasses, beforeSleep, f, afterSleep).get();
    }

    static <R, E extends Exception> R retryWithException(
            int numRetries, long sleep, SupplierWithException<R, E> f)
                    throws E {
        return retriedWithException(numRetries, sleep, f).get();
    }

    static <R, E extends Exception> R retryWithException(
            RetryPolicy p,
            SupplierWithException<R, E> f) throws E {
        return retriedWithException(p, f).get();
    }

    static <R, E extends Exception> R retryWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            SupplierWithException<R, E> f) throws E {
        return retriedWithException(p, exceptionClasses, f).get();
    }

    static <R, E extends Exception> R retryWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            SupplierWithException<R, E> f) throws E {
        return retriedWithException(p, beforeSleep, f).get();
    }

    static <R, E extends Exception> R retryWithException(
            RetryPolicy p,
            Consumer<E> beforeSleep,
            SupplierWithException<R, E> f,
            Runnable afterSleep) throws E {
        return retriedWithException(p, beforeSleep, f, afterSleep).get();
    }

    static <R, E extends Exception> R retryWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            SupplierWithException<R, E> f) throws E {
        return retriedWithException(p, exceptionClasses, beforeSleep, f).get();
    }

    static <R, E extends Exception> R retryWithException(
            RetryPolicy p,
            List<Class<? extends E>> exceptionClasses,
            Consumer<E> beforeSleep,
            SupplierWithException<R, E> f,
            Runnable afterSleep) throws E {
        return retriedWithException(
                p, exceptionClasses, beforeSleep, f, afterSleep).get();
    }
}
