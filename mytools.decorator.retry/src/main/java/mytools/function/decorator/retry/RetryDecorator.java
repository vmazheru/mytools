package mytools.function.decorator.retry;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import mytools.function.BiFunctionWithException;
import mytools.function.decorator.DecoratorWithException;
import mytools.util.thread.Threads;

/**
 * This decorator applies retry logic as defined in the given
 * {@link RetryPoliy}.
 *
 * <p>
 * The decorator may be given a list of exception types which it should
 * intercept. If no array of exception classes is given, the decorator will
 * retry on any exception type.
 *
 * <p>
 * Exception types are checked with {@code Class.isInstance} method, so the
 * decorator will intercept exception subclasses as well.
 *
 * <p>
 * Also, the decorator may be given callbacks which it executes right after an
 * exception is caught, and after it is done sleeping.
 *
 * @see RetryPolicy
 */
final class RetryDecorator<T, U, R, E extends Exception>
    implements DecoratorWithException<T, U, R, E> {

    private final RetryPolicy retryPolicy;
    private final List<Class<? extends E>> exceptionClasses;
    private final Optional<Consumer<? super E>> before;
    private final Optional<Runnable> after;

    /**
     * Package access constructor.
     *
     * @param retryPolicy      Retry policy
     * @param exceptionClasses List of exception classes on which the decorator
     *                         should retry
     * @param before           A lambda to run on exception thrown (before the
     *                         decorator goes to sleep)
     * @param after            A lambda to run after the decorator wakes up from
     *                         sleeping
     */
    RetryDecorator(
            RetryPolicy retryPolicy,
            List<Class<? extends E>> exceptionClasses,
            Consumer<? super E> before,
            Runnable after) {
        this.retryPolicy = retryPolicy;
        this.exceptionClasses = exceptionClasses;
        this.before = Optional.ofNullable(before);
        this.after  = Optional.ofNullable(after);
    }

    /**
     * Implements the logic of this decorator. This decorator will:
     *
     * <ol>
     * <li>Wrap a function in try/catch and call it.</li>
     * <li>
     *   If exception happens, check if it is of one of the target types (if
     *   any).
     * </li>
     * <li>
     *   Consult the retry policy on how long to sleep before the next retry.
     *   If the retry policy returns 0, stop retrying and
     *   re-throw the exception.
     * </li>
     * <li>
     *   Else if the retry policy returns a result greater than zero, execute
     *   "before" call back (if given), sleep for specified time, and execute an
     *   "after" callback (if given)
     * </li>
     * </ol>
     */
    @Override
    public BiFunctionWithException<T, U, R, E> decorate(
            BiFunctionWithException<T, U, R, E> f) {
        return (t, u) -> {
            while (true) {
                try {
                    return f.apply(t, u);
                } catch (Exception e) {
                    @SuppressWarnings("unchecked") E ex = (E) e;
                    if (ofTargetClass(ex)) {
                        long sleepTime = retryPolicy.nextRetryIn();
                        if (sleepTime >= 0) {
                            before.ifPresent(before -> before.accept(ex));
                            if (sleepTime > 0) {
                                Threads.sleep(sleepTime);
                            }
                            after.ifPresent(after -> after.run());
                            continue;
                        }
                    }
                    throw e;
                }
            }
        };
    }

    private boolean ofTargetClass(Exception e) {
        if (exceptionClasses == null || exceptionClasses.isEmpty()) {
            return true;
        }
        for (Class<? extends E> klass : exceptionClasses) {
            if (klass.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

}
