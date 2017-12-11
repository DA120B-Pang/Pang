package com.pang.game.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class DoubleBoubble {
    public DoubleBoubble(World world, Bubble.BubbleState startSize, Bubble.BubbleColor color, Vector2 position, AssetManager assetManager, float offset, Bubble.BubbleState minSize){
        Vector2 leftPos = new Vector2(position.x-offset, position.y);
        bubbleLeft = new Bubble(world, startSize, color, position, assetManager, false, minSize);
        bubbleRight = new Bubble(world, startSize, color, position, assetManager, true, minSize);
    }
    public Bubble bubbleLeft;
    public Bubble bubbleRight;
}
