package com.pang.game.Screens;

import com.badlogic.gdx.Gdx;
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
import com.pang.game.Pang;
import sun.tools.jconsole.Tab;

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

    instruction = new Label("INSTRUCTION", new Label.LabelStyle(instructionFont, instructionFontColor ));
    instruction.setFontScale(0.4f);

    Label move = new Label("   -->", new Label.LabelStyle(instructionFont, instructionFontColor));
    move.setFontScale(0.3f);

    Label move1 = new Label("  <--", new Label.LabelStyle(instructionFont, instructionFontColor));
    move1.setFontScale(0.3f);

    Label moveExp = new Label("Move Right", new Label.LabelStyle(instructionFont, instructionFontColor));
    moveExp.setFontScale(0.3f);

    Label move1Exp = new Label("Move Left", new Label.LabelStyle(instructionFont, instructionFontColor));
    move1Exp.setFontScale(0.3f);

    Label shoot = new Label("Z", new Label.LabelStyle(instructionFont, instructionFontColor));
    shoot.setFontScale(0.3f);

    Label shootExp = new Label("Shoot", new Label.LabelStyle(instructionFont, instructionFontColor));
    shootExp.setFontScale(0.3f);

    Label explain = new Label("Destroy all the bubbles", new Label.LabelStyle(instructionFont, instructionFontColor));
    explain.setFontScale(0.3f);

    Label explain2 = new Label("Do not get hit!", new Label.LabelStyle(instructionFont, instructionFontColor));
    explain2.setFontScale(0.3f);

    Label empty = new Label(" ", new Label.LabelStyle(instructionFont, instructionFontColor));
    empty.setFontScale(0.3f);

    Table table = new Table();
    table.setFillParent(true);
    table.top();
    table.add(instruction);
    stage.addActor(table);

    Table table1 = new Table();
    table1.setFillParent(true);
    table1.top().left();
    table1.row();
    table1.add(empty);
    table1.row();
    table1.add(empty);
    table1.row();
    table1.add(move);
    table1.row();
    table1.add(move1);
    table1.row();
    table1.add(shoot);
    stage.addActor(table1);

    Table table2 = new Table();
    table2.setFillParent(true);
    table2.top().right();
    table2.row();
    table2.add(empty);
    table2.row();
    table2.add(moveExp);
    table2.row();
    table2.add(move1Exp);
    table2.row();
    table2.add(shootExp);
    stage.addActor(table2);


    Table table3 = new Table();
    table3.setFillParent(true);
    table3.bottom();
    table3.add(explain);
    table3.row();
    table3.add(explain2);
    stage.addActor(table3);

    dispose();
}




    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin();
        this.skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
        this.skin.add("default-font", game.font);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));

        initButtons();
    }

    private void initButtons() {
        backButton = new TextButton("BACK", skin, "default");
        backButton.setSize(90,30);
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


