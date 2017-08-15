package by.binarylifestyle.exception.wrapper.exception.dao;

import by.binarylifestyle.exception.wrapper.exception.common.UncheckedException;

public class UncheckedDaoException extends UncheckedException {
    public UncheckedDaoException() {
    }

    public UncheckedDaoException(Throwable cause) {
        super(cause);
    }
}
