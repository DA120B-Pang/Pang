package com.pang.game.HUD;

import java.util.Comparator;

public class HighScoreDataSort implements Comparator<HighScoreData> {

    @Override
    public int compare(HighScoreData o1, HighScoreData o2) {
        if(o1.score < o2.score){
            return 1;
        }
        else{
            return -1;
        }
    }
}
