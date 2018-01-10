package com.pang.game.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pang.game.Pang;

/**
 * Class för att kunna returnera två bubblor när en bubbla går i sönder
 */
public class DoubleBoubble {

    /**
     *
     * @param world referens till box2d värld
     * @param startSize storlek bubbla startar sitt liv på
     * @param color bubblans färg... barn och barbarn har samma färg
     * @param position Position att skapas på
     * @param game referens till Pang Objekt
     * @param offset hur långt ifrån varandra bubblorna skapas
     * @param minSize Storlek där bubblan slutar att föröka sig vid sönderskjutning
     * @param isFrozen kontroll om powerUp stopptime är aktiv när bubblan bildas
     */
    public DoubleBoubble(World world, Bubble.BubbleState startSize, Bubble.BubbleColor color, Vector2 position, Pang game, float offset, Bubble.BubbleState minSize, boolean isFrozen){
        Vector2 leftPos = new Vector2(position.x-offset, position.y);
        bubbleLeft = new Bubble(world, startSize, color, position, game, false, minSize, isFrozen,true);
        bubbleRight = new Bubble(world, startSize, color, position, game, true, minSize, isFrozen,true);
    }
    public Bubble bubbleLeft;
    public Bubble bubbleRight;
}
