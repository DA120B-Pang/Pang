package com.pang.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

import java.util.ArrayList;

import static com.pang.game.Constants.Constants.WORLD_HEIGHT;
import static com.pang.game.Constants.Constants.WORLD_WIDTH;

/**
 * Klass för skärm för input av higscore
 */
public class HighScoreInputScreen implements Screen {
    private Pang game;
    private Label nameLbl;
    private BitmapFont scoreFont;
    private Label highScoreInputLbl;
    private Label messageLbl;
    private Stage stage;
    private Viewport viewport;
    private TextButton okBtn;
    private TextButton skipBtn;
    private Skin skin;
    private String name ="";
    private float messageTimer = 0f;

    public HighScoreInputScreen(Pang game){
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        this.game = game;

        this.skin = new Skin(Gdx.files.internal("ui/skin/neon-ui.json"));

        okBtn = new TextButton("OK", skin, "default");
        okBtn.setSize(70,42);
        okBtn.getLabel().setFontScale(0.25f);
        okBtn.setPosition(Constants.WORLD_WIDTH / 2 - (okBtn.getWidth()/2)+ 50, 20);
        okBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(name == "") {
                    setMessageLbl(3, 0);
                }
                else{
                    game.hud.addToHighScore(name);
                    game.setScreen(new StartingScreen(game));
                    dispose();
                }
            }
        });

        skipBtn = new TextButton("SKIP", skin, "default");
        skipBtn.setSize(70,42);
        skipBtn.getLabel().setFontScale(0.25f);
        skipBtn.setPosition(Constants.WORLD_WIDTH / 2 - (skipBtn.getWidth()/2) - 50, 20);
        skipBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));
                dispose();
            }
        });


        scoreFont = new BitmapFont(Gdx.files.internal("font/robot/size72dead.fnt"));
        Color highScoreColor = new Color(Color.WHITE);


        highScoreInputLbl = new Label("New highscore!!", new Label.LabelStyle(scoreFont,highScoreColor));
        highScoreInputLbl.setFontScale(0.4f);

        nameLbl = new Label("", new Label.LabelStyle(scoreFont,highScoreColor));
        nameLbl.setFontScale(0.3f);

        messageLbl = new Label("Input your name and press OK button\nor press SKIP button to cancel.", new Label.LabelStyle(scoreFont,highScoreColor));
        messageLbl.setFontScale(0.3f);

        ArrayList<HighScoreData> highscorelist = game.hud.getHighScorelist();



        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.add(highScoreInputLbl);
        stage.addActor(table);

        Table tableName = new Table();
        tableName.setFillParent(true);
        tableName.top().padTop(100f);
        tableName.add(nameLbl);
        tableName.row();
        tableName.add(messageLbl);
        stage.addActor(tableName);

        stage.addActor(okBtn);
        stage.addActor(skipBtn);


    }
    @Override
    public void show() {

    }

    public void update(float dt){
        String tmp = getInput();//Kolla keyboard input.
        if (tmp != null){
            if(tmp.equalsIgnoreCase("1")){//Otillåtet tecken intryckt.
                setMessageLbl(1, dt);
            }
            else if(tmp.equalsIgnoreCase("99")){//Backspace
                if (name.length()>1) {
                    name = name.substring(0, name.length() - 1);
                }
                else{
                    name = "";
                }
            }
            else if(name.length()>11){//Max storlek uppnådd(max 12 tecken)
                setMessageLbl(2, dt);
            }
            else {
                name += tmp;//Lägger till tecken.
            }
        }
        nameLbl.setText(name);
        setMessageLbl(0,dt);

        stage.act(dt);
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

    /**
     * Meddelande till användare.
     * @param order vilket meddelande ska visas
     * @param dt delta tid
     */
    private void setMessageLbl(int order, float dt){
        if(messageTimer>0){
            messageTimer -= dt;
        }
        else {
            switch (order) {
                case 1:
                    messageLbl.setText("A-Z, Space and Backspace are allowed!");
                    messageTimer = 3f;
                    break;
                case 2:
                    messageLbl.setText("Maximum length 12 characters!");
                    messageTimer = 3f;
                    break;
                case 3:
                    messageLbl.setText("Must enter name or SKIP!");
                    messageTimer = 3f;
                    break;
                    default:
                        messageLbl.setText("Input your name and press OK button\nor press SKIP button to cancel.");


            }
        }
    }

    /**
     * Inläsning av keyboard input.
     * @return String
     */
    private String getInput(){
        String resultat = null;
        if(Gdx.input.isKeyJustPressed(Input.Keys.A))
            resultat = "A";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.B))
            resultat = "B";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.C))
            resultat = "C";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.D))
            resultat = "D";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            resultat = "E";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.F))
            resultat = "F";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.G))
            resultat = "G";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.H))
            resultat = "H";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.I))
            resultat = "I";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.J))
            resultat = "J";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.K))
            resultat = "K";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.L))
            resultat = "L";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.M))
            resultat = "M";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.N))
            resultat = "N";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.O))
            resultat = "O";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.P))
            resultat = "P";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.Q))
            resultat = "Q";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.R))
            resultat = "R";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.S))
            resultat = "S";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.T))
            resultat = "T";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.U))
            resultat = "U";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.V))
            resultat = "V";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.W))
            resultat = "W";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.X))
            resultat = "X";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.Y))
            resultat = "Y";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.Z))
            resultat = "Z";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            resultat = " ";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE))
            resultat = "99";
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))
            resultat = "1";

        return resultat;
    }
}
