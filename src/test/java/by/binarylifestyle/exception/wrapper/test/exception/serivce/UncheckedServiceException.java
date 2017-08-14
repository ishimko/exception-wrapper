package by.binarylifestyle.exception.wrapper.test.exception.serivce;

import by.binarylifestyle.exception.wrapper.test.exception.common.UncheckedException;

public class UncheckedServiceException extends UncheckedException {
    public UncheckedServiceException() {
    }

    public UncheckedServiceException(Throwable cause) {
        super(cause);
    }
}
