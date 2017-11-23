package com.pang.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pang.game.Screens.Level1;
import com.pang.game.Screens.StartingScreen;

public class Pang extends Game {

	public AssetManager assetManager;
	public TextureAtlas test;
	public float aspectRatio;


	public SpriteBatch batch;


	@Override
	public void create () {
		assetManager = new AssetManager();
		assetManager.load("sprites/sprites.pack", TextureAtlas.class);
		assetManager.load("audio/sound/shoot.wav", Sound.class);
		assetManager.finishLoading();
		batch = new SpriteBatch();
		aspectRatio = Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
		System.out.println(aspectRatio);


		setScreen(new StartingScreen(this));//Starta med Meny

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
//Kategorier i box 2d
//1: the dude(huvudkaraktären
//2: Väggar, golv och tak categori
//3: Bollar