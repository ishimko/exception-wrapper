package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.callable.FailingCallable;
import by.binarylifestyle.exception.wrapper.exception.dao.CheckedDaoException;
import by.binarylifestyle.exception.wrapper.exception.dao.CheckedSpecificDaoException;
import by.binarylifestyle.exception.wrapper.exception.dao.UncheckedDaoException;
import by.binarylifestyle.exception.wrapper.exception.dao.UncheckedSpecificDaoException;
import by.binarylifestyle.exception.wrapper.exception.thirdparty.CheckedThirdPartyException;
import by.binarylifestyle.exception.wrapper.exception.thirdparty.UncheckedThirdPartyException;
import by.binarylifestyle.exception.wrapper.supplier.FailingSupplier;
import by.binarylifestyle.exception.wrapper.support.TestData;
import by.binarylifestyle.exception.wrapper.support.Wrappers;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class ExceptionToOptionalWrapperTests {
    @Test
    public void wrappingUncheckedExceptionToWrapThrownTest() {
        Optional<Object> result = Wrappers.daoExceptionToOptional()
                .wrap(new FailingSupplier<>(UncheckedDaoException::new));
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void applyingUncheckedExceptionToWrapThrownTest() {
        Optional<Object> result = Wrappers.daoExceptionToOptional()
                .applyTo(new FailingSupplier<>(UncheckedDaoException::new))
                .get();
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void wrappingUncheckedNoExceptionTest() {
        int actual = Wrappers.<Integer>daoExceptionToOptional()
                .wrap(TestData.supplier())
                .get();
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test
    public void applyingUncheckedNoExceptionTest() {
        int actual = Wrappers.<Integer>daoExceptionToOptional()
                .applyTo(TestData.supplier())
                .get()
                .get();
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void wrappingUncheckedAnotherExceptionThrownTest() {
        Wrappers.daoExceptionToOptional().wrap(new FailingSupplier<>(UncheckedThirdPartyException::new));
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void applyingUncheckedAnotherExceptionThrownTest() {
        Wrappers.daoExceptionToOptional().applyTo(new FailingSupplier<>(UncheckedThirdPartyException::new)).get();
    }

    @Test
    public void wrappingUncheckedSubclassTest() {
        Optional<Object> result = Wrappers.daoExceptionToOptional()
                .wrap(new FailingSupplier<>(UncheckedSpecificDaoException::new));
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void applyingUncheckedSubclassTest() {
        Optional<Object> result = Wrappers.daoExceptionToOptional()
                .applyTo(new FailingSupplier<>(UncheckedSpecificDaoException::new))
                .get();
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void wrappingCheckedExceptionToWrapThrownTest() throws Exception{
        Optional<Object> result = Wrappers.daoExceptionToOptional()
                .wrapChecked(new FailingCallable<>(CheckedDaoException::new));
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void applyingCheckedExceptionToWrapThrownTest() throws Exception {
        Optional<Object> result = Wrappers.daoExceptionToOptional()
                .applyToChecked(new FailingCallable<>(CheckedDaoException::new))
                .call();
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void wrappingCheckedNoExceptionTest() throws Exception {
        int actual = Wrappers.<Integer>daoExceptionToOptional()
                .wrapChecked(TestData.callable())
                .get();
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test
    public void applyingCheckedNoExceptionTest() throws Exception {
        int actual = Wrappers.<Integer>daoExceptionToOptional()
                .applyToChecked(TestData.callable())
                .call()
                .get();
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test(expected = CheckedThirdPartyException.class)
    public void wrappingCheckedAnotherExceptionThrownTest() throws Exception {
        Wrappers.daoExceptionToOptional().wrapChecked(new FailingCallable<>(CheckedThirdPartyException::new));
    }

    @Test(expected = CheckedThirdPartyException.class)
    public void applyingCheckedAnotherExceptionThrownTest() throws Exception {
        Wrappers.daoExceptionToOptional()
                .applyToChecked(new FailingCallable<>(CheckedThirdPartyException::new)).call();
    }

    @Test
    public void wrappingCheckedSubclassTest() throws Exception {
        Optional<Object> result = Wrappers.daoExceptionToOptional()
                .wrapChecked(new FailingCallable<>(CheckedSpecificDaoException::new));
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void applyingCheckedSubclassTest() throws Exception {
        Optional<Object> result = Wrappers.daoExceptionToOptional()
                .applyToChecked(new FailingCallable<>(CheckedSpecificDaoException::new))
                .call();
        Assert.assertFalse(result.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrappingUncheckedNullTest() {
        Wrappers.daoExceptionToOptional().wrap(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyingUncheckedNullTest() {
        Wrappers.daoExceptionToOptional().applyTo(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrappingCheckedNullTest() throws Exception{
        Wrappers.daoExceptionToOptional().wrapChecked(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyingCheckedNullTest() {
        Wrappers.daoExceptionToOptional().applyToChecked(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullExceptionsToWrapTest() {
        new ExceptionToOptionalWrapper((Class<? extends Exception>[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullExceptionInExceptionsToWrapTest() {
        new ExceptionToOptionalWrapper(null, UncheckedDaoException.class);
    }

    @Test
    public void emptyExceptionsToWrapTest() {
        ExceptionToOptionalWrapper exceptionWrapperImplicit = new ExceptionToOptionalWrapper();
        ExceptionToOptionalWrapper exceptionWrapperExplicit = new ExceptionToOptionalWrapper(Exception.class);
        Assert.assertEquals(exceptionWrapperExplicit, exceptionWrapperImplicit);
    }

    @Test
    public void equalsTest() {
        EqualsVerifier.forClass(ExceptionToOptionalWrapper.class).usingGetClass().verify();
    }
}
