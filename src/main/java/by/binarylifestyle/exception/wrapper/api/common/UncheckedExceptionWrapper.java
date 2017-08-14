package by.binarylifestyle.exception.wrapper.api.common;

import by.binarylifestyle.exception.wrapper.api.common.typed.TypedUncheckedExceptionTypedWrapper;
import by.binarylifestyle.exception.wrapper.api.common.untyped.VoidUncheckedExceptionWrapper;

public interface UncheckedExceptionWrapper<S, D> extends TypedUncheckedExceptionTypedWrapper<S, D>, VoidUncheckedExceptionWrapper {
    @Override
    default Runnable applyTo(Runnable runnable) {
        return applyTo(() -> {
            runnable.run();
            return null;
        })::get;
    }
}
