package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.api.common.typed.TypedExceptionWrapper;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;

public class ExceptionToOptionalWrapper<T> implements TypedExceptionWrapper<T, Optional<T>> {
    private Class<? extends Exception>[] exceptionsToWrap;

    @SafeVarargs
    public ExceptionToOptionalWrapper(Class<? extends Exception>... exceptionsToWrap) {
        this.exceptionsToWrap = exceptionsToWrap;
    }

    @Override
    public Callable<Optional<T>> applyToChecked(Callable<T> callable) {
        return new ExceptionToOptionalWrappingCallable<>(callable, exceptionsToWrap);
    }

    private static class ExceptionToOptionalWrappingCallable<T> implements Callable<Optional<T>> {
        private final Callable<T> callable;
        private final Class<? extends Exception>[] exceptionsToWrap;

        @SafeVarargs
        ExceptionToOptionalWrappingCallable(Callable<T> callable, Class<? extends Exception>... exceptionsToWrap) {
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
