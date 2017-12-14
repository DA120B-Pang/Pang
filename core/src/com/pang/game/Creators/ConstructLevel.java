package com.pang.game.Creators;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.pang.game.Pang;
import com.pang.game.Sprites.Bubble;

import static com.pang.game.Constants.Constants.FLOOR_OR_WHOT.*;
import static com.pang.game.Constants.Constants.*;
import static com.pang.game.Constants.Constants.FLOOR_OR_WHOT.ID_ROOF;
import static com.pang.game.Sprites.Bubble.BubbleColor.*;
import static com.pang.game.Sprites.Bubble.BubbleState.*;

public class ConstructLevel {
    public final static void createWallFloorRoof(World world, TiledMap tiledMap, int layer) throws Exception{
        BodyDef bodydef = new BodyDef();
        PolygonShape polygonshape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;
        int test;
        FLOOR_OR_WHOT type = ID_FLOOR;

        for (MapObject o: tiledMap.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) o).getRectangle();
            bodydef.type = BodyDef.BodyType.StaticBody;
            bodydef.position.set((rectangle.getX() + rectangle.getWidth()/2)/PPM, (rectangle.getY() + rectangle.getHeight()/2)/PPM);

            body = world.createBody(bodydef);

            polygonshape.setAsBox((rectangle.getWidth()/2)/PPM, (rectangle.getHeight()/2)/PPM);
            fixtureDef.shape = polygonshape;
            fixtureDef.filter.categoryBits = FLOOR_WALL;
            fixtureDef.filter.maskBits = -1;
            switch(o.getName()){
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
    public final static void createBubbles(Pang game, BubbleHandler bubbleHandler,  World world, TiledMap tiledMap, int layer) throws Exception {
        for (MapObject o: tiledMap.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)){
            String item[] = new String[4];//Färg en bokstav, startstorlek, stoppstorlek, spawn färdriktning;
            String items;
            int index = 0;
            items = o.getName();
            Bubble.BubbleColor color = null;
            Bubble.BubbleState startSize = null;
            Bubble.BubbleState endSize = null;
            boolean spawnRight = false;
            for (int i = 0; i <item.length ; i++) {
                do{
                    String sub = items.substring(index,index+1);
                    index++;
                    if (sub.equalsIgnoreCase(",")){
                        if(item[i] == null){
                            throw new Exception("Fel på index: "+i+" när bubbla ska skapas");
                        }

                        break;
                    }
                    else{
                        if (item[i]==null){
                            item[i] = sub;
                        }
                        else {
                            item[i] += sub;
                        }
                    }
                }while(true);
            }
            for (int i = 0; i <item.length ; i++) {
                switch (i){
                    case 0://Färg
                        switch (item[i].toUpperCase()){
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
                        switch (item[i].toUpperCase()){
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
                        switch (item[i].toUpperCase()){
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
                        switch (item[i].toUpperCase()){
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

            bubbleHandler.addBubble(new Bubble(world, startSize, color, new Vector2(((RectangleMapObject) o).getRectangle().x,((RectangleMapObject) o).getRectangle().y), game.assetManager,spawnRight, endSize));
        }

    }
    public final static void createObstacles(Pang game, ObstacleHandler obstacleHandler, World world, TiledMap tiledMap, int layer) throws Exception {
        for (MapObject o : tiledMap.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
            String item[] = new String[2];//Färg en bokstav, startstorlek, stoppstorlek, spawn färdriktning;
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
}
