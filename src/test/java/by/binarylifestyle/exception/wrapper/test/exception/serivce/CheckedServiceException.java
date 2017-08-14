package by.binarylifestyle.exception.wrapper.test.exception.serivce;

import by.binarylifestyle.exception.wrapper.test.exception.common.CheckedException;

public class CheckedServiceException extends CheckedException {
    public CheckedServiceException() {
    }

    public CheckedServiceException(Throwable cause) {
        super(cause);
    }
}
