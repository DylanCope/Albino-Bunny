package com.albinobunny.screens;

import com.albinobunny.AlbinoBunnyMain;
import com.albinobunny.utils.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class LoadingScreen
{
	private SpriteBatch batch;
	private Sprite logo;
	private AlbinoBunnyMain main;
	
	public LoadingScreen(AlbinoBunnyMain main) 
	{
		batch = new SpriteBatch();
		this.main = main;
		Assets.loadBefore();
		
		logo = new Sprite(Assets.manager.get("graphics/candela_logo.png", Texture.class));
		
		logo.setSize(Gdx.graphics.getWidth() / 3, Gdx.graphics.getWidth() / 3);
		logo.setPosition(
				(Gdx.graphics.getWidth() - logo.getWidth()) / 2,
				Gdx.graphics.getHeight() * 2 / 3f - logo.getHeight() / 2);
		
		Assets.loadAll();
	}

	public void render(float delta) 
	{
		batch.begin();
		logo.draw(batch);
		batch.end();

		ShapeRenderer renderer = new ShapeRenderer();
		
		float percent = Assets.getProgress();
		
		if (Assets.isAllLoaded())
			main.init();
		
		renderer.begin(ShapeType.Filled);
		
		float barMax = Gdx.graphics.getWidth() * 4 / 6;
		
		renderer.setColor(new Color(0.8f * 0.227f, 0.8f * 0.106f, 0, 1));
		renderer.rect(
				Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 3, 
				barMax, Gdx.graphics.getHeight() / 20);
		
		renderer.setColor(new Color(1.4f * 0.227f, 1.4f * 0.106f, 0, 1));
		renderer.rect(
				Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 3, 
				barMax * percent, Gdx.graphics.getHeight() / 20);
		
		renderer.end();
		
	}
}
