package mytools.util.tuple;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import mytools.test.ObjectTests;

public class TripleTest {
    
    @Test
    public void equalsAndHashCodeAreImplemented() {
        Pair<Triple<Integer, String, List<String>>, Triple<Integer, String, List<String>>> triples =
                getTripleWithCopy();
        ObjectTests.equalsAndHashCodeAreImplemented(triples._1(), triples._2());
    }
    
    @Test
    public void toStringIsImplemented() {
        Pair<Triple<Integer, String, List<String>>, Triple<Integer, String, List<String>>> triples =
                getTripleWithCopy();
        ObjectTests.toStringIsImplemented(triples._1(), triples._2());
    }
    
    @Test
    public void tripleAsArray() {
        Triple<Integer, String, List<String>> t = getTripleWithCopy()._1();
        Object[] objects = t.asArray();
        assertNotNull(objects);
        assertEquals(3, objects.length);
        assertEquals(t._1(), objects[0]);
        assertEquals(t._2(), objects[1]);
        assertEquals(t._3(), objects[2]);
    }
    
    @Test
    public void tripleOfObjectsOfTheSameTypesAsList() {
        Triple<Integer, Integer, Integer> t =
                new Triple<>(Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(123));
        List<Integer> integers = Triple.asList(t);
        assertNotNull(integers);
        assertEquals(3, integers.size());
        assertEquals(t._1(), integers.get(0));
        assertEquals(t._2(), integers.get(1));
        assertEquals(t._3(), integers.get(2));
    }
    
    private static Pair<Triple<Integer, String, List<String>>, Triple<Integer, String, List<String>>>
    getTripleWithCopy() {
        Triple<Integer, String, List<String>> p1 =
                new Triple<>(Integer.valueOf(42), "foo", Arrays.asList("a", "b", "c"));
        Triple<Integer, String, List<String>> p2 =
                new Triple<>(Integer.valueOf(42), "foo", Arrays.asList("a", "b", "c"));
        return new Pair<>(p1, p2);
    }
}
