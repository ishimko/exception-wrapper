package exception.wrapper;

import java.util.function.Function;

public class WrapException {
    private Class<? extends Exception> exceptionToWrap;
    private Function<Exception, Exception> wrapper;

    private WrapException(Class<? extends Exception> exceptionToWrap, Function<Exception, Exception> wrapper) {
        this.exceptionToWrap = exceptionToWrap;
        this.wrapper = wrapper;
    }

    public Class<? extends Exception> getExceptionToWrap() {
        return exceptionToWrap;
    }

    public Function<Exception, Exception> getWrapper() {
        return wrapper;
    }
}
