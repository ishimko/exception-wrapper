package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.api.common.typed.TypedExceptionWrapper;
import by.binarylifestyle.exception.wrapper.api.support.VarargsFunction;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class ExceptionToDefaultWrapper<T> implements TypedExceptionWrapper<T, T> {
    private T defaultValue;
    private Class<? extends Exception>[] exceptionsToWrap;

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public ExceptionToDefaultWrapper(T defaultValue, Class<? extends Exception>... exceptionsToWrap) {
        this.defaultValue = defaultValue;
        if (exceptionsToWrap.length == 0) {
            //Default behaviour is to wrap all exceptions to default value
            this.exceptionsToWrap = new Class[]{Exception.class};
        } else {
            this.exceptionsToWrap = exceptionsToWrap;
        }
    }

    @Override
    public Callable<T> applyToChecked(Callable<T> callable) {
        return new ExceptionToDefaultWrappingCallable<>(callable, defaultValue, exceptionsToWrap);
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
