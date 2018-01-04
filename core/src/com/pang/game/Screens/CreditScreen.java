package com.pang.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.Constants.Constants;
import com.pang.game.Pang;

import static com.pang.game.Constants.Constants.WORLD_HEIGHT;
import static com.pang.game.Constants.Constants.WORLD_WIDTH;

public class CreditScreen implements Screen {

    private Pang game;
    private Label credits;
    private Viewport viewport;
    private float timer;
    private Stage stage;
    private BitmapFont creditFont;
    private Label name;
    private Label name1;
    private Label name2;
    private Label name3;
    private Label name4;


    private Skin skin;

    private TextButton backButton;


    public CreditScreen (Pang game) {

        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        this.game = game;

        creditFont = new BitmapFont(Gdx.files.internal("font/robot/size72dead.fnt"));
        Color creditFontColor = new Color(Color.WHITE);

        credits = new Label("CREDITS", new Label.LabelStyle(creditFont, creditFontColor));
        credits.setFontScale(0.4f);

        name = new Label("Filip Fredlund", new Label.LabelStyle(creditFont, creditFontColor));
        name.setFontScale(0.3f);

        name1 = new Label("Oskar Karstrom", new Label.LabelStyle(creditFont, creditFontColor));
        name1.setFontScale(0.3f);

        name2 = new Label("Christian Zaar", new Label.LabelStyle(creditFont, creditFontColor));
        name2.setFontScale(0.3f);

        name3 = new Label("Elias Magnusson", new Label.LabelStyle(creditFont, creditFontColor));
        name3.setFontScale(0.3f);

        name4 = new Label("Alex Hsiung", new Label.LabelStyle(creditFont, creditFontColor));
        name4.setFontScale(0.3f);

        Label name5 = new Label(" ", new Label.LabelStyle(creditFont, creditFontColor));
        name5.setFontScale(0.3f);

        timer = 0f;

        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.add(credits);
        stage.addActor(table);

        Table padtable1 = new Table();
        table.setFillParent(true);
        table.row();
        table.padTop(0.8f);
        table.add(name5);
        table.row();
        table.add(name);
        table.row();
        table.add(name1);
        table.row();
        table.add(name2);
        table.row();
        table.add(name3);
        table.row();
        table.add(name4);
        stage.addActor(padtable1);




        // padtop flytta ner lite från toppen



        dispose();
    }


    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        /*this.skin = new Skin();
        this.skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
        this.skin.add("default-font", game.font);*/
        this.skin = new Skin(Gdx.files.internal("ui/skin/neon-ui.json"));

        initButtons();
    }

    private void initButtons() {
        backButton = new TextButton("BACK", skin, "default");
        backButton.setSize(90,40);
        backButton.getLabel().setFontScale(0.25f);
        backButton.setPosition(0,0);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));
                dispose();
            }
        });

        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        update(delta);
        stage.draw();


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


    }
    public void update(float dt) {

        stage.act(dt);

    }
}
