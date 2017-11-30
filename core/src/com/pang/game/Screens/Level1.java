package com.pang.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.ContactHandling.ContactHandler;
import com.pang.game.Creators.BubbleHandler;
import com.pang.game.Creators.ConstructLevel;
import com.pang.game.Creators.ShotHandler;
import com.pang.game.Pang;
import com.pang.game.Sprites.Bubble;
import com.pang.game.Sprites.DoubleBoubble;
import com.pang.game.Sprites.Dude;
import com.pang.game.Sprites.Shot;


import java.util.ArrayList;

import static com.pang.game.Constants.Constants.*;
import static com.pang.game.Sprites.Bubble.BubbleColor.*;
import static com.pang.game.Sprites.Bubble.BubbleState.*;


public class Level1 implements Screen {

    private BubbleHandler bubbleHandler;
    private ShotHandler shotHandler;
    private Pang game;
    private OrthographicCamera orthographicCamera;

    private Viewport viewPort;

    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;


    private Dude dude;
    private boolean endMusicStarted;
    private ArrayList<Bubble> myBubbles = new ArrayList<>();
    private ArrayList<Bubble> myDestoyedBubbles = new ArrayList<>();
    private float gameOverTimer;
    public Level1(Pang game){


        bubbleHandler = new BubbleHandler();
        this.game = game;

        game.hud.startTimer();
        gameOverTimer = 0;
        endMusicStarted = false;
        //Ladda musik till assetmanagern
        game.assetManager.load("audio/music/overworld.ogg", Music.class);
        game.assetManager.load("audio/music/nighttideWaltz.ogg", Music.class);
        game.assetManager.finishLoading();
        game.assetManager.get("audio/music/overworld.ogg",Music.class).setLooping(true);
        game.assetManager.get("audio/music/overworld.ogg",Music.class).setVolume(0.7f);
        game.assetManager.get("audio/music/overworld.ogg",Music.class).play();

        box2DDebugRenderer = new Box2DDebugRenderer();

        orthographicCamera = new OrthographicCamera();
        //Sätt storlek på viewport  och skalera  då box2d har enheten meter så delar vi med pixels per meter
        viewPort = new FitViewport(((WORLD_WIDTH))/PPM,(WORLD_HEIGHT)/PPM, orthographicCamera);
        viewPort.apply();
        //Kamera för att visa spelyta
        orthographicCamera.position.set(((viewPort.getWorldWidth())/2), ((viewPort.getWorldHeight()+300)/2), 0);

        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("maps/level1.tmx");
        //Ladda karta och skalera då box2d har enheten meter så delar vi med pixels per meter
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/PPM);


        //Ny box2d värld dragningskraft på -5 (på jorden är det -9.8) inga object ska vara i sleep då alla syns hela tiden
        world = new World(new Vector2(0f,-2.5f), true);
        //Registrera conntactlistener
        world.setContactListener(new ContactHandler());
        //Debug renderer så vi kan se våra kroppars linjer. (dessa syns inte annars) ska bara användas under utveckling
        box2DDebugRenderer = new Box2DDebugRenderer();
        //Läs in väggar golv och tak från "Tiled" karta
        try{
            ConstructLevel.createWallFloorRoof(world, tiledMap, 1);
        }catch (Exception e){
            System.out.println(e);
        }
        //Skapa dude
        dude = new Dude(world,game.assetManager,new Vector2(150f,70f));
        //Bollar till start
        bubbleHandler.addBubble(new Bubble(world, XLARGE, GREEN, new Vector2(250,200), game.assetManager,true));
        bubbleHandler.addBubble(new Bubble(world, LARGE, BLUE, new Vector2(250,200), game.assetManager,false));
        bubbleHandler.addBubble(new Bubble(world, MEDIUM, RED, new Vector2(250,200), game.assetManager,true));
        bubbleHandler.addBubble(new Bubble(world, SMALL, GREEN, new Vector2(250,200), game.assetManager,false));
        bubbleHandler.addBubble(new Bubble(world, XSMALL, GREEN, new Vector2(250,200), game.assetManager,true));

        shotHandler = new ShotHandler(world, dude.dudeBody);
}

    @Override
    public void show() {

    }

    private void musicEnd(){//Musik till död dude
        game.assetManager.get("audio/music/nighttideWaltz.ogg",Music.class).setVolume(0.8f);
        game.assetManager.get("audio/music/nighttideWaltz.ogg",Music.class).setLooping(true);
        game.assetManager.get("audio/music/nighttideWaltz.ogg",Music.class).play();
        game.assetManager.get("audio/music/overworld.ogg",Music.class).stop();
        endMusicStarted = true;//Kvittera att musik är startad
    }

    private void update(float dt){
        //uppdatera kameran
        orthographicCamera.update();
        //Bara det som visas ska renderas
        orthogonalTiledMapRenderer.setView(orthographicCamera);
        world.step(1/60f, 6,2);
        if(dude.isDudeDead()){
            gameOverTimer += dt;
            if(!endMusicStarted){
                musicEnd();
                game.hud.takeLife();
            }
            game.hud.stopTimer();
            bubbleHandler.setToSleep();//Stoppa bubblor
        }
        //Updatera bubblor
        bubbleHandler.update(dt);
        shotHandler.update(dt);

        //Uppdatera dude
        dude.update(dt);
        game.hud.update(dt);
    }
    @Override
    public void render(float dt) {

        //Logix
        update(dt);
        //Sätt skärmen svart
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render map
        orthogonalTiledMapRenderer.render();
        //Debug linjer för box2d
        box2DDebugRenderer.render(world,orthographicCamera.combined);
        game.batch.setProjectionMatrix(orthographicCamera.combined);

        game.batch.begin();

        //Rita dude
        dude.draw(game.batch);

        //Rita shot
        //shot.draw(game.batch);

        //Rita bubbla
        bubbleHandler.renderer(game.batch);


        game.batch.end();

        if(gameOverTimer>4) {
            if (game.hud.getLives() == 0) {//Tillfällig lösning för att byta skärm
                game.setScreen(new GameOverScreen(game));
                dispose();
            }
            else{

                game.hud.newLevel(100);
                game.assetManager.get("audio/music/nighttideWaltz.ogg",Music.class).stop();
                dispose();
                game.setScreen(new Level1(game));
            }
        }
        if(bubbleHandler.getBubbles()==0){//Tillfällig lösning för att byta skärm
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        game.hud.stage.draw();


    }

     @Override
   public void resize(int width, int height) {//Vid ändring av Storlek på fönster ska Viewport och kamera uppdateras
        viewPort.update(width,height);
         orthographicCamera.position.set((viewPort.getWorldWidth()/2), (viewPort.getWorldHeight()/2), 0);
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
    public void dispose() {//Kasta resurser vid skärmbyte
        box2DDebugRenderer.dispose();
        world.dispose();
        tiledMap.dispose();
        orthogonalTiledMapRenderer.dispose();
    }

    public World getWorld() {
        return world;
    }
}
