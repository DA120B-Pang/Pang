package com.pang.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Klass för att skapa fyrverkeri på GameCompleteScreen
 */
public class FireWork extends Sprite {
    private Random random;
    private Vector2 min;
    private Vector2 max;
    private Vector2 position;
    private float animationTimer;
    private Animation animation;
    private float randomWait;

    /**
     *
     * @param animation fyrverkeri animation
     * @param min minimum position
     * @param max maximum position
     */
    public FireWork(Animation animation, Vector2 min, Vector2 max) {
        this.animation = animation;
        this.min = min;
        this.max = max;
        animationTimer = 0;
        random = new Random();
        position = getPosition();

        setBounds(0, 0, 64, 64);
        setPosition( position.x, position.y);
        System.out.println(position);
        randomWait = random.nextFloat();
    }

    public void update(float dt){
        animationTimer += dt;
        setRegion((TextureRegion)animation.getKeyFrame(animationTimer,false));
        if(animation.isAnimationFinished(animationTimer)){
            int bound = 56+random.nextInt(15);
            setBounds(0, 0, bound, bound);
            position = getPosition();
            setPosition( position.x, position.y);
            randomWait -= dt;
            if(randomWait<=0) {
                animationTimer = 0;
                randomWait = random.nextFloat();
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    /**
     *
     * @return Vector2 .. Ny random position mellan min - max position
     */
    private final Vector2 getPosition(){
        int minY = Math.round(min.y);
        int minX = Math.round(min.x);
        int maxY = Math.round(max.y);
        int maxX = Math.round(max.x);
        int y = minY+random.nextInt(maxY-minY);
        int x = minX+random.nextInt(maxX-minX);
        return new Vector2(x,y);
    }
}
