package com.pang.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.pang.game.Constants.Constants.*;


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



    public enum BubbleState {
        XLARGE,LARGE,MEDIUM,SMALL,XSMALL
    }
    public enum BubbleColor {
        BLUE,RED,GREEN
    }
    public Bubble(World world, BubbleState state, BubbleColor color, Vector2 position, AssetManager assetManager,boolean spawnRight){
        this.size = state;
        this.color = color;
        this.world = world;
        int colorPosX = 0;
        int colorPosY = 0;
        this.spawnRight = spawnRight;
        this.bubbleLinearSpd = 0.0f;
        this.bubbleBounceForce = new Vector2(0,0);
        //Sätter horisontell hastighet
        bubbleLinearSpd = 0.65f;
        //Sätter hopp kraft i x alltid 0
        bubbleBounceForce.x = 0f;
        stopUpdatingSprite = false;
        switch (state) {
            case XLARGE:

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
                setBounds(0, 0, 22 / PPM, 22 / PPM);
                radius = 11;
                setRegion(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Balloons"), colorPosX, colorPosY, 22, 22));
                break;
            case SMALL:
                //Sätter hopp kraft i y uppåt
                bubbleBounceForce.y = 0.000015f;
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
                setBounds(0, 0, 11 / PPM, 11 / PPM);
                radius = 5;
                setRegion(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Balloons"), colorPosX, colorPosY, 11, 11));
                break;
            case XSMALL:
                //Sätter hopp kraft i y uppåt
                bubbleBounceForce.y = 0.000003f;
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
                radius = 2.5f;
                setRegion(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Balloons"), colorPosX, colorPosY, 5, 5));
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
    public void stopUpdate(){
        stopUpdatingSprite = true;
    }


    public void update(float dt) {
        float offset = 2/PPM; //Offset för att korrigera position av sprite vid gång
        if(!stopUpdatingSprite) {//Slutar updatera position när spelare dör
            //Sätter Texture region till sprite
            setPosition(bubbleBody.getPosition().x - getWidth() / 2, bubbleBody.getPosition().y - getHeight() / 2);
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
        super.draw(batch);
    }

}
