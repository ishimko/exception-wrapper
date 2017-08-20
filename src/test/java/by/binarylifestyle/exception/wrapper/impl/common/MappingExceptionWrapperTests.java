package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.exception.dao.UncheckedDaoException;
import by.binarylifestyle.exception.wrapper.exception.dao.UncheckedSpecificDaoException;
import by.binarylifestyle.exception.wrapper.exception.serivce.UncheckedServiceException;
import by.binarylifestyle.exception.wrapper.exception.serivce.UncheckedSpecificServiceException;
import by.binarylifestyle.exception.wrapper.exception.thirdparty.UncheckedThirdPartyException;
import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;
import by.binarylifestyle.exception.wrapper.runnable.FailingRunnable;
import by.binarylifestyle.exception.wrapper.supplier.FailingSupplier;
import by.binarylifestyle.exception.wrapper.support.TestData;
import by.binarylifestyle.exception.wrapper.support.Wrappers;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Supplier;

import static by.binarylifestyle.exception.wrapper.support.TestUtil.expectException;

@SuppressWarnings("ThrowableNotThrown")
public class MappingExceptionWrapperTests {
    private static final WrappingConfiguration<UncheckedDaoException, UncheckedServiceException>
            PARENT_EXCEPTIONS_WRAPPING_CONFIGURATION =
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new);
    private static final WrappingConfiguration<UncheckedSpecificDaoException, UncheckedSpecificServiceException>
            CHILD_EXCEPTIONS_WRAPPING_CONFIGURATION =
                new WrappingConfiguration<>(UncheckedSpecificDaoException.class, UncheckedSpecificServiceException::new);

    private static final MappingExceptionWrapper INVERTED_HIERARCHY_CONFIGURATION_WRAPPER =
            new MappingExceptionWrapper(
                    PARENT_EXCEPTIONS_WRAPPING_CONFIGURATION,
                    CHILD_EXCEPTIONS_WRAPPING_CONFIGURATION
            );
    private static final MappingExceptionWrapper DIRECT_HIERARCHY_CONFIGURATION_WRAPPER =
            new MappingExceptionWrapper(
                    CHILD_EXCEPTIONS_WRAPPING_CONFIGURATION,
                    PARENT_EXCEPTIONS_WRAPPING_CONFIGURATION
            );


    @Test(expected = UncheckedServiceException.class)
    public void typedWrappingExceptionToWrapThrownTest() {
        Wrappers.daoToServiceException().wrap(new FailingSupplier<>(UncheckedDaoException::new));
    }

    @Test(expected = UncheckedServiceException.class)
    public void typedApplyingExceptionToWrapThrownTest() {
        Wrappers.daoToServiceException().applyTo(new FailingSupplier<>(UncheckedDaoException::new)).get();
    }

    @Test
    public void typedWrappingSubclassOverrideTest() {
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
    public void typedApplyingSubclassOverrideTest() {
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
    public void voidWrappingSubclassOverrideTest() {
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
    public void voidApplyingSubclassOverrideTest() {
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
        int actual = Wrappers.<Integer>daoToServiceException().wrap(TestData.supplier());
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test
    public void typedApplyingNoExceptionTest() {
        int actual = Wrappers.<Integer>daoToServiceException().applyTo(TestData.supplier()).get();
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void typedWrappingAnotherExceptionThrownTest() {
        Wrappers.daoToServiceException().wrap(new FailingSupplier<>(UncheckedThirdPartyException::new));
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void typedApplyingAnotherExceptionThrownTest() {
        Wrappers.daoToServiceException().applyTo(new FailingSupplier<>(UncheckedThirdPartyException::new)).get();
    }

    @Test(expected = UncheckedServiceException.class)
    public void voidWrappingExceptionToWrapThrownTest() {
        Wrappers.daoToServiceException().wrap(new FailingRunnable(UncheckedDaoException::new));
    }

    @Test(expected = UncheckedServiceException.class)
    public void voidApplyingExceptionToWrapThrownTest() {
        Wrappers.daoToServiceException().applyTo(new FailingRunnable(UncheckedDaoException::new)).run();
    }

    @Test
    public void voidWrappingNoExceptionTest() {
        ArrayList<Object> list = new ArrayList<>();
        Runnable runnable = () -> list.add(new Object());
        Wrappers.daoToServiceException().wrap(runnable);
        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void voidApplyingNoExceptionTest() {
        ArrayList<Object> list = new ArrayList<>();
        Runnable runnable = () -> list.add(new Object());
        Wrappers.daoToServiceException().applyTo(runnable).run();
        Assert.assertFalse(list.isEmpty());
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void voidWrappingAnotherExceptionTest() {
        Wrappers.daoToServiceException().wrap(new FailingRunnable(UncheckedThirdPartyException::new));
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void voidApplyingAnotherExceptionTest() {
        Wrappers.daoToServiceException().applyTo(new FailingRunnable(UncheckedThirdPartyException::new)).run();
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyConfigurationTest() {
        new MappingExceptionWrapper();
    }

    @Test(expected = UncheckedServiceException.class)
    public void typedWrappingSubclassTest() {
        Wrappers.daoToServiceException().wrap(new FailingSupplier<>(UncheckedSpecificDaoException::new));
    }

    @Test(expected = UncheckedServiceException.class)
    public void typedApplyingSubclassTest() {
        Wrappers.daoToServiceException().applyTo(new FailingSupplier<>(UncheckedSpecificDaoException::new)).get();
    }

    @Test(expected = UncheckedServiceException.class)
    public void voidWrappingSubclassTest() {
        Wrappers.daoToServiceException().wrap(new FailingRunnable(UncheckedSpecificDaoException::new));
    }

    @Test(expected = UncheckedServiceException.class)
    public void voidApplyingSubclassTest() {
        Wrappers.daoToServiceException().applyTo(new FailingRunnable(UncheckedSpecificDaoException::new)).run();
    }

    @Test(expected = IllegalArgumentException.class)
    public void typedApplyingNullTest() {
        Wrappers.daoToServiceException().applyTo((Supplier<Object>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void typedWrappingNullTest() {
        Wrappers.daoToServiceException().wrap((Supplier<Object>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voidWrappingNullTest() {
        Wrappers.daoToServiceException().wrap((Runnable) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voidApplyingNullTest() {
        Wrappers.daoToServiceException().applyTo((Runnable) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConfigurationsTest() {
        new MappingExceptionWrapper((WrappingConfiguration<? extends RuntimeException, ? extends RuntimeException>[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConfigurationTest() {
        new MappingExceptionWrapper(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void configurationWithRepeatedExceptionsTest() {
        new MappingExceptionWrapper(
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new),
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new)
        );
    }

    @Test
    public void equalsTest() {
        EqualsVerifier.forClass(MappingExceptionWrapper.class).usingGetClass().verify();
    }
}
