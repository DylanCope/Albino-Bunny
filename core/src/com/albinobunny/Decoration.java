package com.albinobunny;

import java.util.ArrayList;
import java.util.Random;

import com.albinobunny.utils.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Decoration 
{
	Sprite sky, floor;
	ArrayList<Sprite> foreground, background;
	float floorLevel;
	
	public Decoration()
	{
		sky = new Sprite(Assets.manager.get("graphics/decor/sky.png", Texture.class));
		float scale = Gdx.graphics.getHeight() / sky.getHeight();
		sky.setSize(sky.getWidth() * scale, sky.getHeight() * scale);
		sky.setX(Gdx.graphics.getWidth() - sky.getWidth());
		
		floor = new Sprite(Assets.manager.get("graphics/decor/floor.png", Texture.class));
		floor.setSize(floor.getWidth() * scale, floor.getHeight() * scale);
		
		floorLevel = floor.getHeight();
		
		int size = Gdx.graphics.getWidth() / 12;
		
		Sprite grass1 = new Sprite(Assets.manager.get("graphics/decor/grass1.png", Texture.class));
		Sprite grass2 = new Sprite(Assets.manager.get("graphics/decor/grass2.png", Texture.class));
		Sprite flower1 = new Sprite(Assets.manager.get("graphics/decor/flower1.png", Texture.class)); 
		Sprite flower2 = new Sprite(Assets.manager.get("graphics/decor/flower2.png", Texture.class));  
		
		grass1.setSize(size, size);
		grass2.setSize(size, size);
		flower1.setSize(size, size);
		flower2.setSize(size, size);
		
		background = new ArrayList<Sprite>();
		foreground = new ArrayList<Sprite>();
		
		Random rand = new Random();
		for (int i = 0; i < 12; i++)
		{
			ArrayList<Sprite> arr;
			if (rand.nextBoolean())
				arr = background;
			else
				arr = foreground;

			int r = rand.nextInt(8);
			
			Sprite s = new Sprite();
			if (r == 0 || r == 1)
				s.set(grass2);
			else if (r == 2 || r == 3)
				s.set(grass2);
			else if (r == 4)
				s.set(flower1);
			else if (r == 5)
				s.set(flower2);
			
			if (s.getTexture() != null)
			{
				s.flip(rand.nextBoolean(), false);
					
				s.setPosition(i * size, floorLevel);
				arr.add(s);
			}
			
		}
	}
	
	public void renderBackground(SpriteBatch batch)
	{
		sky.draw(batch);
		floor.draw(batch);
		for (Sprite sprite : background)
			sprite.draw(batch);
	}
	
	public void renderForeground(SpriteBatch batch)
	{
		for (Sprite sprite : foreground)
			sprite.draw(batch);
	}
	
	public Sprite getFloor()
	{
		return floor;
	}
	
	public Sprite getSky()
	{
		return sky;
	}
}
