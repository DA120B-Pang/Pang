package com.pang.game.HUD;

import java.util.Comparator;

/**
 * Klass för att sortera highscorelista
 */
public class HighScoreDataSort implements Comparator<HighScoreData> {

    @Override
    public int compare(HighScoreData o1, HighScoreData o2) {
        if(o1.getScore() < o2.getScore()){
            return 1;
        }
        else{
            return -1;
        }
    }
}
