package mytools.function.decorator.retry;

/**
 * A simple {@code RetryPolicy}, where instructs to execute retries for specific
 * number of times with equal pauses between them.
 *
 * <p>
 * This retry policy maintains an internal retry counter, and cannot be used
 * more than once.
 */
public final class LinearRetryPolicy implements RetryPolicy {

    private final int numRetries;
    private final long sleepTime;
    private int counter;

    /**
     * Create an instance of this retry policy.
     *
     * @param numRetries How many times to retry?
     * @param sleepTime  For how long to sleep between retries?
     */
    public LinearRetryPolicy(int numRetries, long sleepTime) {
        if (sleepTime < 0) {
            throw new IllegalArgumentException(
                    "Sleep time must be non-negative");
        }
        this.sleepTime = sleepTime;
        this.numRetries = numRetries;
    }

    @Override
    public long nextRetryIn() {
        return (++counter >= numRetries) ? -1 : sleepTime;
    }

    public int getNumRetries() {
        return numRetries;
    }

    public long getSleepTime() {
        return sleepTime;
    }

}
