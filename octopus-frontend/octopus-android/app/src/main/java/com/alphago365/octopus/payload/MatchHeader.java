package com.alphago365.octopus.payload;

import com.alphago365.octopus.mvp.model.ListItem;

import java.time.Instant;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class MatchHeader implements ListItem {

    private Instant date;

    private String description;

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

    @Override
    public boolean areItemsTheSame(ListItem target) {
        if(! (target instanceof MatchHeader)) {
            return false;
        }
        return this.date.equals(((MatchHeader) target).date);
    }

    @Override
    public boolean areContentsTheSame(ListItem target) {
        return this.equals(target);
    }

}
