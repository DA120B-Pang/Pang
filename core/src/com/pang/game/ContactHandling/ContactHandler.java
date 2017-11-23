package com.pang.game.ContactHandling;

import com.badlogic.gdx.physics.box2d.*;
import com.pang.game.Sprites.Bubble;
import com.pang.game.Sprites.Dude;

import static com.pang.game.Constants.Constants.*;
import static com.pang.game.Constants.Constants.FLOOR_OR_WHOT.*;

public class ContactHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture A = contact.getFixtureA();
        Fixture B = contact.getFixtureB();

        int category  = A.getFilterData().categoryBits | B.getFilterData().categoryBits;
        Fixture main;
        Fixture other;

        switch (category) {
            case BUBBLE | FLOOR_WALL_ROOF:

                if (A.getBody().getUserData() instanceof Bubble) {
                    main = A;
                    other = B;
                } else {
                    main = B;
                    other = A;
                }
                switch (((FLOOR_OR_WHOT)other.getBody().getUserData())) {
                    case FLOOR:
                        ((Bubble) main.getBody().getUserData()).bumpFloor();
                        break;
                    case LEFT_WALL:
                        ((Bubble) main.getBody().getUserData()).bumpLeftWall();
                        break;
                    case RIGHT_WALL:
                        ((Bubble) main.getBody().getUserData()).bumpRightWall();
                        break;
                    default:
                }
                break;
            case FLOOR_WALL_ROOF | DUDE:
                main = A.getBody().getUserData() instanceof Dude? A:B;
                if(((Dude)main.getBody().getUserData()).isDudeDead()){
                    Filter filter = new Filter();
                    filter = ((Dude)main.getBody().getUserData()).dudeBody.getFixtureList().get(0).getFilterData();
                    filter.maskBits = 0;
                    ((Dude)main.getBody().getUserData()).dudeBody.getFixtureList().get(0).setFilterData(filter);
                    ((Dude)main.getBody().getUserData()).jumpOfDeath();
                }
                break;
            case BUBBLE | DUDE:
                main = A.getBody().getUserData() instanceof Dude? A:B;
                ((Dude)main.getBody().getUserData()).dudeDie();
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
