package com.pang.game.Creators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pang.game.Constants.Constants.*;
import com.pang.game.Pang;
import com.pang.game.Sprites.PowerUpUnit;
import com.pang.game.Sprites.Shot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.pang.game.Constants.Constants.PPM;
import static com.pang.game.Creators.ShotHandler.ShotTypeHandler.*;
import static com.pang.game.Sprites.Shot.ShotType.*;

/**Hanterar skott & powerups i världen*/
public class ShotHandler {

    private ArrayList<Shot> myShots;
    private ArrayList<Shot> myDestroyedShots;
    private ShotTypeHandler type;
    private Shot.ShotType shotType;
    private ArrayList<PowerUp> myPowerUps;
    private ArrayList<PowerUpUnit> myActivePowerUps;
    private ArrayList<PowerUpUnit> myDestroyedPowerUps;
    private World world;
    private Pang game;
    private AssetManager assetManager;
    private PowerUp nextPowerUp;
    private Vector2 nextPowerUpPosition;

    public enum ShotTypeHandler{
        SINGLE, DOUBLE, BARB;
    }

    public ShotHandler(World world, Pang game, AssetManager assetManager) {
        this.world = world;
        this.game = game;
        this.assetManager = assetManager;
        type = ShotTypeHandler.SINGLE;
        myShots = new ArrayList<>();
        myDestroyedShots = new ArrayList<>();
        myPowerUps = new ArrayList<>();
        myActivePowerUps = new ArrayList<>();
        myDestroyedPowerUps  = new ArrayList<>();
        nextPowerUp = null;
        nextPowerUpPosition = new Vector2();
        shotType = SHOT_STANDARD;
}

    /*************************** Shots ***************************/
    /**Lägger till skott i världen*/
    public void addShot(Vector2 position){
        position.x -= 4/PPM;
        myShots.add(new Shot(world, position, assetManager, shotType,this));
    }

    /*************************** Shots ***************************/
    /**Kollar om powerup är för skott. Ändras isf skott typ*/
    public void setPowerUp(PowerUp powerUp){
        switch (powerUp){
            case DOUBLESHOT:
                type = DOUBLE;
                shotType = SHOT_STANDARD;
                break;
            case BARBSHOT:
                type = BARB;
                shotType = SHOT_BARB;
                break;
        }
    }

    /*************************** Shots ***************************/
    /**Kollar om skott kan avfyras det finns två begränsningar, singelskott eller dubbel (Används i dude)*/
    public boolean isReadyForShot(){
        boolean ready = false;
        switch (type){
            case SINGLE: case BARB:
                if(myShots.size()<1){
                    ready = true;
                }
                break;
            case DOUBLE:
                if(myShots.size()<2){
                    ready = true;
                }
                break;
        }
        return ready;
    }

    public void update(float dt){
        /*************************** PowerUps ***************************/
        if(nextPowerUp != null){
            myActivePowerUps.add(new PowerUpUnit(world, game, nextPowerUp, nextPowerUpPosition));
            nextPowerUp = null;
        }
        for(PowerUpUnit p: myActivePowerUps){
            p.update(dt);
            if(p.isDestroyed()){
                myDestroyedPowerUps.add(p);
            }
        }

        for(PowerUpUnit p: myDestroyedPowerUps){
            myActivePowerUps.remove(p);
        }
        myDestroyedPowerUps.clear();
        /*************************** Shots ***************************/
        for(Shot s: myShots){
            s.update(dt);
            if(s.isDestroyed()){
                myDestroyedShots.add(s);
            }
        }
        for(Shot s: myDestroyedShots){
            myShots.remove(s);
        }
        myDestroyedShots.clear();
    }

    public void renderer(Batch batch){//För att rita alla skott
        /*************************** PowerUps ***************************/
        for(PowerUpUnit p: myActivePowerUps){
            p.draw(batch);
        }
        /*************************** Shots ***************************/
        for (Shot s: myShots){
            s.draws(batch);
        }

    }

    /*************************** PowerUps ***************************/
    /**Portionerar ut powerup som är definierade för bana slumpmässigt i lista. Varje object som går att skjuta isönder har en plats i listan.
     * Varje gång något skjuts tas första objectet från listan.*/
    public void loadPowerUps(PowerUp[] powerUps, int destroyables){
        Random myRandom = new Random();
        int myRandomNbr = 0;
        int nbrOfPowerUps;
        int randomPowerUp = 0;
        ArrayList<PowerUp> tmpList = new ArrayList<>(Arrays.asList(new PowerUp[destroyables]));

        if(game.hud.getLevel()<5){
            nbrOfPowerUps = 2;
        }
        else{
            nbrOfPowerUps = 4;
        }
        for (int i = 0; i <nbrOfPowerUps  ; i++) {
            if(tmpList.size()<=i){//Fler powerups än breakables
                break;
            }
            randomPowerUp = myRandom.nextInt(powerUps.length);
            tmpList.set(i,powerUps[randomPowerUp]);
        }
        do{
            myRandomNbr = myRandom.nextInt(tmpList.size());
            myPowerUps.add(tmpList.get(myRandomNbr));
            tmpList.remove(myRandomNbr);

        }while (tmpList.size()>0);
    }

    /*************************** PowerUps ***************************/
    /**Genererar en ny powerup i världen*/
    public void generatePowerUp(Vector2 position, PowerUp powerUp){
        nextPowerUp = powerUp;
        nextPowerUpPosition = position;
    }

    /*************************** PowerUps ***************************/
    /**Kollar vad nästa object i powerup listan är en powerup.
     * Raderar sedan platsen i listan*/
    public PowerUp getPowerUp(){
        PowerUp powerUp = null;
        if (myPowerUps.size()>0){
            powerUp = myPowerUps.get(0);
            myPowerUps.remove(0);
        }
            return powerUp;
    }
}
