package com.alphago365.octopus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends AuthenticationException {
    public RoleNotFoundException(String msg) {
        super(msg);
    }

    public RoleNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}