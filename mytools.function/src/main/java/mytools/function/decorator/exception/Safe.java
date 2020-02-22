package mytools.function.decorator.exception;

import java.util.stream.Stream;

/**
 * Transform a function which throws exception into a function which returns
 * null in cases when exception is thrown.
 */
final class Safe <T, U, R> extends AbstractExceptionHandlingDecorator<T, U, R> {

    private final Class<? extends Exception>[] catchExceptions;

    @SafeVarargs
    Safe(Class<? extends Exception>... catchExceptions) {
        this.catchExceptions = catchExceptions;
    }

    @Override
    R dealWithException(Exception e) {
        if (catchExceptions.length == 0 ||
            catchExceptions != null && exceptionMatches(e)) {
            return null;
        }
        throw toUnchecked(e);
    }

    private boolean exceptionMatches(Exception e) {
        return Stream.of(catchExceptions).anyMatch(
                klass -> klass.isInstance(e));
    }

}
