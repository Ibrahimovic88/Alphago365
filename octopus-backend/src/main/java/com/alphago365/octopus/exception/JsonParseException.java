package com.alphago365.octopus.exception;

public class JsonParseException extends ParseException {

    public JsonParseException(String msg) {
        super(msg);
    }

    public JsonParseException(String msg, Throwable t) {
        super(msg, t);
    }
}