package by.binarylifestyle.exception.wrapper.test.support;

import by.binarylifestyle.exception.wrapper.api.common.UncheckedExceptionWrapper;
import by.binarylifestyle.exception.wrapper.impl.common.MappingExceptionWrapper;
import by.binarylifestyle.exception.wrapper.impl.support.WrappingConfiguration;
import by.binarylifestyle.exception.wrapper.test.exception.dao.UncheckedDaoException;
import by.binarylifestyle.exception.wrapper.test.exception.serivce.UncheckedServiceException;

public final class Wrappers {
    public static <T> UncheckedExceptionWrapper<T, T> daoToServiceExceptionWrapper() {
        return new MappingExceptionWrapper<>(
                new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new)
        );
    }
}
