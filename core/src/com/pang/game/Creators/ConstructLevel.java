package com.pang.game.Creators;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import static com.pang.game.Constants.Constants.FLOOR_OR_WHOT.*;
import static com.pang.game.Constants.Constants.*;

public class ConstructLevel {
    public final static void createWallFloorRoof(World world, TiledMap tiledMap, int layer) throws Exception{
        BodyDef bodydef = new BodyDef();
        PolygonShape polygonshape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;
        int test;
        FLOOR_OR_WHOT type = FLOOR;

        for (MapObject o: tiledMap.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) o).getRectangle();
            bodydef.type = BodyDef.BodyType.StaticBody;
            bodydef.position.set((rectangle.getX() + rectangle.getWidth()/2)/PPM, (rectangle.getY() + rectangle.getHeight()/2)/PPM);

            body = world.createBody(bodydef);

            polygonshape.setAsBox((rectangle.getWidth()/2)/PPM, (rectangle.getHeight()/2)/PPM);
            fixtureDef.shape = polygonshape;
            fixtureDef.filter.categoryBits = FLOOR_WALL_ROOF;
            switch(o.getName()){
                case "floor":
                    type = FLOOR;
                    break;
                case "roof":
                    type = ROOF;
                case "left":
                    type = LEFT_WALL;
                    break;
                case "right":
                    type = RIGHT_WALL;
                    break;
                    default:
                        throw new Exception("Kunde inte hitta namn på tak,golv eller väggar. Kolla i map editorn så namnen är rätt. \n Tak = roof, Golv = floor, Vänster vägg = left & Höger vägg = right  ");
            }
            //Beskriver vad vi krockat med
            body.createFixture(fixtureDef);
            body.setUserData(type);
        }
    }
}
