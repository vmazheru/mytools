package mytools.function.object;

public final class Counter {

    private int i;
    private final int initValue;
    
    public Counter(int initValue) {
        this.initValue = initValue;
        i = initValue;
    }
    
    public Counter() {
        this(0);
    }

    public void add(int increment) {
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
    
    public int get() {
        return i;
    }
    
    public int getAndIncrement() {
        int n = i;
        i++;
        return n;
    }

    public int getAndReset() {
        int n = i;
        reset();
        return n;
    }
    
    public int incrementAndGet() {
        return ++i;
    }
    
    @Override
    public String toString() {
        return Integer.toString(i);
    }
}
