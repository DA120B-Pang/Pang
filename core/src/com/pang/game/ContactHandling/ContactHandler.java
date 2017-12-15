package com.pang.game.ContactHandling;

import com.badlogic.gdx.physics.box2d.*;
import com.pang.game.Sprites.Bubble;
import com.pang.game.Sprites.Dude;
import com.pang.game.Sprites.Obstacle;
import com.pang.game.Sprites.Shot;

import static com.pang.game.Constants.Constants.*;
import static com.pang.game.Constants.Constants.ROOF;

public class ContactHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture A = contact.getFixtureA();//Läser ut kolliderade fixtures
        Fixture B = contact.getFixtureB();//Läser ut kolliderade fixtures

        int category  = A.getFilterData().categoryBits | B.getFilterData().categoryBits;//Or för att veta vilka kategorier som krockat
        Fixture main;
        Fixture other;

        switch (category) {
            case BUBBLE | FLOOR_WALL://Bubbla och golv,väggar eller tak har kolliderat

                if (A.getBody().getUserData() instanceof Bubble) {//Kolla om A är en bubbla om inte så är B bubblan
                    main = A;
                    other = B;
                } else {
                    main = B;
                    other = A;
                }
                switch (((FLOOR_OR_WHOT)other.getBody().getUserData())) {//Userdata i väggar,golv och tak är enum på vilken typ det är.
                    case ID_FLOOR:
                        //UserData i body är bubblans referens därför kan vi kalla på metoder från bubblan
                        ((Bubble) main.getBody().getUserData()).bumpFloor();//Om bubbla kolliderat med golv ska den studsa, bumfloor i bubbleobject kallas på
                        break;
                    case ID_LEFT_WALL:
                        ((Bubble) main.getBody().getUserData()).bumpLeftWall();//Om bubbla kolliderar med vänster vägg ska den ändra färdriktning åt höger.
                        break;
                    case ID_RIGHT_WALL:
                        ((Bubble) main.getBody().getUserData()).bumpRightWall();//Om bubbla kolliderar med höger vägg ska den ändra färdriktning åt vänster.
                        break;
                    default:
                }
                break;
            case BUBBLE | OBSTACLE_TOP:
                main = A.getBody().getUserData() instanceof Shot ? A:B;
                ((Bubble) main.getBody().getUserData()).bumpObstacaleTop();
                break;
            case BUBBLE | OBSTACLE_SIDE:
                main = A.getBody().getUserData() instanceof Shot ? A:B;
                ((Bubble) main.getBody().getUserData()).bumpObstacale();
                break;
            case FLOOR_WALL | DUDE: //Dude kolliderar med golv vägg eller tak... denna är till för dudes dödsryck när han studsar ut ur bild
                main = A.getBody().getUserData() instanceof Dude? A:B;//Kolla om Fixture A eller B är Dude
                //Userdata i dudes body är referensen till dude därför kan vi kalla på metoder via den
                if(((Dude)main.getBody().getUserData()).isDudeDead()){//Kolla om dude är död
                    Filter filter = ((Dude)main.getBody().getUserData()).dudeBody.getFixtureList().get(0).getFilterData();//Läs in dudes kollisions filter
                    filter.maskBits = 0;//Dude ska inte kollidera med något alls... han ska ramla ut ur bild
                    ((Dude)main.getBody().getUserData()).dudeBody.getFixtureList().get(0).setFilterData(filter);//skriv in nya filtret
                    ((Dude)main.getBody().getUserData()).jumpOfDeath();//Gör dödsstudsen ut från scenen
                }
                break;
            case BUBBLE | DUDE: // Bubbla träffar dude
                main = A.getBody().getUserData() instanceof Dude? A:B;//Kolla om Fixture A eller B är Dude
                //Userdata i dudes body är referensen till dude därför kan vi kalla på metoder via den
                ((Dude)main.getBody().getUserData()).dudeDie();//Sätt att dude ska dö
                break;
            case SHOT | ROOF:
                main = A.getBody().getUserData() instanceof Shot ? A:B;
                if(((Shot)main.getBody().getUserData()).getShotType() == Shot.ShotType.STANDARD) {//Förstör skott
                    ((Shot) main.getBody().getUserData()).destroyNextUpdate();
                }
                else{//Stoppa skott om Barbskott
                    ((Shot) main.getBody().getUserData()).setBarbStop();
                }
                break;
            case SHOT | BUBBLE:
                if( A.getBody().getUserData() instanceof Shot){
                    main = A;
                    other = B;
                }
                else{
                    main = B;
                    other = A;
                }
                ((Shot)main.getBody().getUserData()).destroyNextUpdate();
                ((Bubble)other.getBody().getUserData()).destroyNextUpdate();
                break;
            case SHOT | OBSTACLE:
                if( A.getBody().getUserData() instanceof Shot){
                    main = A;
                    other = B;
                }
                else{
                    main = B;
                    other = A;
                }
                if(((Shot)main.getBody().getUserData()).getShotType() == Shot.ShotType.STANDARD || ((Obstacle)other.getBody().getUserData()).isBreakable()) {//Förstör skott
                    ((Shot) main.getBody().getUserData()).destroyNextUpdate();
                }
                else{//Stoppa skott om Barbskott
                    ((Shot) main.getBody().getUserData()).setBarbStop();
                }
                ((Obstacle)other.getBody().getUserData()).destroyNextUpdate();
                break;

            default:
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
