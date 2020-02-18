package mytools.util.thread;

public final class Threads {

    private Threads() { }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (@SuppressWarnings("unused") InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
