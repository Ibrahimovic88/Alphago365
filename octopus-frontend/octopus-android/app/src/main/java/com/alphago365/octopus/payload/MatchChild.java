package com.alphago365.octopus.payload;

import androidx.annotation.NonNull;

import com.alphago365.octopus.mvp.model.ListItem;
import com.alphago365.octopus.mvp.model.Match;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class MatchChild implements ListItem {

    @NonNull
    private final Match match;

    public MatchChild(Match match) {
        this.match = match;
    }

    @Override
    public int getType() {
        return TYPE_ITEM;
    }

    @Override
    public boolean areItemsTheSame(ListItem target) {
        if(!(target instanceof MatchChild)) {
            return false;
        }
        return match.getId().equals((((MatchChild) target).match).getId());
    }

    @Override
    public boolean areContentsTheSame(ListItem target) {
        return this.equals(target);
    }
}
