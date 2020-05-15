package com.alphago365.octopus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserForbiddenException extends AuthenticationException {

    public UserForbiddenException(String msg) {
        super(msg);
    }

    public UserForbiddenException(String msg, Throwable t) {
        super(msg, t);
    }
}