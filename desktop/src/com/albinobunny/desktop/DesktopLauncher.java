package com.albinobunny.desktop;

import com.albinobunny.AlbinoBunnyMain;
import com.albinobunny.utils.AdsController;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 400;
		config.height = 500;
		
		AdsController ads = new AdsController() {

			@Override
			public void showBannerAd() {}

			@Override
			public void hideBannerAd() {}
			
			@Override
			public boolean isWifiConnected() { return false; }
		};
		
		new LwjglApplication(new AlbinoBunnyMain(ads), config);
	}
}
