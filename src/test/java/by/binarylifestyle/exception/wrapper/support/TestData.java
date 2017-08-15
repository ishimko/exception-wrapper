package by.binarylifestyle.exception.wrapper.support;

import by.binarylifestyle.exception.wrapper.exception.dao.CheckedDaoException;
import by.binarylifestyle.exception.wrapper.exception.dao.UncheckedDaoException;
import by.binarylifestyle.exception.wrapper.impl.support.ClassHierarchyComparator;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TestData {
    private static final int EXPECTED = 42;
    private static final int DEFAULT = 0;
    private static final Supplier<Integer> TEST_SUPPLIER = () -> EXPECTED;
    private static final Callable<Integer> TEST_CALLABLE = TEST_SUPPLIER::get;
    private static final Class[] DAO_EXCEPTIONS = {CheckedDaoException.class, UncheckedDaoException.class};
    private static final ClassHierarchyComparator CLASS_HIERARCHY_COMPARATOR = new ClassHierarchyComparator();
    private static final Function<Integer[], Integer> SUM_FUNCTION =
            integers -> Arrays.stream(integers)
                    .mapToInt(Integer::intValue)
                    .sum();

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
        return DAO_EXCEPTIONS;
    }

    public static Function<Integer[], Integer> function() {
        return SUM_FUNCTION;
    }

    public static ClassHierarchyComparator classHierarchyComparator() {
        return CLASS_HIERARCHY_COMPARATOR;
    }
}
