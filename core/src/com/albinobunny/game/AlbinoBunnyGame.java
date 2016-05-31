package com.albinobunny.game;

import java.io.IOException;
import java.util.ArrayList;

import com.albinobunny.Data;
import com.albinobunny.Decoration;
import com.albinobunny.utils.Assets;
import com.albinobunny.utils.MusicPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AlbinoBunnyGame 
{
	private Sprite 				floor, exFrag1, exFrag2;
	private float				timeToNextDrop, playTime, dropTime;
	private ArrayList<Bullet> 	bullets;
	private int					score;
	private Data				data;
	private boolean 			playerJustDied, tutorialMode, saveData;
	private float				respawnTime;
	public Player 				player;
	
	public static float grav = Gdx.graphics.getHeight() * 2;

	public AlbinoBunnyGame(Decoration decor, Player player)
	{
		tutorialMode = false;
		saveData = true;
		player.ressurect();
		playerJustDied = false;
		this.player = player;
		Texture text = Assets.manager.get("graphics/decor/gradient.png", Texture.class);
		floor = decor.getFloor();

		exFrag1 = new Sprite(text);
		exFrag1.setColor(Color.RED);
		exFrag1.setSize(Gdx.graphics.getWidth() / 20, Gdx.graphics.getWidth() / 20);
		exFrag1.setPosition(0, -2*exFrag1.getHeight());
		exFrag2 = new Sprite(text);
		exFrag2.setColor(Color.RED);
		exFrag2.setSize(Gdx.graphics.getWidth() / 20, Gdx.graphics.getWidth() / 20);
		exFrag2.setPosition(0, -2*exFrag1.getHeight());
		
		timeToNextDrop = 0;
		playTime = 0;
		score = 0;
		respawnTime = 0;
		
		bullets = new ArrayList<Bullet>();

		try {
			data = Data.readData();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			Gdx.app.exit();
		} catch (IOException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
		
		dropTime = 1;
	}
	
	public void updateAndRender(float delta, SpriteBatch batch)
	{	
		playerJustDied = false;
		if (player.isAlive()) 
		{
			float dx = delta * Gdx.graphics.getWidth();
			exFrag1.setPosition(exFrag1.getX() - dx, exFrag1.getY());
			exFrag2.setPosition(exFrag2.getX() + dx, exFrag2.getY());
			
			exFrag1.draw(batch);
			exFrag2.draw(batch);
			
			if (exFrag1.getBoundingRectangle().overlaps(player.getSprite().getBoundingRectangle()) ||
				exFrag2.getBoundingRectangle().overlaps(player.getSprite().getBoundingRectangle()))
				kill();
			
			for (int i = 0; i < bullets.size(); i++) {
				Bullet bullet = bullets.get(i);
				
				bullet.update(delta);
				bullet.render(batch);
				
				if (bullet.collidingWith(floor.getBoundingRectangle())) {
					score++;
					bullets.remove(bullet);
					MusicPlayer.death(0.1f);
					
					exFrag1.setY(floor.getY() + floor.getHeight());
					exFrag1.setX(bullet.getX());
					
					exFrag2.setX(exFrag1.getX());
					exFrag2.setY(exFrag1.getY());
				}
				
				if (bullet.collidingWith(player.getSprite().getBoundingRectangle()))
					kill();
			}
			
			playTime += delta;
			
			if (!tutorialMode)
			{
				timeToNextDrop -= delta;
				
				if (timeToNextDrop <= 0) {
					timeToNextDrop = dropTime;
					bullets.add(new Bullet());
				}
			}
			player.update(delta);
		}

		player.render(batch);
		
		if (respawnTime > 0)
			respawnTime -= delta;
	}
	
	private void kill()
	{
		player.kill();
		playerJustDied = true;
		if (saveData)
			data.addScore(score, playTime);
		MusicPlayer.death(0.1f);
		MusicPlayer.gameOver(0.1f);
		
		try {
			if (saveData)
				Data.saveData(data);
		} 
		catch (IOException e) {
			Gdx.app.error("Save data IOException", e.getMessage());
			Gdx.app.exit();
		}
		
		respawnTime = 0.3f;
	}
	
	public int getCurrentScore()
	{
		return score;
	}
	
	public boolean canRespawn()
	{
		return !player.isAlive() && respawnTime <= 0;
	}
	
	public boolean didPlayerJustDie() {
		return playerJustDied;
	}
	
	public boolean isPlayerAlive()
	{
		return player.isAlive();
	}
	
	public void setTutorialMode(boolean tutorialMode)
	{
		this.tutorialMode = tutorialMode;
		setDropTime(1.5f);
	}
	
	public void setDropTime(float dropTime)
	{
		this.dropTime = dropTime;
	}
	
	public void dropBullet()
	{
		bullets.add(new Bullet());
	}
	
	public void dontSave()
	{
		saveData = false;
	}
}
