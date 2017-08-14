package by.binarylifestyle.exception.wrapper.test.callable;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class FailingCallable<T> implements Callable<T> {
    private Supplier<Exception> exceptionSupplier;

    public FailingCallable(Supplier<Exception> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    @Override
    public T call() throws Exception {
        throw exceptionSupplier.get();
    }
}
