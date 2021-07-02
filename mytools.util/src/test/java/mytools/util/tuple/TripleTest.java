package mytools.util.tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import mytools.test.ObjectTests;

public class TripleTest {

    private static Random r = new Random();
    private static final int TRIPLE_SIZE = 3;

    @Test
    public void equalsAndHashCodeAreImplemented() {
        Pair<Triple<Integer, String, List<String>>,
             Triple<Integer, String, List<String>>> triples =
                getTripleWithCopy();
        ObjectTests.equalsAndHashCodeAreImplemented(
                triples.getFirst(), triples.getSecond());
    }

    @Test
    public void toStringIsImplemented() {
        Pair<Triple<Integer, String, List<String>>,
             Triple<Integer, String, List<String>>> triples =
                getTripleWithCopy();
        ObjectTests.toStringIsImplemented(
                triples.getFirst(), triples.getSecond());
    }

    @Test
    public void tripleAsArray() {
        Triple<Integer, String, List<String>> t =
                getTripleWithCopy().getFirst();
        Object[] objects = t.asArray();
        assertNotNull(objects);
        assertEquals(TRIPLE_SIZE, objects.length);
        assertEquals(t.getFirst(), objects[0]);
        assertEquals(t.getSecond(), objects[1]);
        assertEquals(t.getThird(), objects[2]);
    }

    @Test
    public void tripleOfObjectsOfTheSameTypesAsList() {
        Triple<Integer, Integer, Integer> t = new Triple<>(
                Integer.valueOf(r.nextInt()),
                Integer.valueOf(r.nextInt()),
                Integer.valueOf(r.nextInt()));
        List<Integer> integers = Triple.asList(t);
        assertNotNull(integers);
        assertEquals(TRIPLE_SIZE, integers.size());
        assertEquals(t.getFirst(), integers.get(0));
        assertEquals(t.getSecond(), integers.get(1));
        assertEquals(t.getThird(), integers.get(2));
    }

    private static Pair<Triple<Integer, String, List<String>>,
                        Triple<Integer, String, List<String>>>
    getTripleWithCopy() {
        int i = r.nextInt();
        Triple<Integer, String, List<String>> p1 = new Triple<>(
                i, "foo", Arrays.asList("a", "b", "c"));
        Triple<Integer, String, List<String>> p2 = new Triple<>(
                i, "foo", Arrays.asList("a", "b", "c"));
        return new Pair<>(p1, p2);
    }
}
