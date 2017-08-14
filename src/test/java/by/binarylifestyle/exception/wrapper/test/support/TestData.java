package by.binarylifestyle.exception.wrapper.test.support;

import by.binarylifestyle.exception.wrapper.test.exception.dao.UncheckedSpecificDaoException;
import by.binarylifestyle.exception.wrapper.test.exception.serivce.UncheckedSpecificServiceException;

import java.util.concurrent.Callable;
import java.util.function.Function;
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

    public static Class<UncheckedSpecificDaoException> specificDaoException() {
        return UncheckedSpecificDaoException.class;
    }

    public static Class<UncheckedSpecificServiceException> specificServiceException() {
        return UncheckedSpecificServiceException.class;
    }

    public static Function<UncheckedSpecificDaoException, UncheckedSpecificServiceException> specificDaoExceptionWrapper() {
        return UncheckedSpecificServiceException::new;
    }
}
