package mytools.function.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class HolderTest {

    private static final Integer FOURTY_TWO = 42;
    private static final Integer FOURTY_THREE = 43;

    @Test
    public void constructor() {
        assertNull(new Holder<>().get());
    }

    @Test
    public void set() {
        assertEquals(FOURTY_TWO, new Holder<>(FOURTY_TWO).get());
    }

    @Test
    public void getAndSet() {
        Holder<Integer> h = new Holder<>(FOURTY_TWO);
        assertEquals(FOURTY_TWO, h.getAndSet(FOURTY_THREE));
        assertEquals(FOURTY_THREE, h.get());
    }
}
