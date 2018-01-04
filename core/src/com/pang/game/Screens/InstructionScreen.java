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

public class InstructionScreen implements Screen {

    private Pang game;
    private Label instruction;
    private Viewport viewport;
    private Stage stage;
    private BitmapFont instructionFont;
    private Skin skin;
    private TextButton backButton;

public InstructionScreen (Pang game) {

    viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
    stage = new Stage(viewport, game.batch);
    this.game = game;

    instructionFont = new BitmapFont(Gdx.files.internal("font/robot/size72dead.fnt"));
    Color instructionFontColor = new Color(Color.WHITE);

    instruction = new Label("INSTRUCTIONS", new Label.LabelStyle(instructionFont, instructionFontColor ));
    instruction.setFontScale(0.4f);

    Texture texture = new Texture(Gdx.files.internal("images/keyboard_key_left.png"));
    Image left = new Image();
    left.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));

    texture = new Texture(Gdx.files.internal("images/keyboard_key_right.png"));
    Image right = new Image();
    right.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));

    texture = new Texture(Gdx.files.internal("images/keyboard_key_z.png"));
    Image z = new Image();
    z.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));

    Label moveRightLbl = new Label("Move Right", new Label.LabelStyle(instructionFont, instructionFontColor));
    moveRightLbl.setFontScale(0.3f);

    Label move1LeftLbl = new Label("Move Left", new Label.LabelStyle(instructionFont, instructionFontColor));
    move1LeftLbl.setFontScale(0.3f);

    Label shootExp = new Label("Shoot", new Label.LabelStyle(instructionFont, instructionFontColor));
    shootExp.setFontScale(0.3f);

    Label explain = new Label("Destroy all the bubbles", new Label.LabelStyle(instructionFont, instructionFontColor));
    explain.setFontScale(0.3f);

    Label explain2 = new Label("Do not get hit!", new Label.LabelStyle(instructionFont, instructionFontColor));
    explain2.setFontScale(0.3f);

    Table table = new Table();
    table.setFillParent(true);
    table.top();
    table.add(instruction);
    stage.addActor(table);

    Table tableControls = new Table().padTop(40f);
    tableControls.setFillParent(true);
    tableControls.top();
    tableControls.add(right).width(16).height(16).padRight(10f);
    tableControls.add(moveRightLbl);
    tableControls.row();
    tableControls.add(left).width(16).height(16).padRight(10f);
    tableControls.add(move1LeftLbl);
    tableControls.row();
    tableControls.add(z).width(16).height(16).padRight(10f);
    tableControls.add(shootExp);
    stage.addActor(tableControls);


    Table table3 = new Table();
    table3.setFillParent(true);
    table3.bottom();
    table3.add(explain);
    table3.row();
    table3.add(explain2).padBottom(50f);
    stage.addActor(table3);

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
                game.setScreen(new MainMenu(game));
                dispose();
            }
        });

        stage.addActor(backButton);

        TextButton powerUpButton = new TextButton("PowerUps", skin, "default");
        powerUpButton.setSize(100,40);
        powerUpButton.getLabel().setFontScale(0.25f);
        powerUpButton.setPosition(284,0);
        powerUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new InstructionPupScreen(game));
                dispose();
            }
        });

        stage.addActor(powerUpButton);
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


