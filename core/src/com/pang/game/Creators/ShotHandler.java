package com.pang.game.Creators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.pang.game.Sprites.Shot;

import java.util.ArrayList;

public class ShotHandler {

    private ArrayList<Shot> myShots;
    private ArrayList<Shot> myDestroyedShots;
    private ShotTypeHandler type;

    public enum ShotTypeHandler{
        SINGLE,DOUBLE, BARB;
    }

    public ShotHandler() {
        type = ShotTypeHandler.SINGLE;
        myShots = new ArrayList<>();
        myDestroyedShots = new ArrayList<>();
}
    public void addShot(World world, Vector2 position, AssetManager assetManager){
        myShots.add(new Shot(world, position, assetManager, Shot.ShotType.STANDARD));
    }

    public int getShotsFired(){
        return myShots.size();
    }
    public boolean isReadyForShot(){
        boolean ready = false;
        switch (type){
            case SINGLE:
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
}
