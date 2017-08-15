package by.binarylifestyle.exception.wrapper.impl.common;

import by.binarylifestyle.exception.wrapper.api.support.CheckedRunnable;
import by.binarylifestyle.exception.wrapper.callable.FailingCallable;
import by.binarylifestyle.exception.wrapper.exception.serivce.CheckedServiceException;
import by.binarylifestyle.exception.wrapper.exception.serivce.UncheckedServiceException;
import by.binarylifestyle.exception.wrapper.runnable.FailingCheckedRunnable;
import by.binarylifestyle.exception.wrapper.support.TestData;
import by.binarylifestyle.exception.wrapper.support.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CheckedToRuntimeWrapperTests extends CheckedToRuntimeWrapper {
    @Test
    public void voidWrappingCheckedExceptionThrownTest() {
        TestUtil.expectCause(
                () -> CheckedToRuntimeWrapper.wrap(new FailingCheckedRunnable(CheckedServiceException::new)),
                CheckedServiceException.class
        );
    }

    @Test
    public void voidApplyingCheckedExceptionThrownTest() {
        TestUtil.expectCause(
                CheckedToRuntimeWrapper.applyTo(new FailingCheckedRunnable(CheckedServiceException::new)),
                CheckedServiceException.class
        );
    }

    @Test
    public void typedWrappingCheckedExceptionThrownTest() {
        TestUtil.expectCause(
                () -> CheckedToRuntimeWrapper.wrap(new FailingCallable<>(CheckedServiceException::new)),
                CheckedServiceException.class
        );
    }

    @Test
    public void typedApplyingCheckedExceptionThrownTest() {
        TestUtil.expectCause(
                CheckedToRuntimeWrapper.applyTo(new FailingCallable<>(CheckedServiceException::new))::get,
                CheckedServiceException.class
        );
    }

    @Test(expected = UncheckedServiceException.class)
    public void voidWrappingUncheckedExceptionThrownTest() {
        CheckedToRuntimeWrapper.wrap(new FailingCheckedRunnable(UncheckedServiceException::new));
    }

    @Test(expected = UncheckedServiceException.class)
    public void voidApplyingUncheckedExceptionThrownTest() {
        CheckedToRuntimeWrapper.applyTo(new FailingCheckedRunnable(UncheckedServiceException::new)).run();
    }

    @Test(expected = UncheckedServiceException.class)
    public void typedWrappingUncheckedExceptionThrownTest() {
        CheckedToRuntimeWrapper.wrap(new FailingCallable<>(UncheckedServiceException::new));
    }

    @Test(expected = UncheckedServiceException.class)
    public void typedApplyingUncheckedExceptionThrownTest() {
        CheckedToRuntimeWrapper.applyTo(new FailingCallable<>(UncheckedServiceException::new)).get();
    }

    @Test
    public void typedWrappingNoExceptionThrownTest() {
        int actual = CheckedToRuntimeWrapper.wrap(TestData.callable());
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test
    public void typedApplyingNoExceptionThrownTest() {
        int actual = CheckedToRuntimeWrapper.applyTo(TestData.callable()).get();
        Assert.assertEquals(TestData.expectedForAllGetters(), actual);
    }

    @Test
    public void voidWrappingNoExceptionThrownTest() {
        List<Object> list = new ArrayList<>();
        CheckedRunnable checkedRunnable = () -> list.add(new Object());
        CheckedToRuntimeWrapper.wrap(checkedRunnable);
        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void voidApplyingNoExceptionThrownTest() {
        List<Object> list = new ArrayList<>();
        CheckedRunnable checkedRunnable = () -> list.add(new Object());
        CheckedToRuntimeWrapper.applyTo(checkedRunnable).run();
        Assert.assertFalse(list.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void typedWrappingNullTest() {
        CheckedToRuntimeWrapper.wrap((Callable<Object>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void typedApplyingNullTest() {
        CheckedToRuntimeWrapper.applyTo((Callable<Object>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voidWrappingNullTest() {
        CheckedToRuntimeWrapper.wrap((CheckedRunnable) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voidApplyingNullTest() {
        CheckedToRuntimeWrapper.applyTo((CheckedRunnable) null);
    }

}
