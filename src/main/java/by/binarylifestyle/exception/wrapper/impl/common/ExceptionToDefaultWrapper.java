package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.api.support.VarargsFunction;
import by.binarylifestyle.exception.wrapper.impl.support.ValidationUtil;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExceptionToDefaultWrapper<T> {
    private final T defaultValue;
    private final Class<? extends Exception>[] exceptionsToWrap;

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public ExceptionToDefaultWrapper(T defaultValue, Class<? extends Exception>... exceptionsToWrap) {
        ValidationUtil.requireNotNull(defaultValue, "defaultValue");
        ValidationUtil.requireAllNotNull(exceptionsToWrap, "exceptionsToWrap");
        this.defaultValue = defaultValue;
        if (exceptionsToWrap.length == 0) {
            //Default behaviour is to wrap all exceptions to default value
            this.exceptionsToWrap = new Class[]{Exception.class};
        } else {
            this.exceptionsToWrap = exceptionsToWrap;
        }
    }

    public Callable<T> applyToChecked(Callable<T> callable) {
        ValidationUtil.requireNotNull(callable, "callable");
        return new ExceptionToDefaultWrappingCallable<>(callable, defaultValue, exceptionsToWrap);
    }

    public Supplier<T> applyTo(Supplier<T> supplier) {
        ValidationUtil.requireNotNull(supplier, "supplier");
        return CheckedToRuntimeWrapper.applyTo(applyToChecked(supplier::get));
    }

    public T wrap(Supplier<T> supplier) {
        return applyTo(supplier).get();
    }

    public T wrapChecked(Callable<T> callable) throws Exception {
        return applyToChecked(callable).call();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionToDefaultWrapper<?> that = (ExceptionToDefaultWrapper<?>) o;
        return Objects.equals(defaultValue, that.defaultValue) &&
                Arrays.equals(exceptionsToWrap, that.exceptionsToWrap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultValue, Arrays.hashCode(exceptionsToWrap));
    }

    @SafeVarargs
    public static <T> Function<T, ExceptionToDefaultWrapper<T>> bindExceptions(Class<? extends Exception>... exceptionsToWrap) {
        return defaultValue -> new ExceptionToDefaultWrapper<>(defaultValue, exceptionsToWrap);
    }

    public static <T> VarargsFunction<Class<? extends Exception>, ExceptionToDefaultWrapper<T>> bindDefaultValue(T defaultValue) {
        return new VarargsFunction<>(classes -> new ExceptionToDefaultWrapper<>(defaultValue, classes));
    }

    private static class ExceptionToDefaultWrappingCallable<T> implements Callable<T> {
        private Class<? extends Exception>[] exceptionsToWrap;
        private Callable<T> callable;
        private T defaultValue;

        @SafeVarargs
        ExceptionToDefaultWrappingCallable(Callable<T> callable, T defaultValue, Class<? extends Exception>... exceptionsToWrap) {
            this.callable = callable;
            this.defaultValue = defaultValue;
            this.exceptionsToWrap = exceptionsToWrap;
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
