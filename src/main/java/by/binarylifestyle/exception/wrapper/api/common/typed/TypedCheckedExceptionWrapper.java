package by.binarylifestyle.exception.wrapper.api.common.typed;

import java.util.concurrent.Callable;

public interface TypedCheckedExceptionWrapper<T, R> {
    Callable<R> applyToChecked(Callable<T> callable);

    default R wrapChecked(Callable<T> callable) throws Exception {
        return applyToChecked(callable).call();
    }
}
