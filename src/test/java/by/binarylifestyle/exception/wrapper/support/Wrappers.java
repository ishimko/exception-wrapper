package by.binarylifestyle.exception.wrapper.support;

import by.binarylifestyle.exception.wrapper.exception.dao.UncheckedDaoException;
import by.binarylifestyle.exception.wrapper.exception.serivce.UncheckedServiceException;
import by.binarylifestyle.exception.wrapper.impl.common.ExceptionToDefaultWrapper;
import by.binarylifestyle.exception.wrapper.impl.common.ExceptionToOptionalWrapper;
import by.binarylifestyle.exception.wrapper.impl.common.MappingExceptionWrapper;
import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;

public final class Wrappers {
    public static MappingExceptionWrapper daoToServiceExceptionWrapper() {
        return new MappingExceptionWrapper(
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new)
        );
    }

    public static ExceptionToOptionalWrapper daoExceptionToOptionalWrapper() {
        return new ExceptionToOptionalWrapper(
                TestData.daoExceptions()
        );
    }

    public static ExceptionToDefaultWrapper<Integer> daoExceptionToDefaultWrapper() {
        return new ExceptionToDefaultWrapper<>(
                TestData.defaultValueForWrapping(),
                TestData.daoExceptions()
        );
    }
}
