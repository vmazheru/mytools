package mytools.util.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;

public final class Exceptions {

    private Exceptions() { }

    public static String getStackTrace(Throwable e) {
        try (Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result)) {
            e.printStackTrace(printWriter);
            return result.toString();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static Throwable getRootCause(Throwable e) {
        Throwable c = e;
        Throwable cause = c;
        while ((c = c.getCause()) != null) {
            cause = c;
        }
        return cause;
    }

    public static String getRootStackTrace(Throwable e) {
        return getStackTrace(getRootCause(e));
    }

}
