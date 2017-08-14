package by.binarylifestyle.exception.wrapper.api.common.untyped;

import by.binarylifestyle.exception.wrapper.impl.common.CheckedToRuntimeWrapper;

public interface VoidExceptionWrapper extends VoidCheckedExceptionWrapper, VoidUncheckedExceptionWrapper {
    @Override
    default Runnable applyTo(Runnable runnable) {
        return CheckedToRuntimeWrapper.applyTo(applyToChecked(runnable::run));
    }
}
