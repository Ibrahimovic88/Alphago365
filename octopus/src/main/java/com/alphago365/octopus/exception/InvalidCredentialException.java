package com.alphago365.octopus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredentialException extends AuthenticationException {
    public InvalidCredentialException(String msg) {
        super(msg);
    }

    public InvalidCredentialException(String msg, Throwable t) {
        super(msg, t);
    }
}