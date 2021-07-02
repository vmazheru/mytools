package mytools.util.tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import mytools.test.ObjectTests;

public class PairTest {

    private static Random r = new Random();
    private static final int PAIR_SIZE = 2;

    @Test
    public void equalsAndHashCodeAreImplemented() {
        Pair<Pair<Integer, List<String>>, Pair<Integer, List<String>>> pairs =
                getPairWithCopy();
        ObjectTests.equalsAndHashCodeAreImplemented(
                pairs.getFirst(), pairs.getSecond());
    }

    @Test
    public void toStringIsImplemented() {
        Pair<Pair<Integer, List<String>>, Pair<Integer, List<String>>> pairs =
                getPairWithCopy();
        ObjectTests.toStringIsImplemented(pairs.getFirst(), pairs.getSecond());
    }

    @Test
    public void pairAsArray() {
        Pair<Integer, List<String>> p = getPairWithCopy().getFirst();
        Object[] objects = p.asArray();
        assertNotNull(objects);
        assertEquals(2, objects.length);
        assertEquals(p.getFirst(), objects[0]);
        assertEquals(p.getSecond(), objects[1]);
    }

    @Test
    public void pairOfObjectsOfTheSameTypesAsList() {
        Pair<Integer, Integer> p = new Pair<>(
                Integer.valueOf(r.nextInt()), Integer.valueOf(r.nextInt()));
        List<Integer> integers = Pair.asList(p);
        assertNotNull(integers);
        assertEquals(PAIR_SIZE, integers.size());
        assertEquals(p.getFirst(), integers.get(0));
        assertEquals(p.getSecond(), integers.get(1));
    }

    private static Pair<Pair<Integer, List<String>>,
                        Pair<Integer, List<String>>> getPairWithCopy() {
        int i = r.nextInt();
        Pair<Integer, List<String>> p1 =
                new Pair<>(i, Arrays.asList("a", "b", "c"));
        Pair<Integer, List<String>> p2 =
                new Pair<>(i, Arrays.asList("a", "b", "c"));
        return new Pair<>(p1, p2);
    }

}
