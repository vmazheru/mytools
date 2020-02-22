package mytools.function.decorator.exception;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.BiFunction;

import mytools.function.BiFunctionWithException;
import mytools.function.decorator.ExceptionHidingDecorator;

abstract class AbstractExceptionHandlingDecorator<T, U, R>
    implements ExceptionHidingDecorator<T, U, R> {

    @Override
    public BiFunction<T, U, R> decorate(BiFunctionWithException<T, U, R> f) {
        return (t, u) -> {
            try {
                return f.apply(t, u);
            } catch (Exception e) {
                return dealWithException(e);
            }
        };
    }

    abstract R dealWithException(Exception e);

    static RuntimeException toUnchecked(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else if (e instanceof IOException) {
            return new UncheckedIOException((IOException) e);
        }
        return new RuntimeException(e);
    }

}
