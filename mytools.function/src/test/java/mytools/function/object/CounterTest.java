package mytools.function.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class CounterTest {

    private static final int FOURTY_TWO = 42;

    @Test
    public void constructor() {
        assertEquals(0, new Counter().get());
        assertEquals(FOURTY_TWO, new Counter(FOURTY_TWO).get());
    }

    @Test
    public void increment() {
        Counter c = new Counter();
        c.increment();
        assertEquals(1, c.get());
    }

    @Test
    public void decrement() {
        Counter c = new Counter();
        c.decrement();
        assertEquals(-1, c.get());
    }

    @Test
    public void reset() {
        int initValue = FOURTY_TWO;
        Counter c = new Counter(initValue);
        c.increment();
        c.increment();
        assertNotEquals(initValue, c.get());
        c.reset();
        assertEquals(initValue, c.get());
    }

    @Test
    public void getAndIncrement() {
        Counter c = new Counter();
        assertEquals(0, c.getAndIncrement());
        assertEquals(1, c.get());
    }

    @Test
    public void incrementAndGet() {
        Counter c = new Counter();
        assertEquals(1, c.incrementAndGet());
        assertEquals(1, c.get());
    }

    @Test
    public void add() {
        Counter c = new Counter();
        c.add(FOURTY_TWO);
        assertEquals(FOURTY_TWO, c.get());
    }

}
