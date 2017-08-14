package by.binarylifestyle.exception.wrapper.test.exception.dao;

import by.binarylifestyle.exception.wrapper.test.exception.common.UncheckedException;

public class UncheckedDaoException extends UncheckedException {
    public UncheckedDaoException() {
    }

    public UncheckedDaoException(Throwable cause) {
        super(cause);
    }
}
