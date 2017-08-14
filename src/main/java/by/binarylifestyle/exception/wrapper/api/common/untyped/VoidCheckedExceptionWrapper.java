package by.binarylifestyle.exception.wrapper.api.common.untyped;

import by.binarylifestyle.exception.wrapper.api.support.CheckedRunnable;

public interface VoidCheckedExceptionWrapper {
    CheckedRunnable applyToChecked(CheckedRunnable runnable);

    default void wrapChecked(CheckedRunnable runnable) throws Exception{
        applyToChecked(runnable).run();
    }
}
