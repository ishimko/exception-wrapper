package by.binarylifestyle.exception.wrapper.supplier;

import java.util.function.Supplier;

public class FailingSupplier<T> implements Supplier<T> {
    private Supplier<RuntimeException> exceptionSupplier;

    public FailingSupplier(Supplier<RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    @Override
    public T get() {
        throw exceptionSupplier.get();
    }
}
