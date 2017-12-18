package com.pang.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.Pang;
import com.pang.game.Sprites.FireWork;

import static com.pang.game.Constants.Constants.WORLD_HEIGHT;
import static com.pang.game.Constants.Constants.WORLD_WIDTH;

public class GameCompleteScreen implements Screen {
    private Pang game;
    private boolean restartGame;
    private Label timeBonus;
    private BitmapFont scoreFont;
    private Label scoreTotal;
    private Stage stage;
    private float timer;
    private int timeBonusScore;
    private Animation animation;
    private Sprite spriteDude;
    private Sprite spriteBubbleDude;
    private Label levelComplete;
    private OrthographicCamera camera;
    private Viewport viewPort;
    private int loopTalk;
    private boolean isTalking;
    private FireWork fireWork1;
    private FireWork fireWork2;
    private FireWork fireWork3;
    private FireWork fireWork4;
    private FireWork fireWork5;
    private Vector2 min;
    private Vector2 max;

    public GameCompleteScreen(Pang game){

        game.assetManager.load("audio/music/Royal_Entrance_Loop.ogg", Music.class);
        game.assetManager.load("rockets/rockets.pack", TextureAtlas.class);
        game.assetManager.finishLoading();


        Array<TextureRegion> frames = new Array<>();
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 4, 105, 64, 64));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 70, 105, 64, 64));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 136, 105, 64, 64));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 70, 105, 64, 64));
        animation = new Animation(0.2f, frames);
        frames.clear();

        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 0, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 192, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 384, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 576, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 768, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 0, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 192, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 384, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 576, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 768, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 0, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 192, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 384, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 576, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 768, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 0, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 192, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 384, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 576, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 768, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 0, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 192, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 384, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket1"), 576, 768, 192, 192));

        min = new Vector2(50,WORLD_HEIGHT/1.5f);
        max = new Vector2(WORLD_WIDTH-100, WORLD_HEIGHT-64);

        fireWork1 = new FireWork(new Animation( 0.1f,frames),min,max);

        frames.clear();

        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 0, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 192, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 384, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 576, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 768, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 0, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 192, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 384, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 576, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 768, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 0, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 192, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 384, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 576, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 768, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 0, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 192, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 384, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 576, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 768, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 0, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 192, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 384, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket2"), 576, 768, 192, 192));

        fireWork2 = new FireWork(new Animation( 0.1f,frames),min,max);

        frames.clear();

        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 0, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 192, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 384, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 576, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 768, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 0, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 192, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 384, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 576, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 768, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 0, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 192, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 384, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 576, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 768, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 0, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 192, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 384, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 576, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 768, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 0, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 192, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 384, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 576, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 768, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 0, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 192, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 384, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 576, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 768, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 0, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 192, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 384, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 576, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket3"), 768, 1344, 192, 192));

        fireWork3 = new FireWork(new Animation( 0.05f,frames),min,max);

        frames.clear();

        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 0, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 192, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 384, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 576, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 768, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 0, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 192, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 384, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 576, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 768, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 0, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 192, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 384, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 576, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 768, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 0, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 192, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 384, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 576, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 768, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 0, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 192, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 384, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 576, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 768, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 0, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 192, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 384, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 576, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 768, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 0, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 192, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 384, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 576, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket4"), 768, 1344, 192, 192));

        fireWork4 = new FireWork(new Animation( 0.1f,frames),min,max);

        frames.clear();

        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 0, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 192, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 384, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 576, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 768, 0, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 0, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 192, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 384, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 576, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 768, 192, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 0, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 192, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 384, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 576, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 768, 384, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 0, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 192, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 384, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 576, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 768, 576, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 0, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 192, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 384, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 576, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 768, 768, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 0, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 192, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 384, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 576, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 768, 1152, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 0, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 192, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 384, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 576, 1344, 192, 192));
        frames.add(new TextureRegion(game.assetManager.get("rockets/rockets.pack",TextureAtlas.class).findRegion("rocket5"), 768, 1344, 192, 192));

        fireWork5 = new FireWork(new Animation( 0.1f,frames),min,max);

        frames.clear();

        camera = new OrthographicCamera();
        viewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(((viewPort.getWorldWidth())/2), ((viewPort.getWorldHeight())/2), 0);
        stage = new Stage(viewPort, game.batch);
        this.game = game;
        restartGame = false;
        loopTalk = 0;
        isTalking =false;

        timeBonusScore = game.hud.getTimeLeft()*10;

        scoreFont = new BitmapFont(Gdx.files.internal("font/robot/size72getReady.fnt"));
        Color scoreColor = new Color(Color.WHITE);
        timeBonus = new Label(String.format("TimeBonus: %06d", timeBonusScore), new Label.LabelStyle(scoreFont,scoreColor));
        timeBonus.setFontScale(0.5f);
        game.hud.addScore(timeBonusScore);
        scoreTotal = new Label(String.format("Total score: %06d", game.hud.getScore()), new Label.LabelStyle(scoreFont,scoreColor));
        scoreTotal.setFontScale(0.5f);

        levelComplete = new Label("Victory!!!", new Label.LabelStyle(scoreFont,scoreColor));
        levelComplete.setFontScale(0.7f);

        timer = -5f;

        spriteDude = new Sprite();
        spriteDude.setBounds(0 ,0,64,64);
        spriteBubbleDude = new Sprite();
        spriteBubbleDude.setBounds(0,0,150,98);

        spriteBubbleDude.setRegion(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("talkBubble"), 0, 0, 299, 220));
        spriteDude.setPosition((viewPort.getWorldWidth()/2f - spriteDude.getWidth()/2),(viewPort.getWorldHeight()/1.7f - spriteDude.getHeight()/2));
        spriteBubbleDude.setPosition(spriteDude.getX()+40,spriteDude.getY()+15);
        Table tableTop = new Table();
        tableTop.setFillParent(true);
        tableTop.top().padTop(5);
        tableTop.add(levelComplete);
        stage.addActor(tableTop);


        Table table = new Table();
        table.setFillParent(true);
        table.bottom().padBottom(10);
        table.add(timeBonus);
        table.row();
        table.add(scoreTotal);
        stage.addActor(table);
        musicStart();
    }
    @Override
    public void show() {

    }
    private TextureRegion getRegion(float dt){
        TextureRegion textureRegion = new TextureRegion();
        timer += dt;
        if(timer>0f) {
            textureRegion = (TextureRegion) animation.getKeyFrame(timer, false);
            isTalking = true;
            if (animation.isAnimationFinished(timer)){
                loopTalk++;
                if(loopTalk>4){
                    timer = -7;
                    isTalking = false;
                    loopTalk = 0;
                }
                else{
                    timer = 0;
                }
            }
        }else{
            textureRegion = (TextureRegion) animation.getKeyFrame(0, false);
        }
        return textureRegion;
    }

    private void musicStart(){
        game.assetManager.get("audio/music/Royal_Entrance_Loop.ogg",Music.class).setLooping(true);
        game.assetManager.get("audio/music/Royal_Entrance_Loop.ogg",Music.class).setVolume(0.7f);
        game.assetManager.get("audio/music/Royal_Entrance_Loop.ogg",Music.class).play();

    }
    private void musicStop(){
        game.assetManager.get("audio/music/Royal_Entrance_Loop.ogg",Music.class).stop();
    }

    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            restartGame = true;
        }
    }
    public void update(float dt){

        spriteDude.setRegion(getRegion(dt));
        handleInput(dt);
        fireWork1.update(dt);
        fireWork2.update(dt);
        fireWork3.update(dt);
        fireWork4.update(dt);
        fireWork5.update(dt);
    }
    @Override
    public void render(float dt) {
        //Logix
        update(dt);
        //Sätt skärmen svart
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(camera.combined);//.combined);
        game.batch.begin();
        fireWork1.draw(game.batch);
        fireWork2.draw(game.batch);
        fireWork3.draw(game.batch);
        fireWork4.draw(game.batch);
        fireWork5.draw(game.batch);
        game.batch.end();
        stage.draw();
        game.batch.begin();
        spriteDude.draw(game.batch);
        if(isTalking) {
            spriteBubbleDude.draw(game.batch);
        }
        game.batch.end();



        if(restartGame){
            musicStop();
            game.setScreen(new StartingScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width,height);
        camera.position.set(((viewPort.getWorldWidth())/2), ((viewPort.getWorldHeight())/2), 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.assetManager.unload("audio/music/Royal_Entrance_Loop.ogg");
        game.assetManager.unload("rockets/rockets.pack");
        game.assetManager.finishLoading();
        fireWork1.getTexture().dispose();
        fireWork2.getTexture().dispose();
        fireWork3.getTexture().dispose();
        fireWork4.getTexture().dispose();
        fireWork5.getTexture().dispose();
        System.out.println(game.assetManager.isLoaded("audio/music/Royal_Entrance_Loop.ogg"));
    }
}