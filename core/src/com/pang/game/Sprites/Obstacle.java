package com.pang.game.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.pang.game.Pang;
import com.badlogic.gdx.math.Rectangle;


import static com.pang.game.Constants.Constants.*;

public class Obstacle extends Sprite{
    private BodyDef bodydef;
    private Body body;
    private PolygonShape polygonshape;
    private FixtureDef fixtureDef;
    private boolean colorYellow;
    private Animation animation;
    private AssetManager assetManager;
    private boolean destroyNextUpdate;
    private boolean isDestroyed;
    private boolean destroySoundStarted;
    private boolean filterDataSet;
    private boolean isBreakable;
    private World world;
    private float timerAnimation;
    private Pang game;

    public Obstacle( Pang game, Rectangle rectangle, World world, boolean colorYellow, boolean isBreakable){
        this.game = game;
        assetManager = game.assetManager;
        this.world = world;
        this.colorYellow = colorYellow;
        this.isBreakable = isBreakable;
        isDestroyed = false;
        destroyNextUpdate = false;
        destroySoundStarted = false;
        filterDataSet = false;
        timerAnimation = 0;
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

        int[] x = new int[]{0,34,68,102,136};//fem olika bilder i x ledx[0] = 0;
        int y = colorYellow? 31:63; // två olika höjder
        int width = 32;
        int height = 7;

        if(!isBreakable){
            x[0] = colorYellow? 0:34;
            y = 52;
        }

        setRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), x[0], y, width, height);

        Array<TextureRegion> frames = new Array<>();
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), x[1], y, width, height));
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), x[2], y, width, height));
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), x[3], y, width, height));
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), x[4], y, width, height));

        animation = new Animation(0.1f, frames);
    }
    public void draw(Batch batch){
        super.draw(batch);
    }

    public void destroyNextUpdate(){
        if(isBreakable) {
            destroyNextUpdate = true;
        }
        else {
            assetManager.get("audio/sound/boomLarge.wav", Sound.class).setVolume(assetManager.get("audio/sound/tileNonBreak.wav", Sound.class).play(), 1.0f*game.soundVolume);
        }
    }

    public boolean isBreakable(){
        return isBreakable;
    }

    public void update(float dt){
        if(!isDestroyed) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            if (destroyNextUpdate && isBreakable) {
                if (!filterDataSet) {
                    Filter[] filter = new Filter[3];
                    for (int i = 0; i <3 ; i++) {
                        filter[i] = body.getFixtureList().get(i).getFilterData();
                        filter[i].maskBits = FREEFALL;
                        body.getFixtureList().get(i).setFilterData(filter[i]);
                    }
                    filterDataSet = true;
                    body.setUserData(null);
                }
                timerAnimation += dt;
                setRegion((TextureRegion) animation.getKeyFrame(timerAnimation, false));
                if(!destroySoundStarted) {
                    assetManager.get("audio/sound/boomLarge.wav", Sound.class).setVolume(assetManager.get("audio/sound/tileBreak.wav", Sound.class).play(), 1.0f*game.soundVolume);
                    destroySoundStarted = true;
                }
                if (animation.isAnimationFinished(timerAnimation)) {
                    world.destroyBody(body);
                    isDestroyed = true;
                }
            }
        }
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public boolean isDestroyed(){
        return isDestroyed;
    }

    public int getDestroyables() {
        if(isBreakable){
            return 1;
        }
        else{
            return 0;
        }
    }
}
