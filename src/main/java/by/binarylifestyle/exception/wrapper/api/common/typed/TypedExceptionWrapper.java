package by.binarylifestyle.exception.wrapper.api.common.typed;

import by.binarylifestyle.exception.wrapper.impl.common.CheckedToRuntimeWrapper;

import java.util.function.Supplier;

public interface TypedExceptionWrapper<S, D> extends TypedCheckedExceptionWrapper<S, D>, TypedUncheckedExceptionTypedWrapper<S, D> {
    @Override
    default Supplier<D> applyTo(Supplier<S> supplier) {
        return CheckedToRuntimeWrapper.applyTo(applyToChecked(supplier::get));
    }
}
