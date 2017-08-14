package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.api.support.CheckedRunnable;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class CheckedToRuntimeWrapper {
    public static void wrap(CheckedRunnable runnable) {
        applyTo(runnable).run();
    }

    public static Runnable applyTo(CheckedRunnable runnable) {
        return applyTo(() -> {
            runnable.run();
            return null;
        })::get;
    }

    public static <T> T wrap(Callable<T> callable) {
        return applyTo(callable).get();
    }

    public static <T> Supplier<T> applyTo(Callable<T> callable) {
        return new CheckedToRuntimeWrapperSupplier<>(callable);
    }

    private static class CheckedToRuntimeWrapperSupplier<T> implements Supplier<T> {
        private final Callable<T> callable;

        CheckedToRuntimeWrapperSupplier(Callable<T> callable) {
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
