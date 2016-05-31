package com.albinobunny.screens;

import com.albinobunny.AlbinoBunnyMain;
import com.albinobunny.game.Player;
import com.albinobunny.utils.Assets;
import com.albinobunny.utils.Interpolator;
import com.albinobunny.utils.MusicPlayer;
import com.albinobunny.utils.Interpolator.Type;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MainMenu
{
	private AlbinoBunnyMain main;
	private TextButton playButton, statsButton, muteButton, tutorialButton;
	
	public MainMenu(final AlbinoBunnyMain main)
	{
		this.main = main;
		
		FreeTypeFontGenerator gen = Assets.manager.get("graphics/Chunq.ttf", FreeTypeFontGenerator.class);
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = Gdx.graphics.getHeight() / 10;
		param.borderWidth = param.size / 15;
		param.borderColor = Color.BLACK;
		param.shadowOffsetX = Gdx.graphics.getWidth() / 100;
		param.shadowOffsetY = Gdx.graphics.getWidth() / 100;
		param.shadowColor = new Color(0, 0, 0, 0.2f);
		
		BitmapFont font = gen.generateFont(param);
		
		TextButtonStyle tbStyle = new TextButtonStyle();
		tbStyle.font = font;
		
		playButton = new TextButton("Play", tbStyle);
		
		playButton.setPosition(
				(Gdx.graphics.getWidth() - playButton.getWidth()) / 2, 
				Gdx.graphics.getHeight() * 2 / 3f);
		
		playButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int playerStats) {
				hide();
				Timer.schedule(new Task() {
					public void run() {
						main.playScreen.show(main.player);
					}
				}, 0.55f);
				return true;
			}

		});

		
		param.size = (int) (0.6f * param.size);
		param.borderWidth = param.size / 15;
		BitmapFont small = gen.generateFont(param);
		
		TextButtonStyle tbStyle2 = new TextButtonStyle();
		tbStyle2.font = small;
		
		
		tutorialButton = new TextButton("Tutorial", tbStyle2);
		tutorialButton.setPosition(
				(Gdx.graphics.getWidth() - tutorialButton.getWidth()) / 2, 
				playButton.getY() - Gdx.graphics.getHeight() * 0.12f);
		tutorialButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int playerStats) {
				hide();
				Timer.schedule(new Task() {
					public void run() {
						main.tutorialScreen.show();
					}
				}, 0.55f);
				return true;
			}

		});
		
		
		statsButton = new TextButton("Scores", tbStyle2);
		statsButton.setPosition(
				(Gdx.graphics.getWidth() - statsButton.getWidth()) / 2, 
				tutorialButton.getY() - 1.1f * tutorialButton.getHeight());
		statsButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int playerStats) {
				hide();
				Timer.schedule(new Task() {
					public void run() {
						main.statsScreen.show();
					}
				}, 0.55f);
				return true;
			}

		});
		
		
		muteButton = new TextButton("Mute", tbStyle2);
		muteButton.setPosition(
				(Gdx.graphics.getWidth() - muteButton.getWidth()) / 2, 
				statsButton.getY() - 1.1f * statsButton.getHeight());
		muteButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int playerStats) {
				muteButton.setText(muteButton.getText().toString().equals("Mute") ? "Unmute" : "Mute");
				MusicPlayer.toggleMute();
				return true;
			}

		});

		
//		quitButton = new TextButton("Quit", tbStyle2);
//		quitButton.setPosition(
//				(Gdx.graphics.getWidth() - quitButton.getWidth()) / 2f,
//				muteButton.getY() - 1.1f * muteButton.getHeight());
//		quitButton.addListener(new InputListener() {
//			@Override
//			public boolean touchDown(InputEvent event, float x, float y, int pointer, int playerStats) {
//				hide();
//				Timer.schedule(new Task() {
//					public void run() {
//						Gdx.app.exit();
//					}
//				}, 0.55f);
//				return true;
//			}
//		});
	}

	public void show(Player player) 
	{
		main.player = player;
		player.setWalkSpeed(Gdx.graphics.getWidth() / 4f);
		
		main.stage.clear();
		main.stage.addActor(playButton);
		main.stage.addActor(tutorialButton);
		main.stage.addActor(statsButton);
		main.stage.addActor(muteButton);
//		main.stage.addActor(quitButton);
		
		playButton.setX(- 1.2f * playButton.getWidth());
		tutorialButton.setX(-1.2f * playButton.getWidth());
		statsButton.setX(- 1.2f * statsButton.getWidth());
		muteButton.setX(- 1.2f * muteButton.getWidth());
//		quitButton.setX(- 1.2f * quitButton.getWidth());
		
		Interpolator play = new Interpolator(playButton);
		play.interpolate(Type.X, (Gdx.graphics.getWidth() - playButton.getWidth()) / 2, 0.4f);
		Interpolator tutorial = new Interpolator(tutorialButton);
		tutorial.interpolate(Type.X,(Gdx.graphics.getWidth() - tutorialButton.getWidth()) / 2, 0.4f, 0.04f);
		Interpolator stats = new Interpolator(statsButton);
		stats.interpolate(Type.X,(Gdx.graphics.getWidth() - statsButton.getWidth()) / 2, 0.4f, 0.08f);
		Interpolator mute = new Interpolator(muteButton);
		mute.interpolate(Type.X,(Gdx.graphics.getWidth() - muteButton.getWidth()) / 2, 0.4f, 0.12f);
//		Interpolator quit = new Interpolator(quitButton);
//		quit.interpolate(Type.X,(Gdx.graphics.getWidth() - quitButton.getWidth()) / 2, 0.4f, 0.16f);
		
		main.stage.addActor(play);
		main.stage.addActor(tutorial);
		main.stage.addActor(stats);
		main.stage.addActor(mute);
//		main.stage.addActor(quit);
		
	}

	public void hide() 
	{
		Interpolator play = new Interpolator(playButton);
		play.interpolate(Type.X, -1.2f * playButton.getWidth(), 0.4f);
		Interpolator tutorial = new Interpolator(tutorialButton);
		tutorial.interpolate(Type.X,-1.2 * tutorialButton.getWidth(), 0.4f, 0.04f);
		Interpolator stats = new Interpolator(statsButton);
		stats.interpolate(Type.X, -1.2f * statsButton.getWidth(), 0.4f, 0.08f);
		Interpolator mute = new Interpolator(muteButton);
		mute.interpolate(Type.X, -1.2f * muteButton.getWidth(), 0.4f, 0.12f);
//		Interpolator quit = new Interpolator(quitButton);
//		quit.interpolate(Type.X, -1.2f * quitButton.getWidth(), 0.4f, 0.16f);
		
		main.stage.addActor(play);
		main.stage.addActor(tutorial);
		main.stage.addActor(stats);
		main.stage.addActor(mute);
//		main.stage.addActor(quit);
		
		Timer.schedule(new Task() {
			@Override
			public void run() {
				main.stage.clear();
			}
		}, 0.5f);
	}

}
