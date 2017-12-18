package com.pang.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pang.game.ContactHandling.ContactHandler;
import com.pang.game.Creators.BubbleHandler;
import com.pang.game.Creators.ObstacleHandler;
import com.pang.game.Pang;
import com.pang.game.Sprites.Bubble;
import com.pang.game.Sprites.Dude;
import static com.pang.game.Constants.Constants.*;
import static com.pang.game.Constants.Constants.FLOOR_OR_WHOT.*;
import static com.pang.game.Sprites.Bubble.BubbleColor.*;
import static com.pang.game.Sprites.Bubble.BubbleState.*;


public class Level implements Screen {

    private BubbleHandler bubbleHandler;
    private ObstacleHandler obstacleHandler;
    private Pang game;
    private OrthographicCamera orthographicCamera;
    private Viewport viewPort;
    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Sprite mapBottom;
    private Dude dude;
    private boolean endMusicStarted;
    private float gameOverTimer;
    private boolean timeOut;
    private int lastLevelNbr;

    public Level(Pang game){

        obstacleHandler = new ObstacleHandler();
        bubbleHandler = new BubbleHandler(game);
        this.game = game;
        timeOut = false;

        gameOverTimer = 0;
        endMusicStarted = false;
        loadLevelMusic();

        mapBottom = new Sprite();
        mapBottom.setRegion(new Texture(Gdx.files.internal("maps/LevelBottom.png")));
        mapBottom.setBounds(0,0,384/PPM,56/PPM);

        orthographicCamera = new OrthographicCamera();

        //Sätt storlek på viewport  och skalera  då box2d har enheten meter så delar vi med pixels per meter
        viewPort = new FitViewport(((WORLD_WIDTH))/PPM,(WORLD_HEIGHT)/PPM, orthographicCamera);
        viewPort.apply();
        //Kamera för att visa spelyta
        orthographicCamera.position.set(((viewPort.getWorldWidth())/2), ((viewPort.getWorldHeight())/2), 0);

        tmxMapLoader = new TmxMapLoader();

        lastLevelNbr = 3;
        loadMap();
        //Ladda karta och skalera då box2d har enheten meter så delar vi med pixels per meter
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/PPM);


        //Ny box2d värld dragningskraft på -5 (på jorden är det -9.8) inga object ska vara i sleep då alla syns hela tiden
        world = new World(new Vector2(0f,-2.5f), true);
        //Registrera conntactlistener
        world.setContactListener(new ContactHandler());
        //Debug renderer så vi kan se våra kroppars linjer. (dessa syns inte annars) ska bara användas under utveckling
        box2DDebugRenderer = new Box2DDebugRenderer();

        try{//Läs in väggar golv och tak från "Tiled" karta
            createWallFloorRoof(1);
        }
        catch (Exception e){
            System.out.println(e);
        }

        try {//Bollar till start från "Tiled" karta
            createBubbles(2);
        }
        catch(Exception e){
            System.out.println(e);
        }

        try{//Hinder från "Tiled" karta
            createObstacles(3);
        }
        catch (Exception e){
            System.out.println(e);
        }

        try{//Skapa dude
            createDude(4);
        }
        catch (Exception e){
            System.out.println(e);
        }

        game.hud.newLevel(100);
        game.hud.startTimer();
        dude.setToSleep();
        bubbleHandler.setToSleep();
}

    @Override
    public void show() {

    }

    private void update(float dt){
        //uppdatera kameran
        orthographicCamera.update();
        //Bara det som visas ska renderas
        orthogonalTiledMapRenderer.setView(orthographicCamera);
        world.step(1/60f, 6,2);
        if(game.hud.isTimeOut() && !dude.isDudeDead()){
            dude.dudeDie();
            timeOut = true;
        }
        if(dude.isDudeDead()){
            gameOverTimer += dt;
            if(!endMusicStarted){
                musicEnd();
                game.hud.takeLife();
            }
            game.hud.stopTimer();
            bubbleHandler.setToSleep();//Stoppa bubblor
        }
        obstacleHandler.update(dt);
        //Updatera bubblor
        bubbleHandler.update(dt, game.hud);

        //Uppdatera dude
        dude.update(dt);

        game.hud.update(dt);

        if(game.hud.startGame()){
            dude.setToAwake();
            bubbleHandler.setToAwake();
            game.hud.gameIsStarted();
            musicStart();
        }
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
        //box2DDebugRenderer.render(world,orthographicCamera.combined);
        game.batch.setProjectionMatrix(orthographicCamera.combined);//.combined);

        game.batch.begin();

        dude.drawShot(game.batch);

        obstacleHandler.renderer(game.batch);
        //Rita bubbla
        bubbleHandler.renderer(game.batch);
        mapBottom.draw(game.batch);
        //Rita dude
        dude.draw(game.batch);
        game.batch.end();

        if(gameOverTimer>4) {
            if (game.hud.getLives() == 0) {//Dude är helt död
                dispose();
                game.setScreen(new GameOverScreen(game));
            }
            else{
                dispose();
                game.setScreen(new DeadScreen(game,timeOut));
            }
        }
        if(bubbleHandler.getBubbles()==0) {//Bana klar
            musicStop();
            unloadLevelMusic();
            if (game.hud.getLevel() < lastLevelNbr) {
                game.setScreen(new LevelCompleteScreen(game));
            }
            else{
                game.setScreen(new GameCompleteScreen(game));
            }
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

    private void musicEnd(){//Musik till död dude
        game.assetManager.get("audio/music/nighttideWaltz.ogg",Music.class).setVolume(0.8f);
        game.assetManager.get("audio/music/nighttideWaltz.ogg",Music.class).setLooping(true);
        game.assetManager.get("audio/music/nighttideWaltz.ogg",Music.class).play();
        musicStop();
        endMusicStarted = true;//Kvittera att musik är startad
    }
    private void musicStart(){
        switch(game.hud.getLevel()) {
            case 1:
                game.assetManager.get("audio/music/overworld.ogg", Music.class).setLooping(true);
                game.assetManager.get("audio/music/overworld.ogg", Music.class).setVolume(0.7f);
                game.assetManager.get("audio/music/overworld.ogg", Music.class).play();
                break;
            case 2:
                game.assetManager.get("audio/music/barbarianKing.ogg", Music.class).setLooping(true);
                game.assetManager.get("audio/music/barbarianKing.ogg", Music.class).setVolume(0.7f);
                game.assetManager.get("audio/music/barbarianKing.ogg", Music.class).play();
                break;
            case 3:
                game.assetManager.get("audio/music/enterTheEmperor.ogg", Music.class).setLooping(true);
                game.assetManager.get("audio/music/enterTheEmperor.ogg", Music.class).setVolume(0.7f);
                game.assetManager.get("audio/music/enterTheEmperor.ogg", Music.class).play();
                break;
        }
    }

    private void musicStop(){
        switch(game.hud.getLevel()) {
            case 1:
                game.assetManager.get("audio/music/overworld.ogg", Music.class).stop();
                break;
            case 2:
                game.assetManager.get("audio/music/barbarianKing.ogg", Music.class).stop();
                break;
            case 3:
                game.assetManager.get("audio/music/enterTheEmperor.ogg", Music.class).stop();
                break;

        }
    }

    private void loadLevelMusic(){

        switch (game.hud.getLevel()) {//Ladda musik till assetmanagern
            case 1:
                if(!game.assetManager.isLoaded("audio/music/overworld.ogg", Music.class)) {
                    game.assetManager.load("audio/music/overworld.ogg", Music.class);
                    game.assetManager.finishLoading();
                }
                break;
            case 2:
                if(!game.assetManager.isLoaded("audio/music/barbarianKing.ogg", Music.class)) {
                    game.assetManager.load("audio/music/barbarianKing.ogg", Music.class);
                    game.assetManager.finishLoading();
                }
                break;
            case 3:
                if(!game.assetManager.isLoaded("audio/music/enterTheEmperor.ogg", Music.class)) {
                    game.assetManager.load("audio/music/enterTheEmperor.ogg", Music.class);
                    game.assetManager.finishLoading();
                }
                break;
        }
    }
    private void unloadLevelMusic(){
        switch (game.hud.getLevel()) {//Ta bort musik från assetmanagern
            case 1:
                game.assetManager.unload("audio/music/overworld.ogg");
                game.assetManager.finishLoading();
                break;
            case 2:
                game.assetManager.unload("audio/music/barbarianKing.ogg");
                game.assetManager.finishLoading();
                break;
            case 3:
                game.assetManager.unload("audio/music/enterTheEmperor.ogg");
                game.assetManager.finishLoading();
                break;
        }
    }

    private void loadMap(){
        switch (game.hud.getLevel()) {
            case 1:
                tiledMap = tmxMapLoader.load("maps/level1.tmx");
                break;
            case 2:
                tiledMap = tmxMapLoader.load("maps/level2.tmx");
                break;
            case 3:
                tiledMap = tmxMapLoader.load("maps/level3.tmx");
                break;
        }
    }

    private final void createWallFloorRoof(int layer) throws Exception {
        BodyDef bodydef = new BodyDef();
        PolygonShape polygonshape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;
        int test;
        FLOOR_OR_WHOT type = ID_FLOOR;

        for (MapObject o : tiledMap.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) o).getRectangle();
            bodydef.type = BodyDef.BodyType.StaticBody;
            bodydef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PPM, (rectangle.getY() + rectangle.getHeight() / 2) / PPM);

            body = world.createBody(bodydef);

            polygonshape.setAsBox((rectangle.getWidth() / 2) / PPM, (rectangle.getHeight() / 2) / PPM);
            fixtureDef.shape = polygonshape;
            fixtureDef.filter.categoryBits = FLOOR_WALL;
            fixtureDef.filter.maskBits = -1;
            switch (o.getName()) {
                case "floor":
                    type = ID_FLOOR;
                    break;
                case "roof":
                    type = ID_ROOF;
                    fixtureDef.filter.categoryBits = ROOF;
                    fixtureDef.filter.maskBits = BUBBLE | SHOT;
                    break;
                case "left":
                    type = ID_LEFT_WALL;
                    break;
                case "right":
                    type = ID_RIGHT_WALL;
                    break;
                default:
                    throw new Exception("Kunde inte hitta namn på tak,golv eller väggar. Kolla i map editorn så namnen är rätt. \n Tak = roof, Golv = floor, Vänster vägg = left & Höger vägg = right  ");
            }
            //Beskriver vad vi krockat med
            body.createFixture(fixtureDef);
            body.setUserData(type);
        }
    }

    private final  void createBubbles( int layer) throws Exception {
        for (MapObject o : tiledMap.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
            String item[] = new String[4];//Färg en bokstav, startstorlek, stoppstorlek, spawn färdriktning;
            String items;
            int index = 0;
            items = o.getName();
            Bubble.BubbleColor color = null;
            Bubble.BubbleState startSize = null;
            Bubble.BubbleState endSize = null;
            boolean spawnRight = false;
            for (int i = 0; i < item.length; i++) {
                do {
                    String sub = items.substring(index, index + 1);
                    index++;
                    if (sub.equalsIgnoreCase(",")) {
                        if (item[i] == null) {
                            throw new Exception("Fel på index: " + i + " när bubbla ska skapas");
                        }

                        break;
                    } else {
                        if (item[i] == null) {
                            item[i] = sub;
                        } else {
                            item[i] += sub;
                        }
                    }
                } while (true);
            }
            for (int i = 0; i < item.length; i++) {
                switch (i) {
                    case 0://Färg
                        switch (item[i].toUpperCase()) {
                            case "R":
                                color = RED;
                                break;
                            case "G":
                                color = GREEN;
                                break;
                            case "B":
                                color = BLUE;
                                break;
                            default:
                                throw new Exception("Fel ingen färg när bubbla ska skapas");
                        }
                        break;
                    case 1://Start storlek
                        switch (item[i].toUpperCase()) {
                            case "XL":
                                startSize = XLARGE;
                                break;
                            case "L":
                                startSize = LARGE;
                                break;
                            case "M":
                                startSize = MEDIUM;
                                break;
                            case "S":
                                startSize = SMALL;
                                break;
                            case "XS":
                                startSize = XSMALL;
                                break;
                            default:
                                throw new Exception("Fel ingen max storlek när bubbla ska skapas");
                        }
                        break;
                    case 2://stopp storlek
                        switch (item[i].toUpperCase()) {
                            case "XL":
                                endSize = XLARGE;
                                break;
                            case "L":
                                endSize = LARGE;
                                break;
                            case "M":
                                endSize = MEDIUM;
                                break;
                            case "S":
                                endSize = SMALL;
                                break;
                            case "XS":
                                endSize = XSMALL;
                                break;
                            default:
                                throw new Exception("Fel ingen max storlek när bubbla ska skapas");
                        }
                        break;
                    case 3://Spawna höger eller vänster
                        switch (item[i].toUpperCase()) {
                            case "R":
                                spawnRight = true;
                                break;
                            case "L":
                                spawnRight = false;
                                break;
                            default:
                                throw new Exception("Fel ingen max storlek när bubbla ska skapas");
                        }
                        break;
                }
            }
            bubbleHandler.addBubble(new Bubble(world, startSize, color, new Vector2(((RectangleMapObject) o).getRectangle().x, ((RectangleMapObject) o).getRectangle().y), game.assetManager, spawnRight, endSize,false,false));
        }

    }

    private final  void createObstacles(int layer) throws Exception {
        for (MapObject o : tiledMap.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
            String item[] = new String[2];//Färg en bokstav, går att skjuta isönder?
            String items;
            int index = 0;
            items = o.getName();
            boolean colorYellow = false;
            boolean isBreakable = false;
            for (int i = 0; i < item.length; i++) {
                do {
                    String sub = items.substring(index, index + 1);
                    index++;
                    if (sub.equalsIgnoreCase(",")) {
                        if (item[i] == null) {
                            throw new Exception("Fel på index: " + i + " när hinder ska skapas");
                        }

                        break;
                    } else {
                        if (item[i] == null) {
                            item[i] = sub;
                        } else {
                            item[i] += sub;
                        }
                    }
                } while (true);
            }
            for (int i = 0; i < item.length; i++) {
                switch (i) {
                    case 0://Färg
                        switch (item[i].toUpperCase()) {
                            case "Y":
                                colorYellow = true;
                                break;
                            case "P":
                                colorYellow = false;
                                break;
                            default:
                                throw new Exception("Fel ingen färg när hinder ska skapas");
                        }
                        break;
                    case 1://Start storlek
                        switch (item[i].toUpperCase()) {
                            case "T":
                                isBreakable = true;
                                break;
                            case "F":
                                isBreakable = false;
                                break;
                            default:
                                throw new Exception("Fel ingen status på isBreakable  när hinder ska skapas");
                        }
                        break;
                    default:
                        throw new Exception("Fel vid skapande av hinder");
                }
            }
            Rectangle rectangle = ((RectangleMapObject) o).getRectangle();
            obstacleHandler.addObstacle(game, rectangle, world, colorYellow, isBreakable);
        }
    }

    private final void createDude(int layer)throws Exception {
        if(tiledMap.getLayers().get(layer).getObjects().getCount() != 1){
            throw new Exception("Tiled karta får bara innehålla en dude");
        }
        for (MapObject o : tiledMap.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
            dude = new Dude(world, game, new Vector2(((RectangleMapObject) o).getRectangle().x, ((RectangleMapObject) o).getRectangle().y + ((RectangleMapObject) o).getRectangle().getHeight()/2),bubbleHandler.getDestroyables()+obstacleHandler.getDestroyables(), bubbleHandler);
        }
        if(dude == null){
            throw new Exception("Fel vid skapande av dude");
        }
    }
}
