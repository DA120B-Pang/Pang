package com.pang.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.Pang;

import static com.pang.game.Constants.Constants.WORLD_HEIGHT;
import static com.pang.game.Constants.Constants.WORLD_WIDTH;

public class SettingsScreen implements Screen {

    private Pang game;
    private Label instruction;
    private Viewport viewport;
    private Stage stage;
    private BitmapFont instructionFont;
    Label soundVolumeLbl;
    Label musicVolumeLbl;
    Label startLevelLbl;

    private Skin skin;
    private TextButton backButton;

public SettingsScreen(Pang game) {
    this.skin = new Skin(Gdx.files.internal("ui/skin/neon-ui.json"));
    this.skin.remove("font", BitmapFont.class);
    this.skin.add("font", Gdx.files.internal("font/robot/size72dead.fnt"));
    viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
    stage = new Stage(viewport, game.batch);
    this.game = game;

    instructionFont = new BitmapFont(Gdx.files.internal("font/robot/size72dead.fnt"));
    Color instructionFontColor = new Color(Color.WHITE);

    Label settingsLbl = new Label("Settings", new Label.LabelStyle(instructionFont, instructionFontColor));
    settingsLbl.setFontScale(0.4f);

    soundVolumeLbl = new Label(String.format("Sound Vol: %.0f%%", game.soundVolume*100), new Label.LabelStyle(instructionFont, instructionFontColor));
    soundVolumeLbl.setFontScale(0.3f);

    Slider soundSlider = new Slider(0, 1, 0.05f, false, skin);
    soundSlider.setValue(game.soundVolume);
    soundSlider.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            game.soundVolume = soundSlider.getValue();
            soundSlider.setValue(game.soundVolume);
        }
    });
    musicVolumeLbl = new Label(String.format("Music Vol: %.0f%%", game.musicVolume*100), new Label.LabelStyle(instructionFont, instructionFontColor));
    musicVolumeLbl.setFontScale(0.3f);

    Slider musicSlider = new Slider(0, 1, 0.05f, false, skin);
    musicSlider.setValue(game.musicVolume);
    musicSlider.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            game.musicVolume = musicSlider.getValue();
            musicSlider.setValue(game.musicVolume);
        }
    });

    startLevelLbl = new Label(String.format("Start at level: %d", game.hud.startLevel), new Label.LabelStyle(instructionFont, instructionFontColor));
    startLevelLbl.setFontScale(0.3f);

    Slider startLevelSlider = new Slider(1, 7, 1f, false, skin);
    startLevelSlider.setValue(game.hud.startLevel);
    startLevelSlider.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            game.hud.startLevel = (int)startLevelSlider.getValue();
            startLevelSlider.setValue(game.hud.startLevel);
        }
    });


    Table table = new Table();
    table.setFillParent(true);
    table.top();
    table.add(settingsLbl);
    stage.addActor(table);

    Table tableSettings = new Table().padTop(40f).padLeft(50);
    tableSettings.setFillParent(true);
    tableSettings.top().padRight(10f);
    tableSettings.add(soundVolumeLbl).left().expandX();
    tableSettings.add(soundSlider).padRight(10f).expandX();
    tableSettings.row();
    tableSettings.add(musicVolumeLbl).left().expandX();
    tableSettings.add(musicSlider).padRight(10f).expandX();
    tableSettings.row();
    tableSettings.add(startLevelLbl).left().expandX();
    tableSettings.add(startLevelSlider).padRight(10f).expandX();
    stage.addActor(tableSettings);


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
        soundVolumeLbl.setText(String.format("Sound Vol: %.0f%%", game.soundVolume*100));
        musicVolumeLbl.setText(String.format("Music Vol: %.0f%%", game.musicVolume*100));
        startLevelLbl.setText(String.format("Start at level: %d", game.hud.startLevel));
    }

}


