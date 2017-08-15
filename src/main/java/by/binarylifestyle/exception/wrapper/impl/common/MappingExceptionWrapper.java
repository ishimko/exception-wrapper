package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.api.common.UncheckedExceptionWrapper;
import by.binarylifestyle.exception.wrapper.impl.support.ValidationUtil;
import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

public class MappingExceptionWrapper<T> implements UncheckedExceptionWrapper<T, T> {
    private WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>[] configurations;

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

    @Override
    public Supplier<T> applyTo(Supplier<T> supplier) {
        ValidationUtil.requireNotNull(supplier, "supplier");
        return new CheckedExceptionWrappingSupplier<>(supplier, configurations);
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
        private WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>[] configurations;
        private Supplier<T> supplier;

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

    private static class ClassHierarchyComparator implements Comparator<Class<?>> {
        @Override
        public int compare(Class<?> c1, Class<?> c2) {
            if (c1 == c2) {
                return 0;
            } else if (c1.isAssignableFrom(c2)) {
                return 1;
            } else if (c2.isAssignableFrom(c1)) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
