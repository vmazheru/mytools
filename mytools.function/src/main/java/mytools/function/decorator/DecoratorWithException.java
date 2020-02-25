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

    default SupplierWithException<R, E> decorate(
            SupplierWithException<R, E> f) {
        return toS(decorate(toBF(f)));
    }

    default ConsumerWithException<T, E> decorate(
            ConsumerWithException<T, E> f) {
        return toC(decorate(toBF(f)));
    }

    default FunctionWithException<T, R, E> decorate(
            FunctionWithException<T, R, E> f) {
        return toF(decorate(toBF(f)));
    }

    default BiConsumerWithException<T, U, E> decorate(
            BiConsumerWithException<T, U, E> f) {
        return toBC(decorate(toBF(f)));
    }

    @Override
    default BiFunction<T, U, R> decorate(BiFunction<T, U, R> f) {
        return unchecked(decorate(withException(f)));
    }

    BiFunctionWithException<T, U, R, E> decorate(
            BiFunctionWithException<T, U, R, E> f);
}
