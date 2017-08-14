package by.binarylifestyle.exception.wrapper.api.common.typed;

import java.util.function.Supplier;

public interface TypedUncheckedExceptionTypedWrapper<S, D> {
    Supplier<D> applyTo(Supplier<S> supplier);

    default D wrap(Supplier<S> supplier){
        return applyTo(supplier).get();
    }
}
