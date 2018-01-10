package com.pang.game.Creators;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.pang.game.HUD.HUD;
import com.pang.game.Pang;
import com.pang.game.Sprites.Bubble;
import com.pang.game.Sprites.DoubleBoubble;

import java.util.ArrayList;

/**
 * Klass för att hantera bubblor
 */
public class BubbleHandler {
    private ArrayList<Bubble> myBubbles; //Här har vi bubblor som används i spelet
    private ArrayList<DoubleBoubble> myCreatedDoubles;//Här lagrar vi nyskapade bubblor
    private ArrayList<Bubble> myBubblesToDestroy;//Här lagrar vi bubblor som ska förstöras
    private float freezeTimer;
    private boolean bubblesFrozen;
    private boolean freezeBubblesNextUpdate;
    private float freezeTime;
    private int freezeSoundMask;
    private Pang game;

    /**
     *
     * @param game referens till Pangobjekt
     */
    public BubbleHandler(Pang game){
        this.game = game;
        myBubbles = new ArrayList<>();
        myCreatedDoubles = new ArrayList<>();
        myBubblesToDestroy = new ArrayList<>();
        freezeTimer = 0;
        bubblesFrozen = false;
        freezeBubblesNextUpdate = false;
        freezeTime = 7;
        freezeSoundMask = 0;
    }

    /**
     * Metod för att lägga till bubblor
     * @param bubble bubbla att lägga till
     */
    public void addBubble(Bubble bubble){//Metod för att lägga till bubblor vid start av bana
        myBubbles.add(bubble);
    }

    /**
     *
     * @return int .. så många bubblor det finns
     */
    public int getBubbles(){//För att veta om det finns några bubblor kvar
        return myBubbles.size();
    }
    public void renderer(Batch batch){//För att rita alla bubblor
        for (int i = myBubbles.size()-1; i >=0 ; i--) {

         //(Bubble b: myBubbles){
            myBubbles.get(i).draw(batch);
        }
    }

    /**
     * Avaktiverar alla bubblors kroppar
     */
    public void setToSleep(){//Stoppar alla bubblor
        for (Bubble b:myBubbles){
            b.setToSleep();
        }
    }

    /**
     * Aktiverar alla bubblors kroppar
     */
    public void setToAwake(){
        for (Bubble b:myBubbles){
            b.setToAwake();
        }
    }


    /**
     *
     * @return int .. hur många bubblor som kommer produceras på leveln(för powerUp generering)
     */
    public int getDestroyables(){
        int destroyables = 0;
        for(Bubble b:myBubbles){
            destroyables += b.getDestroyables();
        }
        return destroyables;
    }

    public void update(float dt, HUD hud) {//Kollar om bubblor ska skapas och förstöras

        for(Bubble b:myBubbles){
            b.update(dt);
            if (!b.isBubblesCreated() && b.isSetToDestroy()) {//Varje bubbla ska producera två nya bubblor om den inte har minsta storleken
                myCreatedDoubles.add(b.createNewBubbles());
                hud.addScore(b.getPoints());
            }
            if (b.isDestroyed()) {//Kollar vilka bubblor som ska bort
                myBubblesToDestroy.add(b);//Lägg till i förstörda listan
            }
        }
        for(
        DoubleBoubble dB:myCreatedDoubles) {//Kollar om några nya bubblor blev skapade
            if (dB != null) {
                myBubbles.add(dB.bubbleLeft);
                myBubbles.add(dB.bubbleRight);
            }
        }
        myCreatedDoubles.clear();//Rensa skapade bubblor

        for(Bubble b:myBubblesToDestroy) {//Loopa igenom bubblor att ta bort
            myBubbles.remove(b);
        }
        myBubblesToDestroy.clear();//Rensa bubblor att radera

        if(bubblesFrozen){//StopTime powerUp är aktiv alla bubblor är stilla
            freezeTimer += dt;
            if(freezeTimer>freezeTime){//Återställer bubblor när tid gått ut
                unFreezeBubbles();
            }
            if (freezeTimer>freezeTime-3){//Varna att bubblor snart börjar röra på sig Varning 1
                if(0 == (freezeSoundMask & 1)){
                    game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).play(), 0.3f);
                    freezeSoundMask |= 1;
                }
                if (freezeTimer>freezeTime-2){//Varna att bubblor snart börjar röra på sig Varning 2
                    if(0 == (freezeSoundMask & 2)){
                        game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).play(), 0.3f);
                        freezeSoundMask |= 2;
                    }
                    if (freezeTimer>freezeTime-1){//Varna att bubblor snart börjar röra på sig Varning 3
                        if(0 == (freezeSoundMask & 4)){
                            game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).play(), 0.3f);
                            freezeSoundMask |= 4;
                        }
                    }
                }
            }
        }

        if(freezeBubblesNextUpdate){
            freezeBubbles();//Stoppar bubblor
        }
    }

    /**
     * Metod för att stoppa bubblor vid Stop time powerUp
     */
    private void freezeBubbles(){
        freezeTimer = 0;
        freezeSoundMask = 0;
        for(Bubble b:myBubbles) {//loopa igenom alla bubblor
            b.freezeBubble();
        }
        bubblesFrozen = true;
        freezeBubblesNextUpdate = false;
    }

    /**
     * Alla bubblor återgår till rörelse
     */
    private void unFreezeBubbles(){
        for(Bubble b:myBubbles) {
            b.unFreezeBubble();
        }
        bubblesFrozen = false;
    }

    /**
     * Vid nästa update ska bubblorna stanna
     */
    public void freezeBubblesNextUpdate(){
        freezeBubblesNextUpdate = true;
    }
}
