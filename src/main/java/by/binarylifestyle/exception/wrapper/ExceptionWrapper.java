package by.binarylifestyle.exception.wrapper;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ExceptionWrapper {
    private WrappingConfiguration<Exception, Exception>[] configuration;

    public ExceptionWrapper(WrappingConfiguration<Exception, Exception>... configuration) {
        if (!isDistinct(configuration)) {
            throw new IllegalArgumentException("Configuration has repeating elements");
        } else {
            this.configuration = configuration;
        }
    }

    public <T> T wrapChecked(Callable<T> callable) throws Exception {
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

    public <T> T wrapUnchecked(Supplier<T> supplier) {
        return CheckedToRuntimeWrapper.wrap(() -> wrapChecked(supplier::get));
    }

    public void wrapChecked(CheckedRunnable runnable) throws Exception {
        wrapChecked(() -> {
            runnable.run();
            return null;
        });
    }

    public void wrapUnchecked(Runnable runnable) {
        CheckedToRuntimeWrapper.wrap(() -> wrapChecked(runnable::run));
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
