package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.impl.support.ValidationUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ExceptionToOptionalWrapper {
    private final Class<? extends Exception>[] exceptionsToWrap;

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public ExceptionToOptionalWrapper(Class<? extends Exception>... exceptionsToWrap) {
        ValidationUtil.requireNotNull(exceptionsToWrap, "exceptionsToWrap");
        ValidationUtil.requireAllNotNull(exceptionsToWrap, "exceptionsToWrap");
        if (exceptionsToWrap.length == 0) {
            //Default behaviour is to wrap all exceptions to Optional.empty()
            this.exceptionsToWrap = new Class[]{Exception.class};
        } else {
            this.exceptionsToWrap = exceptionsToWrap;
        }
    }

    public <T> Supplier<Optional<T>> applyTo(Supplier<T> supplier) {
        ValidationUtil.requireNotNull(supplier, "supplier");
        return CheckedToRuntimeWrapper.applyTo(applyToChecked(supplier::get));
    }

    public <T> Callable<Optional<T>> applyToChecked(Callable<T> callable) {
        ValidationUtil.requireNotNull(callable, "callable");
        return new ExceptionToOptionalWrappingCallable<>(callable, exceptionsToWrap);
    }

    public <T> Optional<T> wrapChecked(Callable<T> callable) throws Exception {
        return applyToChecked(callable).call();
    }

    public <T> Optional<T> wrap(Supplier<T> supplier) {
        return applyTo(supplier).get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionToOptionalWrapper that = (ExceptionToOptionalWrapper) o;
        return Arrays.equals(exceptionsToWrap, that.exceptionsToWrap);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(exceptionsToWrap);
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
