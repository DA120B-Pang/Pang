package com.pang.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.pang.game.Constants.Constants;
import com.pang.game.Pang;

import static com.pang.game.Constants.Constants.*;
import static com.pang.game.Constants.Constants.PowerUp.*;

public class PowerUpUnit extends Sprite {
    private Constants.PowerUp powerUp;
    private World world;
    private Body body;
    private PolygonShape polygonshape;
    private FixtureDef fixtureDef;
    private boolean destroyNextUpdate;
    private boolean isDestroyed;
    private Pang game;
    private float terminateTimer;
    private float alphaTimer;
    private boolean alphaUp;
    private float redAlpha;

    public PowerUpUnit(World world, Pang game, Constants.PowerUp powerUp, Vector2 position){
        this.game = game;
        destroyNextUpdate = false;
        isDestroyed = false;
        terminateTimer = 0;
        alphaTimer = 1;
        alphaUp = false;
        redAlpha = 0;
        this.powerUp = powerUp;
        this.world = world;
        BodyDef bodydef = new BodyDef();
        bodydef.type = BodyDef.BodyType.DynamicBody;
        bodydef.position.set(position.x, position.y);
        body = this.world.createBody(bodydef);
        polygonshape = new PolygonShape();
        polygonshape.setAsBox(8/PPM, 8/PPM);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonshape;
        fixtureDef.filter.categoryBits = POWER_UP;
        fixtureDef.filter.maskBits = DUDE | FLOOR_WALL;

        body.createFixture(fixtureDef);
        body.setUserData(this);


        setBounds(0,0,16/PPM,16/PPM);
        int x = 0;
        int y = 0;
        if(powerUp == LIFE){
            setRegion(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 5, 43, 16, 16));
        }
        else {
            switch (powerUp) {
                case DOUBLESHOT:
                    x = 208;
                    y = 39;
                    break;
                case BARBSHOT:
                    x = 191;
                    y = 39;
                    break;
                case SHEILD:
                    x = 280;
                    y = 59;
                    break;
                case STOPTIME:
                    x = 262;
                    y = 59;
                    break;
            }
            setRegion(new TextureRegion(game.assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion("pangStuff"), x, y, 16, 16));
        }

        setPosition(position.x, position.y);
    }

    public void update(float dt){
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        terminateTimer += dt;

        if(terminateTimer>7f && terminateTimer<10f){
            float alpha = 0;
            if(redAlpha<0.2) {
                redAlpha += dt*0.2f;
            }
            setColor(1,0,0,redAlpha);
            alphaTimer += dt;
            if (alphaTimer >= 0.5f) {
                alphaUp = !alphaUp;
                alphaTimer = 0;
            }
            if (alphaUp){
                alpha = Math.min(1f, 0.5f+alphaTimer);
            }
            else{
                alpha = Math.max(0.5f, 1f-alphaTimer);
            }
            System.out.println(alpha);
            setAlpha(alpha);
        }
        if (destroyNextUpdate){
            destroy();
        }
        else if(terminateTimer>10){
            alphaDown(dt);
        }
    }

    public void draw(Batch batch){
        super.draw(batch);
    }

    public PowerUp getPower(){
        return powerUp;
    }
    public void destroyNextUpdate(){
        playPickSound();
        destroyNextUpdate = true;
        Filter filter = body.getFixtureList().get(0).getFilterData();
        filter.maskBits = FLOOR_WALL;
        body.getFixtureList().get(0).setFilterData(filter);
    }
    public boolean isDestroyed(){
        return isDestroyed;
    }
    public void destroy(){//Raderar kropp från värld
        isDestroyed = true;
        world.destroyBody(body);
    }

    private void alphaDown(float dt){
        body.setActive(false);
        body.setUserData(null);
        alphaTimer -= dt;
        if (alphaTimer<0){
            alphaTimer = 0;
            destroy();
        }
        setAlpha(alphaTimer);

    }

    private void playPickSound(){
        switch (powerUp) {
            case BARBSHOT:
                game.assetManager.get("audio/sound/powerUpBarb.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpBarb.wav", Sound.class).play(), 0.6f);
                break;
            case SHEILD:
                game.assetManager.get("audio/sound/powerUpSheild.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpSheild.wav", Sound.class).play(), 1.0f);
                break;
            case STOPTIME:
                game.assetManager.get("audio/sound/powerUpStopTime.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpStopTime.wav", Sound.class).play(), 0.6f);
                break;
            case DOUBLESHOT:
                game.assetManager.get("audio/sound/powerUpDouble.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpDouble.wav", Sound.class).play(), 0.6f);
                break;
                //LIFE görs i hud
        }
    }

}
