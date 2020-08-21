package mytools.function.decorator.retry;

/**
 * Retry policy defines the way of how code will be re-executed when an
 * exception is thrown.
 *
 * @see RetryDecorator
 * @see RetryDecorators
 */
@FunctionalInterface
public interface RetryPolicy {

    /**
     * Return a number of milliseconds which code should be suspended for
     * before the next retry takes place.
     * Returning a negative value forces he decorator to give up and
     * re-throw the exception.
     *
     * @return number of milliseconds for which the code will be sleeping
     *         until the next re-try happens, or zero which indicates
     *         that no more re-tries should take place.
     */
    long nextRetryIn();
}
