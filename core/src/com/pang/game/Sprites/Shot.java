package com.pang.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;


import static com.pang.game.Constants.Constants.PPM;

public class Shot extends Sprite {

    private Body shot;
    private boolean setToDestroyShot;


    public static void shot(World world, Body dudeBody) {
        Texture shot1;
        BodyDef shotdef = new BodyDef();
        PolygonShape polygonshape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body shot;


        shotdef.type = BodyDef.BodyType.DynamicBody;
        shotdef.position.set((dudeBody.getPosition().x), dudeBody.getPosition().y);


        shot = world.createBody(shotdef);

        polygonshape.setAsBox(4 / PPM, 70 / PPM);
        fixtureDef.shape = polygonshape;
        // fixtureDef.filter.categoryBits = BUBBLE;

        shot.createFixture(fixtureDef);
        shot.setUserData("shot");
        shot.setLinearVelocity(0, 3);
        shot1 = new Texture("sprites/shot.png");


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

}
