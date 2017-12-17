package com.pang.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
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
    private int timeBonusScore;
    private Animation animation;
    private Sprite spriteDude;
    private Sprite spriteBubbleDude;
    private Label levelComplete;
    private OrthographicCamera camera;
    private Viewport viewPort;
    private int loopTalk;
    private boolean isTalking;

    public LevelCompleteScreen(Pang game){
        if(!game.assetManager.isLoaded("audio/music/theEmpire.ogg", Music.class)) {
            game.assetManager.load("audio/music/theEmpire.ogg", Music.class);
            game.assetManager.finishLoading();
        }

        Array<TextureRegion> frames = new Array<>();
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 4, 105, 64, 64));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 70, 105, 64, 64));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 136, 105, 64, 64));
        frames.add(new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 70, 105, 64, 64));
        animation = new Animation(0.2f, frames);

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

        levelComplete = new Label("Level "+game.hud.getLevel()+" complete", new Label.LabelStyle(scoreFont,scoreColor));
        levelComplete.setFontScale(0.7f);

        timer = -5f;
        game.hud.levelComplete();
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

        spriteDude.setRegion(getRegion(dt));
        handleInput(dt);

    }
    @Override
    public void render(float dt) {
        //Logix
        update(dt);
        //Sätt skärmen svart
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(camera.combined);//.combined);
        stage.draw();
        game.batch.begin();
        spriteDude.draw(game.batch);
        if(isTalking) {
            spriteBubbleDude.draw(game.batch);
        }
        game.batch.end();



        if(restartGame){
            musicStop();
            game.setScreen(new Level(game));
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
    }
}