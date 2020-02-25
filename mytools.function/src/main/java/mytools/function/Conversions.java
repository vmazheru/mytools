package mytools.function;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class contains method which "convert" between operations
 * of different types.
 *
 * <p>Whenever the conversion happens from the more specific interface
 * to the more general one, for example a function is getting converted into
 * a bi-function, the more general function's arguments/results missing for
 * the more specific function are treated as nulls.
 *
 * <p>When the conversion, on the other hand, happens from the more general
 * function to the more specific one, then the more general's function
 * arguments or results are discarded.
 */
public interface Conversions {

    //////////////////////  Conversions to Runnable /////////////////////////

    /**
     * Convert a {@code Supplier} into a {@code Runnable}.
     * The supplier's return value will be discarded.
     */
    static Runnable toR(Supplier<?> s) {
        return () -> s.get();
    }

    /**
     * Convert a {@link SupplierWithException}
     * into a {@link RunnableWithException}.
     * The supplier's return value will be discarded.
     */
    static <E extends Exception> RunnableWithException<E> toR(
            SupplierWithException<?, E> s) {
        return () -> s.get();
    }

    /**
     * Convert a {@code Consumer} to a {@code Runnable}.
     * Null will be passed to the consumer as an argument.
     */
    static Runnable toR(Consumer<?> c) {
        return () -> c.accept(null);
    }

    /**
     * Convert a {@link ConsumerWithException}
     * into a {@link RunnableWithException}.
     * Null will be passed to the consumer as an argument.
     */
    static <E extends Exception> RunnableWithException<E> toR(
            ConsumerWithException<?, E> c) {
        return () -> c.accept(null);
    }

    /**
     * Convert a {@code BiConsumer} into a {@code Runnable}.
     * Null values will be passed to the consumer as arguments.
     */
    static Runnable toR(BiConsumer<?, ?> c) {
        return () -> c.accept(null, null);
    }

    /**
     * Convert a {@link BiConsumerWithException}
     * into a {@link RunnableWithException}.
     * Null values will be passed to the consumer as arguments.
     */
    static <E extends Exception> RunnableWithException<E> toR(
            BiConsumerWithException<?, ?, E> c) {
        return () -> c.accept(null, null);
    }

    /**
     * Convert a {@code Function} into a {@code Runnable}.
     * Null will be passed to the function as an argument and
     * its return value will be discarded.
     */
    static Runnable toR(Function<?, ?> f) {
        return () -> f.apply(null);
    }

    /**
     * Convert a {@link FunctionWithException}
     * into a {@link RunnableWithException}.
     * Null will be passed to the function as an argument and
     * its return value will be discarded.
     */
    static <E extends Exception> RunnableWithException<E> toR(
            FunctionWithException<?, ?, E> f) {
        return () -> f.apply(null);
    }

    /**
     * Convert a {@code BiFunction} into a {@code Runnable}.
     * Null values will be passed to the function as arguments and
     * its return value will be discarded.
     */
    static Runnable toR(BiFunction<?, ?, ?> f) {
        return () -> f.apply(null, null);
    }

    /**
     * Convert a {@link BiFunctionWithException}
     * into a {@link RunnableWithException}.
     * Null values will be passed to the function as arguments and
     * its return value will be discarded.
     */
    static <E extends Exception> RunnableWithException<E> toR(
            BiFunctionWithException<?, ?, ?, E> f) {
        return () -> f.apply(null, null);
    }

    //////////////////////Conversions to Supplier ///////////////////////////

    /**
     * Convert a {@code Runnable} into a {@code Supplier}.
     * The return value of the supplier will be null.
     */
    static <R> Supplier<R> toS(Runnable r) {
        return () -> {
            r.run();
            return null;
        };
    }

    /**
     * Convert a {@link RunnableWithException}
     * into a {@link SupplierWithException}.
     * The return value of the supplier will be null.
     */
    static <R, E extends Exception> SupplierWithException<R, E> toS(
            RunnableWithException<E> r) {
        return () -> {
            r.run();
            return null;
        };
    }

    /**
     * Convert a {@code Consumer} into a {@code Supplier}.
     * The value passed to the consumer will be null.
     * The return value of the supplier will be null.
     */
    static <R> Supplier<R> toS(Consumer<?> c) {
        return () -> {
            c.accept(null);
            return null;
        };
    }

    /**
     * Convert a {@link ConsumerWithException}
     * into a {@link SupplierWithException}.
     * The value passed to the consumer will be null.
     * The return value of the supplier will be null.
     */
    static <R, E extends Exception> SupplierWithException<R, E> toS(
            ConsumerWithException<?, E> c) {
        return () -> {
            c.accept(null);
            return null;
        };
    }

    /**
     * Convert a {@code BiConsumer} into a {@code Supplier}.
     * The arguments passed to the bi-consumer will be nulls.
     * The return value of the supplier will be null.
     */
    static <R> Supplier<R> toS(BiConsumer<?, ?> c) {
        return () -> {
            c.accept(null, null);
            return null;
        };
    }

    /**
     * Convert a {@link BiConsumerWithException}
     * into a {@code SupplierWithException}.
     * The arguments passed to the bi-consumer will be nulls.
     * The return value of the supplier will be null.
     */
    static <R, E extends Exception> SupplierWithException<R, E> toS(
            BiConsumerWithException<?, ?, E> c) {
        return () -> {
            c.accept(null, null);
            return null;
        };
    }

    /**
     * Convert a {@code Function} into a {@code Supplier}.
     * The function argument value will be null.
     * The supplier will return the value returned by the underlying function.
     */
    static <R> Supplier<R> toS(Function<?, R> f) {
        return () -> f.apply(null);
    }

    /**
     * Convert a {@link FunctionWithException}
     * into a {@link SupplierWithException}.
     * The function argument value will be null.
     * The supplier will return the value returned by the underlying function.
     */
    static <R, E extends Exception> SupplierWithException<R, E> toS(
            FunctionWithException<?, R, E> f) {
        return () -> f.apply(null);
    }

    /**
     * Convert a {@code BiFunction} into a {@code Supplier}.
     * The function argument values will be nulls.
     * The supplier will return the value returned by the underlying function.
     */
    static <R> Supplier<R> toS(BiFunction<?, ?, R> f) {
        return () -> f.apply(null, null);
    }

    /**
     * Convert a {@link BiFunctionWithException}
     * into a {@link SupplierWithException}.
     * The function argument values will be nulls.
     * The supplier will return the value returned by the underlying function.
     */
    static <R, E extends Exception> SupplierWithException<R, E> toS(
            BiFunctionWithException<?, ?, R, E> f) {
        return () -> f.apply(null, null);
    }


    //////////////////////Conversions to Consumer ////////////////////////////

    /**
     *  Convert a {@code Runnable} into a {@code Consumer}.
     *  The argument passed to the consumer will be discarded.
     */
    static <T> Consumer<T> toC(Runnable r) {
        return (t) -> r.run();
    }

    /**
     *  Convert a {@link RunnableWithException}
     *  into a {@link ConsumerWithException}.
     *  The argument passed to the consumer will be discarded.
     */
    static <T, E extends Exception> ConsumerWithException<T, E> toC(
            RunnableWithException<E> r) {
        return (t) -> r.run();
    }

    /**
     * Convert a {@code Supplier} into a {@code Consumer}.
     * The argument passed to the consumer will be discarded.
     * The return value of the supplier will be discarded.
     */
    static <T> Consumer<T> toC(Supplier<?> s) {
        return (t) -> s.get();
    }

    /**
     * Convert a {@link SupplierWithException}
     * into a {@link ConsumerWithException}.
     * The argument passed to the consumer will be discarded.
     * The return value of the supplier will be discarded.
     */
    static <T, E extends Exception> ConsumerWithException<T, E> toC(
            SupplierWithException<?, E> s) {
        return (t) -> s.get();
    }

    /**
     * Convert a {@code BiConsumer} into a {@code Consumer}.
     * The argument passed to the consumer will be passed to the bi-consumer as
     * its first argument.
     * The second bi-consumer argument will be null.
     */
    static <T> Consumer<T> toC(BiConsumer<T, ?> c) {
        return (t) -> c.accept(t, null);
    }

    /**
     * Convert a {@link BiConsumerWithException}
     * into a {@link ConsumerWithException}.
     * The argument passed to the consumer will be passed to the bi-consumer as
     * its first argument.
     * The second bi-consumer argument will be null.
     */
    static <T, E extends Exception> ConsumerWithException<T, E> toC(
            BiConsumerWithException<T, ?, E> c) {
        return (t) -> c.accept(t, null);
    }

    /**
     * Convert a {@code Function} into a {@code Consumer}.
     * The argument passed to the consumer will be passed to the function.
     * The function return value will be discarded.
     */
    static <T> Consumer<T> toC(Function<T, ?> f) {
        return (t) -> f.apply(t);
    }

    /**
     * Convert a {@link FunctionWithException}
     * into a {@link ConsumerWithException}.
     * The argument passed to the consumer will be passed to the function.
     * The function return value will be discarded.
     */
    static <T, E extends Exception> ConsumerWithException<T, E> toC(
            FunctionWithException<T, ?, E> f) {
        return (t) -> f.apply(t);
    }

    /**
     * Convert a {@code BiFunction} into a {@code Consumer}.
     * The argument passed to the consumer will be passed to the function
     * as its first argument.
     * The second function's argument will be null.
     * The function return value will be discarded.
     */
    static <T> Consumer<T> toC(BiFunction<T, ?, ?> f) {
        return (t) -> f.apply(t, null);
    }

    /**
     * Convert a {@link BiFunctionWithException}
     * into a {@link ConsumerWithException}.
     * The argument passed to the consumer will be passed to the function
     * as its first argument.
     * The second function's argument will be null.
     * The function return value will be discarded.
     */
    static <T, E extends Exception> ConsumerWithException<T, E> toC(
            BiFunctionWithException<T, ?, ?, E> f) {
        return (t) -> f.apply(t, null);
    }


    //////////////////////Conversions to BiConsumer //////////////////////////

    /**
     * Convert a {@code Runnable} into a {@code BiConsumer}.
     * The consumer arguments will be discarded.
     */
    static <T, U> BiConsumer<T, U> toBC(Runnable r) {
        return (t, u) -> r.run();
    }

    /**
     * Convert a {@link RunnableWithException}
     * into a {@link BiConsumerWithException}.
     * The consumer arguments will be discarded.
     */
    static <T, U, E extends Exception> BiConsumerWithException<T, U, E> toBC(
            RunnableWithException<E> r) {
        return (t, u) -> r.run();
    }

    /**
     * Convert a {@code Supplier} into a {@code BiConsumer}.
     * The consumer arguments will be discarded.
     * The supplier return value will be discarded.
     */
    static <T, U> BiConsumer<T, U> toBC(Supplier<?> s) {
        return (t, u) -> s.get();
    }

    /**
     * Convert a {@link SupplierWithException}
     * into a {@link BiConsumerWithException}.
     * The consumer arguments will be discarded.
     * The supplier return value will be discarded.
     */
    static <T, U, E extends Exception> BiConsumerWithException<T, U, E> toBC(
            SupplierWithException<?, E> s) {
        return (t, u) -> s.get();
    }

    /**
     * Convert a {@code Consumer} into a {@code BiConsumer}.
     * The first argument to the bi-consumer will be passed as an argument
     * to the consumer.
     * The second argument to the bi-consumer will be discarded.
     */
    static <T, U> BiConsumer<T, U> toBC(Consumer<T> c) {
        return (t, u) -> c.accept(t);
    }

    /**
     * Convert a {@link ConsumerWithException}
     * into a {@link BiConsumerWithException}.
     * The first argument to the bi-consumer will be passed as an argument
     * to the consumer.
     * The second argument to the bi-consumer will be discarded.
     */
    static <T, U, E extends Exception> BiConsumerWithException<T, U, E> toBC(
            ConsumerWithException<T, E> c) {
        return (t, u) -> c.accept(t);
    }

    /**
     * Convert a {@code Function} into a {@code BiConsumer}.
     * The first argument to the bi-consumer will be passed as an argument
     * to the function.
     * The second argument to the bi-consumer will be discarded.
     * The function return value will be discarded.
     */
    static <T, U> BiConsumer<T, U> toBC(Function<T, ?> f) {
        return (t, u) -> f.apply(t);
    }

    /**
     * Convert a {@link FunctionWithException}
     * into a {@link BiConsumerWithException}.
     * The first argument to the bi-consumer will be passed as an argument
     * to the function.
     * The second argument to the bi-consumer will be discarded.
     * The function return value will be discarded.
     */
    static <T, U, E extends Exception> BiConsumerWithException<T, U, E> toBC(
            FunctionWithException<T, ?, E> f) {
        return (t, u) -> f.apply(t);
    }

    /**
     * Convert a {@code BiFunction} into a {@code BiConsumer}.
     * The first and second arguments to the bi-consumer will be passed
     * to the function.
     * The function return value will be discarded.
     */
    static <T, U> BiConsumer<T, U> toBC(BiFunction<T, U, ?> f) {
        return (t, u) -> f.apply(t, u);
    }

    /**
     * Convert a {@link BiFunctionWithException}
     * into a {@link BiConsumerWithException}.
     * The first and second arguments to the bi-consumer will be passed
     * to the function.
     * The function return value will be discarded.
     */
    static <T, U, E extends Exception> BiConsumerWithException<T, U, E> toBC(
            BiFunctionWithException<T, U, ?, E> f) {
        return (t, u) -> f.apply(t, u);
    }


    //////////////////////Conversions to Function //////////////////////////

    /**
     * Convert a {@code Runnable} into a {@code Function}.
     * The argument passed to the function will be discarded.
     * The return value of the function will be null.
     */
    static <T, R> Function<T, R> toF(Runnable r) {
        return (t) -> {
            r.run();
            return null;
        };
    }

    /**
     * Convert a {@link RunnableWithException}
     * into a {@link FunctionWithException}.
     * The argument passed to the function will be discarded.
     * The return value of the function will be null.
     */
    static <T, R, E extends Exception> FunctionWithException<T, R, E> toF(
            RunnableWithException<E> r) {
        return (t) -> {
            r.run();
            return null;
        };
    }

    /**
     * Convert a {@code Supplier} into a {@code Function}.
     * The argument passed to the function will be discarded.
     * The function will return the value produced by the underlying supplier.
     */
    static <T, R> Function<T, R> toF(Supplier<R> s) {
        return (t) -> s.get();
    }

    /**
     * Convert a {@link SupplierWithException}
     * into a {@link FunctionWithException}.
     * The argument passed to the function will be discarded.
     * The function will return the value produced by the underlying supplier.
     */
    static <T, R, E extends Exception> FunctionWithException<T, R, E> toF(
            SupplierWithException<R, E> s) {
        return (t) -> s.get();
    }

    /**
     * Convert a {@code Consumer} into a {@code Function}.
     * The argument passed to the function will be passed
     * to the underlying consumer.
     * The return value of the function will be null.
     */
    static <T, R> Function<T, R> toF(Consumer<T> c) {
        return (t) -> {
            c.accept(t);
            return null;
        };
    }

    /**
     * Convert a {@link ConsumerWithException}
     * into a {@link FunctionWithException}.
     * The argument passed to the function will be passed
     * to the underlying consumer.
     * The return value of the function will be null.
     */
    static <T, R, E extends Exception> FunctionWithException<T, R, E> toF(
            ConsumerWithException<T, E> c) {
        return (t) -> {
            c.accept(t);
            return null;
        };
    }

    /**
     * Convert a {@code BiConsumer} into a {@code Function}.
     * The argument passed to the function will be passed as the first argument
     * to the underlying consumer.
     * The second argument to the consumer will receive null value.
     * The return value of the function will be null.
     */
    static <T, R> Function<T, R> toF(BiConsumer<T, ?> c) {
        return (t) -> {
            c.accept(t, null);
            return null;
        };
    }

    /**
     * Convert a {@link BiConsumerWithException}
     * into a {@link FunctionWithException}.
     * The argument passed to the function will be passed as the first argument
     * to the underlying consumer.
     * The second argument to the consumer will receive null value.
     * The return value of the function will be null.
     */
    static <T, R, E extends Exception> FunctionWithException<T, R, E> toF(
            BiConsumerWithException<T, ?, E> c) {
        return (t) -> {
            c.accept(t, null);
            return null;
        };
    }

    /**
     * Convert a {@code BiFunction} into a {@code Function}.
     * The argument passed to the function will be passed as the first argument
     * to the underlying bi-function.
     * The second argument to the bi-function will receive null value.
     * The return value of the function will be null.
     */
    static <T, R> Function<T, R> toF(BiFunction<T, ?, R> f) {
        return (t) -> f.apply(t, null);
    }

    /**
     * Convert a {@link BiFunctionWithException}
     * into a {@link FunctionWithException}.
     * The argument passed to the function will be passed as the first argument
     * to the underlying bi-function.
     * The second argument to the bi-function will receive null value.
     * The return value of the function will be null.
     */
    static <T, R, E extends Exception> FunctionWithException<T, R, E> toF(
            BiFunctionWithException<T, ?, R, E> f) {
        return (t) -> f.apply(t, null);
    }


    //////////////////////Conversions to BiFunction //////////////////////////

    /**
     * Convert a {@code Runnable} to a {@code BiFunction}.
     * The arguments passed to the function will be discarded.
     * The return value of the function will be null.
     */
    static <T, U, R> BiFunction<T, U, R> toBF(Runnable r) {
        return (t, u) -> {
            r.run();
            return null;
        };
    }

    /**
     * Convert a {@link RunnableWithException}
     * to a {@link BiFunctionWithException}.
     * The arguments passed to the function will be discarded.
     * The return value of the function will be null.
     */
    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    toBF(RunnableWithException<E> r) {
        return (t, u) -> {
            r.run();
            return null;
        };
    }

    /**
     * Convert a {@code Supplier} to a {@code BiFunction}.
     * The arguments passed to the function will be discarded.
     * The return value of the function will be the value produced
     * by the underlying supplier.
     */
    static <T, U, R> BiFunction<T, U, R> toBF(Supplier<R> s) {
        return (t, u) -> s.get();
    }

    /**
     * Convert a {@link SupplierWithException}
     * to a {@link BiFunctionWithException}.
     * The arguments passed to the function will be discarded.
     * The return value of the function will be the value produced
     * by the underlying supplier.
     */
    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    toBF(SupplierWithException<R, E> s) {
        return (t, u) -> s.get();
    }

    /**
     * Convert a {@code Consumer} into a {@code BiFunction}.
     * The first argument passed to the function will be passed
     * to the underlying consumer.
     * The second argument passed to the function will be discarded.
     * The return value of the function will be null.
     */
    static <T, U, R> BiFunction<T, U, R> toBF(Consumer<T> c) {
        return (t, u) -> {
            c.accept(t);
            return null;
        };
    }

    /**
     * Convert a {@link ConsumerWithException}
     * into a {@link BiFunctionWithException}.
     * The first argument passed to the function will be passed
     * to the underlying consumer.
     * The second argument passed to the function will be discarded.
     * The return value of the function will be null.
     */
    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    toBF(ConsumerWithException<T, E> c) {
        return (t, u) -> {
            c.accept(t);
            return null;
        };
    }

    /**
     * Convert a {@code BiConsumer} into a {@code BiFunction}.
     * The arguments passed to the function will be passed to the consumer.
     * The return value of the function will be null.
     */
    static <T, U, R> BiFunction<T, U, R> toBF(BiConsumer<T, U> c) {
        return (t, u) -> {
            c.accept(t, u);
            return null;
        };
    }

    /**
     * Convert a {@link BiConsumerWithException}
     * into a {@link BiFunctionWithException}.
     * The arguments passed to the function will be passed to the consumer.
     * The return value of the function will be null.
     */
    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    toBF(BiConsumerWithException<T, U, E> c) {
        return (t, u) -> {
            c.accept(t, u);
            return null;
        };
    }

    /**
     * Convert a {@code Function} into a {@code BiFunction}.
     * The first argument passed to the bi-function will be passed as
     * an argument to the function.
     * The second argument passed to the bi-function will be discarded.
     * The return value of the bi-function will be the value return
     * by the underlying function.
     */
    static <T, U, R> BiFunction<T, U, R> toBF(Function<T, R> f) {
        return (t, u) -> f.apply(t);
    }

    /**
     * Convert a {@link FunctionWithException}
     * into a {@link BiFunctionWithException}.
     * The first argument passed to the bi-function will be passed
     * as an argument to the function.
     * The second argument passed to the bi-function will be discarded.
     * The return value of the bi-function will be the value return
     * by the underlying function.
     */
    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    toBF(FunctionWithException<T, R, E> f) {
        return (t, u) -> f.apply(t);
    }


    ////////////////////// Functions to functions with exceptions  //////////

    static <E extends Exception> RunnableWithException<E> withException(
            Runnable f) {
        return () -> f.run();
    }

    static <R, E extends Exception> SupplierWithException<R, E> withException(
            Supplier<R> f) {
        return () -> f.get();
    }

    static <T, E extends Exception> ConsumerWithException<T, E> withException(
            Consumer<T> f) {
        return t -> f.accept(t);
    }

    static <T, U, E extends Exception> BiConsumerWithException<T, U, E>
    withException(BiConsumer<T, U>f) {
        return (t, u) -> f.accept(t, u);
    }

    static <T, R, E extends Exception> FunctionWithException<T, R, E>
    withException(Function<T, R> f) {
        return t -> f.apply(t);
    }

    static <T, U, R, E extends Exception> BiFunctionWithException<T, U, R, E>
    withException(BiFunction<T, U, R> f) {
        return (t, u) -> f.apply(t, u);
    }

}
