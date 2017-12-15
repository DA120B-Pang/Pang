package com.pang.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.pang.game.Creators.ShotHandler;
import com.pang.game.Pang;

import static com.pang.game.Constants.Constants.*;

public class Dude extends Sprite {
    private World world;
    private ShotHandler shotHandler;
    public Body dudeBody;
    private Animation goRight;
    private Animation goLeft;
    private enum State { RUNLEFT, RUNRIGHT, STANDING, SHOOTING, DIE };
    private State previousState;
    private State currentState;
    private AssetManager assetManager;
    private TextureRegion dudeStand;
    private TextureRegion dudeshoot;
    private TextureRegion dudeDie;

    private boolean isShooting;
    private float shooterTime;
    private float shooterTimer;
    private float stateTimer;
    private boolean booleanOfDeath;

    public Dude(World world, Pang game, Vector2 startPos, int destroyables){
        this.assetManager = game.assetManager;
        this.world = world;
        //Initiera boolean of death
        booleanOfDeath = false;
        //Initiera shoot
        isShooting = false;
        shooterTimer = 0f;
        shooterTime = 0.1f;
        //skapa kropp
        BodyDef dudeDef= new BodyDef();
        //Start position
        dudeDef.position.set(startPos.x/PPM,startPos.y/PPM);
        //Typ av kropp
        dudeDef.type = BodyDef.BodyType.DynamicBody;
        //Skapa kropp i box2d värld
        dudeBody = this.world.createBody(dudeDef);
        // För contact handlern så vi kan använda metoder
        dudeBody.setUserData(this);
        //Skapa form
        PolygonShape dudeShape = new PolygonShape();
        //Gör en fyrkant
        dudeShape.setAsBox(8f/PPM,13f/PPM);
        //Skapa Fixture Def
        FixtureDef dudeFixtureDef = new FixtureDef();
        //Ge fixture Def en form
        dudeFixtureDef.shape = dudeShape;
        //Sätter kategoribit DUDE för kollisioner
        dudeFixtureDef.filter.categoryBits = DUDE;
        //Dude ska kollidera med boll och
        dudeFixtureDef.filter.maskBits = BUBBLE | FLOOR_WALL;

        //Fäster en form till kroppen
        dudeBody.createFixture(dudeFixtureDef);

        //Kasta form
        dudeShape.dispose();
        //Fixturen ska aldrig vridas
        dudeBody.setFixedRotation(true);
        //Position och storlek för (super)sprite när den ska ritas
        setBounds(0, 0, 32 / PPM, 32 / PPM);

        isShooting = false;
        //Array för animationer
        Array<TextureRegion> frames = new Array<>();

        //Gå åt höger
        //Väljer bild Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 1, 2, 32, 32));
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 35, 2, 32, 32));
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 69, 2, 32, 32));
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 102, 2, 32, 32));
        //Sätter tid på animation i sekunder samt anger en Array av frames
        goRight = new Animation(0.07f, frames);
        //Raderar frame Array
        frames.clear();

        //Gå åt vänster
        //Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 209, 2, 32, 32));
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 243, 2, 32, 32));
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 277, 2, 32, 32));
        frames.add(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 310, 2, 32, 32));
        //Sätter tid på animation i sekunder samt anger en Array av frames
        goLeft = new Animation(0.07f, frames);

        //Bild för stående gubbe
        //Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        dudeStand = new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 166, 69, 32, 32);

        //Bild för stående gubbe
        //Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        dudeshoot = new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 133, 69, 32, 32);

        //Bild för stående gubbe
        //Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        dudeDie = new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 84, 69, 38, 32);

        shotHandler = new ShotHandler();

        shotHandler.loadPowerUps(game.hud.getPowerUps(),destroyables);

    }

    public void handleInput(float dt){
        float vel = 0;
        if(isDudeDead() || !dudeBody.isActive()){
            //Inget händer här
        }
        else if((Gdx.input.isKeyJustPressed(Input.Keys.Z) && shotHandler.isReadyForShot()) || isShooting){//dude skjuter
            if(!isShooting) {
                dudeBody.setLinearVelocity((0f), 0);
                assetManager.get("audio/sound/shoot.wav", Sound.class).setVolume(assetManager.get("audio/sound/shoot.wav", Sound.class).play(), 0.1f);
                isShooting = true;
                shooterTimer = 0f;
                shotHandler.addShot(world,dudeBody.getPosition(),assetManager);
            }
            else if(shooterTimer>shooterTime){
                isShooting = false;
            }
            else{
                shooterTimer+=dt;
            }
        }else
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){//Dude gå åt vänster
            dudeBody.setLinearVelocity(-1.5f,0f);
        }else
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){//Dude gå åt höger
            dudeBody.setLinearVelocity(1.5f,0f);
        }else if(!isDudeDead()) {// Stoppa dude
            if(dudeBody.getLinearVelocity().x > 0.01 || dudeBody.getLinearVelocity().x < -0.01) {
                dudeBody.setLinearVelocity((0f), 0);
            }
        }
    }

    public void setToSleep(){
        dudeBody.setActive(false);
    }

    public void setToAwake(){
        dudeBody.setActive(true);
    }

    public TextureRegion getAnimation(float dt){
        //Kolla vilket state dude har
        currentState = getState();

        TextureRegion region = null;

        //Utför animation enligt aktuellt state
        switch(currentState){
            case DIE:
                region = dudeDie;
                break;
            case RUNLEFT:
                //State timer är tiden till animationen så den vet när den ska skifta bild och
                //loop är att den ska börja om när denkommit till sista bilden
                region = (TextureRegion)goLeft.getKeyFrame(stateTimer, true);
                break;
            case RUNRIGHT:
                //State timer är tiden till animationen så den vet när den ska skifta bild och
                //loop är att den ska börja om när denkommit till sista bilden
                region = (TextureRegion)goRight.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
                region = dudeStand;
                break;
            case SHOOTING:
                region = dudeshoot;
                break;
            default:
                region = dudeStand;
                break;
        }
        //Lägg till aktuell tid mellan program körningar ( Tidräkning till animation) Nolla vid byte av state
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //returnera
        return region;

    }
    public State getState(){
        State state;
        if(booleanOfDeath){
            state = State.DIE;
        }
        else if(isShooting){
            state = State.SHOOTING;
        }//Om dude rör sig till vänster
        else if(dudeBody.getLinearVelocity().x < -0.1) {
            state =  State.RUNLEFT;
        }//Om dude rör sig till höger
        else if(dudeBody.getLinearVelocity().x > 0.1) {
            state = State.RUNRIGHT;
        }
        else {//dude står stilla
            state = State.STANDING;
        }
        return state;
    }
    public void update(float dt) {
        handleInput(dt);
        float offset = 2/PPM; //Offset för att korrigera position av sprite vid gång
        //Sätter Texture region till sprite
        setRegion(getAnimation(dt));
        if(currentState==State.RUNLEFT) {
            setPosition(dudeBody.getPosition().x - offset - getWidth() / 2, dudeBody.getPosition().y - getHeight() / 2);
        }//Justera position av sprite när vi går åt höger
        else if(currentState==State.RUNRIGHT) {
            setPosition(dudeBody.getPosition().x + offset - getWidth() / 2, dudeBody.getPosition().y - getHeight() / 2);
        }//Vid stillastående
        else {
            setPosition(dudeBody.getPosition().x - getWidth() / 2, dudeBody.getPosition().y - getHeight() / 2);
        }

        shotHandler.update(dt);
    }

    public  void dispose(){
        dudeshoot.getTexture().dispose();
        dudeStand.getTexture().dispose();
        dudeDie.getTexture().dispose();
    }
    public void jumpOfDeath(){
        dudeBody.setLinearVelocity((0f), 0);
        dudeBody.applyLinearImpulse(new Vector2(-0.5f,1.5f), dudeBody.getWorldCenter(), true);
    }

    public boolean isDudeDead(){
        return booleanOfDeath;
    }

    public void dudeDie(){
        booleanOfDeath = true;
        Filter filter = new Filter();
        filter = dudeBody.getFixtureList().get(0).getFilterData();
        filter.maskBits = FLOOR_WALL;
        dudeBody.getFixtureList().get(0).setFilterData(filter);
        jumpOfDeath();
    }

    public void drawShot(Batch batch) {
        shotHandler.renderer(batch);
    }
    @Override
    public void draw(Batch batch) {

        super.draw(batch);

    }
}

