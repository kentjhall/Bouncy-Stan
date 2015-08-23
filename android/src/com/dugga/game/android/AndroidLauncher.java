package com.dugga.game.android;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.dugga.game.IActivityRequestHandler;
import com.dugga.game.MyGdxGame;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {
	private RelativeLayout layout;
	private View gameView;
	private AdView adView;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the layout
		layout = new RelativeLayout(this);

		// Do the stuff that initialize() would do for you
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		// Create and setup the AdMob view
		adView = new AdView(this);
		adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setBackgroundColor(Color.TRANSPARENT);
		// Add the AdMob view
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		adView.setLayoutParams(adParams);
		layout.addView(adView);

		// Create the libgdx View
		gameView = initializeForView(new MyGdxGame(this));
		layout.addView(gameView);

		// Hook it all up
		setContentView(layout);
		layout.bringChildToFront(adView);
	}

	private final int LOAD_ADS=2;
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;

	protected Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case LOAD_ADS:
				{
					adView.loadAd(new AdRequest.Builder().build());
					adView.setVisibility(AdView.GONE);
					break;
				}
				case SHOW_ADS:
				{
					adView.setVisibility(AdView.VISIBLE);
					break;
				}
				case HIDE_ADS:
				{
					adView.destroy();
					break;
				}
			}
		}
	};

	@Override
	public void showAds(adState show) {
		switch (show){
			case LOAD:
				handler.sendEmptyMessage(LOAD_ADS);
				break;
			case SHOW:
				handler.sendEmptyMessage(SHOW_ADS);
				break;
			case HIDE:
				handler.sendEmptyMessage(HIDE_ADS);
				break;
		}
	}
}
