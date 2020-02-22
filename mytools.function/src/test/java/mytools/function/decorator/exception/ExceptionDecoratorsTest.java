package mytools.function.decorator.exception;

import static mytools.function.decorator.exception.ExceptionDecorators.safe;
import static mytools.function.decorator.exception.ExceptionDecorators.safely;
import static mytools.function.decorator.exception.ExceptionDecorators.uncheck;
import static mytools.function.decorator.exception.ExceptionDecorators.unchecked;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import mytools.function.BiConsumerWithException;
import mytools.function.BiFunctionWithException;
import mytools.function.ConsumerWithException;
import mytools.function.FunctionWithException;
import mytools.function.RunnableWithException;
import mytools.function.SupplierWithException;
import mytools.function.object.Counter;

/**
 * Test static exception decorators
 */
public class ExceptionDecoratorsTest {

    @Test
    public void testUnchecked() {
        int expectedCounterValue = 0;
        testUnchecked(++expectedCounterValue, () -> unchecked(
                Functions.runnable()).run());
        testUnchecked(++expectedCounterValue, () -> uncheck(
                Functions.runnable()));
        testUnchecked(++expectedCounterValue, () -> unchecked(
                Functions.supplier()).get());
        testUnchecked(++expectedCounterValue, () -> uncheck(
                Functions.supplier()));
        testUnchecked(++expectedCounterValue, () -> unchecked(
                Functions.consumer()).accept(1));
        testUnchecked(++expectedCounterValue, () -> unchecked(
                Functions.biConsumer()).accept(1, 2));
        testUnchecked(++expectedCounterValue, () -> unchecked(
                Functions.function()).apply(1));
        testUnchecked(++expectedCounterValue, () -> unchecked(
                Functions.biFunction()).apply(1, 2));

        testUncheckedWithMyException(++expectedCounterValue, () -> unchecked(
                MyRuntimeException.class, Functions.runnable()).run());
        testUncheckedWithMyException(++expectedCounterValue, () -> uncheck(
                MyRuntimeException.class, Functions.runnable()));
        testUncheckedWithMyException(++expectedCounterValue, () -> unchecked(
                MyRuntimeException.class, Functions.supplier()).get());
        testUncheckedWithMyException(++expectedCounterValue, () -> uncheck(
                MyRuntimeException.class, Functions.supplier()));
        testUncheckedWithMyException(++expectedCounterValue, () -> unchecked(
                MyRuntimeException.class, Functions.consumer()).accept(1));
        testUncheckedWithMyException(++expectedCounterValue, () -> unchecked(
                MyRuntimeException.class, Functions.biConsumer()).accept(1, 2));
        testUncheckedWithMyException(++expectedCounterValue, () -> unchecked(
                MyRuntimeException.class, Functions.function()).apply(1));
        testUncheckedWithMyException(++expectedCounterValue, () -> unchecked(
                MyRuntimeException.class, Functions.biFunction()).apply(1, 2));
    }

    @Test
    public void testSafe() {

        // test with no exceptions given

        assertNull(safe(() -> {
            throwMyException();
            return "Hello, World";
        }).get());

        assertNull(safely(() -> {
            throwMyException();
            return "Hello, World";
        }));

        assertNull(safe((Integer i) -> {
            throwMyException();
            return i.toString();
        }).apply(1));

        assertNull(safe((Integer i, Integer j) -> {
            throwMyException();
            return i * j;
        }).apply(1, 2));

        // test with exception, which is thrown by the function
        // it should be caught

        assertNull(safe(() -> {
            throwMyException();
            return "Hello, World";
        }, MyRuntimeException.class).get());

        assertNull(safely(() -> {
            throwMyException();
            return "Hello, World";
        }, MyRuntimeException.class));

        assertNull(safe((Integer i) -> {
            throwMyException();
            return i.toString();
        }, MyRuntimeException.class).apply(1));

        assertNull(safe((Integer i, Integer j) -> {
            throwMyException();
            return i * j;
        }, MyRuntimeException.class, IllegalStateException.class).apply(1, 2));

        // test with exception, which is not thrown by the function
        // it should be re-thrown

        try {
            safe(() -> {
                throwMyException();
                return "Hello, World";
            }, IllegalStateException.class).get();
        } catch (@SuppressWarnings("unused") MyRuntimeException e) {
            //ok
        }

        try {
            safely(() -> {
                throwMyException();
                return "Hello, World";
            }, IllegalStateException.class);
        } catch (@SuppressWarnings("unused") MyRuntimeException e) {
            //ok
        }

        try {
            safe((Integer i) -> {
                throwMyException();
                return i.toString();
            }, IllegalStateException.class).apply(1);
        } catch (@SuppressWarnings("unused") MyRuntimeException e) {
            //ok
        }

        try {
            safe((Integer i, Integer j) -> {
                throwMyException();
                return i * j;
            }, IllegalStateException.class).apply(1, 2);
        } catch (@SuppressWarnings("unused") MyRuntimeException e) {
            //ok
        }

    }

    private static void testUnchecked(int expectedCounterValue, Runnable r) {
        try {
            r.run();
            fail("Runtime exception must be thrown");
        } catch (RuntimeException e) {
            assertEquals("foo", e.getCause().getMessage());
            assertEquals(expectedCounterValue, Functions.getCounter().get());
        }
    }

    private static void testUncheckedWithMyException(
            int expectedCounterValue, Runnable r) {
        try {
            r.run();
            fail("MyException must be thrown");
        } catch (MyRuntimeException e) {
            assertEquals("foo", e.getCause().getMessage());
            assertEquals(expectedCounterValue, Functions.getCounter().get());
        } catch (@SuppressWarnings("unused") RuntimeException e) {
            fail("MyException must be thrown");
        }
    }

    private static void throwMyException() {
        throw new MyRuntimeException("problem");
    }

}

@SuppressWarnings("serial")
class MyRuntimeException extends RuntimeException {
    MyRuntimeException(String message) {
        super(message);
    }

    // must have a public constructor which takes a Throwable
    MyRuntimeException(Throwable cause) {
        super(cause);
    }
}

final class Functions {

    private Functions() { }

    private static Counter counter = new Counter();

    static Counter getCounter() {
        return counter;
    }

    static RunnableWithException runnable() {
        return () ->  {
            makeSideEffect();
            throwIt();
        };
    }

    static SupplierWithException<Integer> supplier() {
        return () ->  {
            makeSideEffect();
            throwIt();
            return 0;
        };
    }

    static ConsumerWithException<Integer> consumer() {
        return i -> {
            makeSideEffect();
            throwIt();
        };
    }

    static BiConsumerWithException<Integer, Integer> biConsumer() {
        return (list, i) -> {
            makeSideEffect();
            throwIt();
        };
    }

    static FunctionWithException<Integer, Integer> function() {
        return i -> {
            makeSideEffect();
            throwIt();
            return i + 1;
        };
    }

    static BiFunctionWithException<Integer, Integer, Integer> biFunction() {
        return (a, b) -> {
            makeSideEffect();
            throwIt();
            return a + b;
        };
    }

    private static void makeSideEffect() {
        counter.increment();
    }

    private static void throwIt() throws Exception {
        throw new Exception("foo");
    }

}
