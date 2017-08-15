package by.binarylifestyle.exception.wrapper.api.support;

import java.util.function.Function;

public class VarargsFunction<T, R> implements Function<T[], R> {
    private Function<T[], R> function;

    public VarargsFunction(Function<T[], R> function) {
        this.function = function;
    }

    @SafeVarargs
    @Override
    public final R apply(T... ts) {
        return function.apply(ts);
    }

    public Function<T[], R> asFunction() {
        return this;
    }
}
