package mytools.util.exception;

import static mytools.util.exception.Exceptions.getRootCause;
import static mytools.util.exception.Exceptions.getRootStackTrace;
import static mytools.util.exception.Exceptions.getStackTrace;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mytools.util.tuple.Pair;

public class ExceptionsTest {

    @Test
    public void getRootCauseWhenExceptionIsItsOwnRoot() {
        Exception root = new Exception("The one which root cause is self");
        assertEquals(root, getRootCause(root));
    }

    @Test
    public void getRootCauseWithNestedExceptions() {
        Pair<Exception, Exception> exWithRoot = exceptionWithItsRoot();
        assertEquals(exWithRoot.getSecond(),
                getRootCause(exWithRoot.getFirst()));
    }

    @Test
    public void getStackTraceOfTheTopLevelException() {
        Exception ex = exceptionWithItsRoot().getFirst();
        assertTrue(getStackTrace(ex).startsWith(
                "java.lang.Exception: " + ex.getMessage()));
    }

    @Test
    public void getStackTraceOfRootException() {
        Pair<Exception, Exception> exWithRoot = exceptionWithItsRoot();
        assertTrue(getRootStackTrace(exWithRoot.getFirst()).startsWith(
                "java.lang.Exception: " + exWithRoot.getSecond().getMessage()));
    }

    private static Pair<Exception, Exception> exceptionWithItsRoot() {
        Exception root = new Exception("Deeply nested root cause");
        Exception intermediate = new Exception(
                "Some intermediate exception", root);
        Exception actuallyCaught = new Exception(
                "The one which was actually caught", intermediate);
        return new Pair<>(actuallyCaught, root);
    }

}
