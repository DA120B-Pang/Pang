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

    public ArrayList<Shot> myShots;
    public ArrayList<Shot> myDestroyedShots;

    public ShotHandler() {
        myShots = new ArrayList<>();
        myDestroyedShots = new ArrayList<>();
}
    public void addShot(World world, Vector2 position, AssetManager assetManager){
        myShots.add(new Shot(world, position, assetManager, Shot.ShotType.STANDARD));
    }

    public int getShotsFired(){
        return myShots.size();
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
    public void renderer(Batch batch){//För att rita alla bubblor
        for (Shot s: myShots){
            s.draws(batch);
        }
    }
}
