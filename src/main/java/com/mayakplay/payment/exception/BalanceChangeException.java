package com.mayakplay.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BalanceChangeException extends RuntimeException {

    public BalanceChangeException() {
        super();
    }

    public BalanceChangeException(String message) {
        super(message);
    }

    public BalanceChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BalanceChangeException(Throwable cause) {
        super(cause);
    }

    protected BalanceChangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
