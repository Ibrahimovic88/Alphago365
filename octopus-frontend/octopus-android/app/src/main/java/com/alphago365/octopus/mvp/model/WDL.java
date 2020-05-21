package com.alphago365.octopus.mvp.model;

public enum WDL {

    UNKNOWN(-1), WIN(3), DRAW(1), LOSE(0);

    private final int score;

    WDL(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
