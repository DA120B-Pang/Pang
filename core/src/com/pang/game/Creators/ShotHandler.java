package com.pang.game.Creators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.pang.game.Sprites.Shot;

import java.util.ArrayList;

public class ShotHandler {

    public ArrayList<Shot> shots;
    World world;
    Body dudeBody;

    public ShotHandler(World world, Body dudeBody) {
        this.world = world;
        this.dudeBody = dudeBody;
        shots = new ArrayList<>();
}
    public void addShot(Shot shot){
        shots.add(shot);
    }

    public void update(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            Shot shot = new Shot(world, dudeBody);
        }
    }
}
