package mytools.function.decorator.batch;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import mytools.util.thread.Threads;

public class BatchDecoratorTest {

    private static final int BATCH_SIZE = 3;
    private static final List<Long> INPUT_LIST =
            Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
    private static final Integer MULTIPLIER = 2;
    private static final long SLEEP_TIME = 100;

    //@Test
    public void runListInBatches() {
        // this function sleeps and then processes the list
        BiFunction<List<Long>, Integer, List<String>> function =
                (list, n) -> {
                    Threads.sleep(SLEEP_TIME);
                    return list.stream()
                            .map(l -> l * n).map(Objects::toString)
                            .collect(toList());
                };

        long functionStart = System.currentTimeMillis();
        List<String> functionResult = function.apply(INPUT_LIST, MULTIPLIER);
        long functionEnd = System.currentTimeMillis();
        long functionTimeSpent = functionEnd - functionStart;

        BatchDecorator<Long, Integer, String, List<Long>> bd =
                new BatchDecorator<>(BATCH_SIZE);

        // the decorated function should run longer since lists
        // are processed in batches
        BiFunction<List<Long>, Integer, List<String>> decorated =
                bd.decorate(function);

        long decoratedFunctionStart = System.currentTimeMillis();
        List<String> decoratedFunctionResult =
                decorated.apply(INPUT_LIST, MULTIPLIER);
        long decoratedFunctionEnd = System.currentTimeMillis();
        long decoatedFunctionTimeSpent =
                decoratedFunctionEnd - decoratedFunctionStart;

        assertEquals(functionResult, decoratedFunctionResult);

        System.out.println(functionTimeSpent / SLEEP_TIME);
        System.out.println(decoatedFunctionTimeSpent / SLEEP_TIME);

        // functionTimeSpent / SLEEP_TIME = 1
        // decoatedFunctionTimeSpent / SLEEP_TIME = 3
        assertTrue(decoatedFunctionTimeSpent / SLEEP_TIME >=
                  functionTimeSpent / SLEEP_TIME * BATCH_SIZE);
    }

    @Test
    public void runSetInBatches() {
        // this function sleeps and then processes the list
        BiFunction<Set<Long>, Integer, List<String>> function =
                (list, n) -> {
                    Threads.sleep(SLEEP_TIME);
                    return list.stream()
                            .map(l -> l * n).map(Objects::toString)
                            .collect(toList());
                };

        long functionStart = System.currentTimeMillis();
        List<String> functionResult =
                function.apply(new HashSet<>(INPUT_LIST), MULTIPLIER);
        long functionEnd = System.currentTimeMillis();
        long functionTimeSpent = functionEnd - functionStart;

        BatchDecorator<Long, Integer, String, Set<Long>> bd =
                new BatchDecorator<>(BATCH_SIZE);

        // the decorated function should run longer since lists
        // are processed in batches
        BiFunction<Set<Long>, Integer, List<String>> decorated =
                bd.decorate(function);

        long decoratedFunctionStart = System.currentTimeMillis();
        List<String> decoratedFunctionResult =
                decorated.apply(new HashSet<>(INPUT_LIST), MULTIPLIER);
        long decoratedFunctionEnd = System.currentTimeMillis();
        long decoatedFunctionTimeSpent =
                decoratedFunctionEnd - decoratedFunctionStart;

        assertEquals(functionResult, decoratedFunctionResult);

        System.out.println(functionTimeSpent / SLEEP_TIME);
        System.out.println(decoatedFunctionTimeSpent / SLEEP_TIME);

        // functionTimeSpent / SLEEP_TIME = 1
        // decoatedFunctionTimeSpent / SLEEP_TIME = 3
        assertTrue(decoatedFunctionTimeSpent / SLEEP_TIME >=
                  functionTimeSpent / SLEEP_TIME * BATCH_SIZE);
    }



}
