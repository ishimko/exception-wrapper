package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.api.support.VarargsFunction;
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

import java.util.function.Function;

public class ExceptionToDefaultValueWrapperTests {
    @Test
    public void uncheckedWrappingExceptionToWrapThrownTest() {
        int actual = Wrappers.daoExceptionToDefaultValue()
                .wrap(new FailingSupplier<>(UncheckedDaoException::new));
        Assert.assertEquals(TestData.defaultValueForWrapping(), actual);
    }

    @Test
    public void uncheckedApplyingExceptionToWrapThrownTest() {
        int actual = Wrappers.daoExceptionToDefaultValue()
                .applyTo(new FailingSupplier<>(UncheckedDaoException::new))
                .get();
        Assert.assertEquals(TestData.defaultValueForWrapping(), actual);
    }

    @Test
    public void uncheckedWrappingNoExceptionTest() {
        int actual = Wrappers.daoExceptionToDefaultValue()
                .wrap(TestData.supplier());
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test
    public void uncheckedApplyingNoExceptionTest() {
        int actual = Wrappers.daoExceptionToDefaultValue()
                .applyTo(TestData.supplier())
                .get();
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void uncheckedWrappingAnotherExceptionThrownTest() {
        Wrappers.daoExceptionToDefaultValue()
                .wrap(new FailingSupplier<>(UncheckedThirdPartyException::new));
    }

    @Test(expected = UncheckedThirdPartyException.class)
    public void uncheckedApplyingAnotherExceptionThrownTest() {
        Wrappers.daoExceptionToDefaultValue()
                .applyTo(new FailingSupplier<>(UncheckedThirdPartyException::new))
                .get();
    }

    @Test
    public void uncheckedWrappingSubclassTest() {
        int actual = Wrappers.daoExceptionToDefaultValue()
                .wrap(new FailingSupplier<>(UncheckedSpecificDaoException::new));
        Assert.assertEquals(TestData.defaultValueForWrapping(), actual);
    }

    @Test
    public void uncheckedApplyingSubclassTest() {
        int actual = Wrappers.daoExceptionToDefaultValue()
                .applyTo(new FailingSupplier<>(UncheckedSpecificDaoException::new))
                .get();
        Assert.assertEquals(TestData.defaultValueForWrapping(), actual);
    }

    @Test
    public void checkedWrappingExceptionToWrapThrownTest() throws Exception{
        int actual = Wrappers.daoExceptionToDefaultValue()
                .wrapChecked(new FailingCallable<>(CheckedDaoException::new));
        Assert.assertEquals(TestData.defaultValueForWrapping(), actual);
    }

    @Test
    public void checkedApplyingExceptionToWrapThrownTest() throws Exception {
        int actual = Wrappers.daoExceptionToDefaultValue()
                .applyToChecked(new FailingCallable<>(CheckedDaoException::new))
                .call();
        Assert.assertEquals(TestData.defaultValueForWrapping(), actual);
    }

    @Test
    public void checkedWrappingNoExceptionTest() throws Exception {
        int actual = Wrappers.daoExceptionToDefaultValue()
                .wrapChecked(TestData.callable());
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test
    public void checkedApplyingNoExceptionTest() throws Exception {
        int actual = Wrappers.daoExceptionToDefaultValue()
                .applyToChecked(TestData.callable())
                .call();
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test(expected = CheckedThirdPartyException.class)
    public void checkedWrappingAnotherExceptionThrownTest() throws Exception {
        Wrappers.daoExceptionToDefaultValue()
                .wrapChecked(new FailingCallable<>(CheckedThirdPartyException::new));
    }

    @Test(expected = CheckedThirdPartyException.class)
    public void checkedApplyingAnotherExceptionThrownTest() throws Exception {
        Wrappers.daoExceptionToDefaultValue()
                .applyToChecked(new FailingCallable<>(CheckedThirdPartyException::new))
                .call();
    }

    @Test
    public void checkedWrappingSubclassTest() throws Exception {
        int actual = Wrappers.daoExceptionToDefaultValue()
                .wrapChecked(new FailingCallable<>(CheckedSpecificDaoException::new));
        Assert.assertEquals(TestData.defaultValueForWrapping(), actual);
    }

    @Test
    public void checkedApplyingSubclassTest() throws Exception {
        int actual = Wrappers.daoExceptionToDefaultValue()
                .applyToChecked(new FailingCallable<>(CheckedSpecificDaoException::new))
                .call();
        Assert.assertEquals(TestData.defaultValueForWrapping(), actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrappingUncheckedNullTest() {
        Wrappers.daoExceptionToDefaultValue().wrap(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyingUncheckedNullTest() {
        Wrappers.daoExceptionToDefaultValue().applyTo(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrappingCheckedNullTest() throws Exception{
        Wrappers.daoExceptionToDefaultValue().wrapChecked(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyingCheckedNullTest() {
        Wrappers.daoExceptionToDefaultValue().applyToChecked(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullExceptionsToWrapTest() {
        new ExceptionToDefaultValueWrapper<>((Class<? extends Exception>[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullExceptionInExceptionsToWrapTest() {
        new ExceptionToDefaultValueWrapper<>(null, UncheckedDaoException.class);
    }

    @Test
    public void emptyExceptionsToWrapTest() {
        ExceptionToDefaultValueWrapper<Object> actual =
                new ExceptionToDefaultValueWrapper<>(TestData.defaultValueForWrapping());
        ExceptionToDefaultValueWrapper<Object> expected =
                new ExceptionToDefaultValueWrapper<>(TestData.defaultValueForWrapping(), Exception.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void defaultValueBindingTest() {
        VarargsFunction<Class<? extends Exception>, ExceptionToDefaultValueWrapper<Integer>> wrapperFactory =
                ExceptionToDefaultValueWrapper.bindDefaultValue(TestData.defaultValueForWrapping());
        ExceptionToDefaultValueWrapper<Integer> actual = wrapperFactory.apply(TestData.daoExceptions());
        ExceptionToDefaultValueWrapper<Integer> expected =
                new ExceptionToDefaultValueWrapper<>(TestData.defaultValueForWrapping(), TestData.daoExceptions());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void exceptionsBindingTest() {
        Function<Integer, ExceptionToDefaultValueWrapper<Integer>> wrapperFactory =
                ExceptionToDefaultValueWrapper.bindExceptions(TestData.daoExceptions());
        ExceptionToDefaultValueWrapper<Integer> actual = wrapperFactory.apply(TestData.defaultValueForWrapping());
        ExceptionToDefaultValueWrapper<Integer> expected =
                new ExceptionToDefaultValueWrapper<>(TestData.defaultValueForWrapping(), TestData.daoExceptions());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void equalsTest() {
        EqualsVerifier.forClass(ExceptionToDefaultValueWrapper.class).usingGetClass().verify();
    }
}
