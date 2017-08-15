package by.binarylifestyle.exception.wrapper.test.support;

import by.binarylifestyle.exception.wrapper.test.exception.dao.CheckedDaoException;
import by.binarylifestyle.exception.wrapper.test.exception.dao.UncheckedDaoException;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class TestData {
    private static final int EXPECTED = 42;
    private static final int DEFAULT = 0;
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

    public static int defaultValueForWrapping() {
        return DEFAULT;
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends Exception>[] daoExceptions() {
        return new Class[]{CheckedDaoException.class, UncheckedDaoException.class};
    }
}
