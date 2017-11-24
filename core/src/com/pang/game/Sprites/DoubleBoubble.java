package com.pang.game.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class DoubleBoubble {
    public DoubleBoubble(World world, Bubble.BubbleState state, Bubble.BubbleColor color, Vector2 position, AssetManager assetManager, float offset){
        Vector2 leftPos = new Vector2(position.x-offset, position.y);
        bubbleLeft = new Bubble(world, state, color, position, assetManager, false);
        bubbleRight = new Bubble(world, state, color, position, assetManager, true);
    }
    public Bubble bubbleLeft;
    public Bubble bubbleRight;
}
