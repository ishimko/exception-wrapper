package by.binarylifestyle.exception.wrapper;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class CheckedToRuntimeWrapper {
    public static void wrap(CheckedRunnable runnable) {
        from(runnable).run();
    }

    public static Runnable from(CheckedRunnable runnable) {
        return from(() -> {
            runnable.run();
            return null;
        })::get;
    }

    public static <T> T wrap(Callable<T> callable) {
        return from(callable).get();
    }

    public static <T> Supplier<T> from(Callable<T> callable) {
        return new CheckedToRuntimeWrapperSupplier<>(callable);
    }

    private static class CheckedToRuntimeWrapperSupplier<T> implements Supplier<T> {
        private final Callable<T> callable;

        public CheckedToRuntimeWrapperSupplier(Callable<T> callable) {
            this.callable = callable;
        }

        @Override
        public T get() {
            try {
                return callable.call();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
