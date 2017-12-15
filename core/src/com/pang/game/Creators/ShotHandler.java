package com.pang.game.Creators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pang.game.Constants.Constants.*;
import com.pang.game.Sprites.Shot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ShotHandler {

    private ArrayList<Shot> myShots;
    private ArrayList<Shot> myDestroyedShots;
    private ShotTypeHandler type;
    private ArrayList<PowerUp> myPowerUps;

    public enum ShotTypeHandler{
        SINGLE,DOUBLE, BARB;
    }

    public ShotHandler() {
        type = ShotTypeHandler.SINGLE;
        myShots = new ArrayList<>();
        myDestroyedShots = new ArrayList<>();
        myPowerUps = new ArrayList<>();
}
    public void addShot(World world, Vector2 position, AssetManager assetManager){
        myShots.add(new Shot(world, position, assetManager, Shot.ShotType.BARB,this));
    }
    public void powerUp(PowerUp powerUp){
        switch (powerUp){
            case DOUBLESHOT:

        }
    }
    public boolean isReadyForShot(){
        boolean ready = false;
        switch (type){
            case SINGLE: case BARB:
                if(myShots.size()<1){
                    ready = true;
                }
                break;
            case DOUBLE:
                if(myShots.size()<2){
                    ready = true;
                }
                break;
        }
        return ready;
    }
    public void update(float dt){

        for(Shot s: myShots){
            s.update(dt);
            if(s.isDestroyed()){
                myDestroyedShots.add(s);
            }
        }
        for(Shot s: myDestroyedShots){
            myShots.remove(s);
        }
        myDestroyedShots.clear();
    }
    public void renderer(Batch batch){//För att rita alla skott
        for (Shot s: myShots){
            s.draws(batch);
        }
    }
    public void loadPowerUps(PowerUp[] powerUps, int destroyables){//denna metoden portionerar ut power ups när object skjuts isönder kollas index 0 och tas sedan bort.
        Random myRandom = new Random();
        int myRandomNbr = 0;
        ArrayList<PowerUp> tmpList = new ArrayList<>(Arrays.asList(new PowerUp[destroyables]));

        for (int i = 0; i <powerUps.length ; i++) {
            if(tmpList.size()<=i){//Fler powerups än breakables
                break;
            }
            tmpList.set(i,powerUps[i]);
        }
        int random = 0;
        do{
            myRandomNbr = myRandom.nextInt(tmpList.size());
            myPowerUps.add(tmpList.get(myRandomNbr));
            tmpList.remove(myRandomNbr);

        }while (tmpList.size()>0);
        myPowerUps.size();

    }
}
