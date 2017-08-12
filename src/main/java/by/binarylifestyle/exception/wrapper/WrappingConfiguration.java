package by.binarylifestyle.exception.wrapper;

import java.util.function.Function;

public class WrappingConfiguration<TSource extends Exception, TDestination> {
    private Class<TSource> exceptionToWrap;
    private Function<TSource, TDestination> wrapper;

    private WrappingConfiguration(Class<TSource> exceptionToWrap, Function<TSource, TDestination> wrapper) {
        this.exceptionToWrap = exceptionToWrap;
        this.wrapper = wrapper;
    }

    @SuppressWarnings("unchecked")
    public TDestination wrap(Exception exception) {
        if (exceptionToWrap.isInstance(exception)) {
            return wrapper.apply((TSource)exception);
        } else {
            throw new IllegalArgumentException(String.format("Provided exception can not be cast to %s", exceptionToWrap));
        }
    }

    public boolean canWrap(Exception e) {
        return exceptionToWrap.isInstance(e);
    }

    public Class<TSource> getExceptionToWrap() {
        return exceptionToWrap;
    }
}
