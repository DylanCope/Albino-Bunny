package com.albinobunny.screens;

import java.io.IOException;

import com.albinobunny.AlbinoBunnyMain;
import com.albinobunny.Data;
import com.albinobunny.utils.Assets;
import com.albinobunny.utils.Interpolator;
import com.albinobunny.utils.Interpolator.Type;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class StatsScreen
{
	boolean 	show;
	TextButton	back;
	Label highScore;
	Label[] scoreLabels;
	BitmapFont	font;
	AlbinoBunnyMain main;
	
	public StatsScreen(final AlbinoBunnyMain main)
	{
		this.main = main;
		show = false;

		FreeTypeFontGenerator gen = Assets.manager.get("graphics/Chunq.ttf", FreeTypeFontGenerator.class);
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = Gdx.graphics.getHeight() / 15;
		param.borderWidth = param.size / 15;
		param.borderColor = Color.BLACK;
		param.shadowOffsetX = Gdx.graphics.getWidth() / 100;
		param.shadowOffsetY = Gdx.graphics.getWidth() / 100;
		param.shadowColor = new Color(0, 0, 0, 0.2f);
		
		font = gen.generateFont(param);
		
		TextButtonStyle tbStyle = new TextButtonStyle();
		tbStyle = new TextButtonStyle();
		tbStyle.font = font;
		
		back = new TextButton("back", tbStyle);
		back.setPosition(
				Gdx.graphics.getWidth() - back.getWidth() * 1.1f, 
				Gdx.graphics.getHeight() - back.getHeight());
		
		back.addListener(new InputListener() {
			@Override
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int playerStats) {
				hide();
				Timer.schedule(new Task() {
					public void run() {
						main.mainMenu.show(main.player);
					}
				}, 0.7f);
				return true;
			}
		});
	}

	public void show() 
	{
		LabelStyle lbStyle = new LabelStyle();
		lbStyle.font = font;
		
		scoreLabels = new Label[5];
		
		Data data = null;
		try {
			data = Data.readData();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			Gdx.app.exit();
		} 
		catch (IOException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
		
		for (int i = 0; i < 5; i++)
		{
			scoreLabels[i] = new Label("" + data.getScore(i), lbStyle);
			scoreLabels[i].setPosition(
					(Gdx.graphics.getWidth() - scoreLabels[i].getWidth()) / 2,
					0.7f * Gdx.graphics.getHeight() - 1.1f * i * scoreLabels[i].getHeight());
		}
		
		highScore = new Label("highscores", lbStyle);
		highScore.setPosition(
				(Gdx.graphics.getWidth() - highScore.getWidth()) / 2,
				 Gdx.graphics.getHeight() * 0.8f);
		
		
		
		main.stage.clear();
		main.stage.addActor(back);
		
		final Interpolator backInterpolator = new Interpolator(back);
		back.setPosition(Gdx.graphics.getWidth() + 1.2f * back.getWidth(), back.getY());
		backInterpolator.interpolate(Type.X, Gdx.graphics.getWidth() - back.getWidth() * 1.1f, 0.6f);
		main.stage.addActor(backInterpolator);
		
		main.stage.addActor(highScore);
		
		Interpolator highScoreInterpolator = new Interpolator(highScore);
		
		highScore.setX(-1.2f * highScore.getWidth());
		highScoreInterpolator.interpolate(Type.X, (Gdx.graphics.getWidth() - highScore.getWidth())/2, 0.6f);
		main.stage.addActor(highScoreInterpolator);
		
		for (int i = 0; i < scoreLabels.length; i++) {
			final Label l = scoreLabels[i];
			main.stage.addActor(l);
			final Interpolator labelInterpolator = new Interpolator(l);
			l.setX(-2f * l.getWidth());
			labelInterpolator.interpolate(Type.X, (Gdx.graphics.getWidth() - l.getWidth()) / 2, 0.4f, 0.04f * i);
			main.stage.addActor(labelInterpolator);
		}
		
		show = true;
	}

	public void hide() 
	{	
		final Interpolator backInterpolator = new Interpolator(back);
		backInterpolator.interpolate(Type.X, Gdx.graphics.getWidth() + back.getWidth() * 1.2f, 0.6f);
		main.stage.addActor(backInterpolator);
		
		Interpolator highScoreInterpolator = new Interpolator(highScore);
		highScoreInterpolator.interpolate(Type.X, -1.2f * highScore.getWidth(), 0.6f);
		main.stage.addActor(highScoreInterpolator);
		
		for (int i = 0; i < scoreLabels.length; i++) 
		{
			final Label l = scoreLabels[i];
			final Interpolator labelInterpolator = new Interpolator(l);
			labelInterpolator.interpolate(Type.X, -1.5f * l.getWidth(), 0.4f, 0.04f * i);
			main.stage.addActor(labelInterpolator);
		}
		
		Timer.schedule(new Task() {
			public void run() {
				show = false;
				main.stage.clear();
			}
		}, 0.6f);
		
	}
	
}
