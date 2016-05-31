package com.albinobunny.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.albinobunny.AlbinoBunnyMain;
import com.albinobunny.utils.AdsController;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication implements AdsController
{
	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-1450981991719878/1596625748";
	 
	AdView bannerAd;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		
		View gameView = initializeForView(new AlbinoBunnyMain(this), config);
		setupAds();
		
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
		        ViewGroup.LayoutParams.MATCH_PARENT);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		        ViewGroup.LayoutParams.MATCH_PARENT,
		        ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout.addView(bannerAd, params);
		 
		setContentView(layout);
		
		AdRequest adRequest = new AdRequest.Builder().build();
		bannerAd.loadAd(adRequest);
	}
	
	public void setupAds() {
	    bannerAd = new AdView(this);
	    bannerAd.setVisibility(View.INVISIBLE);
	    bannerAd.setBackgroundColor(0xff000000);
	    bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
	    bannerAd.setAdSize(AdSize.SMART_BANNER);
	}

	@Override
	public void showBannerAd() 
	{
	    runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            bannerAd.setVisibility(View.VISIBLE);
	            AdRequest.Builder builder = new AdRequest.Builder();
	            builder.addTestDevice("393A8C78B26D748E");
	            builder.addTestDevice("97B476D7E48D7AFC");
	            AdRequest ad = builder.build();
	            bannerAd.loadAd(ad);
	        }
	    });
		
	}

	@Override
	public void hideBannerAd() 
	{
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            bannerAd.setVisibility(View.INVISIBLE);
	        }
	    });
	}
	
	@Override
	public boolean isWifiConnected()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		 
		return (ni != null && ni.isConnected());
	}
}
