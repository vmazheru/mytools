package mytools.function.decorator.retry;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import mytools.function.BiConsumerWithException;
import mytools.function.ConsumerWithException;
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

    static RunnableWithException retriedWithException(
            int numRetries, long sleep, RunnableWithException f) {
        return retriedWithException(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static RunnableWithException retriedWithException(
            RetryPolicy p,
            RunnableWithException f) {
        return retriedWithException(p, null, null, f, null);
    }

    static RunnableWithException retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            RunnableWithException f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static RunnableWithException retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            RunnableWithException f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static RunnableWithException retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            RunnableWithException f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static RunnableWithException retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            RunnableWithException f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static RunnableWithException retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            RunnableWithException f,
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
        return new RetryDecorator<Object, Object, R>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    static <R> SupplierWithException<R> retriedWithException(
            int numRetries, long sleep, SupplierWithException<R> f) {
        return retriedWithException(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <R> SupplierWithException<R> retriedWithException(
            RetryPolicy p,
            SupplierWithException<R> f) {
        return retriedWithException(p, null, null, f, null);
    }

    static <R> SupplierWithException<R> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            SupplierWithException<R> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <R> SupplierWithException<R> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            SupplierWithException<R> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <R> SupplierWithException<R> retried(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            SupplierWithException<R> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <R> SupplierWithException<R> retried(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            SupplierWithException<R> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <R> SupplierWithException<R> retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            SupplierWithException<R> f,
            Runnable afterSleep) {
        return new RetryDecorator<Object, Object, R>(
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
        return new RetryDecorator<T, Object, Object>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    static <T> ConsumerWithException<T> retried(
            int numRetries, long sleep, ConsumerWithException<T> f) {
        return retriedWithException(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <T> ConsumerWithException<T> retriedWithException(
            RetryPolicy p,
            ConsumerWithException<T> f) {
        return retriedWithException(p, null, null, f, null);
    }

    static <T> ConsumerWithException<T> retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            ConsumerWithException<T> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <T> ConsumerWithException<T> retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            ConsumerWithException<T> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <T> ConsumerWithException<T> retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            ConsumerWithException<T> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <T> ConsumerWithException<T> retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            ConsumerWithException<T> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T> ConsumerWithException<T> retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            ConsumerWithException<T> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, Object, Object>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    // -------------- BiConsumer ----------------- //

    static <T,U> BiConsumer<T, U> retried(
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
        return new RetryDecorator<T, U, Object>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }

    static <T,U> BiConsumerWithException<T,U> retriedWithException(
            int numRetries, long sleep, BiConsumerWithException<T, U> f) {
        return retriedWithException(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    static <T, U> BiConsumerWithException<T, U> retriedWithException(
            RetryPolicy p,
            BiConsumerWithException<T, U> f) {
        return retriedWithException(p, null, null, f, null);
    }

    static <T, U> BiConsumerWithException<T, U> retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            BiConsumerWithException<T, U> f) {
        return retriedWithException(p, exceptionClasses, null, f, null);
    }

    static <T, U> BiConsumerWithException<T, U> retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            BiConsumerWithException<T, U> f) {
        return retriedWithException(p, null, beforeSleep, f, null);
    }

    static <T, U> BiConsumerWithException<T, U> retriedWithException(
            RetryPolicy p,
            Consumer<Exception> beforeSleep,
            BiConsumerWithException<T, U> f,
            Runnable afterSleep) {
        return retriedWithException(p, null, beforeSleep, f, afterSleep);
    }

    static <T, U> BiConsumerWithException<T, U> retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            BiConsumerWithException<T, U> f) {
        return retriedWithException(p, exceptionClasses, beforeSleep, f, null);
    }

    static <T, U> BiConsumerWithException<T, U> retriedWithException(
            RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            BiConsumerWithException<T, U> f,
            Runnable afterSleep) {
        return new RetryDecorator<T, U, Object>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }


    // -------------- Function ----------------- //

    /**
     * Apply {@link LinearRetryPolicy} to a {@code Function}.
     *
     * @param numRetries how many times to retry
     * @param sleep      for how long to sleep between retries
     * @param f          code to retry
     * @return           {@code Function} which will retry in case of an error
     */
    static <T,R> Function<T,R> retried(
            int numRetries, long sleep, Function<T,R> f) {
        return retried(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Function}.
     *
     * @param p retry policy
     * @param f code to retry
     * @return {@code Function} which will retry in case of an error
     */
    static <T, R> Function<T, R> retried(RetryPolicy p, Function<T, R> f) {
        return retried(p, null, null, f, null);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Function}.
     *
     * @param p                 retry policy
     * @param exceptionClasses  exception types for which retries happen
     * @param f                 code to retry
     * @return {@code Function} which will retry in case of an error
     */
    static <T, R> Function<T, R> retried(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Function<T, R> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Function}.
     *
     * @param p            retry policy
     * @param beforeSleep  code to execute before going to sleep
     * @param f            code to retry
     * @return {@code Function} which will retry in case of an error
     */
    static <T, R> Function<T, R> retried(
            RetryPolicy p, Consumer<Exception> beforeSleep, Function<T, R> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Function}.
     *
     * @param p             retry policy
     * @param beforeSleep   code to execute before going to sleep
     * @param f             code to retry
     * @param afterSleep    code to execute after going to sleep
     * @return {@code Function} which will retry in case of an error
     */
    static <T, R> Function<T, R> retried(RetryPolicy p,
            Consumer<Exception> beforeSleep, Function<T, R> f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Function}.
     *
     * @param p                 retry policy
     * @param exceptionClasses  exception types on which execute the retries
     * @param beforeSleep       code to execute before going to sleep
     * @param f                 code to retry
     * @return {@code Function} which will retry in case of an error
     */
    static <T, R> Function<T, R> retried(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep, Function<T, R> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Function}.
     *
     * @param p                 retry policy
     * @param exceptionClasses  exception types on which execute the retries
     * @param beforeSleep       code to execute before going to sleep
     * @param f                 code to retry
     * @param afterSleep        code to execute after sleeping
     * @return {@code Function} which will retry in case of an error
     */
    static <T, R> Function<T, R> retried(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Function<T, R> f, Runnable afterSleep) {
        return new RetryDecorator<T, Object, R>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }


    // -------------- BiFunction ----------------- //

    /**
     * Apply {@link LinearRetryPolicy} to a {@code BiFunction}.
     *
     * @param numRetries how many times to retry
     * @param sleep      for how long to sleep between retries
     * @param f          code to retry
     * @return           {@code BiFunction} which will retry in case of an error
     */
    static <T,U,R> BiFunction<T,U,R> retried(
            int numRetries, long sleep, BiFunction<T,U,R> f) {
        return retried(new LinearRetryPolicy(
                numRetries, sleep), null, null, f, null);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code BiFunction}.
     *
     * @param p retry policy
     * @param f code to retry
     * @return {@code BiFunction} which will retry in case of an error
     */
    static <T, U, R> BiFunction<T, U, R> retried(
            RetryPolicy p, BiFunction<T, U, R> f) {
        return retried(p, null, null, f, null);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code BiFunction}.
     *
     * @param p                 retry policy
     * @param exceptionClasses  exception types for whith retries happen
     * @param f                 code to retry
     * @return {@code BiFunction} which will retry in case of an error
     */
    static <T, U, R> BiFunction<T, U, R> retried(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            BiFunction<T, U, R> f) {
        return retried(p, exceptionClasses, null, f, null);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code BiFunction}.
     *
     * @param p            retry policy
     * @param beforeSleep  code to execute before going to sleep
     * @param f            code to retry
     * @return {@code BiFunction} which will retry in case of an error
     */
    static <T, U, R> BiFunction<T, U, R> retried(RetryPolicy p,
            Consumer<Exception> beforeSleep,
            BiFunction<T, U, R> f) {
        return retried(p, null, beforeSleep, f, null);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code BiFunction}.
     *
     * @param p             retry policy
     * @param beforeSleep   code to execute before going to sleep
     * @param f             code to retry
     * @param afterSleep    code to execute after going to sleep
     * @return {@code BiFunction} which will retry in case of an error
     */
    static <T, U, R> BiFunction<T, U, R> retried(RetryPolicy p,
            Consumer<Exception> beforeSleep, BiFunction<T, U, R> f,
            Runnable afterSleep) {
        return retried(p, null, beforeSleep, f, afterSleep);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code BiFunction}.
     *
     * @param p                 retry policy
     * @param exceptionClasses  exception types on which execute the retries
     * @param beforeSleep       code to execute before going to sleep
     * @param f                 code to retry
     * @return {@code BiFunction} which will retry in case of an error
     */
    static <T, U, R> BiFunction<T, U, R> retried(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep, BiFunction<T, U, R> f) {
        return retried(p, exceptionClasses, beforeSleep, f, null);
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code BiFunction}.
     *
     * @param p                 retry policy
     * @param exceptionClasses  exception types on which execute the retries
     * @param beforeSleep       code to execute before going to sleep
     * @param f                 code to retry
     * @param afterSleep        code to execute after sleeping
     * @return {@code BiFunction} which will retry in case of an error
     */
    static <T, U, R> BiFunction<T, U, R> retried(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            BiFunction<T, U, R> f, Runnable afterSleep) {
        return new RetryDecorator<T, U, R>(
                p, exceptionClasses, beforeSleep, afterSleep).decorate(f);
    }


    ///////////////// decorator applications //////////////////////

    // -------------- Runnable ----------------- //

    /**
     * Apply {@link LinearRetryPolicy} to a {@code Runnable} and execute it.
     *
     * @param numRetries how many times to retry
     * @param sleep      for how long to sleep between retries
     * @param f          code to retry
     */
    static void retry(int numRetries, long sleep, Runnable f) {
        retried(numRetries, sleep, f).run();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Runnable} and execute it.
     *
     * @param p retry policy
     * @param f code to retry
     */
    static void retry(RetryPolicy p, Runnable f) {
        retried(p, f).run();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Runnable} and execute it.
     *
     * @param p                 retry policy
     * @param exceptionClasses  exception types for which retries happen
     * @param f                 code to retry
     */
    static void retry(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses, Runnable f) {
        retried(p, exceptionClasses, f).run();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Runnable} and execute it.
     *
     * @param p             retry policy
     * @param beforeSleep   code to execute before going to sleep
     * @param f             code to retry
     */
    static void retry(RetryPolicy p,
            Consumer<Exception> beforeSleep, Runnable f) {
        retried(p, beforeSleep, f).run();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Runnable} and execute it.
     *
     * @param p             retry policy
     * @param beforeSleep   code to execute before going to sleep
     * @param f             code to retry
     * @param afterSleep    code to execute after going to sleep
     */
    static void retry(RetryPolicy p,
            Consumer<Exception> beforeSleep, Runnable f, Runnable afterSleep) {
        retried(p, beforeSleep, f, afterSleep).run();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Runnable} and execute it.
     *
     * @param p                 retry policy
     * @param exceptionClasses  exception types on which execute the retries
     * @param beforeSleep       code to execute before going to sleep
     * @param f                 code to retry
     */
    static void retry(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Runnable f) {
        retried(p, exceptionClasses, beforeSleep, f).run();
    }

    /**
     * Apply given {@link RetryPolicy} to a {@code Runnable} and execute it.
     *
     * @param p                 retry policy
     * @param exceptionClasses  exception types on which execute the retries
     * @param beforeSleep       code to execute before going to sleep
     * @param f                 code to retry
     * @param afterSleep        code to execute after sleeping
     */
    static void retry(RetryPolicy p,
            List<Class<? extends Exception>> exceptionClasses,
            Consumer<Exception> beforeSleep,
            Runnable f, Runnable afterSleep) {
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
