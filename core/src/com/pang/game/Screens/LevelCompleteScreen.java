package com.pang.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.Pang;

import static com.pang.game.Constants.Constants.WORLD_HEIGHT;
import static com.pang.game.Constants.Constants.WORLD_WIDTH;

public class LevelCompleteScreen implements Screen {
    private Pang game;
    private boolean restartGame;
    private Label timeBonus;
    private BitmapFont scoreFont;
    private Label scoreTotal;
    private Stage stage;
    private float timer;
    private Viewport viewport;
    private int timeBonusScore;
    private Animation animation;
    private Sprite sprite;
    private Label levelComplete;





    private Viewport viewPort;
    public LevelCompleteScreen(Pang game){
        game.assetManager.load("audio/music/theEmpire.ogg", Music.class);
        game.assetManager.finishLoading();


        Array<TextureRegion> frames = new Array<>();
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 4, 105, 64, 64));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 70, 105, 64, 64));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 136, 105, 64, 64));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 70, 105, 64, 64));
        animation = new Animation(0.2f, frames);


        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        this.game = game;
        restartGame = false;
        game.hud.levelComplete();
        timeBonusScore = game.hud.getTimeLeft()*10;

        scoreFont = new BitmapFont(Gdx.files.internal("font/robot/size72.fnt"));
        Color scoreColor = new Color(Color.WHITE);
        timeBonus = new Label(String.format("TimeBonus: %06d", timeBonusScore), new Label.LabelStyle(scoreFont,scoreColor));
        timeBonus.setFontScale(0.5f);
        game.hud.addScore(timeBonusScore);
        scoreTotal = new Label(String.format("Total score: %06d", game.hud.getScore()), new Label.LabelStyle(scoreFont,scoreColor));
        scoreTotal.setFontScale(0.5f);

        levelComplete = new Label("Level complete", new Label.LabelStyle(scoreFont,scoreColor));
        levelComplete.setFontScale(0.7f);

        timer = 0f;
        sprite = new Sprite();
        sprite.setBounds(viewport.getWorldWidth()/2-32,viewport.getScreenHeight()/4,64,64);
        System.out.println(viewport.getWorldWidth()/5+" "+sprite.getOriginX());
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
        timer += dt;
        return (TextureRegion) animation.getKeyFrame(timer,true);
    }

    private void musicStart(){
        game.assetManager.get("audio/music/theEmpire.ogg",Music.class).setLooping(true);
        game.assetManager.get("audio/music/theEmpire.ogg",Music.class).setVolume(0.7f);
        game.assetManager.get("audio/music/theEmpire.ogg",Music.class).play();

    }
    private void musicStop(){
        game.assetManager.get("audio/music/theEmpire.ogg",Music.class).stop();
    }
    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            restartGame = true;
        }
    }
    public void update(float dt){

        sprite.setRegion(getRegion(dt));
        handleInput(dt);

    }
    @Override
    public void render(float dt) {
        //Logix
        update(dt);
        //Sätt skärmen svart
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        sprite.draw(game.batch);
        game.batch.end();

        stage.draw();

        if(restartGame){
            musicStop();
            switch(game.hud.getLevel()) {
                case 1:
                    game.setScreen(new Level1(game));
                    break;
                case 2:

                    break;
            }
            game.assetManager.get("audio/music/nighttideWaltz.ogg",Music.class).stop();
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

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
    }
}