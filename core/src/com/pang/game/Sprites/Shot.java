package com.pang.game.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.pang.game.Creators.ShotHandler;


import static com.pang.game.Constants.Constants.*;
import static com.pang.game.Sprites.Shot.ShotType.BARB;
import static com.pang.game.Sprites.Shot.ShotType.STANDARD;

public class Shot extends Sprite {

    private Body shot;
    private Texture shot1;
    private World world;
    private boolean setToDestroyShot;
    private boolean destroyShotNextUpdate;
    private boolean shotDestroyed;
    private AssetManager assetManager;
    private Animation shotUp;
    private ShotType shotType;
    private float stateTimer;
    private float barbTimer;
    private boolean barbStop;
    private TextureRegion barbStraight;
    private ShotHandler shotHandler;


    public enum ShotType{
        STANDARD, BARB
    }





    public Shot(World world, Vector2 position, AssetManager assetManager, ShotType shotType, ShotHandler shotHandler){
        this.shotHandler = shotHandler;
        this.world = world;
        this.assetManager = assetManager;
        destroyShotNextUpdate = false;
        this.shotType = shotType;
        stateTimer = 0;
        shotDestroyed = false;
        barbStop = false;
        barbTimer = 0;

        BodyDef shotdef = new BodyDef();
        PolygonShape polygonshape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();


        shotdef.type = BodyDef.BodyType.DynamicBody;
        shotdef.position.set((position.x), position.y - 95/PPM);

        shot = world.createBody(shotdef);
        shot.setGravityScale(0f);// sätter att skottet inte ska ha någon gravitation.


        polygonshape.setAsBox(2 / PPM, 110 / PPM);

        fixtureDef.shape = polygonshape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = SHOT;
        fixtureDef.filter.maskBits = BUBBLE | ROOF | OBSTACLE;


        shot.createFixture(fixtureDef);
        shot.getFixtureList().get(0).setDensity(0.000f);
        shot.resetMassData();
        shot.setUserData(this);
        shot.setLinearVelocity(0, 1.5f);

        //Array för animationer
        Array<TextureRegion> frames = new Array<>();

        //Gå åt höger
        //Väljer bild Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        switch(shotType) {
            case STANDARD:
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion("shots"), 21, 1, 8, 200));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion("shots"), 39, 1, 8, 200));
                //Sätter tid på animation i sekunder samt anger en Array av frames
                shotUp = new Animation(0.15f, frames);
            break;
            case BARB:
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion("shots"), 57, 1, 8, 200));
                frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion("shots"), 75, 1, 8, 200));
                //Sätter tid på animation i sekunder samt anger en Array av frames
                shotUp = new Animation(0.15f, frames);
                barbStraight = new TextureRegion(assetManager.get("sprites/sprites.pack", TextureAtlas.class).findRegion("shots"), 3, 1, 8, 200);
                break;

        }

        setBounds(0, 0, 8 / PPM, 220 / PPM);

    }
    public void update(float dt){
        setRegion(getAnimation(dt));

        if(barbStop){
            barbTimer += dt;
            if(barbTimer>2 && !destroyShotNextUpdate){
                destroyNextUpdate();
            }
        }

        if (destroyShotNextUpdate) {
            destroyShot();
        }

        if(shotType == STANDARD || shotType == BARB){
            setPosition(shot.getPosition().x - getWidth() / 2, shot.getPosition().y - getHeight() / 2);
        }

    }

    public ShotType getShotType(){
        return shotType;
    }

    public void setBarbStop(){
        barbStop = true;
        shot.setLinearVelocity(0, 0f);
        Filter shutofRoof = new Filter();
        shutofRoof = shot.getFixtureList().get(0).getFilterData();;// Skott ska inte kunna skada bubbla mer
        shutofRoof.maskBits = BUBBLE;// Skott ska inte kunna skada bubbla mer
        shot.getFixtureList().get(0).setFilterData(shutofRoof);// Skott ska inte kunna skada bubbla mer
    }

    public void draws(Batch batch) {
        super.draw(batch);
    }
    public void destroyNextUpdate(){
        destroyShotNextUpdate = true;
        Filter shutofShot = new Filter();
        shutofShot = shot.getFixtureList().get(0).getFilterData();// Skott ska inte kunna skada bubbla mer
        shutofShot.maskBits = FREEFALL;// Skott ska inte kunna skada bubbla mer
        shot.getFixtureList().get(0).setFilterData(shutofShot);// Skott ska inte kunna skada bubbla mer

    }

    public boolean isDestroyed(){
        return shotDestroyed;
    }

    public void destroyShot() {//Sätter att bubblan ska förstöras.. denna ska anropas i contact handlern. När bubblan blir skjuten
        shot.setActive(false);
        shot.setUserData(null);
        setToDestroyShot = true;
        world.destroyBody(shot);
        destroyShotNextUpdate = false;
        shotDestroyed = true;
    }

    public TextureRegion getAnimation(float dt){

        TextureRegion region = null;
        if(shotType == STANDARD || (shotType == BARB && !barbStop)) {
            //State timer är tiden till animationen så den vet när den ska skifta bild och
            //loop är att den ska börja om när denkommit till sista bilden
            region = (TextureRegion) shotUp.getKeyFrame(stateTimer, true);
        }
        else{
            region = barbStraight;
        }
        //Lägg till aktuell tid mellan program körningar
        stateTimer =  stateTimer + dt;
        //returnera
        return region;
    }
    public void getPowerUp(){

    }

}
