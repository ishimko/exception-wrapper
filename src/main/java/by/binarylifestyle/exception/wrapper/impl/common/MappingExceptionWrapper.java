package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.impl.support.ClassHierarchyComparator;
import by.binarylifestyle.exception.wrapper.impl.support.ValidationUtil;
import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

public class MappingExceptionWrapper {
    private final WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>[] configurations;

    @SafeVarargs
    public MappingExceptionWrapper(WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>... configurations) {
        ValidationUtil.requireNotNull(configurations, "configurations");
        ValidationUtil.requireNotEmpty(configurations, "configurations");
        ValidationUtil.requireAllNotNull(configurations, "configurations");
        if (!isDistinct(configurations)) {
            throw new IllegalArgumentException("Configuration has repeating elements");
        }
        Arrays.sort(configurations, new WrappingConfigurationComparator());
        this.configurations = configurations;
    }

    public <T> Supplier<T> applyTo(Supplier<T> supplier) {
        ValidationUtil.requireNotNull(supplier, "supplier");
        return new CheckedExceptionWrappingSupplier<>(supplier, configurations);
    }

    public <T> T wrap(Supplier<T> supplier) {
        return applyTo(supplier).get();
    }

    public Runnable applyTo(Runnable runnable) {
        ValidationUtil.requireNotNull(runnable, "runnable");
        return applyTo(() -> {
            runnable.run();
            return null;
        })::get;
    }

    public void wrap(Runnable runnable) {
        applyTo(runnable).run();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MappingExceptionWrapper that = (MappingExceptionWrapper) o;
        return Arrays.equals(configurations, that.configurations);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(configurations);
    }

    private static boolean isDistinct(WrappingConfiguration[] configuration) {
        long initialSize = configuration.length;
        long distinctSize = Arrays.stream(configuration)
                .map(WrappingConfiguration::getExceptionToWrap)
                .distinct()
                .count();
        return initialSize == distinctSize;
    }

    private static class CheckedExceptionWrappingSupplier<T> implements Supplier<T> {
        private final WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>[] configurations;
        private final Supplier<T> supplier;

        @SafeVarargs
        CheckedExceptionWrappingSupplier(Supplier<T> supplier,
                                         WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>... configurations) {
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
    }

    private static class WrappingConfigurationComparator implements Comparator<WrappingConfiguration<?, ?>> {
        private final ClassHierarchyComparator classHierarchyComparator;

        WrappingConfigurationComparator() {
            classHierarchyComparator = new ClassHierarchyComparator();
        }

        @Override
        public int compare(WrappingConfiguration<?, ?> o1, WrappingConfiguration<?, ?> o2) {
            return classHierarchyComparator.compare(o1.getExceptionToWrap(), o2.getExceptionToWrap());
        }
    }

}
