package com.pang.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.Pang;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.pang.game.Constants.Constants.WORLD_HEIGHT;
import static com.pang.game.Constants.Constants.WORLD_WIDTH;

/**
 * Klass för skärm som visas när Dude dör och inte har några liv kvar
 */
public class GameOverScreen implements Screen {
    private Sprite sprite;
    private Pang game;
    private OrthographicCamera orthographicCamera;
    private boolean restartGame;
    private Viewport viewPort;

    /**
     *
     * @param game referens till Pang objekt
     */
    public GameOverScreen(Pang game) {
        this.game = game;
        restartGame = false;
        sprite = new Sprite();
        sprite.setBounds(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        sprite.setRegion(new Texture(Gdx.files.internal("images/gameOver.png")));
        orthographicCamera = new OrthographicCamera();
        //Sätt storlek på viewport
        viewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, orthographicCamera);
        orthographicCamera.position.set((viewPort.getWorldWidth() / 2), (viewPort.getWorldHeight() / 2), 0);
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            restartGame = true;
        }
    }

    public void update(float dt) {
        handleInput(dt);
        orthographicCamera.update();

    }

    @Override
    public void render(float dt) {
        //Logix
        update(dt);
        //Sätt skärmen svart

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(orthographicCamera.combined);
        game.batch.begin();
        sprite.draw(game.batch);
        game.batch.end();
        if (restartGame) {//Space har tryckts på
            if(game.hud.isHighScore()){//kolla om poäng räcker till highscore
                game.setScreen(new HighScoreInputScreen(game));
            }
            else {
                game.setScreen(new StartingScreen(game));
            }
            game.assetManager.get("audio/music/nighttideWaltz.ogg", Music.class).stop();
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
        orthographicCamera.position.set((viewPort.getWorldWidth() / 2), (viewPort.getWorldHeight() / 2), 0);
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
