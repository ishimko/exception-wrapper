package by.binarylifestyle.exception.wrapper.support;

import by.binarylifestyle.exception.wrapper.exception.dao.UncheckedDaoException;
import by.binarylifestyle.exception.wrapper.exception.serivce.UncheckedServiceException;
import by.binarylifestyle.exception.wrapper.impl.common.ExceptionToDefaultValueWrapper;
import by.binarylifestyle.exception.wrapper.impl.common.ExceptionToOptionalWrapper;
import by.binarylifestyle.exception.wrapper.impl.common.MappingExceptionWrapper;
import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;

public class Wrappers {
    private static final MappingExceptionWrapper DAO_TO_SERVICE =
            new MappingExceptionWrapper(
                    new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new)
            );
    private static final ExceptionToOptionalWrapper DAO_EXCEPTION_TO_OPTIONAL =
            new ExceptionToOptionalWrapper(
                    TestData.daoExceptions()
            );
    private static final ExceptionToDefaultValueWrapper<Integer> DAO_EXCEPTION_TO_DEFAULT =
            new ExceptionToDefaultValueWrapper<>(
                    TestData.defaultValueForWrapping(),
                    TestData.daoExceptions()
            );

    public static MappingExceptionWrapper daoToServiceException() {
        return DAO_TO_SERVICE;
    }

    public static ExceptionToOptionalWrapper daoExceptionToOptional() {
        return DAO_EXCEPTION_TO_OPTIONAL;
    }

    public static ExceptionToDefaultValueWrapper<Integer> daoExceptionToDefaultValue() {
        return DAO_EXCEPTION_TO_DEFAULT;
    }
}
