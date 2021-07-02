package mytools.function.decorator.batch;

import static mytools.function.decorator.batch.BatchDecorators.batch;
import static mytools.function.decorator.batch.BatchDecorators.batched;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import mytools.function.object.Counter;

public class BatchDecoratorsTest {

    @Test
    public void batchedConsumer() {
        int batchSize = 3;
        List<Integer> ints = Arrays.asList(0,1,2,3,4,5,6,7,8,9);

        Counter numProcessed = new Counter();

        Consumer<List<Integer>> f = list -> {
            assertTrue(list.size() <= batchSize);
            numProcessed.add(list.size());
        };

        batched(batchSize, f).accept(ints);
        assertEquals(ints.size(), numProcessed.get());

        numProcessed.reset();
        batch(ints, batchSize, f);
        assertEquals(ints.size(), numProcessed.get());
    }

    @Test
    public void batchedConsumerWithGroupFunctionAndSortedInput() {
        int batchSize = 5;
        List<String> fruits = Arrays.asList(
                "Apple 1", "Apple 2", "Avocado 1",
                "Orange 1", "Orange 2", "Orange 3", "Orange 4", "Orange 5");

        Counter numProcessed = new Counter();

        Function<String, String> groupFunction = s -> s.split(" ")[0];

        Consumer<List<String>> f = list -> {
            assertTrue(list.size() <= batchSize);
            assertSorted(list, groupFunction);
            numProcessed.add(list.size());
        };

        batched(batchSize, true, groupFunction, f).accept(fruits);
        assertEquals(fruits.size(), numProcessed.get());

        numProcessed.reset();
        batch(fruits, batchSize, true, groupFunction, f);
        assertEquals(fruits.size(), numProcessed.get());
    }

    @Test
    public void batchedConsumerWithGroupFunctionAndNotSortedInput() {
        int batchSize = 5;
        List<String> fruits = Arrays.asList(
                "Apple 1", "Apple 2", "Avocado 1",
                "Orange 1", "Orange 2", "Orange 3", "Orange 4", "Orange 5");

        Collections.shuffle(fruits);

        Counter numProcessed = new Counter();

        Function<String, String> groupFunction = s -> s.split(" ")[0];

        Consumer<List<String>> f = list -> {
            assertTrue(list.size() <= batchSize);
            assertSorted(list, groupFunction);
            numProcessed.add(list.size());
        };

        batched(batchSize, false, groupFunction, f).accept(fruits);
        assertEquals(fruits.size(), numProcessed.get());

        numProcessed.reset();
        batch(fruits, batchSize, false, groupFunction, f);
        assertEquals(fruits.size(), numProcessed.get());

        numProcessed.reset();
        batch(fruits, batchSize, groupFunction, f);
        assertEquals(fruits.size(), numProcessed.get());
    }

    private static <T, C extends Comparable<C>> void assertSorted(
            List<T> l, Function<T, C> groupFunction) {
        List<C> r = new ArrayList<>();
        for (T t : l) r.add(groupFunction.apply(t));

        C prev = null;
        for (C elem : r) {
            if (prev != null) {
                assertTrue(prev.compareTo(elem) <= 0);
            }
            prev = elem;
        }
    }

}
