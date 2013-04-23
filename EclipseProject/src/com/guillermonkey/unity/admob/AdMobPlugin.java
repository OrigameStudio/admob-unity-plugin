/**
 *
 * This file is part of AdMobPlugin
 * 
 * Copyright (c) 2013 Guillermo Calvo
 *
 * AdMobPlugin is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AdMobPlugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License (http://www.gnu.org/copyleft/lesser.html)
 * for more details.
 *
 */

package com.guillermonkey.unity.admob;


import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;

import com.unity3d.player.UnityPlayer;


public class AdMobPlugin{

	static String LOGTAG = "AdMobPlugin";

	private static final AdMobPlugin instance = new AdMobPlugin();

	private Activity			activity;
	private AdMobConfiguration	config;
	private AdMobTarget			target;
	private LinearLayout		layout;
	private AdView				view;
	private int					received;
	private ErrorCode			lastError;

	private Runnable CONF = new Runnable(){ @Override public void run(){ _conf(); } };
	private Runnable SHOW = new Runnable(){ @Override public void run(){ _show(); } };
	private Runnable HIDE = new Runnable(){ @Override public void run(){ _hide(); } };
	private Runnable LOAD = new Runnable(){ @Override public void run(){ _load(); } };
	private AdListener AD_LISTENER = new AdListener(){

		@Override
		public void onReceiveAd(Ad ad){

			received++;

			Log.i(AdMobPlugin.LOGTAG, "Ad received (total: " + received + ")");
		}

		@Override
		public void onFailedToReceiveAd(Ad ad, ErrorCode errorCode){

			Log.e(AdMobPlugin.LOGTAG, "Failed to receive ad: " + errorCode);

			lastError = errorCode;
		}

		@Override
		public void onPresentScreen(Ad ad){

			//Log.d(AdMobPlugin.LOGTAG, "On present screen");
		}

		@Override
		public void onDismissScreen(Ad ad){

			//Log.d(AdMobPlugin.LOGTAG, "On dismiss screen");
		}

		@Override
		public void onLeaveApplication(Ad ad){

			//Log.d(AdMobPlugin.LOGTAG, "On leaving application");
		}
	};

	public static AdMobPlugin getInstance(
		String publisherId,
		boolean isTesting,
		String[] testDeviceIds,
		boolean guessSelfDeviceId,
		int size,
		int orientation,
		int horizontalPosition,
		int verticalPosition
	){

		if(AdMobPlugin.instance.config == null){

			Log.d(AdMobPlugin.LOGTAG, "Initializing...");

			AdMobPlugin.instance.activity	= UnityPlayer.currentActivity;
			AdMobPlugin.instance.config		= new AdMobConfiguration(publisherId, isTesting, testDeviceIds, guessSelfDeviceId, size, orientation, horizontalPosition, verticalPosition);

			AdMobPlugin.instance.activity.runOnUiThread(AdMobPlugin.instance.CONF);

			Log.i(AdMobPlugin.LOGTAG, "Initialized.");
		}

		return(AdMobPlugin.instance);
	}

	private AdMobPlugin(){

		/* ... */
	}

	public void reconfigure(
		String publisherId,
		boolean isTesting,
		String[] testDeviceIds,
		boolean guessSelfDeviceId,
		int size,
		int orientation,
		int horizontalPosition,
		int verticalPosition
	){

		Log.d(AdMobPlugin.LOGTAG, "Reconfiguring... ");

		this.config = new AdMobConfiguration(publisherId, isTesting, testDeviceIds, guessSelfDeviceId, size, orientation, horizontalPosition, verticalPosition);

		this.activity.runOnUiThread(this.CONF);

		Log.i(AdMobPlugin.LOGTAG, "Reconfigured: " + this.config);
	}

	private void _conf(){

		boolean uninitialized = (this.layout == null);

		Log.d(AdMobPlugin.LOGTAG, "Config: " + this.config);

		try{

			if(uninitialized){

				this.layout		= new LinearLayout(this.activity);
			}

			if(this.view != null){

				Log.d(AdMobPlugin.LOGTAG, "Removing previous AdView...");

				this.layout.removeViewInLayout(this.view);
			}

			Log.d(AdMobPlugin.LOGTAG, "Setting up the layout...");

			this.layout.setOrientation(config.orientation);
			this.layout.setGravity(config.gravity);

			Log.d(AdMobPlugin.LOGTAG, "Creating new AdView...");

			this.view = new AdView(activity, config.size, config.publisherId);

			Log.d(AdMobPlugin.LOGTAG, "Setting up ad listener...");

			this.view.setAdListener(this.AD_LISTENER);

			Log.d(AdMobPlugin.LOGTAG, "Adding the view to the layout...");

			// Add the view to the layout
			this.layout.addView(this.view);

			Log.d(AdMobPlugin.LOGTAG, "View added.");

			if(uninitialized){

				this.activity.addContentView( this.layout, new LayoutParams(-1, -1) );
			}

		}catch(Exception error){

			Log.e(AdMobPlugin.LOGTAG, "Unexpected error while applying config: " + error);

			error.printStackTrace();
		}

		Log.d(AdMobPlugin.LOGTAG, "Config OK.");
	}

	public void show(){

		Log.d(AdMobPlugin.LOGTAG, "Showing ad view...");

		this.activity.runOnUiThread(this.SHOW);
	}

	private void _show(){

		try{

			this.view.setVisibility(View.VISIBLE);

		}catch(Exception error){

			Log.e(AdMobPlugin.LOGTAG, "Unexpected error while showing ad view: " + error);

			error.printStackTrace();
		}

		Log.i(AdMobPlugin.LOGTAG, "Ad view shown.");
	}

	public void hide(){

		Log.d(AdMobPlugin.LOGTAG, "Hiding ad view...");

		this.activity.runOnUiThread(this.HIDE);
	}

	private void _hide(){

		try{

			this.view.setVisibility(View.GONE);

		}catch(Exception error){

			Log.e(AdMobPlugin.LOGTAG, "Unexpected error while hiding ad view: " + error);

			error.printStackTrace();
		}

		Log.i(AdMobPlugin.LOGTAG, "Ad view hidden.");
	}

	public void load(){

		Log.d(AdMobPlugin.LOGTAG, "Loading Ad...");

		this.activity.runOnUiThread(this.LOAD);
	}

	private void _load(){

		try{

			// Create the ad request
			AdRequest adRequest = new AdRequest();

			if( this.config.isTesting ){

				// Add this just to make sure that we are in test mode
				adRequest.addTestDevice(AdRequest.TEST_EMULATOR);

				Log.d(AdMobPlugin.LOGTAG, "Added dummy device ID: TEST_DEVICE_ID");

				if(this.config.guessSelfDeviceId && this.config.selfDeviceId != null){

					adRequest.addTestDevice(this.config.selfDeviceId);

					Log.d(AdMobPlugin.LOGTAG, "Added self device ID: " + this.config.selfDeviceId);
				}

				if(this.config.testDeviceIds != null){

					for(String testDeviceId : this.config.testDeviceIds){

						adRequest.addTestDevice(testDeviceId);

						Log.d(AdMobPlugin.LOGTAG, "Added test device ID: " + testDeviceId);
					}
				}

				if(this.target != null){

					if(this.target.birthday != null){

						adRequest.setBirthday(this.target.birthday);
					}

					if(this.target.gender != null){

						adRequest.setGender(this.target.gender);
					}

					if(this.target.location != null){

						adRequest.setLocation(this.target.location);
					}

					if(this.target.keywords != null){

						adRequest.setKeywords(this.target.keywords);
					}
				}
			}

			// Load the ad
			this.view.loadAd(adRequest);

			Log.i(AdMobPlugin.LOGTAG, "Ad request sent.");

		}catch(Exception error){

			Log.e(AdMobPlugin.LOGTAG, "Unexpected error while loading ad: " + error);

			error.printStackTrace();
		}
	}

	public String getLastError(){

		String error;

		if(this.lastError == null){

			//Log.d(AdMobPlugin.LOGTAG, "Last error: no error");

			return(null);
		}

		error = this.lastError.toString();

		Log.i(AdMobPlugin.LOGTAG, "Last error: " + error);

		this.lastError = null;

		return(error);
	}

	public int getReceived(){

		//Log.i(AdMobPlugin.LOGTAG, "Getting number of ads received: " + this.received);

		return(this.received);
	}

	public void setTarget(int gender, int birthYear, int birthMonth, int birthDay, String[] keywords, double latitude, double longitude, double altitude){

		Log.d(AdMobPlugin.LOGTAG, "Setting target... ");

		try{

			this.target = new AdMobTarget(gender, birthYear, birthMonth, birthDay, keywords, latitude, longitude, altitude);

		}catch(Exception error){

			Log.e(AdMobPlugin.LOGTAG, "Unexpected error while setting target: " + error);

			error.printStackTrace();
		}

		Log.i(AdMobPlugin.LOGTAG, "Target set: " + this.target);
	}
}
