package by.binarylifestyle.exception.wrapper.api.common.untyped;

public interface VoidUncheckedExceptionWrapper {
    Runnable applyTo(Runnable runnable);

    default void wrap(Runnable runnable) {
        applyTo(runnable).run();
    }
}
