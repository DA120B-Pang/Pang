package com.pang.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.pang.game.HUD.HUD;
import com.pang.game.Screens.StartingScreen;


public class Pang extends Game {

	public AssetManager assetManager;
	public HUD hud;
	public SpriteBatch batch;
	public BitmapFont font;
	public float musicVolume = 1f;
	public float soundVolume = 1f;


	@Override
	public void create () {

		assetManager = new AssetManager();
        assetManager.load("sprites/sprites.pack", TextureAtlas.class);
		assetManager.load("audio/sound/shoot.wav", Sound.class);
		assetManager.load("audio/sound/boomSmall.wav", Sound.class);
		assetManager.load("audio/sound/boomMedium.wav", Sound.class);
		assetManager.load("audio/sound/boomLarge.wav", Sound.class);
		assetManager.load("audio/sound/boomXlarge.wav", Sound.class);
		assetManager.load("audio/sound/countDown.wav", Sound.class);
		assetManager.load("audio/sound/tileBreak.wav", Sound.class);
		assetManager.load("audio/sound/tileNonBreak.wav", Sound.class);
		assetManager.load("audio/sound/powerUpBarb.wav", Sound.class);
        assetManager.load("audio/sound/powerUpDouble.wav", Sound.class);
        assetManager.load("audio/sound/powerUpLife.wav", Sound.class);
        assetManager.load("audio/sound/powerUpLifeFull.wav", Sound.class);
        assetManager.load("audio/sound/powerUpSheild.wav", Sound.class);
        assetManager.load("audio/sound/powerUpSheildDown.wav", Sound.class);
        assetManager.load("audio/sound/powerUpStopTime.wav", Sound.class);
        assetManager.load("audio/sound/powerUpStopTimeDown.wav", Sound.class);
		assetManager.load("audio/music/nighttideWaltz.ogg", Music.class);
		assetManager.finishLoading();
		batch = new SpriteBatch();

		initFonts();

		hud = new HUD(this, assetManager);
		setScreen(new StartingScreen(this));//Starta med Meny
	}

	private void initFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Arcon.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = 18;
		parameter.color = Color.BLACK;
		font = generator.generateFont(parameter);
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}

	@Override
	public void render () {
		super.render();
	}
}