package by.binarylifestyle.exception.wrapper.api.common;

import by.binarylifestyle.exception.wrapper.api.common.typed.TypedExceptionWrapper;
import by.binarylifestyle.exception.wrapper.api.common.untyped.VoidCheckedExceptionWrapper;
import by.binarylifestyle.exception.wrapper.api.support.CheckedRunnable;

public interface CheckedExceptionWrapper<S, D> extends TypedExceptionWrapper<S, D>, VoidCheckedExceptionWrapper {
    @Override
    default CheckedRunnable applyToChecked(CheckedRunnable runnable) {
        return applyToChecked(() -> {
            runnable.run();
            return null;
        })::call;
    }
}
