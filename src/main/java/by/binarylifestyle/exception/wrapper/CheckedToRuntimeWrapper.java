package by.binarylifestyle.exception.wrapper;

import java.util.concurrent.Callable;

public class CheckedToRuntimeWrapper {
    public static void wrap(CheckedRunnable runnable) {
        wrap(() -> {
            runnable.run();
            return null;
        });
    }

    public static <T> T wrap(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }
}
