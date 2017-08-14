package by.binarylifestyle.exception.wrapper;

import by.binarylifestyle.exception.wrapper.impl.common.MappingExceptionWrapper;
import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;
import by.binarylifestyle.exception.wrapper.test.exception.dao.UncheckedDaoException;
import by.binarylifestyle.exception.wrapper.test.exception.serivce.UncheckedServiceException;
import by.binarylifestyle.exception.wrapper.test.exception.thirdparty.UncheckedThirdPartyException;
import by.binarylifestyle.exception.wrapper.test.runnable.FailingRunnable;
import by.binarylifestyle.exception.wrapper.test.supplier.FailingSupplier;
import by.binarylifestyle.exception.wrapper.test.support.Wrappers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Supplier;

public class MappingExceptionWrapperTests {
    private static final int EXPECTED = 42;
    private static final Supplier<Integer> TEST_SUPPLIER = () -> EXPECTED;

    @Test(expected = UncheckedServiceException.class)
    public void typedWrappingExceptionToWrapThrownTest() {
        Wrappers.daoToServiceExceptionWrapper().wrap(new FailingSupplier<>(UncheckedDaoException::new));
    }

    @Test(expected = UncheckedServiceException.class)
    public void typedApplyingExceptionToWrapThrownTest() {
        Wrappers.daoToServiceExceptionWrapper().applyTo(new FailingSupplier<>(UncheckedDaoException::new)).get();
    }

    @Test
    public void typedWrappingNoExceptionTest() {
        int actual = Wrappers.<Integer>daoToServiceExceptionWrapper().wrap(TEST_SUPPLIER);
        Assert.assertEquals(EXPECTED, actual);
    }

    @Test
    public void typedApplyingNoExceptionTest() {
        int actual = Wrappers.<Integer>daoToServiceExceptionWrapper().applyTo(TEST_SUPPLIER).get();
        Assert.assertEquals(EXPECTED, actual);
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void typedWrappingAnotherExceptionThrownTest() {
        Wrappers.daoToServiceExceptionWrapper().wrap(new FailingSupplier<>(UncheckedThirdPartyException::new));
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void typedApplyingAnotherExceptionThrownTest() {
        Wrappers.daoToServiceExceptionWrapper().applyTo(new FailingSupplier<>(UncheckedThirdPartyException::new)).get();
    }

    @Test(expected = UncheckedServiceException.class)
    public void voidWrappingExceptionToWrapThrownTest() {
        Wrappers.daoToServiceExceptionWrapper().wrap(new FailingRunnable(UncheckedDaoException::new));
    }

    @Test(expected = UncheckedServiceException.class)
    public void voidApplyingExceptionToWrapThrownTest() {
        Wrappers.daoToServiceExceptionWrapper().applyTo(new FailingRunnable(UncheckedDaoException::new)).run();
    }

    @Test
    public void voidWrappingNoExceptionTest() {
        ArrayList<Object> list = new ArrayList<>();
        Runnable runnable = () -> list.add(new Object());
        Wrappers.daoToServiceExceptionWrapper().wrap(runnable);
        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void voidUncheckedApplyingNoExceptionTest() {
        ArrayList<Object> list = new ArrayList<>();
        Runnable runnable = () -> list.add(new Object());
        Wrappers.daoToServiceExceptionWrapper().applyTo(runnable).run();
        Assert.assertFalse(list.isEmpty());
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void voidUncheckedWrappingAnotherExceptionTest() {
        Wrappers.daoToServiceExceptionWrapper().wrap(new FailingRunnable(UncheckedThirdPartyException::new));
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void voidUncheckedApplyingAnotherExceptionTest() {
        Wrappers.daoToServiceExceptionWrapper().applyTo(new FailingRunnable(UncheckedThirdPartyException::new)).run();
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyConfigurationTest() {
        new MappingExceptionWrapper<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConfigurationsTest() {
        new MappingExceptionWrapper<>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConfigurationTest() {
        new MappingExceptionWrapper<>(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void configurationWithRepeatedExceptionsTest() {
        new MappingExceptionWrapper<>(
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new),
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new));
    }
}
