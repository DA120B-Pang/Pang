package com.pang.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pang.game.Constants.Constants;
import com.pang.game.Pang;

import static com.badlogic.gdx.Gdx.app;


public class MainMenu implements Screen {

    private Pang game;

    private Stage stage;
    private Skin skin;

    private TextButton buttonPlay, buttonExit, buttonHighScore, buttonCredits;

    private ShapeRenderer shapeRenderer;


    public MainMenu(final Pang game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, new OrthographicCamera()));
        this.shapeRenderer = new ShapeRenderer();



    }

    @Override
    public void show() {
        System.out.println("MENU");
        Gdx.input.setInputProcessor(stage);

        this.skin = new Skin();
        this.skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
        this.skin.add("default-font", game.font);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));

        initButtons();


    }

    private void initButtons() {
        buttonPlay = new TextButton("PLAY", skin, "default");
        buttonPlay.setSize(70,20);
        buttonPlay.setPosition(Constants.WORLD_WIDTH / 2 - buttonPlay.getWidth() / 2,180);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.hud.resetHud();
                game.setScreen(new Level(game));
                dispose();
            }
                               });


        buttonExit = new TextButton("EXIT", skin, "default");
        buttonExit.setSize(70,20);
        buttonExit.setPosition(Constants.WORLD_WIDTH / 2 - buttonExit.getWidth() / 2,50);

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.exit();
            }
        });

        buttonHighScore = new TextButton("HIGHSCORE", skin, "default");
        buttonHighScore.setSize(70, 20);
        buttonHighScore.setPosition(Constants.WORLD_WIDTH / 2 - buttonHighScore.getWidth() / 2, 130);


        buttonCredits = new TextButton("CREDITS", skin, "default");
        buttonCredits.setSize(70, 20);
        buttonCredits.setPosition(Constants.WORLD_WIDTH / 2 - buttonCredits.getWidth() / 2,80);


        stage.addActor(buttonPlay);
        stage.addActor(buttonExit);
        stage.addActor(buttonCredits);
        stage.addActor(buttonHighScore);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        game.batch.begin();
        game.font.draw(game.batch, "SCREEN: MAIN MENU", 20,20   );
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();

    }

    public void update(float delta) {
        stage.act(delta);
    }
}


