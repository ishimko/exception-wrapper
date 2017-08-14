package by.binarylifestyle.exception.wrapper.api.common;

import by.binarylifestyle.exception.wrapper.api.common.typed.TypedExceptionWrapper;
import by.binarylifestyle.exception.wrapper.api.common.untyped.VoidExceptionWrapper;
import by.binarylifestyle.exception.wrapper.api.support.CheckedRunnable;

public interface ExceptionWrapper<S, D> extends TypedExceptionWrapper<S, D>, VoidExceptionWrapper {
    @Override
    default CheckedRunnable applyToChecked(CheckedRunnable runnable) {
        return applyToChecked(() -> {
            runnable.run();
            return null;
        })::call;
    }
}
