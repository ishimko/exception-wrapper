package by.binarylifestyle.exception.wrapper;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ExceptionWrapper {
    @SafeVarargs
    public static <T> Callable<T> fromChecked(Callable<T> callable,
                                              WrappingConfiguration<? extends Exception, ? extends Exception>... configurations) {
        return new CheckedExceptionWrapper<>(callable, configurations);
    }

    @SafeVarargs
    public static <T> Supplier<T> fromUnchecked(Supplier<T> supplier,
                                                WrappingConfiguration<? extends Exception, ? extends Exception>... configurations) {
        return CheckedToRuntimeWrapper.from(fromChecked(supplier::get, configurations));
    }

    @SafeVarargs
    public static CheckedRunnable fromChecked(CheckedRunnable runnable,
                                              WrappingConfiguration<? extends Exception, ? extends Exception>... configurations) {
        return fromChecked(() -> {
            runnable.run();
            return null;
        }, configurations)::call;
    }

    @SafeVarargs
    public static Runnable fromUnchecked(Runnable runnable,
                                         WrappingConfiguration<? extends Exception, ? extends Exception>... configurations) {
        return CheckedToRuntimeWrapper.from(fromChecked(runnable::run, configurations));
    }

    @SafeVarargs
    public static <T> T wrapChecked(Callable<T> callable,
                                    WrappingConfiguration<? extends Exception, ? extends Exception>... configurations) throws Exception {
        return fromChecked(callable, configurations).call();
    }

    @SafeVarargs
    public static <T> T wrapUnchecked(Supplier<T> supplier,
                                      WrappingConfiguration<? extends Exception, ? extends Exception>... configurations) {
        return fromUnchecked(supplier, configurations).get();
    }

    @SafeVarargs
    public static void wrapChecked(CheckedRunnable runnable,
                                   WrappingConfiguration<? extends Exception, ? extends Exception>... configurations) throws Exception {
        fromChecked(runnable, configurations).run();
    }

    @SafeVarargs
    public static void wrapUnchecked(Runnable runnable,
                                     WrappingConfiguration<? extends Exception, ? extends Exception>... configurations) {
        fromUnchecked(runnable, configurations).run();
    }

    private static class CheckedExceptionWrapper<T> implements Callable<T> {
        private WrappingConfiguration<? extends Exception, ? extends Exception>[] configurations;
        private Callable<T> callable;

        @SafeVarargs
        public CheckedExceptionWrapper(Callable<T> callable,
                                       WrappingConfiguration<? extends Exception, ? extends Exception>... configurations) {
            if (configurations.length == 0) {
                throw new IllegalArgumentException("No configuration provided");
            }
            if (!isDistinct(configurations)) {
                throw new IllegalArgumentException("Configuration has repeating elements");
            }
            this.callable = callable;
            this.configurations = configurations;
        }

        @Override
        public T call() throws Exception {
            try {
                return callable.call();
            } catch (Exception e) {
                Optional<WrappingConfiguration<? extends Exception, ? extends Exception>> wrappingConfigurationOptional =
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
