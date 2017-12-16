package com.pang.game.Creators;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.pang.game.HUD.HUD;
import com.pang.game.Pang;
import com.pang.game.Sprites.Bubble;
import com.pang.game.Sprites.DoubleBoubble;

import java.util.ArrayList;

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

    public void addBubble(Bubble bubble){//Metod för att lägga till bubblor vid start av bana
        myBubbles.add(bubble);
    }

    public int getBubbles(){//För att veta om det finns några bubblor kvar
        return myBubbles.size();
    }
    public void renderer(Batch batch){//För att rita alla bubblor
        for (int i = myBubbles.size()-1; i >=0 ; i--) {

         //(Bubble b: myBubbles){
            myBubbles.get(i).draw(batch);
        }
    }

    public void setToSleep(){//Stoppar alla bubblor
        for (Bubble b:myBubbles){
            b.setToSleep();
        }
    }

    public void setToAwake(){
        for (Bubble b:myBubbles){
            b.setToAwake();
        }
    }

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
                myBubblesToDestroy.add(b);
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

        if(bubblesFrozen){
            freezeTimer += dt;
            if(freezeTimer>freezeTime){
                unFreezeBubbles();
            }
            if (freezeTimer>freezeTime-3){
                if(0 == (freezeSoundMask & 1)){
                    game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).play(), 0.3f);
                    freezeSoundMask |= 1;
                }
                if (freezeTimer>freezeTime-2){
                    if(0 == (freezeSoundMask & 2)){
                        game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).play(), 0.3f);
                        freezeSoundMask |= 2;
                    }
                    if (freezeTimer>freezeTime-1){
                        if(0 == (freezeSoundMask & 4)){
                            game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpStopTimeDown.wav", Sound.class).play(), 0.3f);
                            freezeSoundMask |= 4;
                        }
                    }
                }
            }
        }

        if(freezeBubblesNextUpdate){
            freezeBubbles();
        }
    }

    private void freezeBubbles(){
        freezeTimer = 0;
        freezeSoundMask = 0;
        for(Bubble b:myBubbles) {
            b.freezeBubble();
        }
        bubblesFrozen = true;
        freezeBubblesNextUpdate = false;
    }

    private void unFreezeBubbles(){
        for(Bubble b:myBubbles) {
            b.unFreezeBubble();
        }
        bubblesFrozen = false;
    }

    public void freezeBubblesNextUpdate(){
        freezeBubblesNextUpdate = true;
    }
}
