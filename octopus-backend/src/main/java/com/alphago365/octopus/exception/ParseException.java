package com.alphago365.octopus.exception;

public class ParseException extends Exception {

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(String msg, Throwable t) {
        super(msg, t);
    }
}