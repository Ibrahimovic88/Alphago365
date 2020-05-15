package com.alphago365.octopus.parser;

import com.alphago365.octopus.exception.ParseException;

public interface ParseInterface<POJO, Response> {
    POJO parseResponse(Response response) throws ParseException;
}
