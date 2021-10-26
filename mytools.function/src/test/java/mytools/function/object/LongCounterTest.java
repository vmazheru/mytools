package mytools.function.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class LongCounterTest {

    private static final int FOURTY_TWO = 42;

    @Test
    public void constructor() {
        assertEquals(0, new LongCounter().get());
        assertEquals(FOURTY_TWO, new LongCounter(FOURTY_TWO).get());
    }

    @Test
    public void increment() {
        LongCounter c = new LongCounter();
        c.increment();
        assertEquals(1, c.get());
    }

    @Test
    public void decrement() {
        LongCounter c = new LongCounter();
        c.decrement();
        assertEquals(-1, c.get());
    }

    @Test
    public void reset() {
        int initValue = FOURTY_TWO;
        LongCounter c = new LongCounter(initValue);
        c.increment();
        c.increment();
        assertNotEquals(initValue, c.get());
        c.reset();
        assertEquals(initValue, c.get());
    }

    @Test
    public void getAndIncrement() {
        LongCounter c = new LongCounter();
        assertEquals(0, c.getAndIncrement());
        assertEquals(1, c.get());
    }

    @Test
    public void incrementAndGet() {
        LongCounter c = new LongCounter();
        assertEquals(1, c.incrementAndGet());
        assertEquals(1, c.get());
    }

    @Test
    public void add() {
        LongCounter c = new LongCounter();
        c.add(FOURTY_TWO);
        assertEquals(FOURTY_TWO, c.get());
    }

}
