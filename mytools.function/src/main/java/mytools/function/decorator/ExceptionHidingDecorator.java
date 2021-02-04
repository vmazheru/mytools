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
public interface ExceptionHidingDecorator<T, U, R, E extends Exception> {

    default Runnable decorate(RunnableWithException<E> f) {
        return toR(decorate(toBF(f)));
    }

    default Supplier<? extends R> decorate(SupplierWithException<? extends R, E> f) {
        return toS(decorate(toBF(f)));
    }

    default Consumer<? super T> decorate(ConsumerWithException<? super T, E> f) {
        return toC(decorate(toBF(f)));
    }

    default Function<? super T, ? extends R> decorate(FunctionWithException<? super T, ? extends R, E> f) {
        return toF(decorate(toBF(f)));
    }

    default BiConsumer<? super T, ? super U> decorate(BiConsumerWithException<? super T, ? super U, E> f) {
        return toBC(decorate(toBF(f)));
    }

    BiFunction<? super T, ? super U, ? extends R> decorate(BiFunctionWithException<? super T, ? super U, ? extends R, E> f);
}
