package by.binarylifestyle.exception.wrapper.api.common.typed;

import by.binarylifestyle.exception.wrapper.impl.common.CheckedToRuntimeWrapper;

import java.util.function.Supplier;

public interface TypedExceptionWrapper<T, R> extends TypedCheckedExceptionWrapper<T, R>, TypedUncheckedExceptionTypedWrapper<T, R> {
    @Override
    default Supplier<R> applyTo(Supplier<T> supplier) {
        return CheckedToRuntimeWrapper.applyTo(applyToChecked(supplier::get));
    }
}
