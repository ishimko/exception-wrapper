package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;
import by.binarylifestyle.exception.wrapper.exception.dao.UncheckedDaoException;
import by.binarylifestyle.exception.wrapper.exception.dao.UncheckedSpecificDaoException;
import by.binarylifestyle.exception.wrapper.exception.serivce.UncheckedServiceException;
import by.binarylifestyle.exception.wrapper.exception.thirdparty.UncheckedThirdPartyException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

public class WrappingConfigurationTests {
    @Test
    public void canWrapEqualTypesTest() {
        WrappingConfiguration<UncheckedDaoException, UncheckedServiceException> configuration =
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new);
        Exception exception = new UncheckedDaoException();
        Assert.assertTrue(configuration.canWrap(exception));
    }

    @Test
    public void canWrapSubclassTest() {
        WrappingConfiguration<UncheckedDaoException, UncheckedServiceException> configuration =
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new);
        Exception exception = new UncheckedSpecificDaoException();
        Assert.assertTrue(configuration.canWrap(exception));
    }

    @Test
    public void canWrapIncompatibleTypesTest() {
        WrappingConfiguration<UncheckedDaoException, UncheckedServiceException> configuration =
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new);
        Exception exception = new UncheckedThirdPartyException();
        Assert.assertFalse(configuration.canWrap(exception));
    }

    @Test
    public void wrapEqualTypesTest() {
        WrappingConfiguration<UncheckedDaoException, UncheckedServiceException> configuration =
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new);
        Exception exception = new UncheckedDaoException();
        UncheckedServiceException wrappedException = configuration.wrap(exception);
        Assert.assertEquals(UncheckedServiceException.class, wrappedException.getClass());
        Assert.assertEquals(UncheckedDaoException.class, wrappedException.getCause().getClass());
    }

    @Test
    public void wrapSubclassTest() {
        WrappingConfiguration<UncheckedDaoException, UncheckedServiceException> configuration =
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new);
        Exception exception = new UncheckedSpecificDaoException();
        UncheckedServiceException wrappedException = configuration.wrap(exception);
        Assert.assertEquals(UncheckedServiceException.class, wrappedException.getClass());
        Assert.assertEquals(UncheckedSpecificDaoException.class, wrappedException.getCause().getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrapIncompatibleTypesTest() {
        WrappingConfiguration<UncheckedDaoException, UncheckedServiceException> configuration =
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new);
        Exception exception = new UncheckedThirdPartyException();
        configuration.wrap(exception);
    }

    @Test
    public void equalsTest() {
        EqualsVerifier.forClass(WrappingConfiguration.class).usingGetClass().verify();
    }
}
