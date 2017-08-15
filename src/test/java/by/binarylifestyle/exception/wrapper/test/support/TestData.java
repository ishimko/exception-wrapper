package by.binarylifestyle.exception.wrapper.test.support;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class TestData {
    private static final int EXPECTED = 42;
    private static final Supplier<Integer> TEST_SUPPLIER = () -> EXPECTED;
    private static final Callable<Integer> TEST_CALLABLE = TEST_SUPPLIER::get;

    public static int expectedForAllGetters() {
        return EXPECTED;
    }

    public static Supplier<Integer> supplier() {
        return TEST_SUPPLIER;
    }

    public static Callable<Integer> callable() {
        return TEST_CALLABLE;
    }
}
