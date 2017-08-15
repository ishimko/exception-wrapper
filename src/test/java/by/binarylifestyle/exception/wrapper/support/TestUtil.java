package by.binarylifestyle.exception.wrapper.support;

import org.junit.Assert;

public final class TestUtil {
    public static Exception expectException(Runnable runnable, Class<? extends Exception> exceptionClass) {
        Exception exception = getException(runnable);
        Assert.assertEquals(exceptionClass, exception.getClass());
        return exception;
    }

    public static Exception expectCause(Runnable runnable, Class<? extends Exception> expectedException) {
        Exception exception = getException(runnable);
        Assert.assertEquals(expectedException, exception.getCause().getClass());
        return exception;
    }

    private static Exception getException(Runnable runnable) {
        try {
            runnable.run();
            throw new AssertionError("Exception expected");
        } catch (Exception e) {
            return e;
        }
    }
}
