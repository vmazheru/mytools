package mytools.function.object;

/**
 * This class should be used in lambda context where incrementing an
 * primitive long cannot be done. This class is not thread safe.
 */
public final class LongCounter {

    private long i;
    private final long initValue;

    public LongCounter(long initValue) {
        this.initValue = initValue;
        i = initValue;
    }

    public LongCounter() {
        this(0);
    }

    public void add(long increment) {
        i += increment;
    }

    public void increment() {
        i++;
    }

    public void decrement() {
        i--;
    }

    public void reset() {
        i = initValue;
    }

    public long get() {
        return i;
    }

    public long getAndIncrement() {
        long n = i;
        i++;
        return n;
    }

    public long getAndReset() {
        long n = i;
        reset();
        return n;
    }

    public long incrementAndGet() {
        return ++i;
    }

    @Override
    public String toString() {
        return Long.toString(i);
    }
}
