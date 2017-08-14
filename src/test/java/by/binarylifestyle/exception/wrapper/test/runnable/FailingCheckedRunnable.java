package by.binarylifestyle.exception.wrapper.test.runnable;

import by.binarylifestyle.exception.wrapper.api.support.CheckedRunnable;

import java.util.function.Supplier;

public class FailingCheckedRunnable implements CheckedRunnable {
    private Supplier<Exception> exceptionSupplier;

    public FailingCheckedRunnable(Supplier<Exception> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    @Override
    public void run() throws Exception {
        throw exceptionSupplier.get();
    }
}
