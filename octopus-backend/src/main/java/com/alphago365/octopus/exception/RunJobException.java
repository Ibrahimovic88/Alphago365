package com.alphago365.octopus.exception;

public class RunJobException extends Exception {

    public RunJobException(String msg) {
        super(msg);
    }

    public RunJobException(String msg, Throwable t) {
        super(msg, t);
    }
}