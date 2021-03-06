package com.pang.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.pang.game.Creators.BubbleHandler;
import com.pang.game.Creators.ShotHandler;
import com.pang.game.Pang;

import static com.pang.game.Constants.Constants.*;

/**
 * Klass för att göra spelkaraktären Dude
 */
public class Dude extends Sprite {
    private World world;
    private ShotHandler shotHandler;
    public Body dudeBody;
    private Animation goRight;
    private Animation goLeft;
    private enum State { RUNLEFT, RUNRIGHT, STANDING, SHOOTING, DIE };
    private State previousState;
    private State currentState;
    private TextureRegion dudeStand;
    private TextureRegion dudeshoot;
    private TextureRegion dudeDie;
    private BubbleHandler bubbleHandler;

    private boolean isShooting;
    private float shooterTime;
    private float shooterTimer;
    private float stateTimer;
    private boolean booleanOfDeath;
    private boolean isSheilded;
    private float resetSheildtimer;
    private boolean resetSheild;
    private Sprite shotBurst;
    private Animation shotAnimation;
    private float shotBurstTimer;
    private float shotBurstFadeTimer;
    private Pang game;

    /**
     *
     * @param world referens till box2d värld
     * @param game referens till Pang Objekt
     * @param startPos Position att skapas på
     * @param destroyables för generering av powerUps i shothandlern
     * @param bubbleHandler referens till bubblehandlern (för att frysa bubblor vid stopptime powerUp)
     */
    public Dude(World world, Pang game, Vector2 startPos, int destroyables, BubbleHandler bubbleHandler){
        this.game = game;
        this.world = world;
        this.bubbleHandler = bubbleHandler;
        resetSheildtimer = 0;
        resetSheild = false;
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
        dudeFixtureDef.filter.maskBits = BUBBLE | FLOOR_WALL | POWER_UP;

        //Fäster en form till kroppen
        dudeBody.createFixture(dudeFixtureDef);

        //Kasta form
        dudeShape.dispose();
        //Fixturen ska aldrig vridas
        dudeBody.setFixedRotation(true);
        //Position och storlek för (super)sprite när den ska ritas
        setBounds(0, 0, 32 / PPM, 32 / PPM);

        shotBurst = new Sprite();//För att visa en eldkvast när Dude skjuter
        shotBurst.setBounds(0,0,14/PPM,14/PPM);
        shotBurstTimer = 12f;
        shotBurstFadeTimer = 0;


        Array<TextureRegion> frames = new Array<>();

        //Gå åt höger
        //Väljer bild Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), 196, 153, 16, 16));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), 180, 156, 16, 16));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), 164, 157, 15, 16));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), 148, 159, 15, 16));
        //Sätter tid på animation i sekunder samt anger en Array av frames
        shotAnimation = new Animation(0.05f, frames);
        shotBurst.setRegion(frames.get(0));


        isShooting = false;
        dudeNormal();// animationer
        //Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        dudeDie = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 84, 69, 38, 32);

        shotHandler = new ShotHandler(world, game, game.assetManager);

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
                game.assetManager.get("audio/sound/shoot.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/shoot.wav", Sound.class).play(), 0.1f*game.soundVolume);
                isShooting = true;
                shooterTimer = 0f;
                shotBurstTimer = 0f;
                shotHandler.addShot(dudeBody.getPosition());
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

    /**
     * Avaktiverar Dudes kropp i box 2d
     */
    public void setToSleep(){
        dudeBody.setActive(false);
    }

    /**
     * Aktiverar Dudes kropp i box 2d
     */
    public void setToAwake(){
        dudeBody.setActive(true);
    }

    /**
     *
     * @param dt float .. delta time
     * @return TextureRegion.. med rätt bild på Dude för aktivt state
     */
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

    /**
     *
     * @return State .. Vilket state Dude är i. (Springer, står.. etc)
     */
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
        if(shotBurstTimer<0.12f) {//Visar eldkvast när dude skjuter
            shotBurstTimer += dt;
            shotBurst.setRegion(((TextureRegion) shotAnimation.getKeyFrame(shotBurstTimer, false)));
            shotBurst.setPosition(dudeBody.getPosition().x - 11 / PPM, dudeBody.getPosition().y + getHeight() / 2.5f);
            shotBurst.setAlpha(1f);
        }
        else {
            shotBurst.setAlpha(0f);//Gör eldkvast osynlig
        }

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

        if(resetSheild){//Tar bort powerUp effekt från sheild då gubbe är grön.
            resetSheildtimer += dt;
            if(resetSheildtimer>0.1f){
                dudeNormal();//Normal gubbe igen
            }
        }
    }

    public  void dispose(){
        dudeshoot.getTexture().dispose();
        dudeStand.getTexture().dispose();
        dudeDie.getTexture().dispose();
    }

    /**
     * När dude dör startar denna metoden dödshoppet.
     */
    public void jumpOfDeath(){
        dudeBody.setLinearVelocity((0f), 0);
        dudeBody.applyLinearImpulse(new Vector2(-0.5f,1.5f), dudeBody.getWorldCenter(), true);
    }

    /**
     *
     * @return boolean .. Dude liv status
     */
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

    /**
     * ShotHandlerns draw metod
     * @param batch för sprite draw
     */
    public void drawShot(Batch batch) {
        shotHandler.renderer(batch);
    }

    /**
     * Dudes draw metod
     * @param batch för sprite draw
     */
    @Override
    public void draw(Batch batch) {

        shotBurst.draw(batch);
        super.draw(batch);

    }

    /**
     * Aktiverar powerUps
     * @param powerUp powerup typ
     */
    public void setPowerUp(PowerUp powerUp){
        switch (powerUp){
            case BARBSHOT: case DOUBLESHOT:
                shotHandler.setPowerUp(powerUp);
                break;
            case STOPTIME:
                bubbleHandler.freezeBubblesNextUpdate();
                break;
            case LIFE:
                game.hud.addLife();
                break;
            case SHEILD:
                dudeShield();
                break;
        }

    }

    /**
     * Metod för att läsa in Dudes grafik utan sheild powerUp
     */
    private final void dudeNormal(){
        Array<TextureRegion> frames = new Array<>();

        //Gå åt höger
        //Väljer bild Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 1, 2, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 35, 2, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 69, 2, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 102, 2, 32, 32));
        //Sätter tid på animation i sekunder samt anger en Array av frames
        goRight = new Animation(0.07f, frames);
        //Raderar frame Array
        frames.clear();

        //Gå åt vänster
        //Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 209, 2, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 243, 2, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 277, 2, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 310, 2, 32, 32));
        //Sätter tid på animation i sekunder samt anger en Array av frames
        goLeft = new Animation(0.07f, frames);

        //Bild för stående gubbe
        //Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        dudeStand = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 166, 69, 32, 32);

        //Bild för stående gubbe
        //Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        dudeshoot = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 133, 69, 32, 32);
        if(isSheilded){
            game.assetManager.get("audio/sound/powerUpSheildDown.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpSheildDown.wav", Sound.class).play(), 1.0f);
        }
        isSheilded = false;
        resetSheild = false;
    }

    /**
     * Metod för att läsa in Dudes grafik med sheild powerUp
     */
    private final void dudeShield(){
        Array<TextureRegion> frames = new Array<>();

        //Gå åt höger
        //Väljer bild Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 2, 176, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 36, 176, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 70, 176, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 103, 176, 32, 32));
        //Sätter tid på animation i sekunder samt anger en Array av frames
        goRight = new Animation(0.07f, frames);
        //Raderar frame Array
        frames.clear();

        //Gå åt vänster
        //Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 210, 176, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 244, 176, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 278, 176, 32, 32));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 311, 176, 32, 32));
        //Sätter tid på animation i sekunder samt anger en Array av frames
        goLeft = new Animation(0.07f, frames);

        //Bild för stående gubbe
        //Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        dudeStand = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 247, 135, 32, 32);

        //Bild för stående gubbe
        //Player All2 i sprites.pack hämtar sedan bild på x,y koordinat och anger storlek. x har positiv riktning åt höger. y har positiv riktning nedåt.
        dudeshoot = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 214, 135, 32, 32);

        isSheilded = true;
    }

    /**
     *
     * @return boolean .. Dude har sheild powerUp
     */
    public boolean isSheilded(){
        return isSheilded;
    }

    /**
     * Återställ sköld powerUp
     */
    public void resetSheild(){
        resetSheild = true;
        resetSheildtimer = 0;
    }
}

