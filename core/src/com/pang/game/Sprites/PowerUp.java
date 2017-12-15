package com.pang.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.pang.game.Constants.Constants;
import static com.pang.game.Constants.Constants.*;

public class PowerUp extends Sprite {
    private Constants.PowerUp powerUp;
    private World world;
    BodyDef bodydef;
    Body body;
    PolygonShape polygonshape;

    public PowerUp(World world, Constants.PowerUp powerUp, Vector2 position){
        this.powerUp = powerUp;
        this.world = world;
        bodydef = new BodyDef();
        bodydef.type = BodyDef.BodyType.StaticBody;
        bodydef.position.set(position.x, position.y);
        body = world.createBody(bodydef);
        polygonshape = new PolygonShape();
        /*polygonshape.setAsBox(/PPM, (rectangle.getHeight()/2)/PPM);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonshape;
        fixtureDef.filter.categoryBits = OBSTACLE;
        fixtureDef.filter.maskBits = -1;

        body.createFixture(fixtureDef);*/
    }
}
