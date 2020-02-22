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
public interface DecoratorWithException<T, U, R> extends Decorator<T, U, R> {

    default RunnableWithException decorate(RunnableWithException f) {
        return toR(decorate(toBF(f)));
    }

    default SupplierWithException<R> decorate(SupplierWithException<R> f) {
        return toS(decorate(toBF(f)));
    }

    default ConsumerWithException<T> decorate(ConsumerWithException<T> f) {
        return toC(decorate(toBF(f)));
    }

    default FunctionWithException<T, R> decorate(
            FunctionWithException<T, R> f) {
        return toF(decorate(toBF(f)));
    }

    default BiConsumerWithException<T, U> decorate(
            BiConsumerWithException<T, U> f) {
        return toBC(decorate(toBF(f)));
    }

    @Override
    default BiFunction<T, U, R> decorate(BiFunction<T, U, R> f) {
        return unchecked(decorate(withException(f)));
    }

    BiFunctionWithException<T, U, R> decorate(
            BiFunctionWithException<T, U, R> f);
}
