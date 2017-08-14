package by.binarylifestyle.exception.wrapper.impl.support;

import java.util.function.Function;

public class WrappingConfiguration<S extends Exception, D extends Exception> {
    private Class<S> exceptionToWrap;
    private Function<S, D> wrapper;

    public WrappingConfiguration(Class<S> exceptionToWrap, Function<S, D> wrapper) {
        this.exceptionToWrap = exceptionToWrap;
        this.wrapper = wrapper;
    }

    @SuppressWarnings("unchecked")
    public D wrap(Exception exception) {
        if (canWrap(exception)) {
            return wrapper.apply((S)exception);
        } else {
            throw new IllegalArgumentException(String.format("Provided exception can not be cast to %s", exceptionToWrap));
        }
    }

    public boolean canWrap(Exception exception) {
        return exceptionToWrap.isInstance(exception);
    }

    public Class<S> getExceptionToWrap() {
        return exceptionToWrap;
    }
}
