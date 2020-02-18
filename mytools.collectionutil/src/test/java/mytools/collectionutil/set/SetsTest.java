package mytools.collectionutil.set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class SetsTest {

    private static final Collection<String> ONE =
            Arrays.asList("one", "two", "three");
    private static final Collection<String> TWO =
            Set.copyOf(Arrays.asList("two", "three", "four"));

    @Test
    public void setOperations() {
        testSetOperation(Set.of("one", "two", "three", "four"),
                Sets::union, Sets::union, true);
        testSetOperation(Set.of("one"),
                Sets::difference, Sets::difference, false);
        testSetOperation(Set.of("two", "three"),
                Sets::intersection, Sets::intersection, true);
        testSetOperation(Set.of("one", "four"),
                Sets::complement, Sets::complement, true);
    }

    private static void testSetOperation(
            Set<String> expected,
            BiFunction<Collection<String>, Collection<String>, Set<String>> f,
            TriFunction fWithFactory, boolean isSimmetric) {
        assertEquals(expected, f.apply(ONE, TWO));
        assertEquals(expected, fWithFactory.apply(ONE, TWO, TreeSet::new));
        assertEquals(expected, fWithFactory.apply(ONE, TWO, HashSet::new));
        if (isSimmetric) {
            assertEquals(f.apply(ONE, TWO), f.apply(TWO, ONE));
        }
        assertTrue(fWithFactory.apply(ONE, TWO,
                TreeSet::new).getClass() == TreeSet.class);
    }

    @FunctionalInterface
    private interface TriFunction {
        Set<String> apply(Collection<String> c1, Collection<String> c2,
                Supplier<Set<String>> setFactory);
    }

}
