package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.api.common.typed.TypedUncheckedExceptionTypedWrapper;
import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public class MappingExceptionWrapper<T> implements TypedUncheckedExceptionTypedWrapper<T, T> {
    private WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>[] configurations;

    @SafeVarargs
    public MappingExceptionWrapper(WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>... configurations) {
        this.configurations = configurations;
    }

    @Override
    public Supplier<T> applyTo(Supplier<T> supplier) {
        return new CheckedExceptionWrappingSupplier<>(supplier, configurations);
    }

    private static class CheckedExceptionWrappingSupplier<T> implements Supplier<T> {
        private WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>[] configurations;
        private Supplier<T> supplier;

        @SafeVarargs
        CheckedExceptionWrappingSupplier(Supplier<T> supplier,
                                         WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>... configurations) {
            if (configurations.length == 0) {
                throw new IllegalArgumentException("No configuration provided");
            }
            if (!isDistinct(configurations)) {
                throw new IllegalArgumentException("Configuration has repeating elements");
            }
            this.supplier = supplier;
            this.configurations = configurations;
        }

        @Override
        public T get() {
            try {
                return supplier.get();
            } catch (Exception e) {
                Optional<WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>> wrappingConfigurationOptional =
                        Arrays.stream(configurations)
                            .filter(wrappingConfiguration -> wrappingConfiguration.canWrap(e))
                            .findFirst();
                if (wrappingConfigurationOptional.isPresent()) {
                    throw wrappingConfigurationOptional.get().wrap(e);
                } else {
                    throw e;
                }
            }
        }

        private static boolean isDistinct(WrappingConfiguration[] configuration) {
            long initialSize = configuration.length;
            long distinctSize = Arrays.stream(configuration)
                    .map(WrappingConfiguration::getExceptionToWrap)
                    .distinct()
                    .count();
            return initialSize == distinctSize;
        }
    }
}
