package com.albinobunny.screens;

import com.albinobunny.AlbinoBunnyMain;
import com.albinobunny.game.AlbinoBunnyGame;
import com.albinobunny.game.Player;
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

public class TutorialScreen 
{
	private boolean show, justChangedState, hasMovedLeft, hasMovedRight, hasJumped;
	private AlbinoBunnyMain main;
	private AlbinoBunnyGame game;
	private TextButton backButton;
	private Label tiltLabel, jumpLabel, dodgeLabel, practiseLabel;
	private Interpolator tilt, jump, dodge, practise;
	
	private enum TutorialState { MOVE, JUMP, DODGE };
	private TutorialState state;
	
	public TutorialScreen(AlbinoBunnyMain main)
	{
		show = false;
		this.main = main;
	}
	
	public void show() 
	{
		show = true;
		main.renderBunny = false;
		state = TutorialState.MOVE;
		justChangedState = false;
		hasMovedLeft = false;
		hasMovedRight = false;
		hasJumped = false;
		
		main.stage.clear();

		FreeTypeFontGenerator gen = Assets.manager.get("graphics/Chunq.ttf", FreeTypeFontGenerator.class);
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = (int) (0.1 * Gdx.graphics.getWidth());
		param.borderWidth = param.size / 15;
		param.borderColor = Color.BLACK;
		param.shadowOffsetX = Gdx.graphics.getWidth() / 100;
		param.shadowOffsetY = Gdx.graphics.getWidth() / 100;
		param.shadowColor = new Color(0, 0, 0, 0.2f);
		
		BitmapFont fnt = gen.generateFont(param);
		
		TextButtonStyle tbStyle = new TextButtonStyle();
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
		
		tiltLabel = new Label("Tilt the screen\nto move", lbStyle);
		jumpLabel = new Label("Tap to jump", lbStyle);
		dodgeLabel = new Label("Dodge falling\nbullets", lbStyle);
		practiseLabel = new Label("Tap to\ntry again", lbStyle);
		
		tiltLabel.setColor(1, 1, 1, 0);
		jumpLabel.setColor(1, 1, 1, 0);
		dodgeLabel.setColor(1, 1, 1, 0);
		practiseLabel.setColor(1, 1, 1, 0);

		main.stage.addActor(backButton);
		main.stage.addActor(tiltLabel);
		main.stage.addActor(jumpLabel);
		main.stage.addActor(dodgeLabel);
		main.stage.addActor(practiseLabel);
		
		tiltLabel.setPosition(
				(Gdx.graphics.getWidth() - tiltLabel.getWidth()) / 2,
				Gdx.graphics.getHeight() * 2/3 - tiltLabel.getHeight() / 3);
		jumpLabel.setPosition(
				(Gdx.graphics.getWidth() - jumpLabel.getWidth()) / 2,
				Gdx.graphics.getHeight() * 2/3 - jumpLabel.getHeight() / 3);
		dodgeLabel.setPosition(
				(Gdx.graphics.getWidth() - dodgeLabel.getWidth()) / 2,
				Gdx.graphics.getHeight() * 2/3 - dodgeLabel.getHeight() / 3);
		practiseLabel.setPosition(
				(Gdx.graphics.getWidth() - practiseLabel.getWidth()) / 2,
				Gdx.graphics.getHeight() * 2/3 - practiseLabel.getHeight() / 3);

		
		tilt = new Interpolator(tiltLabel);
		jump = new Interpolator(jumpLabel);
		dodge = new Interpolator(dodgeLabel);
		practise = new Interpolator(practiseLabel);
		
		tilt.interpolate(Type.Alpha, 1, 0.6f);
		
		main.stage.addActor(tilt);
		main.stage.addActor(jump);
		main.stage.addActor(dodge);
		main.stage.addActor(practise);
		
		Interpolator backInterpolator = new Interpolator(backButton);
		backButton.setX(Gdx.graphics.getWidth() + 1.2f * backButton.getWidth());
		backInterpolator.interpolate(Type.X, Gdx.graphics.getWidth() - backButton.getWidth() * 1.1f, 0.5f);
		main.stage.addActor(backInterpolator);
		
		game = new AlbinoBunnyGame(main.decoration, main.player);
		game.setTutorialMode(true);
		game.dontSave();
		AlbinoBunnyGame.grav /= 1.5f;
		
		Timer.schedule(new Task() {
			public void run() {
				main.player.setControls(Player.TiltControls);
				main.player.setWalkSpeed(Gdx.graphics.getWidth() * 3 / 4f);
			}
		}, 0.2f);
		
		state = TutorialState.MOVE;
	}
	
	public void hide()
	{
		show = false;
		main.renderBunny = true;
		AlbinoBunnyGame.grav *= 1.5f;
		
		final Interpolator backInterpolator = new Interpolator(backButton);
		backInterpolator.interpolate(Type.X, Gdx.graphics.getWidth() + backButton.getWidth() * 1.2f, 0.5f);
		main.stage.addActor(backInterpolator);
		
		tilt.interpolate(Type.Alpha, 0, 0.5f);
		jump.interpolate(Type.Alpha, 0, 0.5f);
		dodge.interpolate(Type.Alpha, 0, 0.5f);
		practise.interpolate(Type.Alpha, 0, 0.5f);
		
		main.ads.hideBannerAd();
	}
	
	private void moveStateUpdate()
	{
		if (main.player.moveLeft())
			Timer.schedule(new Task() {
				public void run() {
					hasMovedLeft = true;
				}
			}, 0.4f);
		if (main.player.moveRight())
			Timer.schedule(new Task() {
				public void run() {
					hasMovedRight = true;
				}
			}, 0.4f);
		if (hasMovedRight && hasMovedLeft) {
			state = TutorialState.JUMP;
			justChangedState = true;
		}
	}
	
	private void jumpStateUpdate()
	{
		if (justChangedState)
		{
			tilt.interpolate(Type.Alpha, 0, 0.5f);
			jump.interpolate(Type.Alpha, 1, 0.5f, 0.5f);
		}
		
		if (main.player.isInAir() && !hasJumped) {
			Timer.schedule(new Task() {
				public void run() {
					state = TutorialState.DODGE;
					justChangedState = true;
				}
			}, 0.4f);
			hasJumped = true;
		}
	}
	
	private void dodgeStateUpdate()
	{
		if (justChangedState)
		{
			jump.interpolate(Type.Alpha, 0, 0.5f);
			dodge.interpolate(Type.Alpha, 1, 0.5f, 0.5f);
			main.ads.showBannerAd();

			Timer.schedule(new Task() {
				public void run() {
					game.setTutorialMode(false);
				}
			}, 1.1f);
			
			Timer.schedule(new Task() {
				public void run() {
					dodge.interpolate(Type.Alpha, 0, 0.5f);
				}
			}, 2f);
			
		}
	}
	
	
	public void render(float delta, SpriteBatch batch)
	{
		if (show)
		{
			game.updateAndRender(delta, batch);
			
			if (state.equals(TutorialState.MOVE))
				moveStateUpdate();
			if (state.equals(TutorialState.JUMP))
				jumpStateUpdate();
			if (state.equals(TutorialState.DODGE))
				dodgeStateUpdate();
			
			justChangedState = false;
			
			if (game.canRespawn() && Gdx.input.justTouched()) {
				practise.interpolate(Type.Alpha, 0, 0.5f);
				game = new AlbinoBunnyGame(main.decoration, game.player);
				game.dontSave();
				game.setDropTime(1.5f);
			}
			

			if (game.didPlayerJustDie()) {
				practise.interpolate(Type.Alpha, 1f, 0.6f);
			}
		}
	}
	
	public boolean isShowing()
	{
		return show;
	}
	
}
