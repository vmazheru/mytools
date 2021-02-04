package mytools.function.decorator.exception;

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
 * This interface contains methods which expose functionality of different
 * {@link cl.core.decorator.ExceptionHidingDecorator} implementations.
 *
 * <p>
 * There are two groups of methods.
 * <ul>
 * <li>Methods which help converting checked exceptions into unchecked
 * exceptions. These are {@code unchecked()} and {@code uncheck()} methods.</li>
 * <li>Methods which catch exceptions, and return NULL values whenever an
 * exception happens. These are {@code safe()} and {@code safely()} methods.
 * </li>
 * </ul>
 *
 * <p>
 * Within each group, there are also two types of methods.
 * <ul>
 * <li>Methods which "decorate" functions without actually executing them, for
 * example {@code unchecked()}, {@code safe()}.</li>
 * <li>Methods which "decorate" functions and execute them with or without
 * returning the result, for example {@code uncheck()}, {@code safely()}.
 * Methods in this group, usually, only apply to functions which take not
 * parameters, that is {@code Runnable} and {@code Supplier}.</li>
 * </ul>
 *
 * <p>
 * The following snippet illustrates how to use {@code unchecked()} methods.
 *
 * <pre>
 * {@code
 *  Function<File, List<String> readAllLinesF =
 *      unchecked(file -> Files.readAllLines(file.toPath()));
 *
 *  // that you have a new function which reads all lines from a file,
 *  // but throws a run-time exception
 *  // instead of IOException
 *
 *  readAllLinesF.apply(myFile);
 * }
 * </pre>
 *
 * <p>
 * For functions which don't accept arguments (Runnable and Supplier), it's
 * easier to combine decorating a function with calling it in one line by using
 * one of the {@code uncheck()} methods, like in the following snippet:
 *
 * <pre>
 * {
 *     &#64;code
 *     File f = getMyFile();
 *     Fist<String> lines = uncheck(() -> Files.readAllLines(f.toPath()));
 * }
 * </pre>
 *
 * <p>
 * {@code safe()} and {@code safely()} methods may be used similarly.
 */
public interface ExceptionDecorators {

    static <E extends Exception> Runnable unchecked(
            RunnableWithException<E> f) {
        return new Uncheck<Object, Object, Object, E>().decorate(f);
    }

    static <E extends Exception> Runnable unchecked(
            Class<? extends RuntimeException> exceptionClass,
            RunnableWithException<E> f) {
        return new Uncheck<Object, Object, Object, E>(exceptionClass)
                .decorate(f);
    }

    static <R, E extends Exception> Supplier<? extends R> unchecked(
            SupplierWithException<? extends R, E> f) {
        return new Uncheck<Object, Object, R, E>().decorate(f);
    }

    static <R, E extends Exception> Supplier<? extends R> unchecked(
            Class<? extends RuntimeException> exceptionClass,
            SupplierWithException<? extends R, E> f) {
        return new Uncheck<Object, Object, R, E>(exceptionClass).decorate(f);
    }

    static <T, E extends Exception> Consumer<? super T> unchecked(
            ConsumerWithException<? super T, E> f) {
        return new Uncheck<T, Object, Object, E>().decorate(f);
    }

    static <T, E extends Exception> Consumer<? super T> unchecked(
            Class<? extends RuntimeException> exceptionClass,
            ConsumerWithException<? super T, E> f) {
        return new Uncheck<T, Object, Object, E>(exceptionClass).decorate(f);
    }

    static <T, U, E extends Exception> BiConsumer<? super T, ? super U> unchecked(
            BiConsumerWithException<? super T, ? super U, E> f) {
        return new Uncheck<T, U, Object, E>().decorate(f);
    }

    static <T, U, E extends Exception> BiConsumer<? super T, ? super U> unchecked(
            Class<? extends RuntimeException> exceptionClass,
            BiConsumerWithException<? super T, ? super U, E> f) {
        return new Uncheck<T, U, Object, E>(exceptionClass).decorate(f);
    }

    static <T, R, E extends Exception> Function<? super T, ? extends R> unchecked(
            FunctionWithException<? super T, ? extends R, E> f) {
        return new Uncheck<T, Object, R, E>().decorate(f);
    }

    static <T, R, E extends Exception> Function<? super T, ? extends R> unchecked(
            Class<? extends RuntimeException> exceptionClass,
            FunctionWithException<? super T, ? extends R, E> f) {
        return new Uncheck<T, Object, R, E>(exceptionClass).decorate(f);
    }

    static <T, U, R, E extends Exception> BiFunction<? super T, ? super U, ? extends R> unchecked(
            BiFunctionWithException<? super T, ? super U, ? extends R, E> f) {
        return new Uncheck<T, U, R, E>().decorate(f);
    }

    static <T, U, R, E extends Exception> BiFunction<? super T, ? super U, ? extends R> unchecked(
            Class<? extends RuntimeException> exceptionClass,
            BiFunctionWithException<? super T, ? super U, ? extends R, E> f) {
        return new Uncheck<T, U, R, E>(exceptionClass).decorate(f);
    }

    static <E extends Exception> void uncheck(RunnableWithException<E> f) {
        unchecked(f).run();
    }

    static <E extends Exception> void uncheck(
            Class<? extends RuntimeException> exceptionClass,
            RunnableWithException<E> f) {
        unchecked(exceptionClass, f).run();
    }

    static <R, E extends Exception> R uncheck(SupplierWithException<? extends R, E> f) {
        return unchecked(f).get();
    }

    static <R, E extends Exception> R uncheck(
            Class<? extends RuntimeException> exceptionClass,
            SupplierWithException<? extends R, E> f) {
        return unchecked(exceptionClass, f).get();
    }

    static <R, E extends Exception> Supplier<? extends R> safe(
            SupplierWithException<? extends R, E> f) {
        return new Safe<Object, Object, R, E>().decorate(f);
    }

    @SafeVarargs
    static <R, E extends Exception> Supplier<? extends R> safe(
            SupplierWithException<? extends R, E> f,
            Class<? extends Exception>... exceptions) {
        return new Safe<Object, Object, R, E>(exceptions).decorate(f);
    }

    static <T, R, E extends Exception> Function<? super T, ? extends R> safe(
            FunctionWithException<? super T, ? extends R, E> f) {
        return new Safe<T, Object, R, E>().decorate(f);
    }

    @SafeVarargs
    static <T, R, E extends Exception> Function<? super T, ? extends R> safe(
            FunctionWithException<? super T, ? extends R, E> f,
            Class<? extends Exception>... exceptions) {
        return new Safe<T, Object, R, E>(exceptions).decorate(f);
    }

    static <T, U, R, E extends Exception> BiFunction<? super T, ? super U, ? extends R> safe(
            BiFunctionWithException<? super T, ? super U, ? extends R, E> f) {
        return new Safe<T, U, R, E>().decorate(f);
    }

    @SafeVarargs
    static <T, U, R, E extends Exception> BiFunction<? super T, ? super U, ? extends R> safe(
            BiFunctionWithException<? super T, ? super U, ? extends R, E> f,
            Class<? extends Exception>... exceptions) {
        return new Safe<T, U, R, E>(exceptions).decorate(f);
    }


    static <R, E extends Exception> R safely(SupplierWithException<? extends R, E> f) {
        return safe(f).get();
    }

    @SafeVarargs
    static <R, E extends Exception> R safely(SupplierWithException<? extends R, E> f,
            Class<? extends Exception>... exceptions) {
        return safe(f, exceptions).get();
    }

}
