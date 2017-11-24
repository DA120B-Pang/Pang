package com.pang.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.pang.game.Constants.Constants.*;
import static com.pang.game.Sprites.Bubble.BubbleState.*;


public class Bubble extends Sprite {
    private World world;
    private BubbleState size;
    private BubbleColor color;
    private Body bubbleBody;
    private float radius;
    private float bubbleLinearSpd;
    private Vector2 bubbleBounceForce;
    private boolean goingRight;
    private boolean spawnRight;
    private boolean spawnSpdIsSet;
    private boolean stopUpdatingSprite;
    private Animation explode;
    private boolean setToDestroy;
    private boolean destroyed;
    private float explosionTimer;
    private boolean explosionSoundDone;
    private AssetManager assetManager;



    public enum BubbleState {
        XLARGE,LARGE,MEDIUM,SMALL,XSMALL
    }
    public enum BubbleColor {
        BLUE,RED,GREEN
    }
    public Bubble(World world, BubbleState state, BubbleColor color, Vector2 position, AssetManager assetManager,boolean spawnRight){
        this.assetManager = assetManager;
        this.size = state;
        this.color = color;
        this.world = world;
        int colorPosX = 0;
        int colorPosY = 0;
        this.spawnRight = spawnRight;
        bubbleLinearSpd = 0.0f;
        bubbleBounceForce = new Vector2(0,0);
        //Sätter horisontell hastighet
        bubbleLinearSpd = 0.65f;
        //Sätter hopp kraft i x alltid 0
        bubbleBounceForce.x = 0f;
        explosionTimer = 0f;
        stopUpdatingSprite = false;
        destroyed = false;
        setToDestroy = false;
        explosionSoundDone = false;

        switch (state) {
            case XLARGE:

                //Sätter tid på animation i sekunder samt anger en Array av frames
                explode = new Animation(0.04f,getExplosionAnimation(XLARGE,assetManager) );
                //Raderar frame Array
                //Sätter hopp kraft i y uppåt
                bubbleBounceForce.y = 0.000409f;

                switch (color){
                    case RED://Röd positioner på atlas
                        colorPosX = 1;
                        colorPosY = 6;
                        break;
                    case BLUE://Blå positioner på atlas
                        colorPosX = 1;
                        colorPosY= 52;
                        break;
                    case GREEN://Grön positioner på atlas
                        colorPosX = 1;
                        colorPosY = 99;
                        break;
                }
                setBounds(0, 0, 45 / PPM, 45 / PPM);
                radius = 22;
                setRegion(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Balloons"), colorPosX, colorPosY, 45, 45));
                break;
            case LARGE:
                explode = new Animation(0.04f,getExplosionAnimation(LARGE,assetManager) );
                //Sätter hopp kraft i y uppåt
                bubbleBounceForce.y = 0.00023f;
                switch (color){
                    case RED://Röd positioner på atlas
                        colorPosX = 46;
                        colorPosY = 11;
                        break;
                    case BLUE://Blå positioner på atlas
                        colorPosX = 47;
                        colorPosY = 57;
                        break;
                    case GREEN://Grön positioner på atlas
                        colorPosX = 47;
                        colorPosY = 104;
                        break;
                }
                setBounds(0, 0, 35 / PPM, 35 / PPM);
                radius = 17;
                setRegion(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Balloons"), colorPosX, colorPosY, 35, 35));
                break;
            case MEDIUM:
                explode = new Animation(0.04f,getExplosionAnimation(MEDIUM,assetManager) );
                //Sätter hopp kraft i y uppåt
                bubbleBounceForce.y = 0.00009f;
                switch (color){
                    case RED://Röd positioner på atlas
                        colorPosX = 81;
                        colorPosY = 17;
                        break;
                    case BLUE://Blå positioner på atlas
                        colorPosX = 81;
                        colorPosY = 63;
                        break;
                    case GREEN://Grön positioner på atlas
                        colorPosX = 81;
                        colorPosY = 110;
                        break;
                }
                setBounds(0, 0, 23 / PPM, 23 / PPM);
                radius = 11;
                setRegion(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Balloons"), colorPosX, colorPosY, 23, 23));
                break;
            case SMALL:
                explode = new Animation(0.04f,getExplosionAnimation(SMALL,assetManager) );
                //Sätter hopp kraft i y uppåt
                bubbleBounceForce.y = 0.000016f;
                switch (color){
                    case RED://Röd positioner på atlas
                        colorPosX = 104;
                        colorPosY = 22;
                        break;
                    case BLUE://Blå positioner på atlas
                        colorPosX = 104;
                        colorPosY = 69;
                        break;
                    case GREEN://Grön positioner på atlas
                        colorPosX = 104;
                        colorPosY = 115;
                        break;
                }
                setBounds(0, 0, 12 / PPM, 12 / PPM);
                radius = 6;
                setRegion(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Balloons"), colorPosX, colorPosY, 12, 12));
                break;
            default:
                explode = new Animation(0.04f,getExplosionAnimation(XSMALL,assetManager) );
                //Sätter hopp kraft i y uppåt
                bubbleBounceForce.y = 0.000004f;
                switch (color){
                    case RED://Röd positioner på atlas
                        colorPosX = 116;
                        colorPosY = 26;
                        break;
                    case BLUE://Blå positioner på atlas
                        colorPosX = 116;
                        colorPosY = 72;
                        break;
                    case GREEN://Grön positioner på atlas
                        colorPosX = 116;
                        colorPosY = 118;
                        break;
                }
                setBounds(0, 0, 6 / PPM, 6 / PPM);
                radius = 3f;
                setRegion(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Balloons"), colorPosX, colorPosY, 6, 6));
                break;
        }
        this.world = world;
        //skapa kropp
        BodyDef bubbleDef= new BodyDef();
        //Start position
        bubbleDef.position.set(position.x/PPM,position.y/PPM);
        //Typ av kropp
        bubbleDef.type = BodyDef.BodyType.DynamicBody;
        //Skapa kropp i box2d värld
        bubbleBody = this.world.createBody(bubbleDef);
        //Skapa form
        CircleShape bubbleShape = new CircleShape();
        //Storlek på kropp
        bubbleShape.setRadius(radius/PPM);

        FixtureDef bubbleFixDef  = new FixtureDef();
        //Tilldela form till Fixture Def
        bubbleFixDef.shape = bubbleShape;

        bubbleFixDef.filter.categoryBits = BUBBLE;
        //Dude ska kollidera med boll och
        bubbleFixDef.filter.maskBits = DUDE | FLOOR_WALL_ROOF;//

        //Fäster en form till kroppen
        bubbleBody.createFixture(bubbleFixDef);
        bubbleBody.getFixtureList().get(0).setDensity(0.001f);
        bubbleBody.resetMassData();
        //Så att vi kan använda metoder via contactlistenern
        bubbleBody.setUserData(this);
        //Kasta form
        bubbleShape.dispose();
        //Fixturen ska aldrig vridas
        bubbleBody.setFixedRotation(true);
        //Position och storlek för (super)sprite när den ska ritas
    }
    public final void bumpLeftWall(){
        bubbleBody.setLinearVelocity(bubbleLinearSpd, bubbleBody.getLinearVelocity().y);
        goingRight = true;
    }
    public final void bumpRightWall(){
        bubbleBody.setLinearVelocity(-bubbleLinearSpd, bubbleBody.getLinearVelocity().y);
        goingRight = false;
    }
    public final void bumpFloor() {
        bubbleBody.setLinearVelocity(0f, 0f);
        bubbleBody.applyLinearImpulse(bubbleBounceForce, bubbleBody.getWorldCenter(), true);
        if(goingRight){
            bumpLeftWall();
        }
        else{
            bumpRightWall();
        }
    }

    public void update(float dt) {

            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1 )&& size==XSMALL
                    || Gdx.input.isKeyJustPressed(Input.Keys.NUM_2 )&& size==SMALL
                    || Gdx.input.isKeyJustPressed(Input.Keys.NUM_3 )&& size==MEDIUM
                    || Gdx.input.isKeyJustPressed(Input.Keys.NUM_4 )&& size==LARGE
                    || Gdx.input.isKeyJustPressed(Input.Keys.NUM_5 )&& size==XLARGE){
                //setToSleep();
                setToDestroy();
            }

        float offset = 2/PPM; //Offset för att korrigera position av sprite vid gång
        if(!stopUpdatingSprite && !setToDestroy) {//Slutar updatera position när spelare dör
            //Sätter Texture region till sprite
            setPosition(bubbleBody.getPosition().x - getWidth() / 2, bubbleBody.getPosition().y - getHeight() / 2);
        }
        else if(setToDestroy){
            if(!explosionSoundDone) {
                setExplodeSound(size, assetManager);
                explosionSoundDone = true;
            }
            if(animateExplosion(dt)){
                destroyed = true;
            }
        }

        if (!spawnSpdIsSet){//Sätt hastighet när bubble bildats
            if(spawnRight){
                bumpLeftWall();
            }
            else{
                bumpRightWall();
            }
            spawnSpdIsSet = true;
        }

    }
    public void dispose(){
        getTexture().dispose();
    }
    public void draw(SpriteBatch batch) {
        if(!destroyed) {
            super.draw(batch);
        }
    }

    private boolean animateExplosion(float dt){
        explosionTimer += dt;
        setRegion((TextureRegion)explode.getKeyFrame(explosionTimer, false));
        return explode.isAnimationFinished(explosionTimer)? true:false;
    }

    public void setToSleep(){
        bubbleBody.setActive(false);
    }

    public void destroy(){
        world.destroyBody(bubbleBody);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setToDestroy(){
        bubbleBody.setActive(false);
        Filter spareDude = new Filter();
        spareDude.maskBits = FLOOR_WALL_ROOF;
        bubbleBody.getFixtureList().get(0).setFilterData(spareDude);
        setToDestroy = true;
    }

    private Array<TextureRegion> getExplosionAnimation(BubbleState size, AssetManager assetManager){
        //Skapa explotions animering
        //Array för animationer
        String fileName;
        Array<TextureRegion> frames = new Array<>();
        int sizeImage, nbr2, nbr3, nbr4, nbr5, nbr6;

        switch(size){
            case XLARGE:
                fileName = "explosionXL";
                sizeImage = 45;
                nbr2 = 45;
                nbr3 = 90;
                nbr4 = 135;
                nbr5 = 180;
                nbr6 = 225;
                break;
            case LARGE:
                fileName = "explosionL";
                sizeImage = 35;
                nbr2 = 35;
                nbr3 = 70;
                nbr4 = 105;
                nbr5 = 140;
                nbr6 = 175;
                break;
            case MEDIUM:
                fileName = "explosionM";
                sizeImage = 23;
                nbr2 = 23;
                nbr3 = 46;
                nbr4 = 69;
                nbr5 = 92;
                nbr6 = 115;
                break;
            case SMALL:
                fileName = "explosionS";
                sizeImage = 12;
                nbr2 = 12;
                nbr3 = 24;
                nbr4 = 36;
                nbr5 = 48;
                nbr6 = 60;
                break;
            default:
                fileName = "explosionXS";
                sizeImage = 6;
                nbr2 = 6;
                nbr3 = 12;
                nbr4 = 18;
                nbr5 = 24;
                nbr6 = 30;
        }


                //Explodera
                //Väljer bild Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), 0, 0, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr2, 0, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr3, 0, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr4, 0, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr5, 0, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr6, 0, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), 0, nbr2, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr2, nbr2, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr3, nbr2, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr4, nbr2, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr5, nbr2, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr6, nbr2, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), 0, nbr3, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr2, nbr3, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr3, nbr3, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr4, nbr3, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr5, nbr3, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr6, nbr3, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), 0, nbr4, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr2, nbr4, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr3, nbr4, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr4, nbr4, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr5, nbr4, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr6, nbr4, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), 0, nbr5, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr2, nbr5, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr3, nbr5, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr4, nbr5, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr5, nbr5, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr6, nbr5, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), 0, nbr6, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr2, nbr6, sizeImage, sizeImage));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion(fileName), nbr3, nbr6, sizeImage, sizeImage));


        return frames;
    }
    private void setExplodeSound(BubbleState size, AssetManager assetManager){
        switch(size){
            case XLARGE:
                assetManager.get("audio/sound/boomXlarge.wav", Sound.class).setVolume(assetManager.get("audio/sound/boomXlarge.wav", Sound.class).play(), 1.2f);
                break;
            case LARGE:
                assetManager.get("audio/sound/boomLarge.wav", Sound.class).setVolume(assetManager.get("audio/sound/boomLarge.wav", Sound.class).play(), 1.0f);
                break;
            case MEDIUM:
                assetManager.get("audio/sound/boomMedium.wav", Sound.class).setVolume(assetManager.get("audio/sound/boomMedium.wav", Sound.class).play(), 0.9f);
                break;
            case SMALL:
                assetManager.get("audio/sound/boomSmall.wav", Sound.class).setVolume(assetManager.get("audio/sound/boomSmall.wav", Sound.class).play(), 0.8f);
                break;
            default:
                assetManager.get("audio/sound/boomSmall.wav", Sound.class).setVolume(assetManager.get("audio/sound/boomSmall.wav", Sound.class).play(), 0.7f);

        }
    }

}
