package by.binarylifestyle.exception.wrapper.test.exception.dao;

import by.binarylifestyle.exception.wrapper.test.exception.common.CheckedException;

public class CheckedDaoException extends CheckedException {
    public CheckedDaoException() {
    }

    public CheckedDaoException(Throwable cause) {
        super(cause);
    }
}
