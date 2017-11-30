package com.pang.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.pang.game.Pang;


import java.util.ArrayList;

import static com.pang.game.Constants.Constants.*;

public class Shot extends Sprite {

    private Body shot;
    private Texture shot1;
    private World world;
    private boolean setToDestroyShot;



    public Shot(World world, Body dudeBody) {
        this.world = world;

        BodyDef shotdef = new BodyDef();
        PolygonShape polygonshape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();


        shotdef.type = BodyDef.BodyType.KinematicBody;
        shotdef.position.set((dudeBody.getPosition().x), dudeBody.getPosition().y - 70/PPM);

        shot = world.createBody(shotdef);

        polygonshape.setAsBox(4 / PPM, 70 / PPM);
        fixtureDef.shape = polygonshape;
        fixtureDef.filter.categoryBits = SHOT;
        fixtureDef.filter.maskBits = BUBBLE | ROOF;

        shot.createFixture(fixtureDef);
        shot.setUserData(this);
        shot.setLinearVelocity(0, 2);
        shot1 = new Texture("sprites/shot.png");


    }
    public void update(float dt){

    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);

    }

    public void setToDestroyShot() {//Sätter att bubblan ska förstöras.. denna ska anropas i contact handlern. När bubblan blir skjuten
        shot.setActive(false);
        shot.setUserData(null);
        //Filter spareDude = new Filter();// Bubblan ska inte kunna skada dude mer
        //spareDude.maskBits = FLOOR_WALL_ROOF;// Bubblan ska inte kunna skada dude mer
        // body.getFixtureList().get(0).setFilterData(spareDude);// Bubblan ska inte kunna skada dude mer
        setToDestroyShot = true;

    }

    public void shotRemove(){
        Filter filter = new Filter();
        filter = shot.getFixtureList().get(0).getFilterData();
        filter.maskBits = FLOOR_WALL_ROOF;
        shot.getFixtureList().get(0).setFilterData(filter);
    }
}
