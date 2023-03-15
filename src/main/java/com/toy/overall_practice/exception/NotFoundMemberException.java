package com.toy.overall_practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotFoundMemberException extends RuntimeException {

    public NotFoundMemberException(String message) {
        super(message);
    }

}
