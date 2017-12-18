package com.pang.game.HUD;

import java.io.Serializable;
import java.time.LocalDate;

public class HighScoreData implements Serializable {
    int score;
    String name;
    LocalDate date;

    public HighScoreData(String name, int score){
        date = LocalDate.now();
        this.name = name;
        this.score = score;

    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }
}
