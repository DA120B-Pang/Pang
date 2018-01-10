package com.pang.game.HUD;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.Pang;

import java.util.ArrayList;

import static com.pang.game.Constants.Constants.*;
import static com.pang.game.Constants.Constants.PowerUp.*;

/**
 * Klass för att göra Head up display som ligger längst ner på spelskärm
 */
public class HUD {
    public Stage stage;
    private Viewport viewport;
    private float timeElapsed;
    private float startUpTimer;
    private int timeLeft;
    private int highScore;
    private int lives;
    private boolean startGame;
    private AssetManager assetManager;
    private int actLevel;
    public int startLevel = 1;
    private boolean startTimer;
    private boolean startUpDone;
    private Label scoreLbl;
    private Label timeLbl;
    private Label highScoreLbl;
    private Label levelLbl;
    private Label levelNameLbl;
    private Label livesLbl;
    private Label getReadyLbl;
    private BitmapFont fontBottom;
    private BitmapFont fontTop;
    private BitmapFont fontGetReady;
    private Group group;
    private Pang game;
    private int startUpSoundMask;
    PowerUp[][] powerUps;
    String[] levelNames;
    private int score = 0;
    private ArrayList<HighScoreData> highScorelist = new ArrayList<>();
    private HighScoreDataSort highScoreDataSort = new HighScoreDataSort();
    private FileReadWriter file = new FileReadWriter();


    public HUD(Pang game, AssetManager assetManager){
        this.assetManager = assetManager;
        this.game = game;
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        fontGetReady = new BitmapFont(Gdx.files.internal("font/robot/size72getReady.fnt"));
        Color colorGetReady = new Color(Color.WHITE);
        getReadyLbl = new Label("Get ready", new Label.LabelStyle(fontGetReady,colorGetReady));
        //Namn på levels
        levelNames = new String[]{  "Balloon Pooopern",
                                    "Die hard Balloon",
                                    "Full Balloon",
                                    "Lake of Doom",
                                    "Balloonatic",
                                    "Nightfall",
                                    "Temple"};
        //resetHud();
        stage = new Stage();
        fontTop = new BitmapFont(Gdx.files.internal("font/robot/size72.fnt"));
        fontBottom = new BitmapFont(Gdx.files.internal("font/robot/size48.png.fnt"));
        Color colorBottom = new Color(Color.WHITE);
        highScoreLbl = new Label(String.format("High score: %06d", highScore), new Label.LabelStyle(fontBottom, colorBottom));
        scoreLbl = new Label(String.format("Score: %06d", score), new Label.LabelStyle(fontBottom, colorBottom));
        actLevel = 1;
        levelNameLbl = new Label(levelNames[0], new Label.LabelStyle(fontBottom, colorBottom));
        levelLbl = new Label("Level "+actLevel, new Label.LabelStyle(fontBottom, colorBottom));

        livesLbl = new Label("Lives:", new Label.LabelStyle(fontBottom, colorBottom));



        Table table = new Table();//Ny tabell
        table.bottom().left();//Tabell mot botten
        table.setFillParent(true);//Gör så att tabellen täcker hela skärmen
        table.add(scoreLbl).uniformX().expandX().padLeft(10f).left();//Aktuell poäng
        table.add(levelNameLbl).uniformX().expandX();//Namn på bana
        Table tableSub = new Table();

        tableSub.add(livesLbl).uniformX().expandX().left();//Lives text framför livs ikoner.
        group = new Group();
        group.setHeight(36f);
        groupAddLife(lives);//Lägger till rätt antal livs ikoner i grupp.
        tableSub.add(group).left();
        table.add(tableSub).uniformX().expandX().left();
        table.row();
        table.add(highScoreLbl).expandX().padBottom(10f).padLeft(10f).left();//Högsta highscore poäng
        table.add(levelLbl).expandX();//Aktuell level nummer
        stage.addActor(table);

        Table tableCenter = new Table();
        tableCenter.setFillParent(true);
        tableCenter.center();
        tableCenter.add(getReadyLbl);
        stage.addActor(tableCenter);

        Color colorTop = new Color(Color.WHITE);
        timeLbl = new Label(String.format("Time: %03d", timeLeft), new Label.LabelStyle(fontTop, colorTop));
        Table tableTop = new Table();
        tableTop.setFillParent(true);
        tableTop.top().right();
        tableTop.add(timeLbl).padRight(20f).width(270f);//Nedräkning av tid.
        stage.addActor(tableTop);
        //Lägg till powerUps här till index 0 = bana 1, index 2 = bana 2-4, index 3 bana 5-resten.
        powerUps = new PowerUp[][]{{null},
                                    {SHEILD, BARBSHOT, STOPTIME},
                                    {LIFE,BARBSHOT,BARBSHOT,DOUBLESHOT,DOUBLESHOT,DOUBLESHOT,SHEILD,SHEILD,STOPTIME,STOPTIME}};


        highScorelist = file.readFile();//Läs in befintlig highscore. Finns ingen skapas ny fil.

    }

    /**
     * Återställer HUD för nytt spel.
     */
    public final void resetHud(){
        score = 0;
        lives = 5;
        if(highScorelist.size()>=1) {
            highScore = highScorelist.get(0).getScore();// länka till highscore
        }
        else{
            highScore = 0;
        }
        highScoreLbl.setText(String.format("High score: %06d", highScore));
        actLevel = startLevel;
    }

    /**
     * Återställer HUD vid ny bana-
     * @param timeLeft hur mkt tid spelaren ska få till att klara level.
     */
    public final void newLevel(int timeLeft){
        timeElapsed = 0f;
        this.timeLeft = timeLeft;
        startTimer = false;
        timeLbl.setText(String.format("Time: %03d", timeLeft));
        startUpDone = false;
        startGame = false;
        startUpSoundMask = 0;
        startUpTimer = 0;
        levelLbl.setText("Level "+actLevel);
        levelNameLbl.setText(levelNames[actLevel-1]);
    }

    /**
     * Lägger till och tar bort livs ikoner från HUD
     * @param lives antal liv spelare har.
     */
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

    /**
     * Startar nedräkning av tid.
     */
    public void startTimer(){
        startTimer = true;
    }

    /**
     * Stoppar nedräkning av tid.
     */
    public void stopTimer(){
        startTimer = false;
    }

    /**
     * Plussar på aktuell level med 1.
     */
    public void levelComplete(){
        actLevel++;
    }

    /**
     *
     * @return int .. returnerar aktuell level.
     */
    public int getLevel(){
        return actLevel;
    }

    /**
     * Startar först nedräkning 3 sekunder innan spel ska startas... sedan Startar nedräkning av speltid.
     * @param dt float .. delta time
     */
    public void setTimeLeft(float dt){
        if(startTimer) {
            if (startUpDone) {//Om 3 sekunders nedräkning är klar ska Speltid räknas ner.
                timeElapsed += dt;
                if (timeElapsed > 1 && timeLeft > 0) {
                    timeLeft--;
                    timeElapsed = 0;
                    timeLbl.setText(String.format("Time: %03d", timeLeft));
                }
            } else {
                float scale = 0;
                startUpTimer += dt;
                if (startUpTimer >= 0 && startUpTimer < 2) {
                    getReadyLbl.setFontScale(1.2f);
                    getReadyLbl.setText("Get ready");
                } else if (startUpTimer >= 2 && startUpTimer < 3) {//Tidsfönster för ljud
                    getReadyLbl.setText("3");
                    if(0 == (startUpSoundMask & 1)) {//Logisk and för att kolla om ljud är uppspelat.
                        assetManager.get("audio/sound/countDown.wav", Sound.class).setVolume(assetManager.get("audio/sound/countDown.wav", Sound.class).play(), 1.0f*game.soundVolume);
                        startUpSoundMask |= 1; // Checka av uppspelning
                    }
                    scale = (3f - startUpTimer) * 1.5f;//Siffra ska krympa ihop
                    if (scale < 0) {
                        scale = 0;
                    }
                    getReadyLbl.setFontScale(scale);
                } else if (startUpTimer >= 3 && startUpTimer < 4) {//Tidsfönster för ljud
                    getReadyLbl.setText("2");
                    if(0 == (startUpSoundMask & 2)) {//Logisk and för att kolla om ljud är uppspelat.
                        assetManager.get("audio/sound/countDown.wav", Sound.class).setVolume(assetManager.get("audio/sound/countDown.wav", Sound.class).play(), 1.0f*game.soundVolume);
                        startUpSoundMask |= 2;// Checka av uppspelning
                    }
                    scale = (4f - startUpTimer) * 1.5f;//Siffra ska krympa ihop
                    if (scale < 0) {
                        scale = 0;
                    }
                    getReadyLbl.setFontScale(scale);
                } else if (startUpTimer >= 4 && startUpTimer < 5) {//Tidsfönster för ljud
                    getReadyLbl.setText("1");
                    if(0 == (startUpSoundMask & 4)) {//Logisk and för att kolla om ljud är uppspelat.
                        assetManager.get("audio/sound/countDown.wav", Sound.class).setVolume(assetManager.get("audio/sound/countDown.wav", Sound.class).play(), 1.0f*game.soundVolume);
                        startUpSoundMask |= 4;// Checka av uppspelning
                    }
                    scale = (5f - startUpTimer) * 1.5f;//Siffra ska krympa ihop
                    if (scale < 0) {
                        scale = 0;
                    }
                    getReadyLbl.setFontScale(scale);
                } else {
                    getReadyLbl.setText("");
                    startGame = true;
                    startUpDone = true;
                }
            }
        }
    }

    /**
     *
     * @return Berättar om spel ska startas och Dude/bubblor låsas upp.
     */
    public boolean startGame(){
        return startGame;
    }

    /**
     * Kommunikation att spel är startat
     */
    public void gameIsStarted(){
        startGame = false;
    }

    /**
     *
     * @return Antal liv kvar
     */
    public int getLives(){
        return lives;
    }

    /**
     * Tar bort ett liv.
     */
    public void takeLife(){
        if (lives>0) {
            lives--;
        }
    }

    /**
     * Ljud när extraliv plockas upp.
     */
    public void addLife(){
        if (lives<5) {
            lives++;
            game.assetManager.get("audio/sound/powerUpLife.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpLife.wav", Sound.class).play(), 0.6f*game.soundVolume);
        }
        else{
            game.assetManager.get("audio/sound/powerUpLifeFull.wav", Sound.class).setVolume(game.assetManager.get("audio/sound/powerUpLifeFull.wav", Sound.class).play(), 0.6f*game.soundVolume);
        }
    }

    /**
     * Metod för att lägga till poäng.
     * @param score poäng att lägga till.
     */
    public void addScore(int score){
        this.score += score;
    }

    /**
     *
     * @return aktuell poäng.
     */
    public int getScore(){
        return score;
    }

    /**
     *
     * @return Highscore list.
     */
    public ArrayList<HighScoreData> getHighScorelist(){
        return highScorelist;
    }

    /**
     *  Metod för att lägga till highscore
     * @param name namn att lägga till i highscore
     */
    public void addToHighScore(String name){
        if (highScorelist.size()>=10){//Max 10 platser
            highScorelist.remove(9);
        }

        highScorelist.add(new HighScoreData(name, score));//Lägger till Highscore
        highScorelist.sort(highScoreDataSort);// Soterar lista
        file.writeFile(highScorelist);//Spara highscore
    }

    /**
     *
     * @return om poäng räcker till highscore.
     */
    public boolean isHighScore(){
        boolean retVal = false;
        if (highScorelist.size()<10){
            retVal = true;
        }
        else if(highScorelist.get(9).getScore() < score){
            retVal = true;
        }
        return retVal;
    }

    /**
     * För tidsbonus.
     * @return tid kvar på bana.
     */
    public int getTimeLeft(){
        return timeLeft;
    }

    /**
     *
     * @return tid har gått ut.
     */
    public boolean isTimeOut(){
        return timeLeft == 0;
    }

    /**
     * Uppdatera HUD
     * @param dt deltatid
     */
    public void update(float dt){
        groupAddLife(lives);
        setTimeLeft(dt);
        scoreLbl.setText(String.format("Score: %06d", score));
    }

    /**
     *
     * @return PowerUp array för aktuell level.
     */
    public PowerUp[] getPowerUps(){
        PowerUp[] tmpPowerUp = null;
        if(actLevel<2){
            tmpPowerUp = powerUps[0];
        }
        else if(actLevel<5){
            tmpPowerUp = powerUps[1];
        }
        else if(actLevel>=5){
            tmpPowerUp = powerUps[2];
        }
        return tmpPowerUp;
    }
}


