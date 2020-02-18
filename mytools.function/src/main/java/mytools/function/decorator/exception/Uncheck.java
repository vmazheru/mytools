package mytools.function.decorator.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * Transform a function which throws checked exception into a function which
 * throws unchecked exception. This unchecked exception will be either of class
 * given as constructor parameter, or of RuntimeException/UncheckedIOException
 * class
 */
final class Uncheck <T,U,R> extends AbstractExceptionHandlingDecorator<T,U,R> {

    private final Class<? extends RuntimeException> exceptionClass;

    private String s;

    Uncheck() {
        this(null);
    }

    Uncheck(Class<? extends RuntimeException> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    @Override
    R dealWithException(Exception e) {
        throw (exceptionClass != null) ?
                toSpecificException(e) : toUnchecked(e);
    }

    private RuntimeException toSpecificException(Exception e) {
        try {
            RuntimeException ex = exceptionClass.getConstructor(Throwable.class)
                    .newInstance(e);
            return ex;
        } catch (NoSuchMethodException  | InvocationTargetException |
                 IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException(
                    "Error calling non-argument constructor of class " +
                            exceptionClass + ": " + ex.getMessage());
        }
    }
}
