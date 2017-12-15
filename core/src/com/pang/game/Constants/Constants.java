package com.pang.game.Constants;

public class Constants {
    public static final int WORLD_WIDTH = 384;//768;
    public static final int WORLD_HEIGHT = 256;//512;
    public static final float PPM = 100;
    public enum FLOOR_OR_WHOT{
        ID_FLOOR, ID_ROOF, ID_LEFT_WALL, ID_RIGHT_WALL
    }
    public enum PowerUp {
        DOUBLESHOT, BARBSHOT, SHEILD, LIFE, STOPTIME
    }


    //Kategoribitar till box 2d ( för kollisioner ) finns totalt 16 kategorier i box2d
    public static final short FREEFALL = 0;
    public static final short FLOOR_WALL = 1;
    public static final short DUDE = 2;
    public static final short BUBBLE = 4;
    public static final short SHOT = 8;
    public static final short ROOF = 16;
    public static final short OBSTACLE = 32;
    public static final short OBSTACLE_TOP = 64;
    public static final short OBSTACLE_SIDE = 128;
    public static final short UNDEFINED_9 = 256;
    public static final short UNDEFINED_10 = 512;
    public static final short UNDEFINED_11 = 1024;
    public static final short UNDEFINED_12 = 2048;
    public static final short UNDEFINED_13 = 4096;
    public static final short UNDEFINED_14 = 8192;
    public static final short UNDEFINED_15 = 16384;
}
