package com.pang.game.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private ArrayList<Image> myLives;
    private float timeElapsed;
    private int timeLeft;
    private int score;
    private int highScore;

    private boolean startTimer;
    private Label scoreLbl;
    private Label timeLbl;
    private Label highScoreLbl;
    private Label levelLbl;
    private Label levelNameLbl;
    private Label livesLbl;
    private BitmapFont fontBottom;
    private BitmapFont fontTop;

    public HUD(Pang game){
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        myLives = new ArrayList<>();
        for (int i = 0; i <4 ; i++) {
            TextureRegion tmpTexture = new TextureRegion(game.assetManager.get("sprites/sprites.pack",TextureAtlas.class).findRegion("Player All2"), 5, 43, 18, 18);
            Image tmpImg = new Image(tmpTexture);
            tmpImg.setScaleX(2f);
            tmpImg.setScaleY(2f);
            myLives.add(tmpImg);
        }
        startTimer = false;
        timeElapsed  = 0f;
        timeLeft = 100;
        score = 0;
        highScore = 0;// länka till highscore

        stage = new Stage();
        fontTop = new BitmapFont(Gdx.files.internal("font/robot/size48.png.fnt"));
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
        table.add(livesLbl).uniformX().expandX().center();
        table.row();
        table.add(highScoreLbl).expandX().padBottom(10f).padLeft(10f).left();
        table.add(levelLbl);
        table.add(myLives.get(0),myLives.get(1));
        stage.addActor(table);

    }
    public final void resetHud(){
        score = 0;
        timeElapsed = 0;
    }
}
