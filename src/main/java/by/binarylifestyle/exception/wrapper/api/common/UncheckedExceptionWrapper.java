package by.binarylifestyle.exception.wrapper.api.common;

import by.binarylifestyle.exception.wrapper.api.common.typed.TypedUncheckedExceptionTypedWrapper;
import by.binarylifestyle.exception.wrapper.api.common.untyped.VoidUncheckedExceptionWrapper;

public interface UncheckedExceptionWrapper<T, R> extends TypedUncheckedExceptionTypedWrapper<T, R>, VoidUncheckedExceptionWrapper {
    @Override
    default Runnable applyTo(Runnable runnable) {
        return applyTo(() -> {
            runnable.run();
            return null;
        })::get;
    }
}
