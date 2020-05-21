package com.alphago365.octopus.mvp.model;

public interface ListItem {

    int TYPE_HEADER = 0;

    int TYPE_ITEM = 1;

    int getType();

    boolean areItemsTheSame(ListItem target);

    boolean areContentsTheSame(ListItem target);

}
