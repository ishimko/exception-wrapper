package by.binarylifestyle.exception.wrapper.exception.serivce;

import by.binarylifestyle.exception.wrapper.exception.common.CheckedException;

public class CheckedServiceException extends CheckedException {
    public CheckedServiceException() {
    }

    public CheckedServiceException(Throwable cause) {
        super(cause);
    }
}
