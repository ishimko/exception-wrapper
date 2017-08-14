package by.binarylifestyle.exception.wrapper.api.common.typed;

import java.util.function.Supplier;

public interface TypedUncheckedExceptionTypedWrapper<T, R> {
    Supplier<R> applyTo(Supplier<T> supplier);

    default R wrap(Supplier<T> supplier){
        return applyTo(supplier).get();
    }
}
