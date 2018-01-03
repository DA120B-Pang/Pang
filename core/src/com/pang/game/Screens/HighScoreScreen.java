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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.Constants.Constants;
import com.pang.game.HUD.HighScoreData;
import com.pang.game.Pang;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.pang.game.Constants.Constants.WORLD_HEIGHT;
import static com.pang.game.Constants.Constants.WORLD_WIDTH;

public class HighScoreScreen implements Screen {
    private Label nameTopLbl;
    private Label scoreTopLbl;
    private Label dateTopLbl;
    private Label[] nameLbl;
    private Label[] scoreLbl;
    private Label[] dateLbl;
    private BitmapFont scoreFont;
    private Label highScoreLbl;
    private Stage stage;
    private Viewport viewport;
    private TextButton backButton;
    private Skin skin;



    private Viewport viewPort;
    public HighScoreScreen(Pang game){
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);



        this.skin = new Skin(Gdx.files.internal("ui/skin/neon-ui.json"));

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

        scoreFont = new BitmapFont(Gdx.files.internal("font/robot/size72dead.fnt"));
        Color highScoreColor = new Color(Color.WHITE);


        highScoreLbl = new Label("HighScore", new Label.LabelStyle(scoreFont,highScoreColor));
        highScoreLbl.setFontScale(0.5f);


        ArrayList<HighScoreData> highscorelist = game.hud.getHighScorelist();

        nameLbl = new Label[10];
        scoreLbl = new Label[10];
        dateLbl = new Label[10];

        float fontScaleSmall = 0.2f;

        nameTopLbl = new Label("  Name" , new Label.LabelStyle(scoreFont,highScoreColor));
        nameTopLbl.setFontScale(fontScaleSmall);
        scoreTopLbl = new Label("Score" , new Label.LabelStyle(scoreFont,highScoreColor));
        scoreTopLbl.setFontScale(fontScaleSmall);
        dateTopLbl = new Label("Date" , new Label.LabelStyle(scoreFont,highScoreColor));
        dateTopLbl.setFontScale(fontScaleSmall);

        for (int i = 0; i <highscorelist.size() ; i++) {
            nameLbl[i] = new Label(String.format("%2d. %s", i+1, highscorelist.get(i).getName()) , new Label.LabelStyle(scoreFont,highScoreColor));
            nameLbl[i].setFontScale(fontScaleSmall);
            scoreLbl[i] = new Label(String.format("%06d", highscorelist.get(i).getScore()) , new Label.LabelStyle(scoreFont,highScoreColor));
            scoreLbl[i].setFontScale(fontScaleSmall);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy MM dd");
            String text = highscorelist.get(i).getDate().format(formatter);
            dateLbl[i] = new Label(text, new Label.LabelStyle(scoreFont,highScoreColor));
            dateLbl[i].setFontScale(fontScaleSmall);
        }




        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(-7f);
        table.add(highScoreLbl);
        stage.addActor(table);

        Table tableScore = new Table();
        tableScore.setFillParent(true);
        tableScore.top().padTop(38f);
        tableScore.add(nameTopLbl);
        tableScore.add(scoreTopLbl);
        tableScore.add(dateTopLbl);
        tableScore.row();
        for (int i = 0; i < highscorelist.size() ; i++) {
            tableScore.add(nameLbl[i]).padRight(5f).uniformX().left();
            tableScore.add(scoreLbl[i]).padRight(5f);
            tableScore.add(dateLbl[i]).padRight(5f);
            tableScore.row();
        }
        stage.addActor(tableScore);

        stage.addActor(backButton);


    }
    @Override
    public void show() {

    }
    public void handleInput(float dt){

    }
    public void update(float dt){
        stage.act(dt);

        handleInput(dt);

    }
    @Override
    public void render(float dt) {
        //Logix
        update(dt);
        //Sätt skärmen svart
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
}
