package com.pang.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.pang.game.Pang;
import com.badlogic.gdx.math.Rectangle;


import static com.pang.game.Constants.Constants.*;

public class Obstacle extends Sprite{
    private BodyDef bodydef;
    private Body body;
    private PolygonShape polygonshape;
    private FixtureDef fixtureDef;
    private boolean colorYellow;
    private boolean isDestroyed;

    public Obstacle( Pang game, Rectangle rectangle, World world, Boolean colorYellow){
        this.colorYellow = colorYellow;
        isDestroyed = false;
        bodydef = new BodyDef();
        bodydef.type = BodyDef.BodyType.StaticBody;
        bodydef.position.set((rectangle.getX() + rectangle.getWidth()/2)/PPM, (rectangle.getY() + rectangle.getHeight()/2)/PPM);
        body = world.createBody(bodydef);
        polygonshape = new PolygonShape();
        polygonshape.setAsBox((rectangle.getWidth()/2)/PPM, (rectangle.getHeight()/2)/PPM);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonshape;
        fixtureDef.filter.categoryBits = OBSTACLE;
        fixtureDef.filter.maskBits = -1;

        body.createFixture(fixtureDef);

        EdgeShape top = new EdgeShape();
        top.set(new Vector2(-((rectangle.width-0.5f)/2) / PPM, 3.6f / PPM), new Vector2(((rectangle.width-0.5f)/2) / PPM, 3.6f / PPM));
        fixtureDef.filter.categoryBits = OBSTACLE_TOP;
        fixtureDef.shape = top;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);

        PolygonShape side = new PolygonShape();
        //side.set(new Vector2(-((rectangle.width+1f)/2) / PPM, 0f / PPM), new Vector2(((rectangle.width+1f)/2) / PPM, 0f / PPM));
        side.setAsBox(((rectangle.getWidth()+1.0f)/2)/PPM, ((rectangle.getHeight()-5)/2)/PPM);
        fixtureDef.filter.categoryBits = OBSTACLE_SIDE;
        fixtureDef.shape = side;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);
        body.setUserData(this);

        setBounds(0,0, rectangle.width/PPM, rectangle.height/PPM);
        setRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), 0, 31, 32, 7);
    }
    public void draw(Batch batch){
        super.draw(batch);
    }

    public void update(float dt){
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    public boolean isDestroyed(){
        return isDestroyed;
    }
}
