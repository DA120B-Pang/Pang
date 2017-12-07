package com.pang.game.HUD;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.Pang;

import java.util.ArrayList;

import static com.pang.game.Constants.Constants.*;

public class HUD {
    public Stage stage;
    private Viewport viewport;
    private float timeElapsed;
    private int timeLeft;
    private int score;
    private int highScore;
    private int lives;

    private boolean startTimer;
    private Label scoreLbl;
    private Label timeLbl;
    private Label highScoreLbl;
    private Label levelLbl;
    private Label levelNameLbl;
    private Label livesLbl;
    private Label test;
    private BitmapFont fontBottom;
    private BitmapFont fontTop;
    private Group group;
    private Pang game;

    public HUD(Pang game){
        this.game = game;
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        resetHud();
        stage = new Stage();
        fontTop = new BitmapFont(Gdx.files.internal("font/robot/size72.fnt"));
        fontBottom = new BitmapFont(Gdx.files.internal("font/robot/size48.png.fnt"));
        Color colorBottom = new Color(Color.WHITE);
        highScoreLbl = new Label(String.format("High score: %06d", highScore), new Label.LabelStyle(fontBottom, colorBottom));
        scoreLbl = new Label(String.format("Score: %06d", score), new Label.LabelStyle(fontBottom, colorBottom));

        levelNameLbl = new Label("Ballon Pooopern", new Label.LabelStyle(fontBottom, colorBottom));
        levelLbl = new Label("1 - 1", new Label.LabelStyle(fontBottom, colorBottom));

        livesLbl = new Label("Lives:", new Label.LabelStyle(fontBottom, colorBottom));

        Table table = new Table();//Ny tabell
        table.bottom().left();//Tabell mot botten
        table.setFillParent(true);//Gör så att tabellen täcker hela skärmen
        table.add(scoreLbl).uniformX().expandX().padLeft(10f).left();
        table.add(levelNameLbl).uniformX().expandX();
        Table tableSub = new Table();
        //myLives.remove(2);
        //myLives.remove(2);
        tableSub.add(livesLbl).uniformX().expandX().left();
        group = new Group();
        group.setHeight(36f);
        groupAddLife(lives);
        tableSub.add(group).left();
        table.add(tableSub).uniformX().expandX().left();
        table.row();
        table.add(highScoreLbl).expandX().padBottom(10f).padLeft(10f).left();
        table.add(levelLbl).expandX();
        stage.addActor(table);

        Color colorTop = new Color(Color.WHITE);
        timeLbl = new Label(String.format("Time: %03d", timeLeft), new Label.LabelStyle(fontTop, colorTop));
        Table tableTop = new Table();
        tableTop.setFillParent(true);
        tableTop.top().right();
        tableTop.add(timeLbl).padRight(20f).width(270f);
        stage.addActor(tableTop);
    }

    public final void resetHud(){
        score = 0;
        lives = 5;
        timeLeft = 100;
        startTimer = false;
        highScore = 0;// länka till highscore
    }

    public void newLevel(int timeLeft){
        timeElapsed = 0f;
        this.timeLeft = timeLeft;
        startTimer = false;
        timeLbl.setText(String.format("Time: %03d", timeLeft));
    }

    private final void groupAddLife(int lives){

        if(group.getChildren().size<(lives-1)){
            for (int i = group.getChildren().size; i <(lives) ; i++) {
                TextureRegion tmpTexture = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 5, 43, 18, 18);
                Image image = new Image(tmpTexture);
                image.setScaleX(2f);
                image.setScaleY(2f);
                image.setOriginX(image.getOriginX()-(i)*36);
                group.addActor(image);
            }
        }
        else if(group.getChildren().size>(lives-1) && lives>0){
            group.getChildren().pop();
        }
    }
    public void startTimer(){
        startTimer = true;
    }
    public void stopTimer(){
        startTimer = false;
    }

    public void setTimeLeft(float dt){
        if(startTimer) {
            timeElapsed += dt;
            if (timeElapsed > 1 && timeLeft > 0) {
                timeLeft--;
                timeElapsed = 0;
                timeLbl.setText(String.format("Time: %03d", timeLeft));
            }
        }
    }
    public int getLives(){
        return lives;
    }

    public void takeLife(){
        if (lives>0) {
            lives--;
        }
    }
    public void addScore(int score){
        this.score += score;
    }

    public void update(float dt){
        groupAddLife(lives);
        setTimeLeft(dt);
        scoreLbl.setText(String.format("Score: %06d", score));



    }
}
