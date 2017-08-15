package by.binarylifestyle.exception.wrapper.test.support;

import by.binarylifestyle.exception.wrapper.api.common.UncheckedExceptionWrapper;
import by.binarylifestyle.exception.wrapper.api.common.typed.TypedExceptionWrapper;
import by.binarylifestyle.exception.wrapper.impl.common.ExceptionToDefaultWrapper;
import by.binarylifestyle.exception.wrapper.impl.common.ExceptionToOptionalWrapper;
import by.binarylifestyle.exception.wrapper.impl.common.MappingExceptionWrapper;
import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;
import by.binarylifestyle.exception.wrapper.test.exception.dao.UncheckedDaoException;
import by.binarylifestyle.exception.wrapper.test.exception.serivce.UncheckedServiceException;

import java.util.Optional;

public final class Wrappers {
    public static <T> UncheckedExceptionWrapper<T, T> daoToServiceExceptionWrapper() {
        return new MappingExceptionWrapper<>(
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new)
        );
    }

    public static <T> TypedExceptionWrapper<T, Optional<T>> daoExceptionToOptionalWrapper() {
        return new ExceptionToOptionalWrapper<>(
                TestData.daoExceptions()
        );
    }

    public static TypedExceptionWrapper<Integer, Integer> daoExceptionToDefaultWrapper() {
        return new ExceptionToDefaultWrapper<>(
                TestData.defaultValueForWrapping(),
                TestData.daoExceptions()
        );
    }
}
