package by.binarylifestyle.exception.wrapper;

import java.util.function.Function;

public class WrappingConfiguration<TSource extends Exception, TDestination extends Exception> {
    private Class<TSource> exceptionToWrap;
    private Function<TSource, TDestination> wrapper;

    private WrappingConfiguration(Class<TSource> exceptionToWrap, Function<TSource, TDestination> wrapper) {
        this.exceptionToWrap = exceptionToWrap;
        this.wrapper = wrapper;
    }

    @SuppressWarnings("unchecked")
    public TDestination wrap(Exception exception) {
        if (canWrap(exception)) {
            return wrapper.apply((TSource)exception);
        } else {
            throw new IllegalArgumentException(String.format("Provided exception can not be cast to %s", exceptionToWrap));
        }
    }

    public boolean canWrap(Exception exception) {
        return exceptionToWrap.isInstance(exception);
    }

    Class<TSource> getExceptionToWrap() {
        return exceptionToWrap;
    }
}
