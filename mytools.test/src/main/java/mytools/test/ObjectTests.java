package mytools.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class ObjectTests {

    private ObjectTests() { }

    public static <T> void equalsAndHashCodeAreImplemented(
            T object, T objectCopy) {
        assertObjectsAreNotNullAndDifferent(object, objectCopy);
        assertEquals(object, objectCopy);
        assertEquals(object.hashCode(), objectCopy.hashCode());
    }

    public static <T> void toStringIsImplemented(T object, T objectCopy) {
        assertObjectsAreNotNullAndDifferent(object, objectCopy);
        assertEquals(object.toString(), objectCopy.toString());
    }

    private static <T> void assertObjectsAreNotNullAndDifferent(
            T object, T objectCopy) {
        assertNotNull(object);
        assertNotNull(objectCopy);
        assertFalse(object == objectCopy);
    }

}
