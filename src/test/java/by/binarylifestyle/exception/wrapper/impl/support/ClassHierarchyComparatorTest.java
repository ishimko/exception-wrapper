package by.binarylifestyle.exception.wrapper.impl.support;

import by.binarylifestyle.exception.wrapper.exception.dao.CheckedDaoException;
import by.binarylifestyle.exception.wrapper.exception.dao.CheckedSpecificDaoException;
import by.binarylifestyle.exception.wrapper.exception.thirdparty.CheckedThirdPartyException;
import by.binarylifestyle.exception.wrapper.support.TestData;
import org.junit.Assert;
import org.junit.Test;

public class ClassHierarchyComparatorTest {
    @Test
    public void compareEqualTypesTest() {
        ClassHierarchyComparator comparator = TestData.classHierarchyComparator();
        int result = comparator.compare(Exception.class, Exception.class);
        Assert.assertEquals(0, result);
    }

    @Test
    public void compareIncompatibleTypesTest() {
        ClassHierarchyComparator comparator = TestData.classHierarchyComparator();
        int result = comparator.compare(CheckedThirdPartyException.class, CheckedDaoException.class);
        Assert.assertEquals(0, result);
    }

    @Test
    public void compareChildWithParentTest() {
        ClassHierarchyComparator comparator = TestData.classHierarchyComparator();
        int result = comparator.compare(CheckedSpecificDaoException.class, CheckedDaoException.class);
        Assert.assertTrue(result < 0);
    }

    @Test
    public void compareParentWithChildTest() {
        ClassHierarchyComparator comparator = TestData.classHierarchyComparator();
        int result = comparator.compare(CheckedDaoException.class, CheckedSpecificDaoException.class);
        Assert.assertTrue(result > 0);
    }

}