package by.binarylifestyle.exception.wrapper.runnable;

import java.util.function.Supplier;

public class FailingRunnable implements Runnable {
    private Supplier<RuntimeException> exceptionSupplier;

    public FailingRunnable(Supplier<RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    @Override
    public void run() {
        throw exceptionSupplier.get();
    }
}
