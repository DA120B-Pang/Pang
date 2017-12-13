package com.pang.game.Creators;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.pang.game.Pang;
import com.pang.game.Sprites.Obstacle;

import java.util.ArrayList;

public class ObstacleHandler {
    private ArrayList<Obstacle> myObstacles;
    private ArrayList<Obstacle> myDestroyedObstacles;

    public ObstacleHandler () {
        myObstacles = new ArrayList<>();
        myDestroyedObstacles = new ArrayList<>();
    }

    public void addObstacle(Pang game, Rectangle rectangle, World world, Boolean colorYellow){
        myObstacles.add(new Obstacle(game, rectangle, world, colorYellow));
    }

    public void update(float dt){

        for(Obstacle o: myObstacles){
            o.update(dt);
            if(o.isDestroyed()){
                myDestroyedObstacles.add(o);
            }
        }
        for(Obstacle o: myDestroyedObstacles){
            myObstacles.remove(o);
        }
        myDestroyedObstacles.clear();
    }
    public void renderer(Batch batch){//För att rita alla skott
        for (Obstacle o: myObstacles){
            o.draw(batch);
        }
    }
}
