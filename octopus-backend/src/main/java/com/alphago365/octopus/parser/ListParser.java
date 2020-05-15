package com.alphago365.octopus.parser;

import com.alphago365.octopus.exception.ParseException;

import javax.validation.constraints.NotNull;
import java.util.List;

public abstract class ListParser<T> implements ParseInterface<List<T>, String> {

    @Override
    public List<T> parseResponse(@NotNull String str) throws ParseException {
        return parseList(str);
    }

    protected abstract List<T> parseList(@NotNull String str) throws ParseException;
}
