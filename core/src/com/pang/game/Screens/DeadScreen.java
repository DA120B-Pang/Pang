package com.pang.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.Pang;

import static com.pang.game.Constants.Constants.WORLD_HEIGHT;
import static com.pang.game.Constants.Constants.WORLD_WIDTH;

/**
 * Klass för skärm som visas när Dude dött men har fler liv kvar
 */
public class DeadScreen implements Screen {
    private Pang game;
    private boolean restartGame;
    private Label youDiedLbl;
    private BitmapFont youDiedFont;
    private Label pressToRestart;
    private Stage stage;
    private float timer;
    private Viewport viewport;

    /**
     *
     * @param game referens till Pang objektet
     * @param timeOut om döden beror på att tiden gick ut
     */
    public DeadScreen(Pang game, boolean timeOut){
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        this.game = game;
        restartGame = false;

        youDiedFont = new BitmapFont(Gdx.files.internal("font/robot/size72dead.fnt"));

        Color youdiedColor = new Color(Color.WHITE);
        String text = timeOut? "Ran out of time!!":"You died!!";
        youDiedLbl = new Label(text, new Label.LabelStyle(youDiedFont,youdiedColor));
        youDiedLbl.setFontScale(0.7f);
        pressToRestart = new Label("Press space to restart level", new Label.LabelStyle(youDiedFont,youdiedColor));
        pressToRestart.setFontScale(0.4f);

        timer = 0f;

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(youDiedLbl);
        stage.addActor(table);

        Table tablebottom = new Table();
        tablebottom.bottom().padBottom(10f);
        tablebottom.setFillParent(true);
        tablebottom.add(pressToRestart);

        stage.addActor(tablebottom);
    }
    @Override
    public void show() {

    }
    public void handleInput(float dt){
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                restartGame = true;
            }
    }
    public void update(float dt){


        handleInput(dt);

        timer += dt;
        if(timer >= 0) {//Texten ska blinka med 2 sekunders intervall

            pressToRestart.setText("");

            if (timer >= 1.0f) {
                pressToRestart.setText("Press space to restart level");
                timer = -2f;
            }
        }
    }
    @Override
    public void render(float dt) {
        //Logix
        update(dt);
        //Sätt skärmen svart
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

        if(restartGame){
            game.setScreen(new LevelScreen(game));
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