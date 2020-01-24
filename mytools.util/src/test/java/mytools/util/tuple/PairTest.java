package mytools.util.tuple;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import mytools.test.ObjectTests;

public class PairTest {

    @Test
    public void equalsAndHashCodeAreImplemented() {
        Pair<Pair<Integer, List<String>>, Pair<Integer, List<String>>> pairs = getPairWithCopy();
        ObjectTests.equalsAndHashCodeAreImplemented(pairs._1(), pairs._2());
    }
    
    @Test
    public void toStringIsImplemented() {
        Pair<Pair<Integer, List<String>>, Pair<Integer, List<String>>> pairs = getPairWithCopy();
        ObjectTests.toStringIsImplemented(pairs._1(), pairs._2());
    }
    
    @Test
    public void pairAsArray() {
        Pair<Integer, List<String>> p = getPairWithCopy()._1();
        Object[] objects = p.asArray();
        assertNotNull(objects);
        assertEquals(2, objects.length);
        assertEquals(p._1(), objects[0]);
        assertEquals(p._2(), objects[1]);
    }
    
    @Test
    public void pairOfObjectsOfTheSameTypesAsList() {
        Pair<Integer, Integer> p = new Pair<>(Integer.valueOf(42), Integer.valueOf(43));
        List<Integer> integers = Pair.asList(p);
        assertNotNull(integers);
        assertEquals(2, integers.size());
        assertEquals(p._1(), integers.get(0));
        assertEquals(p._2(), integers.get(1));
    }
    
    private static Pair<Pair<Integer, List<String>>, Pair<Integer, List<String>>> getPairWithCopy() {
        Pair<Integer, List<String>> p1 =
                new Pair<>(Integer.valueOf(42), Arrays.asList("a", "b", "c"));
        Pair<Integer, List<String>> p2 =
                new Pair<>(Integer.valueOf(42), Arrays.asList("a", "b", "c"));
        return new Pair<>(p1, p2);
    }

}
