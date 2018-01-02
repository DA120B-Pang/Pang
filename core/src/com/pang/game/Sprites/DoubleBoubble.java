package com.pang.game.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pang.game.Pang;

public class DoubleBoubble {
    public DoubleBoubble(World world, Bubble.BubbleState startSize, Bubble.BubbleColor color, Vector2 position, Pang game, float offset, Bubble.BubbleState minSize, boolean isFrozen){
        Vector2 leftPos = new Vector2(position.x-offset, position.y);
        bubbleLeft = new Bubble(world, startSize, color, position, game, false, minSize, isFrozen,true);
        bubbleRight = new Bubble(world, startSize, color, position, game, true, minSize, isFrozen,true);
    }
    public Bubble bubbleLeft;
    public Bubble bubbleRight;
}
