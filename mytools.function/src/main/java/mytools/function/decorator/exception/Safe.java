package mytools.function.decorator.exception;

import java.util.stream.Stream;

/**
 * This exception decorator implementation make execution of operations "safe",
 * that is it returns null instead of throwing an exception should something go wrong.
 * 
 * <p>An optional array of exception classes may be given to the decorator, so
 * it will only check for exceptions of these classes. Otherwise, it'll return 
 * null in case of any type of exception.
 */
final class Safe <T,U,R> extends AbstractExceptionHandlingDecorator<T,U,R> {
    
    private final Class<? extends Exception>[] catchExceptions;

    @SafeVarargs
    Safe(Class<? extends Exception> ... catchExceptions) {
        this.catchExceptions = catchExceptions;
    }
    
    @Override
    R dealWithException(Exception e) {
        if (catchExceptions == null || catchExceptions != null && exceptionMatches(e)) {
            return null;
        }
        throw toUnchecked(e);
    }
    
    private boolean exceptionMatches(Exception e) {
        return Stream.of(catchExceptions).anyMatch(klass -> klass.isInstance(e));
    }

}
