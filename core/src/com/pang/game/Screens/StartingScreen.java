package com.pang.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.*;
import com.pang.game.Pang;

import static com.pang.game.Constants.Constants.*;

public class StartingScreen implements Screen {
    private Sprite sprite;
    private Pang game;
    private OrthographicCamera orthographicCamera;
    private float screenTimer;

    private Viewport viewPort;
    public StartingScreen(Pang game){
        this.game = game;
        screenTimer = 0f;
        sprite = new Sprite();
        sprite.setBounds(0,0,WORLD_WIDTH,WORLD_HEIGHT);
        sprite.setRegion(new Texture(Gdx.files.internal("images/pang3.png")));
        orthographicCamera = new OrthographicCamera();
        //Sätt storlek på viewport  och skalera
        viewPort = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT, orthographicCamera);
        System.out.println(sprite.getWidth()+"  "+ sprite.getHeight());
        orthographicCamera.position.set((viewPort.getWorldWidth()/2 ), (viewPort.getWorldHeight()/2), 0);
    }
    @Override
    public void show() {

    }
    public void update(float dt){
        orthographicCamera.update();
        screenTimer+=dt;
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
        if(screenTimer>2){
            dispose();
            game.setScreen(new MainMenu(game));

        }
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width,height);
        orthographicCamera.position.set((viewPort.getWorldWidth()/2 ), (viewPort.getWorldHeight()/2), 0);
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
