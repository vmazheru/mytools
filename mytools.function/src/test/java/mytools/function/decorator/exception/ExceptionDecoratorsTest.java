package mytools.function.decorator.exception;

import static mytools.function.decorator.exception.ExceptionDecorators.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        testUnchecked(1, () -> unchecked(Functions.runnable()).run());
        testUnchecked(2, () -> uncheck(Functions.runnable()));
        testUnchecked(3, () -> unchecked(Functions.supplier()).get());
        testUnchecked(4, () -> uncheck(Functions.supplier()));
        testUnchecked(5, () -> unchecked(Functions.consumer()).accept(1));
        testUnchecked(6, () -> unchecked(Functions.biConsumer()).accept(1,2));
        testUnchecked(7, () -> unchecked(Functions.function()).apply(1));
        testUnchecked(8, () -> unchecked(Functions.biFunction()).apply(1,2));
        
        testUncheckedWithMyException( 9, () -> unchecked(MyRuntimeException.class, Functions.runnable()).run());
        testUncheckedWithMyException(10, () -> uncheck(MyRuntimeException.class, Functions.runnable()));
        testUncheckedWithMyException(11, () -> unchecked(MyRuntimeException.class, Functions.supplier()).get());
        testUncheckedWithMyException(12, () -> uncheck(MyRuntimeException.class, Functions.supplier()));
        testUncheckedWithMyException(13, () -> unchecked(MyRuntimeException.class, Functions.consumer()).accept(1));
        testUncheckedWithMyException(14, () -> unchecked(MyRuntimeException.class, Functions.biConsumer()).accept(1,2));
        testUncheckedWithMyException(15, () -> unchecked(MyRuntimeException.class, Functions.function()).apply(1));
        testUncheckedWithMyException(16, () -> unchecked(MyRuntimeException.class, Functions.biFunction()).apply(1,2));
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
        }).apply(1,2));
        
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
        }, MyRuntimeException.class, IllegalStateException.class).apply(1,2));
        
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
            }, IllegalStateException.class).apply(1,2);
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
            assertEquals(expectedCounterValue, Functions.counter.get());
        }
    }
    
    private static void testUncheckedWithMyException(int expectedCounterValue, Runnable r) {
        try {
            r.run();
            fail("MyException must be thrown");
        } catch (MyRuntimeException e) {
            assertEquals("foo", e.getCause().getMessage());
            assertEquals(expectedCounterValue, Functions.counter.get());
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
    public MyRuntimeException(String message) { 
        super(message);
    }
    
    public MyRuntimeException(Throwable cause) { // must have a public constructor which takes a Throwable
        super(cause);
    }
}

class Functions {
    
    static Counter counter = new Counter();
    
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
            return i+1;
        };
    }
    
    static BiFunctionWithException<Integer, Integer, Integer> biFunction() {
        return (a, b) -> {
            makeSideEffect();
            throwIt();
            return a + b;
        };
    }
    
    static private void makeSideEffect() {
        counter.increment();
    }
    
    private static void throwIt() throws Exception {
        throw new Exception("foo");
    }

}