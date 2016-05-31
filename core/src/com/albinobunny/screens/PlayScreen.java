package com.albinobunny.screens;

import com.albinobunny.AlbinoBunnyMain;
import com.albinobunny.Decoration;
import com.albinobunny.game.AlbinoBunnyGame;
import com.albinobunny.game.Player;
import com.albinobunny.utils.AdsController;
import com.albinobunny.utils.Assets;
import com.albinobunny.utils.Interpolator;
import com.albinobunny.utils.Interpolator.Type;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class PlayScreen
{
	boolean 		show, mute, closing;
	AlbinoBunnyGame game;
	Label			scoreLabel, touchToRestartLabel;
	TextButton		backButton;
	Decoration		decor;
	AlbinoBunnyMain main;
	AdsController ads;
	Interpolator touch;
	
	public PlayScreen(final AlbinoBunnyMain main, Decoration decor, AdsController ads)
	{
		this.ads = ads;
		show = false;
		this.main = main;
		mute = false;
		closing = false;
		
		this.decor = decor;

		FreeTypeFontGenerator gen = Assets.manager.get("graphics/Chunq.ttf", FreeTypeFontGenerator.class);
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = Gdx.graphics.getHeight() / 15;
		param.borderWidth = param.size / 15;
		param.borderColor = Color.BLACK;
		param.shadowOffsetX = Gdx.graphics.getWidth() / 100;
		param.shadowOffsetY = Gdx.graphics.getWidth() / 100;
		param.shadowColor = new Color(0, 0, 0, 0.2f);
		
		BitmapFont fnt = gen.generateFont(param);
		
		TextButtonStyle tbStyle = new TextButtonStyle();
		tbStyle = new TextButtonStyle();
		tbStyle.font = fnt;
		
		backButton = new TextButton("menu", tbStyle);
		backButton.setPosition(
				Gdx.graphics.getWidth() - backButton.getWidth() * 1.1f, 
				Gdx.graphics.getHeight() - backButton.getHeight());
		
		backButton.addListener(new InputListener() {
			@Override
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int playerStats) {
				hide();
				main.player.setControls(Player.RandomControls);
				Timer.schedule(new Task() {
					public void run() {
						main.mainMenu.show(game.player);
					}
				}, 0.55f);
				return true;
			}
		});
		
		LabelStyle lbStyle = new LabelStyle();
		lbStyle.font = fnt;
		
		scoreLabel = new Label("0", lbStyle);
		scoreLabel.setPosition(Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() - scoreLabel.getHeight());
		
		touchToRestartLabel = new Label("Tap to\ntry again", lbStyle);
		touchToRestartLabel.setPosition(
				(Gdx.graphics.getWidth() - touchToRestartLabel.getWidth()) / 2,
				(Gdx.graphics.getHeight() * 2 - touchToRestartLabel.getHeight()) / 3);
		touch = new Interpolator(touchToRestartLabel);
	}

	public void show(Player player) 
	{
		Timer.schedule(new Task() {
			public void run()
			{
				ads.showBannerAd();
			}
		}, 5);
		
		main.stage.clear();
		
		main.stage.addActor(backButton);
		main.stage.addActor(scoreLabel);
		main.stage.addActor(touchToRestartLabel);
		
		main.stage.addActor(touch);
		
		Interpolator scoreInterpolator = new Interpolator(scoreLabel);
		scoreLabel.setX(-1.2f * scoreLabel.getWidth());
		scoreInterpolator.interpolate(Type.X, Gdx.graphics.getWidth() / 30f, 0.5f);
		main.stage.addActor(scoreInterpolator);
		
		Interpolator backInterpolator = new Interpolator(backButton);
		backButton.setX(Gdx.graphics.getWidth() + 1.2f * backButton.getWidth());
		backInterpolator.interpolate(Type.X, Gdx.graphics.getWidth() - backButton.getWidth() * 1.1f, 0.5f);
		main.stage.addActor(backInterpolator);
		
		main.player.setControls(Player.TiltControls);
		main.player.setWalkSpeed(Gdx.graphics.getWidth() * 3 / 4f);
		game = new AlbinoBunnyGame(decor, main.player);
		
		touchToRestartLabel.setColor(1, 1, 1, 0);
		
		show = true;
		main.renderBunny = false;
	}
	
	public void hide()
	{
		Interpolator backInterpolator = new Interpolator(backButton);
		backInterpolator.interpolate(Type.X, Gdx.graphics.getWidth() + backButton.getWidth() * 1.2f, 0.5f);
		main.stage.addActor(backInterpolator);

		Interpolator scoreInterpolator = new Interpolator(scoreLabel);
		scoreInterpolator.interpolate(Type.X, -1.2f * scoreLabel.getWidth(), 0.5f);
		main.stage.addActor(scoreInterpolator);
		
		touch.interpolate(Type.Alpha, 0f, 0.5f);
		
		Timer.schedule(new Task() {
			public void run() {
				main.stage.clear();
				show = false;
				closing = false;
			}
		}, 0.55f);
		
		main.renderBunny = true;
		
		ads.hideBannerAd();
		closing = true;
	}

	public void render(float delta, SpriteBatch batch) 
	{
		if (show && !closing)
		{
			game.updateAndRender(delta, batch);
			
			scoreLabel.setText("" + game.getCurrentScore());
			
			if (game.canRespawn() && Gdx.input.isTouched())
			{
				game = new AlbinoBunnyGame(decor, game.player);
				touch.interpolate(Type.Alpha, 0f, 0.6f);
			}
			
			if (game.didPlayerJustDie()) {
				touch.interpolate(Type.Alpha, 1f, 0.6f);
			}
			
		}
	}
	
	public boolean isShowing() { return show; }
	
	public void toggleMute()
	{
		mute = !mute;
	}
}
