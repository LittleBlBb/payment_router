package com.kertis.payrouter.exception;

import org.springframework.http.HttpStatus;

public class AlreadyInProcessingOrCompletedException extends BusinessException {
    public AlreadyInProcessingOrCompletedException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }
}
