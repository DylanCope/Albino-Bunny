package com.albinobunny.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.Interpolation;

public class Assets
{

	public static final AssetManager manager = new AssetManager();
	private static float percent;
	
	public static void loadBefore() 
	{
		manager.load("graphics/candela_logo.png", Texture.class);
		manager.finishLoading();
	}
	
	public static void loadAll() 
	{
		percent = 0;

		manager.load("graphics/bullet.png", Texture.class);
		manager.load("graphics/decor/floor.png", Texture.class);
		manager.load("graphics/decor/flower1.png", Texture.class);
		manager.load("graphics/decor/flower2.png", Texture.class);
		manager.load("graphics/decor/gradient.png", Texture.class);
		manager.load("graphics/decor/floor.png", Texture.class);
		manager.load("graphics/decor/grass1.png", Texture.class);
		manager.load("graphics/decor/grass2.png", Texture.class);
		manager.load("graphics/decor/sky.png", Texture.class);
		manager.load("graphics/player/bunny_air1.png", Texture.class);
		manager.load("graphics/player/bunny_air2.png", Texture.class);
		manager.load("graphics/player/bunny_air3.png", Texture.class);
		manager.load("graphics/player/bunny_still.png", Texture.class);
		manager.load("graphics/player/bunny_walk.png", Texture.class);
		
		if (!Gdx.app.getType().equals(ApplicationType.Desktop))
		{
			manager.load("audio/death.ogg", Music.class);
			manager.load("audio/game_over.ogg", Music.class);
			manager.load("audio/jump.ogg", Music.class);
			manager.load("audio/land.ogg", Music.class);
		}

		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		manager.load("graphics/Chunq.ttf", FreeTypeFontGenerator.class);
		
	}
	
	public static float getProgress()
	{
		manager.update();
		float progress = manager.getProgress();
		percent = Interpolation.linear.apply(percent, progress, 0.05f);
		return percent;
	}
	
	public static void destroy(boolean destroy)
	{
		manager.clear();
		if (destroy)
			manager.dispose();
	}
	
	public static boolean isAllLoaded()
	{
		return percent > 0.999f && manager.getQueuedAssets() == 0;
	}
	
}
