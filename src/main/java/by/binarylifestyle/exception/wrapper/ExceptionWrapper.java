package by.binarylifestyle.exception.wrapper;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ExceptionWrapper {
    @SafeVarargs
    public static <T> Callable<T> fromChecked(Callable<T> callable, WrappingConfiguration<Exception, Exception>... configurations) {
        return new CheckedExceptionWrapper<>(callable, configurations);
    }

    @SafeVarargs
    public static <T> Supplier<T> fromUnchecked(Supplier<T> supplier, WrappingConfiguration<Exception, Exception>... configurations) {
        return CheckedToRuntimeWrapper.from(fromChecked(supplier::get, configurations));
    }

    @SafeVarargs
    public static CheckedRunnable fromChecked(CheckedRunnable runnable, WrappingConfiguration<Exception, Exception>... configurations) {
        return fromChecked(() -> {
            runnable.run();
            return null;
        }, configurations)::call;
    }

    @SafeVarargs
    public static Runnable fromUnchecked(Runnable runnable, WrappingConfiguration<Exception, Exception>... configurations) {
        return CheckedToRuntimeWrapper.from(fromChecked(runnable::run, configurations));
    }

    @SafeVarargs
    public static <T> T wrapChecked(Callable<T> callable, WrappingConfiguration<Exception, Exception>... configurations) throws Exception {
        return fromChecked(callable, configurations).call();
    }

    @SafeVarargs
    public static <T> T wrapUnchecked(Supplier<T> supplier, WrappingConfiguration<Exception, Exception>... configurations) {
        return fromUnchecked(supplier, configurations).get();
    }

    @SafeVarargs
    public static void wrapChecked(CheckedRunnable runnable, WrappingConfiguration<Exception, Exception>... configurations) throws Exception {
        fromChecked(runnable, configurations).run();
    }

    @SafeVarargs
    public static void wrapUnchecked(Runnable runnable, WrappingConfiguration<Exception, Exception>... configurations) {
        fromUnchecked(runnable, configurations).run();
    }

    private static class CheckedExceptionWrapper<T> implements Callable<T> {
        private WrappingConfiguration<Exception, Exception>[] configuration;
        private Callable<T> callable;

        public CheckedExceptionWrapper(Callable<T> callable, WrappingConfiguration<Exception, Exception>... configuration) {
            this.callable = callable;
            if (!isDistinct(configuration)) {
                throw new IllegalArgumentException("Configuration has repeating elements");
            } else {
                this.configuration = configuration;
            }
        }

        @Override
        public T call() throws Exception {
            try {
                return callable.call();
            } catch (Exception e) {
                Optional<WrappingConfiguration<Exception, Exception>> wrappingConfigurationOptional = Arrays.stream(configuration)
                        .filter(wrappingConfiguration -> wrappingConfiguration.canWrap(e))
                        .findFirst();
                if (wrappingConfigurationOptional.isPresent()) {
                    WrappingConfiguration<Exception, Exception> wrappingConfiguration = wrappingConfigurationOptional.get();
                    throw wrappingConfiguration.wrap(e);
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
