package by.binarylifestyle.exception.wrapper.test;

import by.binarylifestyle.exception.wrapper.impl.common.MappingExceptionWrapper;
import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;
import by.binarylifestyle.exception.wrapper.test.exception.dao.UncheckedDaoException;
import by.binarylifestyle.exception.wrapper.test.exception.dao.UncheckedSpecificDaoException;
import by.binarylifestyle.exception.wrapper.test.exception.serivce.UncheckedServiceException;
import by.binarylifestyle.exception.wrapper.test.exception.serivce.UncheckedSpecificServiceException;
import by.binarylifestyle.exception.wrapper.test.exception.thirdparty.UncheckedThirdPartyException;
import by.binarylifestyle.exception.wrapper.test.runnable.FailingRunnable;
import by.binarylifestyle.exception.wrapper.test.supplier.FailingSupplier;
import by.binarylifestyle.exception.wrapper.test.support.TestData;
import by.binarylifestyle.exception.wrapper.test.support.Wrappers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static by.binarylifestyle.exception.wrapper.test.support.TestUtil.expectException;

public class MappingExceptionWrapperTests {
    private static final WrappingConfiguration<UncheckedDaoException, UncheckedServiceException>
            PARENT_EXCEPTIONS_WRAPPING_CONFIGURATION =
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new);
    private static final WrappingConfiguration<UncheckedSpecificDaoException, UncheckedSpecificServiceException>
            CHILD_EXCEPTIONS_WRAPPING_CONFIGURATION =
                new WrappingConfiguration<>(UncheckedSpecificDaoException.class, UncheckedSpecificServiceException::new);

    private static final MappingExceptionWrapper<Object> INVERTED_HIERARCHY_CONFIGURATION_WRAPPER =
            new MappingExceptionWrapper<>(
                    PARENT_EXCEPTIONS_WRAPPING_CONFIGURATION,
                    CHILD_EXCEPTIONS_WRAPPING_CONFIGURATION
            );
    private static final MappingExceptionWrapper<Object> DIRECT_HIERARCHY_CONFIGURATION_WRAPPER =
            new MappingExceptionWrapper<>(
                    CHILD_EXCEPTIONS_WRAPPING_CONFIGURATION,
                    PARENT_EXCEPTIONS_WRAPPING_CONFIGURATION
            );


    @Test(expected = UncheckedServiceException.class)
    public void typedWrappingExceptionToWrapThrownTest() {
        Wrappers.daoToServiceExceptionWrapper().wrap(new FailingSupplier<>(UncheckedDaoException::new));
    }

    @Test(expected = UncheckedServiceException.class)
    public void typedApplyingExceptionToWrapThrownTest() {
        Wrappers.daoToServiceExceptionWrapper().applyTo(new FailingSupplier<>(UncheckedDaoException::new)).get();
    }

    @Test
    public void typedWrappingInheritanceTest() {
        FailingSupplier<Object> failingSupplier = new FailingSupplier<>(UncheckedSpecificDaoException::new);
        expectException(
                () -> DIRECT_HIERARCHY_CONFIGURATION_WRAPPER.wrap(failingSupplier),
                UncheckedSpecificServiceException.class
        );
        expectException(
                () -> INVERTED_HIERARCHY_CONFIGURATION_WRAPPER.wrap(failingSupplier),
                UncheckedSpecificServiceException.class
        );
    }

    @Test
    public void typedApplyingInheritanceTest() {
        FailingSupplier<Object> failingSupplier = new FailingSupplier<>(UncheckedSpecificDaoException::new);
        expectException(
                DIRECT_HIERARCHY_CONFIGURATION_WRAPPER.applyTo(failingSupplier)::get,
                UncheckedSpecificServiceException.class
        );
        expectException(
                INVERTED_HIERARCHY_CONFIGURATION_WRAPPER.applyTo(failingSupplier)::get,
                UncheckedSpecificServiceException.class
        );
    }

    @Test
    public void voidWrappingInheritanceTest() {
        FailingRunnable failingRunnable = new FailingRunnable(UncheckedSpecificDaoException::new);
        expectException(
                () -> DIRECT_HIERARCHY_CONFIGURATION_WRAPPER.wrap(failingRunnable),
                UncheckedSpecificServiceException.class
        );
        expectException(
                () -> INVERTED_HIERARCHY_CONFIGURATION_WRAPPER.wrap(failingRunnable),
                UncheckedSpecificServiceException.class
        );
    }

    @Test
    public void voidApplyingInheritanceTest() {
        FailingRunnable failingRunnable = new FailingRunnable(UncheckedSpecificDaoException::new);
        expectException(
                DIRECT_HIERARCHY_CONFIGURATION_WRAPPER.applyTo(failingRunnable),
                UncheckedSpecificServiceException.class
        );
        expectException(
                INVERTED_HIERARCHY_CONFIGURATION_WRAPPER.applyTo(failingRunnable),
                UncheckedSpecificServiceException.class
        );
    }

    @Test
    public void typedWrappingNoExceptionTest() {
        int actual = Wrappers.<Integer>daoToServiceExceptionWrapper().wrap(TestData.supplier());
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test
    public void typedApplyingNoExceptionTest() {
        int actual = Wrappers.<Integer>daoToServiceExceptionWrapper().applyTo(TestData.supplier()).get();
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
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
    public void voidApplyingNoExceptionTest() {
        ArrayList<Object> list = new ArrayList<>();
        Runnable runnable = () -> list.add(new Object());
        Wrappers.daoToServiceExceptionWrapper().applyTo(runnable).run();
        Assert.assertFalse(list.isEmpty());
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void voidWrappingAnotherExceptionTest() {
        Wrappers.daoToServiceExceptionWrapper().wrap(new FailingRunnable(UncheckedThirdPartyException::new));
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void voidApplyingAnotherExceptionTest() {
        Wrappers.daoToServiceExceptionWrapper().applyTo(new FailingRunnable(UncheckedThirdPartyException::new)).run();
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyConfigurationTest() {
        new MappingExceptionWrapper<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConfigurationsTest() {
        new MappingExceptionWrapper<>((WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConfigurationTest() {
        new MappingExceptionWrapper<>(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void configurationWithRepeatedExceptionsTest() {
        new MappingExceptionWrapper<>(
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new),
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new)
        );
    }
}
