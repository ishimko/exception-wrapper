package by.binarylifestyle.exception.wrapper.api.common.typed;

import java.util.concurrent.Callable;

public interface TypedCheckedExceptionWrapper<S, D> {
    Callable<D> applyToChecked(Callable<S> callable);

    default D wrap(Callable<S> callable) throws Exception {
        return applyToChecked(callable).call();
    }
}
