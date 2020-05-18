package com.alphago365.octopus.converter;

import com.alphago365.octopus.model.WDL;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
@Component
public class WdlConverter implements AttributeConverter<WDL, Integer> {
    @Override
    public Integer convertToDatabaseColumn(WDL wdl) {
        return wdl.getScore();
    }

    @Override
    public WDL convertToEntityAttribute(Integer score) {
        switch (score) {
            case 3:
                return WDL.WIN;
            case 1:
                return WDL.DRAW;
            case 0:
                return WDL.LOSE;
            default:
                return WDL.UNKNOWN;
        }
    }
}
