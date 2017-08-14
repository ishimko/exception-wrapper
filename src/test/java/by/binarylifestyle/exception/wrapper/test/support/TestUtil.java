package by.binarylifestyle.exception.wrapper.test.support;

import org.junit.Assert;

public final class TestUtil {
    public static void expectException(Runnable runnable, Class<? extends RuntimeException> exceptionClass) {
        try {
            runnable.run();
            Assert.fail(String.format("Expected exception: %s", exceptionClass));
        } catch (RuntimeException e) {
            Assert.assertEquals(exceptionClass, e.getClass());
        }
    }
}
