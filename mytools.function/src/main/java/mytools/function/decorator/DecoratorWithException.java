package mytools.function.decorator;

import static mytools.function.Conversions.toBC;
import static mytools.function.Conversions.toBF;
import static mytools.function.Conversions.toC;
import static mytools.function.Conversions.toF;
import static mytools.function.Conversions.toR;
import static mytools.function.Conversions.toS;
import static mytools.function.Conversions.withException;
import static mytools.function.decorator.exception.ExceptionDecorators.unchecked;

import java.util.function.BiFunction;

import mytools.function.BiConsumerWithException;
import mytools.function.BiFunctionWithException;
import mytools.function.ConsumerWithException;
import mytools.function.FunctionWithException;
import mytools.function.RunnableWithException;
import mytools.function.SupplierWithException;

/**
 * Add decorating function with exceptions to the original {@link Decorator}
 */
public interface DecoratorWithException<T, U, R, E extends Exception>
    extends Decorator<T, U, R> {

    default RunnableWithException<E> decorate(RunnableWithException<E> f) {
        return toR(decorate(toBF(f)));
    }

    default SupplierWithException<? extends R, E> decorate(
            SupplierWithException<? extends R, E> f) {
        return toS(decorate(toBF(f)));
    }

    default ConsumerWithException<? super T, E> decorate(
            ConsumerWithException<? super T, E> f) {
        return toC(decorate(toBF(f)));
    }

    default FunctionWithException<? super T, ? extends R, E> decorate(
            FunctionWithException<? super T, ? extends R, E> f) {
        return toF(decorate(toBF(f)));
    }

    default BiConsumerWithException<? super T, ? super U, E> decorate(
            BiConsumerWithException<? super T, ? super U, E> f) {
        return toBC(decorate(toBF(f)));
    }

    @Override
    default BiFunction<? super T, ? super U, ? extends R> decorate(BiFunction<? super T, ? super U, ? extends R> f) {
        return unchecked(decorate(withException(f)));
    }

    BiFunctionWithException<? super T, ? super U, ? extends R, E> decorate(
            BiFunctionWithException<? super T, ? super U, ? extends R, E> f);
}
