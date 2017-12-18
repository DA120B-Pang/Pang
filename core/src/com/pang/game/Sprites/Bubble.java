package com.pang.game.Sprites;

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
    private BubbleState startSize;
    private BubbleState minSize;
    private BubbleColor color;
    private Body bubbleBody;
    private float bubbleLinearSpd;
    private Vector2 bubbleBounceForce;
    private Vector2 bubbleBounceForceObstacale;
    private Vector2 bubbleBounceForceCalc;
    private boolean goingRight;
    private boolean spawnRight;
    private boolean spawnSpdIsSet;
    private Animation explode;
    private boolean setToDestroy;
    private boolean destroyNextUpdate;
    private boolean destroyed;
    private float explosionTimer;
    private boolean explosionSoundDone;
    private AssetManager assetManager;
    private boolean newBubblesCreated;
    private boolean pointsCollected;
    private boolean isFrozen;
    private boolean bumpObstacaleTopNextUpdate;
    private boolean bumpObstacaleNextUpdate;
    private boolean bumpFloorNextUpdate;
    private boolean bumpLeftWallNextUpdate;
    private boolean bumpRightWallNextUpdate;
    private boolean upOnBirth;
    private Vector2 getCurrentVel;



    public enum BubbleState {//Visar vilken storlek bubblan har
        XLARGE(100),LARGE(90),MEDIUM(80),SMALL(70),XSMALL(60);
        private final int value;
        BubbleState(int value){
            this.value = value ;
        }
    }
    public enum BubbleColor {//Visar vilken färg bubblan har
        BLUE,RED,GREEN
    }
    public Bubble(World world, BubbleState startSize, BubbleColor color, Vector2 position, AssetManager assetManager,boolean spawnRight, BubbleState minSize, boolean isFrozen, boolean upOnBirth){
        this.assetManager = assetManager;
        this.startSize = startSize;
        this.isFrozen = isFrozen;
        bumpObstacaleTopNextUpdate = false;
        bumpObstacaleNextUpdate = false;
        bumpFloorNextUpdate = false;
        bumpLeftWallNextUpdate = false;
        bumpRightWallNextUpdate = false;
        this.upOnBirth = upOnBirth;
        getCurrentVel = new Vector2();
        if (minSize.ordinal()<startSize.ordinal()){
            this.minSize = startSize;
        }
        else{
            this.minSize = minSize;
        }
        this.color = color;
        this.world = world;
        int colorPosX = 0;
        int colorPosY = 0;
        int pictureWidth = 0;
        int pictureHeight = 0;
        float radius = 0;
        this.spawnRight = spawnRight;
        bubbleLinearSpd = 0.0f;
        bubbleBounceForceObstacale = new Vector2(0,0);
        bubbleBounceForce = new Vector2(0,0);
        bubbleBounceForceCalc = new Vector2(0,0);
        bubbleLinearSpd = 0.65f;//Sätter horisontell hastighet
        explosionTimer = 0f;
        destroyed = false;
        setToDestroy = false;
        destroyNextUpdate = false;
        explosionSoundDone = false;
        newBubblesCreated = false;
        pointsCollected = false;


        switch (startSize) {
            case XLARGE:
                bubbleBounceForce.y = 0.000409f; //0.000109f; //0.000409f;//Sätter hopp kraft i y uppåt
                bubbleBounceForceObstacale.y = 0.000100f;
                pictureWidth = 45;//Bredd på bild i atlas
                pictureHeight = 45;//Höjd på bild i atlas
                radius = 22;//Radie på bubbla
                switch (color){
                    case RED://Röd positioner på atlas
                        colorPosX = 1;
                        colorPosY = 6;
                        break;
                    case BLUE://Blå positioner på atlas
                        colorPosX = 1;
                        colorPosY= 53;
                        break;
                    case GREEN://Grön positioner på atlas
                        colorPosX = 1;
                        colorPosY = 99;
                        break;
                }
                break;
            case LARGE:
                //Sätter hopp kraft i y uppåt
                bubbleBounceForce.y = 0.00023f;
                bubbleBounceForceObstacale.y = 0.00007f;
                pictureWidth = 35;
                pictureHeight = 35;
                radius = 17;
                switch (color){
                    case RED://Röd positioner på atlas
                        colorPosX = 46;
                        colorPosY = 11;
                        break;
                    case BLUE://Blå positioner på atlas
                        colorPosX = 46;
                        colorPosY = 57;
                        break;
                    case GREEN://Grön positioner på atlas
                        colorPosX = 46;
                        colorPosY = 104;
                        break;
                }
                break;
            case MEDIUM:
                //Sätter hopp kraft i y uppåt
                bubbleBounceForce.y = 0.00008f;
                bubbleBounceForceObstacale.y = 0.00005f;
                pictureWidth = 23;
                pictureHeight = 23;
                radius = 11;
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
                break;
            case SMALL:
                //Sätter hopp kraft i y uppåt
                bubbleBounceForce.y = 0.000017f;
                bubbleBounceForceObstacale.y = 0.000015f;
                pictureWidth = 12;
                pictureHeight = 12;
                radius = 6;
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
                break;
            default:
                //Sätter hopp kraft i y uppåt
                bubbleBounceForce.y = 0.000004f;
                bubbleBounceForceObstacale.y = 0.000004f;
                pictureWidth = 6;
                pictureHeight = 6;
                radius = 3f;
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
                break;
        }
        //Sätter storlek på sprite
        setBounds(0, 0, pictureWidth / PPM, pictureHeight / PPM);
        //Laddar explosions animation
        explode = new Animation(0.04f,getExplosionAnimation(this.startSize,assetManager) );
        //Sätter grafik på boll
        setRegion(new TextureRegion(assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Balloons"), colorPosX, colorPosY, pictureWidth, pictureHeight));

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
        bubbleFixDef.filter.maskBits =  DUDE | FLOOR_WALL | ROOF | SHOT | OBSTACLE | OBSTACLE_SIDE | OBSTACLE_TOP;//

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
    private void checkSpd(){
        if(!isFrozen) {
            if (bubbleBody.getLinearVelocity().x > bubbleLinearSpd || goingRight && bubbleBody.getLinearVelocity().x < bubbleLinearSpd - 0.10f) {
                bubbleBody.setLinearVelocity(bubbleLinearSpd - 0.10f, bubbleBody.getLinearVelocity().y);
            } else if (bubbleBody.getLinearVelocity().x < (-bubbleLinearSpd) || !goingRight && bubbleBody.getLinearVelocity().x > (-bubbleLinearSpd + 0.10f)) {
                bubbleBody.setLinearVelocity(-bubbleLinearSpd + 0.10f, bubbleBody.getLinearVelocity().y);
            }
        }
    }

    public void setBumpLeftWallNextUpdate() {
        this.bumpLeftWallNextUpdate = true;
    }

    public final void bumpLeftWall(){//Action för contact listenern när bubbla träffar vänster vägg
        bubbleBody.setLinearVelocity(bubbleLinearSpd, bubbleBody.getLinearVelocity().y);
        bumpLeftWallNextUpdate = false;
        goingRight = true;
    }

    public void setBumpRightWallNextUpdate() {
        this.bumpRightWallNextUpdate = true;
    }

    public final void bumpRightWall(){//Action för contact listenern när bubbla träffar höger vägg
        bubbleBody.setLinearVelocity(-bubbleLinearSpd, bubbleBody.getLinearVelocity().y);
        bumpRightWallNextUpdate = false;
        goingRight = false;
    }

    public void setBumpObstacaleNextUpdate(){
        bumpObstacaleNextUpdate = true;
    }

    public void bumpObstacale(){
        if(goingRight){//kolla vilket håll bollen färdades på och fortsätt på motsatt håll
            bumpRightWall();
        }
        else{
            bumpLeftWall();
        }
        bumpObstacaleNextUpdate = false;
    }

    public void setBumpFloorNextUpdate(){
        bumpFloorNextUpdate = true;
    }

    public final void bumpFloor() {////Action för contact listenern när bubbla träffar marken
        bubbleBody.setLinearVelocity(0f, 0f);//Boll måste först stanna(för att vi alltid ska uppnå samma höjd)
        bubbleBody.applyLinearImpulse(bubbleBounceForce, bubbleBody.getWorldCenter(), true);
        if(goingRight){//kolla vilket håll bollen färdades på och fortsätt på samma håll
            bumpLeftWall();
        }
        else{
            bumpRightWall();
        }
        bumpFloorNextUpdate = false;
    }

    private float scaleForce(){
        float valueIn = bubbleBody.getPosition().y;
        float maxIn = (56)/PPM;
        float minIn = WORLD_HEIGHT/PPM;//upp till 56 är HUD
        float maxOut = bubbleBounceForce.y;
        float minOut = bubbleBounceForceObstacale.y;

        float factor = (maxOut-minOut)/(maxIn-minIn);
        float offset = minOut - (minIn*factor);
        float force = (valueIn*factor)+offset;
        return force;

    }

    public void setBumpObstacaleTopNextUpdate(){
        bumpObstacaleTopNextUpdate = true;
    }

    public final void bumpObstacaleTop() {////Action för contact listenern när bubbla träffar marken
        bubbleBounceForceCalc.y = scaleForce();
        bubbleBody.setLinearVelocity(0f, 0f);//Boll måste först stanna(för att vi alltid ska uppnå samma höjd)
        bubbleBody.applyLinearImpulse(bubbleBounceForceCalc, bubbleBody.getWorldCenter(), true);
        if(goingRight){//kolla vilket håll bollen färdades på och fortsätt på samma håll
            bumpLeftWall();
        }
        else{
            bumpRightWall();
        }
        bumpObstacaleTopNextUpdate = false;
    }


    public void update(float dt) {
        checkSpd();
        scaleForce();

        if(!isFrozen){
            if(bumpObstacaleNextUpdate){
                bumpObstacale();
            }
            if(bumpObstacaleTopNextUpdate){
                bumpObstacaleTop();
            }
            if(bumpFloorNextUpdate){
                bumpFloor();
            }
            if(bumpRightWallNextUpdate){
                bumpRightWall();
            }
            if(bumpLeftWallNextUpdate){
                bumpLeftWall();
            }
        }

        if(destroyNextUpdate){
            setToDestroy();
        }

        if(!setToDestroy) {//Slutar updatera position när spelare dör
            //Sätter Texture region till sprite
            setPosition(bubbleBody.getPosition().x - getWidth() / 2, bubbleBody.getPosition().y - getHeight() / 2);
        }
        else if(setToDestroy && !destroyed){//
            if(!explosionSoundDone) {
                setExplodeSound(startSize, assetManager);
                explosionSoundDone = true;
            }
            if(animateExplosion(dt)){
                destroy();
            }
        }

        if (!spawnSpdIsSet){//Sätt hastighet när bubble bildats
            if(spawnRight){
                bumpLeftWall();
            }
            else{
                bumpRightWall();
            }
            if(upOnBirth){
                bumpObstacaleTop();
            }
            if(isFrozen){
                freezeBubble();
            }
            spawnSpdIsSet = true;
        }
    }
    public void dispose(){
        getTexture().dispose();
    }
    public void draw(Batch batch) {//ritar bubbla om den inte är förstörd
        if(!destroyed) {
            super.draw(batch);
        }
    }

    private boolean animateExplosion(float dt){//Skapar animation för explosion
        explosionTimer += dt;
        setRegion((TextureRegion)explode.getKeyFrame(explosionTimer, false));
        return explode.isAnimationFinished(explosionTimer)? true:false;
    }

    public void setToSleep(){//Inaktiverar bubblans body
        bubbleBody.setActive(false);
    }

    public void setToAwake(){
        bubbleBody.setActive(true);
    }

    public int getPoints(){
        if (destroyNextUpdate && !pointsCollected){
            return startSize.value;
        }
        else{
            return 0;
        }
    }

    public boolean isDestroyed() {//Visar om bubblan är förstörd denna status får bubblan när explosions animationen är klar.
        return destroyed;
    }

    public void destroy(){//Raderar kropp från värld
        destroyed = true;
        world.destroyBody(bubbleBody);
    }

    public void destroyNextUpdate(){
        destroyNextUpdate = true;
        Filter spareDude = new Filter();// Bubblan ska inte kunna skada dude mer
        spareDude.maskBits = FLOOR_WALL;// Bubblan ska inte kunna skada dude mer
        bubbleBody.getFixtureList().get(0).setFilterData(spareDude);// Bubblan ska inte kunna skada dude mer
    }
    public void setToDestroy(){//Sätter att bubblan ska förstöras.. denna ska anropas i contact handlern. När bubblan blir skjuten
        bubbleBody.setActive(false);
        bubbleBody.setUserData(null);
        setToDestroy = true;
    }

    public boolean isSetToDestroy(){
        return setToDestroy;
    }

    private Array<TextureRegion> getExplosionAnimation(BubbleState size, AssetManager assetManager){//Skapar animation för explosion
        String fileName;
        Array<TextureRegion> frames = new Array<>();//Array för animationer
        int sizeImage, nbr2, nbr3, nbr4, nbr5, nbr6;

        switch(size){
            case XLARGE:
                fileName = "explosionXL";//Filnamn på bildark
                sizeImage = 45; // storlek på bild
                nbr2 = 45; // position för index 2
                nbr3 = 90; // position för index 3
                nbr4 = 135;// position för index 4
                nbr5 = 180;// position för index 5
                nbr6 = 225;// position för index 6
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
        //Läser in bilder till explotions animation
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

    private void setExplodeSound(BubbleState size, AssetManager assetManager){//Sätter igång explosions ljud för rätt storlek av bubbla
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

    public boolean isBubblesCreated(){//Spelet behöver veta om bubblan har förökat sig
        return newBubblesCreated;
    }

    public DoubleBoubble createNewBubbles(){//Skapar två nya bubblor om bubblan inte är av minsta sorten XSMALL.
        DoubleBoubble myBoubbles;
        if(startSize!=minSize) {
            switch (startSize) {
                case XLARGE:
                    myBoubbles = new DoubleBoubble(world, LARGE, color, new Vector2(bubbleBody.getPosition().x * PPM, bubbleBody.getPosition().y * PPM), assetManager, 24f, minSize, isFrozen);
                    break;
                case LARGE:
                    myBoubbles = new DoubleBoubble(world, MEDIUM, color, new Vector2(bubbleBody.getPosition().x * PPM, bubbleBody.getPosition().y * PPM), assetManager, 18f, minSize, isFrozen);
                    break;
                case MEDIUM:
                    myBoubbles = new DoubleBoubble(world, SMALL, color, new Vector2(bubbleBody.getPosition().x * PPM, bubbleBody.getPosition().y * PPM), assetManager, 13f, minSize, isFrozen);
                    break;
                case SMALL:
                    myBoubbles = new DoubleBoubble(world, XSMALL, color, new Vector2(bubbleBody.getPosition().x * PPM, bubbleBody.getPosition().y * PPM), assetManager, 7f, minSize, isFrozen);
                    break;
                default:
                    myBoubbles = null;
            }
        }
        else{
            myBoubbles = null;
        }
        newBubblesCreated = true;


        return myBoubbles;
    }

    public int getDestroyables(){
        int generations = minSize.ordinal()-startSize.ordinal()+1;
        return ((int)Math.pow(2,(generations))) - 1;//Hur många bubblor en bubbla kommer att resultera i.
    }

    public Vector2 getPosition(){
        return bubbleBody.getPosition();
    }

    public final void freezeBubble(){
        getCurrentVel = bubbleBody.getLinearVelocity();
        bubbleBody.setGravityScale(0f);
        bubbleBody.setLinearVelocity(new Vector2(0f,0f));
        isFrozen = true;
    }

    public void unFreezeBubble(){
        bubbleBody.setGravityScale(1);
        bubbleBody.setLinearVelocity(getCurrentVel);
        isFrozen = false;
    }
}
