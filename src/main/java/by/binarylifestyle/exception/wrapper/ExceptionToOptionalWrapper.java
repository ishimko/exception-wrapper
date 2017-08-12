package by.binarylifestyle.exception.wrapper;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ExceptionToOptionalWrapper {
    @SafeVarargs
    public static <T> Supplier<Optional<T>> unchecked(Supplier<T> supplier, Class<? extends Exception>... exceptionsToWrap) {
        return CheckedToRuntimeWrapper.from(checked(supplier::get, exceptionsToWrap));
    }

    @SafeVarargs
    public static <T> Callable<Optional<T>> checked(Callable<T> callable, Class<? extends Exception>... exceptionsToWrap) {
        return new ExceptionToOptionalWrapperCallable<>(callable, exceptionsToWrap);
    }

    @SafeVarargs
    public static <T> Optional<T> wrapUnchecked(Supplier<T> supplier, Class<? extends Exception>... exceptionsToWrap) {
        return unchecked(supplier, exceptionsToWrap).get();
    }

    @SafeVarargs
    public static <T> Optional<T> wrapChecked(Callable<T> callable, Class<? extends Exception>... exceptionsToWrap) throws Exception {
        return checked(callable, exceptionsToWrap).call();
    }

    private static class ExceptionToOptionalWrapperCallable<T> implements Callable<Optional<T>> {
        private final Callable<T> callable;
        private final Class<? extends Exception>[] exceptionsToWrap;

        ExceptionToOptionalWrapperCallable(Callable<T> callable, Class<? extends Exception>... exceptionsToWrap) {
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

}
