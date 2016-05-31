package com.albinobunny.utils;

import com.badlogic.gdx.audio.Music;

public class MusicPlayer 
{
	private static Music jump, death, gameOver, land;
	private static boolean isMute = false;

	public static void init()
	{
		jump = Assets.manager.get("audio/jump.ogg", Music.class);
		death = Assets.manager.get("audio/death.ogg", Music.class);
		gameOver = Assets.manager.get("audio/game_over.ogg", Music.class);
		land = Assets.manager.get("audio/land.ogg", Music.class);
	}
	
	public static void toggleMute()
	{
		isMute = !isMute;
		if (jump != null) jump.stop();
		if (death != null) death.stop();
		if (gameOver != null) gameOver.stop();
		if (land != null) land.stop();
	}
	
	public static void jump(float volume)
	{
		if (jump != null)
		{
			jump.setVolume(isMute ? 0 : volume);
			jump.play();
		}
	}
	
	public static void gameOver(float volume)
	{
		if (gameOver != null)
		{
			gameOver.stop();
			gameOver.setVolume(isMute ? 0 : volume);
			gameOver.play();
		}
	}
	
	public static void death(float volume)
	{
		if (death != null)
		{
			death.setVolume(isMute ? 0 : volume);
			death.play();
		}
	}
	
	public static void land(float volume)
	{
		if (land != null)
		{
			land.setVolume(isMute ? 0 : volume);
			land.play();
		}
	}
}
