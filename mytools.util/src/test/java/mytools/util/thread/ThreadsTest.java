package mytools.util.thread;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ThreadsTest {

    @Test
    public void interruptSleepingThread() throws Exception {
        long sleepTime = 5000;
        Thread t = new Thread(() -> Threads.sleep(sleepTime));
        long start = System.currentTimeMillis();
        t.start();
        t.interrupt();
        t.join();
        long end = System.currentTimeMillis();
        assertTrue((end - start) < sleepTime / 2,
                "Execution time must be much less than sleep time, " +
                "since the thread has been interrupted while sleeping");
    }
    
}
