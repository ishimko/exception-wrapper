package by.binarylifestyle.exception.wrapper.impl.support;

import by.binarylifestyle.exception.wrapper.support.TestUtil;
import org.junit.Assert;
import org.junit.Test;

public class ValidationUtilTests extends ValidationUtil {
    private static final String PARAMETER_NAME = "name";
    private static final Object[] ARRAY_WITH_NULLS = new Object[]{null, new Object()};
    private static final Object[] ARRAY_WITHOUT_NULLS = new Object[]{new Object(), new Object()};

    @Test
    public void requireNotNullNullPassedTest() {
        Exception exception = TestUtil.expectException(
                () -> ValidationUtil.requireNotNull(null, PARAMETER_NAME),
                IllegalArgumentException.class
        );
        Assert.assertTrue(exception.getMessage().contains(PARAMETER_NAME));
    }

    @Test
    public void requireNotNullNotNullPassedTest() {
        ValidationUtil.requireNotNull(new Object(), PARAMETER_NAME);
    }

    @Test
    public void requireAllNotNullNullsPassedTest() {
        Exception exception = TestUtil.expectException(
                () -> ValidationUtil.requireAllNotNull(ARRAY_WITH_NULLS, PARAMETER_NAME),
                IllegalArgumentException.class
        );
        Assert.assertTrue(exception.getMessage().contains(PARAMETER_NAME));
    }

    @Test
    public void requireAllNotNullNoNullsPassedTest() {
        ValidationUtil.requireAllNotNull(ARRAY_WITHOUT_NULLS, PARAMETER_NAME);
    }

    @Test()
    public void requireAllNotNullNullArrayPassedTest() {
        Exception exception = TestUtil.expectException(
                () -> ValidationUtil.requireAllNotNull(null, PARAMETER_NAME),
                IllegalArgumentException.class
        );
        Assert.assertTrue(exception.getMessage().contains(PARAMETER_NAME));
    }

    @Test
    public void requireNotEmptyEmptyArrayPassedTest() {
        Exception exception = TestUtil.expectException(
                () -> ValidationUtil.requireNotEmpty(new Object[]{}, PARAMETER_NAME),
                IllegalArgumentException.class
        );
        Assert.assertTrue(exception.getMessage().contains(PARAMETER_NAME));
    }

    @Test
    public void requireNotEmptyNotEmptyArrayPassedTest() {
        ValidationUtil.requireNotEmpty(new Object[]{new Object()}, PARAMETER_NAME);
    }

    @Test
    public void requireNotEmptyNullArrayPassedTest() {
        Exception exception = TestUtil.expectException(
                () -> ValidationUtil.requireNotEmpty(null, PARAMETER_NAME),
                IllegalArgumentException.class
        );
        Assert.assertTrue(exception.getMessage().contains(PARAMETER_NAME));
    }
}
