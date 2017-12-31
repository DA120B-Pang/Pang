package com.pang.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pang.game.Constants.Constants;
import com.pang.game.Pang;

import static com.badlogic.gdx.Gdx.app;


public class   MainMenu implements Screen {

    private Pang game;

    private Stage stage;
    private Skin skin;

    private BitmapFont menuFont;
    private Label mainMenu;

    private TextButton buttonPlay, buttonExit, buttonHighScore, buttonCredits, buttonInstruct;

    private ShapeRenderer shapeRenderer;


    public MainMenu(final Pang game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, new OrthographicCamera()));
        this.shapeRenderer = new ShapeRenderer();

        menuFont = new BitmapFont(Gdx.files.internal("font/robot/size72dead.fnt"));
        Color menuFontColor = new Color(Color.WHITE);
        mainMenu = new Label("Main Menu", new Label.LabelStyle(menuFont, menuFontColor));
        mainMenu.setFontScale(0.4f);

        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.add(mainMenu);
        stage.addActor(table);



    }

    @Override
    public void show() {
        System.out.println("MENU");
        Gdx.input.setInputProcessor(stage);

        this.skin = new Skin(Gdx.files.internal("ui/skin/neon-ui.json"));
        /*this.skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
        this.skin.add("default-font", game.font);
        this.skin.load(Gdx.files.internal("ui/skin/sgx-ui.json"));*/
        this.skin.remove("font", BitmapFont.class);
        this.skin.add("font", Gdx.files.internal("font/robot/size72dead.fnt"));

        initButtons();


    }

    private void initButtons() {
        float buttonWidth = 140;
        float buttonHeight = 42;
        float fontScale = 0.25f;
        buttonPlay = new TextButton("PLAY", skin, "default");
        buttonPlay.setSize(buttonWidth,buttonHeight);
        buttonPlay.getLabel().setFontScale(fontScale);
        buttonPlay.setPosition(Constants.WORLD_WIDTH / 2 - buttonPlay.getWidth() / 2,180);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.hud.resetHud();
                game.setScreen(new LevelScreen(game));
                dispose();
            }
        });

        buttonHighScore = new TextButton("HIGHSCORE", skin, "default");
        buttonHighScore.setSize(buttonWidth, buttonHeight);
        buttonHighScore.getLabel().setFontScale(fontScale);
        buttonHighScore.setPosition(Constants.WORLD_WIDTH / 2 - buttonHighScore.getWidth() / 2, 140);
        /*buttonPlay.addListener(new ClickListener() {
            @Override
        public void clicked(InputEvent event, float x, float y) {
            game.setScreen(new (game));
        }*/
        //});

        buttonInstruct = new TextButton("INSTRUCTIONS", skin, "default");
        buttonInstruct.setSize(buttonWidth, buttonHeight);
        buttonInstruct.getLabel().setFontScale(fontScale);
        buttonInstruct.setPosition(Constants.WORLD_WIDTH / 2 - buttonInstruct.getWidth() / 2,100);
        buttonInstruct.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new InstructionScreen(game));
                dispose();
            }
        });

        buttonCredits = new TextButton("CREDITS", skin, "default");
        buttonCredits.setSize(buttonWidth, buttonHeight);
        buttonCredits.getLabel().setFontScale(fontScale);
        buttonCredits.setPosition(Constants.WORLD_WIDTH / 2 - buttonCredits.getWidth() / 2,60);
        buttonCredits.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CreditScreen(game));
                dispose();
            }
        });


        buttonExit = new TextButton("EXIT", skin, "default");
        buttonExit.setSize(buttonWidth,buttonHeight);
        buttonExit.getLabel().setFontScale(fontScale);
        buttonExit.setPosition(Constants.WORLD_WIDTH / 2 - buttonExit.getWidth() / 2,20);

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.exit();
            }
        });







        stage.addActor(buttonPlay);
        stage.addActor(buttonExit);
        stage.addActor(buttonCredits);
        stage.addActor(buttonHighScore);
        stage.addActor(buttonInstruct);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        game.batch.begin();
        game.font.draw(game.batch, "SCREEN: MAIN MENU", 20,20   );
        game.batch.end();

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
        stage.dispose();

    }

    public void update(float delta) {
        stage.act(delta);
    }
}


