package com.pang.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.Pang;

import static com.pang.game.Constants.Constants.WORLD_HEIGHT;
import static com.pang.game.Constants.Constants.WORLD_WIDTH;

public class InstructionPupScreen implements Screen {

    private Pang game;
    private Viewport viewport;
    private Stage stage;
    private BitmapFont instructionFont;
    private Skin skin;
    private TextButton backButton;

public InstructionPupScreen(Pang game) {

    viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
    stage = new Stage(viewport, game.batch);
    this.game = game;

    instructionFont = new BitmapFont(Gdx.files.internal("font/robot/size72dead.fnt"));
    Color instructionFontColor = new Color(Color.WHITE);

    Label powerUpsLbl = new Label("Powerups", new Label.LabelStyle(instructionFont, instructionFontColor));
    powerUpsLbl.setFontScale(0.4f);

    Label extraLifeLbl = new Label("Adds an extra life\n(max 5 lives)", new Label.LabelStyle(instructionFont, instructionFontColor));
    extraLifeLbl.setFontScale(0.3f);

    Label hulkLbl = new Label("Protection against one hit\n from bubble", new Label.LabelStyle(instructionFont, instructionFontColor));
    hulkLbl.setFontScale(0.3f);

    Label timeLbl = new Label("Stops time for 10 seconds", new Label.LabelStyle(instructionFont, instructionFontColor));
    timeLbl.setFontScale(0.3f);

    Label doubleLbl = new Label("Enables double shoot", new Label.LabelStyle(instructionFont, instructionFontColor));
    doubleLbl.setFontScale(0.3f);

    Label barbLbl = new Label("Enables shot that sticks\n to roof for 2 seconds", new Label.LabelStyle(instructionFont, instructionFontColor));
    barbLbl.setFontScale(0.3f);

    TextureRegion lifeRegion = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 5, 43, 16, 16);
    Image lifeImg = new Image();
    lifeImg.setDrawable(new TextureRegionDrawable(lifeRegion));
    lifeImg.setBounds(0,0,16,16);

    TextureRegion doubleRegion = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), 208, 39, 16, 16);
    Image doubleImg = new Image();
    doubleImg.setDrawable(new TextureRegionDrawable(doubleRegion));
    doubleImg.setBounds(0,0,16,16);

    TextureRegion barbRegion = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), 191, 39, 16, 16);
    Image barbImg = new Image();
    barbImg.setDrawable(new TextureRegionDrawable(barbRegion));
    barbImg.setBounds(0,0,16,16);

    TextureRegion sheildRegion = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), 280, 59, 16, 16);
    Image sheildImg = new Image();
    sheildImg.setDrawable(new TextureRegionDrawable(sheildRegion));
    sheildImg.setBounds(0,0,16,16);

    TextureRegion timeRegion = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("pangStuff"), 262, 59, 16, 16);
    Image timeImg = new Image();
    timeImg.setDrawable(new TextureRegionDrawable(timeRegion));
    timeImg.setBounds(0,0,16,16);

    Label explain = new Label("Destroy all the bubbles", new Label.LabelStyle(instructionFont, instructionFontColor));
    explain.setFontScale(0.3f);

    Label explain2 = new Label("Do not get hit!", new Label.LabelStyle(instructionFont, instructionFontColor));
    explain2.setFontScale(0.3f);

    Table table = new Table();
    table.setFillParent(true);
    table.top();
    table.add(powerUpsLbl);
    stage.addActor(table);

    Table tablePowerUp = new Table().padTop(40f).padLeft(50);
    tablePowerUp.setFillParent(true);
    tablePowerUp.top().padRight(10f);
    tablePowerUp.add(lifeImg).padRight(10f);
    tablePowerUp.add(extraLifeLbl).left();
    tablePowerUp.row();
    tablePowerUp.add(sheildImg).padRight(10f);
    tablePowerUp.add(hulkLbl).left();
    tablePowerUp.row();
    tablePowerUp.add(timeImg).padRight(10f);
    tablePowerUp.add(timeLbl).left();
    tablePowerUp.row();
    tablePowerUp.add(doubleImg).padRight(10f);
    tablePowerUp.add(doubleLbl).left();
    tablePowerUp.row();
    tablePowerUp.add(barbImg).padRight(10f);
    tablePowerUp.add(barbLbl).left();
    stage.addActor(tablePowerUp);

    dispose();
}




    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
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
                game.setScreen(new InstructionScreen(game));
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


