package by.binarylifestyle.exception.wrapper;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ExceptionToDefaultWrapper {
    @SafeVarargs
    public static <T> Supplier<T> fromUnchecked(Supplier<T> supplier, T defaultValue, Class<? extends Exception>... exceptionsToWrap) {
        return CheckedToRuntimeWrapper.from(fromChecked(supplier::get, defaultValue, exceptionsToWrap));
    }

    @SafeVarargs
    public static <T> Callable<T> fromChecked(Callable<T> callable, T defaultValue, Class<? extends Exception>... exceptionsToWrap) {
        return new ExceptionToDefaultWrapperCallable<>(callable, defaultValue, exceptionsToWrap);
    }

    private static class ExceptionToDefaultWrapperCallable<T> implements Callable<T> {
        private Class<? extends Exception>[] exceptionsToWrap;
        private Callable<T> callable;
        private T defaultValue;

        @SafeVarargs
        @SuppressWarnings("unchecked")
        public ExceptionToDefaultWrapperCallable(Callable<T> callable, T defaultValue, Class<? extends Exception>... exceptionsToWrap) {
            this.callable = callable;
            this.defaultValue = defaultValue;
            if (exceptionsToWrap.length == 0) {
                //Default behaviour is to wrap all exceptions to default value
                this.exceptionsToWrap = new Class[]{Exception.class};
            } else {
                this.exceptionsToWrap = exceptionsToWrap;
            }
        }

        @Override
        public T call() throws Exception {
            T result;
            try {
                result = callable.call();
            } catch (Exception e) {
                if (Arrays.stream(exceptionsToWrap).anyMatch(exceptionClass -> exceptionClass.isInstance(e))) {
                    result = defaultValue;
                } else {
                    throw e;
                }
            }
            return result;
        }
    }
}
