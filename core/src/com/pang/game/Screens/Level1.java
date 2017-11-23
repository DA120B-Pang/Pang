package com.pang.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import com.pang.game.Creators.ConstructLevel;
import com.pang.game.Pang;
import com.pang.game.Sprites.Bubble;
import com.pang.game.Sprites.Dude;

import java.util.ArrayList;

import static com.pang.game.Constants.Constants.*;
import static com.pang.game.Sprites.Bubble.BubbleColor.*;
import static com.pang.game.Sprites.Bubble.BubbleState.*;


public class Level1 implements Screen {

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
    private float gameOverTimer;

    public Level1(Pang game){
        this.game = game;
        game.batch.flush();
        gameOverTimer = 0;
        endMusicStarted = false;
        //Ladda musik till assetmanagern
        game.assetManager.load("audio/music/overworld.ogg", Music.class);
        game.assetManager.load("audio/music/nighttideWaltz.ogg", Music.class);
        game.assetManager.finishLoading();
        game.assetManager.get("audio/music/overworld.ogg",Music.class).setLooping(true);
        game.assetManager.get("audio/music/overworld.ogg",Music.class).setVolume(0.1f);
        game.assetManager.get("audio/music/overworld.ogg",Music.class).play();

        box2DDebugRenderer = new Box2DDebugRenderer();

        orthographicCamera = new OrthographicCamera();
        //Sätt storlek på viewport  och skalera  då box2d har enheten meter så delar vi med pixels per meter
        viewPort = new FitViewport(((WORLD_WIDTH))/PPM,(WORLD_HEIGHT)/PPM, orthographicCamera);
        viewPort.apply();
        System.out.println(viewPort.getWorldWidth()+" "+viewPort.getWorldHeight());
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
        //Boll
        myBubbles.add(new Bubble(world, XLARGE, GREEN, new Vector2(250,200), game.assetManager,true));
        myBubbles.add(new Bubble(world, LARGE, BLUE, new Vector2(250,200), game.assetManager,false));
        myBubbles.add(new Bubble(world, MEDIUM, RED, new Vector2(250,200), game.assetManager,true));
        myBubbles.add(new Bubble(world, SMALL, GREEN, new Vector2(250,200), game.assetManager,false));
        myBubbles.add(new Bubble(world, XSMALL, BLUE, new Vector2(250,200), game.assetManager,true));
}

    @Override
    public void show() {

    }

    public void handleInput(float dt){

    }
    private void musicEnd(){
        game.assetManager.get("audio/music/nighttideWaltz.ogg",Music.class).setVolume(0.1f);
        game.assetManager.get("audio/music/nighttideWaltz.ogg",Music.class).play();
        game.assetManager.get("audio/music/overworld.ogg",Music.class).stop();
        endMusicStarted = true;
    }

    private void update(float dt){
        game.aspectRatio = Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
        //Uppdatera bubble
        if(dude.isDudeDead()){
            gameOverTimer += dt;
            if(!endMusicStarted){
                musicEnd();
            }
            for (Bubble b : myBubbles) {
                //b.stopUpdate();
            }
        }
        else {
            for (Bubble b : myBubbles) {
                b.update(dt);
        }
        }
        //Uppdatera dude
        dude.update(dt);
        //Kolla input
        handleInput(dt);
        //uppdatera kameran
        orthographicCamera.update();
        //Bara det som visas ska renderas
        orthogonalTiledMapRenderer.setView(orthographicCamera);
        world.step(1/60f, 6,2);
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
        //Rita bubbla
        for(Bubble b: myBubbles){
            b.draw(game.batch);
        }
        game.batch.end();
        if(gameOverTimer>4){
            game.setScreen(new GameOverScreen(game));
            dispose();

        }



    }

     @Override
   public void resize(int width, int height) {
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
    public void dispose() {
        box2DDebugRenderer.dispose();
        world.dispose();
        tiledMap.dispose();
        orthogonalTiledMapRenderer.dispose();
    }

    public World getWorld() {
        return world;
    }
}
