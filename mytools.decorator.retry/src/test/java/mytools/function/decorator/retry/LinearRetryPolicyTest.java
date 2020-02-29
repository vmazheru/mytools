package mytools.function.decorator.retry;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LinearRetryPolicyTest {

    @Test
    public void throwIllegalArgumentExceptionOnNegativeTimeParameter() {
        final int sleepTime = -1;
        final int retries = 3;
        assertThrows(IllegalArgumentException.class,
                () -> new LinearRetryPolicy(retries, sleepTime));
    }

    @Test
    public void returnNegativeAfterRetries() {
        final int retries = 3;
        final int sleepTime = 10;
        RetryPolicy p = new LinearRetryPolicy(retries, sleepTime);
        for (int i = 0; i < retries; i++) {
            p.nextRetryIn();
        }
        assertTrue(p.nextRetryIn() < 0);
    }

}
