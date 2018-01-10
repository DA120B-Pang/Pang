package com.pang.game.Creators;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.pang.game.Pang;
import com.pang.game.Sprites.Obstacle;

import java.util.ArrayList;

/**
 * Klass för att hantera hinder
 */
public class ObstacleHandler {
    private ArrayList<Obstacle> myObstacles;
    private ArrayList<Obstacle> myDestroyedObstacles;

    public ObstacleHandler () {
        myObstacles = new ArrayList<>();
        myDestroyedObstacles = new ArrayList<>();
    }

    /**
     *
     * @param game referens till Pang Objekt
     * @param rectangle Position och dimension att skapa objekt på
     * @param world referens till box2d värld
     * @param colorYellow om sann är hindret gult annars rosa
     * @param isBreakable om sann går hindret att skjuta isönder
     */
    public void addObstacle(Pang game, Rectangle rectangle, World world, boolean colorYellow, boolean isBreakable){
        myObstacles.add(new Obstacle(game, rectangle, world, colorYellow, isBreakable));
    }

    /**
     *
     * @return int .. så många av hinderna går att skjuta isönder(för powerUp generering
     */
    public int getDestroyables(){
        int destroyables = 0;
        for(Obstacle o:myObstacles){
            destroyables += o.getDestroyables();
        }
        return destroyables;
    }

    public void update(float dt){

        for(Obstacle o: myObstacles){
            o.update(dt);
            if(o.isDestroyed()){
                myDestroyedObstacles.add(o);//Lägg till i förstörda listan
            }
        }
        for(Obstacle o: myDestroyedObstacles){//Ta bort referenser till förstörda hinder
            myObstacles.remove(o);
        }
        myDestroyedObstacles.clear();//Töm lista
    }
    public void renderer(Batch batch){//För att rita alla skott
        for (Obstacle o: myObstacles){
            o.draw(batch);
        }
    }
}
