package com.alphago365.octopus.exception;

public class JobException extends Exception {

    public JobException(String msg) {
        super(msg);
    }

    public JobException(String msg, Throwable t) {
        super(msg, t);
    }
}