package by.binarylifestyle.exception.wrapper.exception.dao;

import by.binarylifestyle.exception.wrapper.exception.common.CheckedException;

public class CheckedDaoException extends CheckedException {
    public CheckedDaoException() {
    }

    public CheckedDaoException(Throwable cause) {
        super(cause);
    }
}
