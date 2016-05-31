package com.albinobunny.game;

import java.util.Random;

import com.albinobunny.utils.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet 
{
	private Vector2 position, velocity;
	private Sprite sprite;
	
	public Bullet()
	{
		velocity = new Vector2(0, 0);
		
		Texture text = Assets.manager.get("graphics/bullet.png", Texture.class);
		sprite = new Sprite(text);
		
		sprite.setSize(Gdx.graphics.getWidth() / 20, Gdx.graphics.getWidth() / 10);
		sprite.setColor(Color.LIGHT_GRAY);

		Random rand = new Random();
		position = new Vector2(
				sprite.getWidth() + rand.nextInt((int) (Gdx.graphics.getWidth() - 2 * sprite.getWidth())),
				1.1f * Gdx.graphics.getHeight());
	}
	
	public void update(float delta)
	{	
		Vector2 dv = new Vector2(0, -delta * AlbinoBunnyGame.grav);
		velocity.add(dv);
		
		Vector2 dx = new Vector2(velocity.x * delta, velocity.y * delta);
		position.add(dx);
	}	
	
	public void render(SpriteBatch batch)
	{
		sprite.setPosition(position.x, position.y);
		sprite.draw(batch);
	}
	
	public boolean collidingWith(Rectangle rect)
	{
		sprite.setPosition(position.x, position.y);
		return rect.overlaps(sprite.getBoundingRectangle());
	}
	
	public float getX() { return sprite.getX(); }
	public float getY() { return sprite.getY(); }
}
