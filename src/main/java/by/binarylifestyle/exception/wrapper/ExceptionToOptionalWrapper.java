package by.binarylifestyle.exception.wrapper;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ExceptionToOptionalWrapper {
    @SafeVarargs
    public static <T> Supplier<Optional<T>> fromSupplier(Supplier<T> supplier, Class<? extends Exception>... exceptionsToWrap) {
        return new ExceptionWrapperFromSupplier<>(supplier, exceptionsToWrap);
    }

    @SafeVarargs
    public static <T> Callable<Optional<T>> fromCallable(Callable<T> callable, Class<? extends Exception>... exceptionsToWrap) {
        return new ExceptionWrapperFromCallable<>(callable, exceptionsToWrap);
    }

    private static class ExceptionWrapperFromCallable<T> implements Callable<Optional<T>> {
        private final Callable<T> callable;
        private final Class<? extends Exception>[] exceptionsToWrap;

        ExceptionWrapperFromCallable(Callable<T> callable, Class<? extends Exception>... exceptionsToWrap) {
            this.callable = callable;
            this.exceptionsToWrap = exceptionsToWrap;
        }

        @Override
        public Optional<T> call() throws Exception {
            Optional<T> result;
            try {
                result = Optional.of(callable.call());
            } catch (Exception e) {
                if (Arrays.stream(exceptionsToWrap).anyMatch(exceptionClass -> exceptionClass.isInstance(e))) {
                    result = Optional.empty();
                } else {
                    throw e;
                }
            }
            return result;
        }
    }

    private static class ExceptionWrapperFromSupplier<T> implements Supplier<Optional<T>> {
        private final Callable<Optional<T>> optionalCallable;

        ExceptionWrapperFromSupplier(Supplier<T> supplier, Class<? extends Exception>... exceptionsToWrap) {
            optionalCallable = ExceptionToOptionalWrapper.fromCallable(
                    supplier::get,
                    exceptionsToWrap
            );
        }

        @Override
        public Optional<T> get() {
            try {
                return optionalCallable.call();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    // Should never happen, because callable is made from supplier, that can not throw checked exceptions
                    throw new IllegalStateException("Internal error: Unexpected checked exception", e);
                }
            }
        }
    }
}
