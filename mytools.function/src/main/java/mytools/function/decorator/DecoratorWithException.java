package mytools.function.decorator;

import static mytools.function.Conversions.toBC;
import static mytools.function.Conversions.toBF;
import static mytools.function.Conversions.toC;
import static mytools.function.Conversions.toF;
import static mytools.function.Conversions.toR;
import static mytools.function.Conversions.toS;

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
 * The purpose of this decorator is to transform functions which throw checked
 * exceptions into functions which throw unchecked exception. See a description
 * of the {@link Decorator} class on how decorators work.
 *
 * @see Decorator
 */
public interface DecoratorWithException<T,U,R> {

    /**
     * Decorate a {@link RunnableWithException}
     */
    default Runnable decorate(RunnableWithException f) {
        return toR(decorate(toBF(f)));
    }

    /**
     * Decorate a {@link SupplierWithException}
     */
    default Supplier<R> decorate(SupplierWithException<R> f) {
        return toS(decorate(toBF(f)));
    }

    /**
     * Decorate a {@link ConsumerWithException}
     */
    default Consumer<T> decorate(ConsumerWithException<T> f) {
        return toC(decorate(toBF(f)));
    }

    /**
     * Decorate a {@link FunctionWithException}
     */
    default Function<T, R> decorate(FunctionWithException<T, R> f) {
        return toF(decorate(toBF(f)));
    }

    /**
     * Decorate a {@link BiConsumerWithException}
     */
    default BiConsumer<T, U> decorate(BiConsumerWithException<T, U> f) {
        return toBC(decorate(toBF(f)));
    }

    /**
     * Decorate a {@link BiFunctionWithException}
     */
    BiFunction<T, U, R> decorate(BiFunctionWithException<T, U, R> f);
}
