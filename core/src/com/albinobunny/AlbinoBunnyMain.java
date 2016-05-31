package com.albinobunny;

import java.io.IOException;

import com.albinobunny.game.Player;
import com.albinobunny.screens.LoadingScreen;
import com.albinobunny.screens.MainMenu;
import com.albinobunny.screens.PlayScreen;
import com.albinobunny.screens.StatsScreen;
import com.albinobunny.screens.TutorialScreen;
import com.albinobunny.utils.AdsController;
import com.albinobunny.utils.MusicPlayer;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class AlbinoBunnyMain extends ApplicationAdapter 
{
	public MainMenu			mainMenu;
	public PlayScreen		playScreen;
	public StatsScreen		statsScreen;
	public TutorialScreen 	tutorialScreen;
	public LoadingScreen	loadingScreen;
	public Player			player;
	public Decoration		decoration;
	public SpriteBatch		batch;
	public Stage			stage;
	public AdsController 	ads;
	public boolean			renderBunny, loaded;
	
	public AlbinoBunnyMain(AdsController ads)
	{
		this.ads = ads;
		renderBunny = true;
	}
	
	@Override
	public void create()
	{
		loadingScreen = new LoadingScreen(this);
		loaded = false;
	}
	
	public void init()
	{
		if (!Gdx.app.getType().equals(ApplicationType.Desktop))
			MusicPlayer.init();
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		batch = new SpriteBatch();
		
		decoration = new Decoration();
		player = new Player(decoration.floor);
		
		player.setControls(Player.RandomControls);
		player.setWalkSpeed(Gdx.graphics.getWidth() / 4f);
		mainMenu = new MainMenu(this);
		playScreen = new PlayScreen(this, decoration, ads);
		statsScreen = new StatsScreen(this);
		tutorialScreen = new TutorialScreen(this);
		
		mainMenu.show(player);
		
		if (!Gdx.files.local("data.dat").exists()) 
		{
			Data data = new Data();
			try {
				Data.saveData(data);
			} catch (IOException e) {
				Gdx.app.exit();
				e.printStackTrace();
			}
		}
		loaded = true;
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0.227f, 0.106f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		float delta = Gdx.graphics.getDeltaTime();
		
		if (loaded) {
			
			batch.begin();
			decoration.renderBackground(batch);

			playScreen.render(delta, batch);
			tutorialScreen.render(delta, batch);

			if (renderBunny)
			{
				player.render(batch);
				player.update(delta);
			}
			
			decoration.renderForeground(batch);
			batch.end();

			stage.draw();
			stage.act();
		}
		else
		{
			loadingScreen.render(delta);
		}
		
	}

}