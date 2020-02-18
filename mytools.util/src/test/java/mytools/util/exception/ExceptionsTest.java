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
        assertEquals(exWithRoot._2(), getRootCause(exWithRoot._1()));
    }

    @Test
    public void getStackTraceOfTheTopLevelException() {
        Exception ex = exceptionWithItsRoot()._1();
        assertTrue(getStackTrace(ex).startsWith(
                "java.lang.Exception: " + ex.getMessage()));
    }

    @Test
    public void getStackTraceOfRootException() {
        Pair<Exception, Exception> exWithRoot = exceptionWithItsRoot();
        assertTrue(getRootStackTrace(exWithRoot._1()).startsWith(
                "java.lang.Exception: " + exWithRoot._2().getMessage()));
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
