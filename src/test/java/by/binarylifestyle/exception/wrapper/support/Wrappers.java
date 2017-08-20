package by.binarylifestyle.exception.wrapper.support;

import by.binarylifestyle.exception.wrapper.exception.dao.UncheckedDaoException;
import by.binarylifestyle.exception.wrapper.exception.serivce.UncheckedServiceException;
import by.binarylifestyle.exception.wrapper.impl.common.ExceptionToDefaultValueWrapper;
import by.binarylifestyle.exception.wrapper.impl.common.ExceptionToOptionalWrapper;
import by.binarylifestyle.exception.wrapper.impl.common.MappingExceptionWrapper;
import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;

public final class Wrappers {
    public static MappingExceptionWrapper daoToServiceException() {
        return new MappingExceptionWrapper(
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new)
        );
    }

    public static ExceptionToOptionalWrapper daoExceptionToOptional() {
        return new ExceptionToOptionalWrapper(
                TestData.daoExceptions()
        );
    }

    public static ExceptionToDefaultValueWrapper<Integer> daoExceptionToDefault() {
        return new ExceptionToDefaultValueWrapper<>(
                TestData.defaultValueForWrapping(),
                TestData.daoExceptions()
        );
    }
}
