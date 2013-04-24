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


import java.util.Arrays;

import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.google.ads.AdSize;


class AdMobConfiguration{

	final String	publisherId;
	final boolean	isTesting;
	final String[]	testDeviceIds;
	final boolean	guessSelfDeviceId;
	final String	selfDeviceId;
	final AdSize	size;
	final int		orientation;
	final int		gravity;

	AdMobConfiguration(
		String publisherId,
		boolean isTesting,
		String[] testDeviceIds,
		boolean guessSelfDeviceId,
		int size,
		/*
	        0: BANNER				Phones and Tablets/Standard Banner(320x50)
	        1: IAB_MRECT			Tablets/IAB Medium Rectangle(300x250)
	        2: IAB_BANNER			Tablets/IAB Full-Size Banner(468x60)
	        3: IAB_LEADERBOARD		Tablets/IAB Leaderboard(728x90)
	        4: SMART_BANNER			Phones and Tablets/Smart Banner(Device will decide)
		 */
		int orientation,
		int horizontalPosition,
		/*
			0: CENTER_HORIZONTAL
			1: LEFT
			2: RIGHT
		*/
		int verticalPosition
		/*
			0: CENTER_VERTICAL
			1: TOP
			2: BOTTOM
		 */
	){
		this.publisherId		= publisherId;
		this.isTesting			= isTesting;
		this.testDeviceIds		= testDeviceIds;
		this.guessSelfDeviceId	= guessSelfDeviceId;
		this.selfDeviceId		= (guessSelfDeviceId ? AdMobUtil.guessSelfDeviceId() : null);
		this.size				= AdMobConfiguration.parseAdSize(size);
		this.orientation		= AdMobConfiguration.parseOrientation(orientation);
		this.gravity			= AdMobConfiguration.parseGravity(horizontalPosition, verticalPosition);
	}

	private static AdSize parseAdSize(int size){

		switch(size){
			case 0: return(AdSize.BANNER);
			case 1: return(AdSize.IAB_MRECT);
			case 2: return(AdSize.IAB_BANNER);
			case 3: return(AdSize.IAB_LEADERBOARD);
			case 4: return(AdSize.SMART_BANNER);
		}

		Log.w(AdMobPlugin.LOGTAG, "Unknown banner size: " + size + "! Resolving to BANNER...");

		return(AdSize.BANNER);
	}

	private static int parseOrientation(int orientation){

		switch(orientation){

			case 0:
				return(LinearLayout.HORIZONTAL);

			case 1:
				return(LinearLayout.VERTICAL);

		}

		Log.w(AdMobPlugin.LOGTAG, "Unknown orientation: " + orientation + "! Resolving to HORIZONTAL...");

		return(LinearLayout.HORIZONTAL);
	}

	private static int parseGravity(int horizontalPosition, int verticalPosition){

		int gravity = Gravity.NO_GRAVITY;

		switch(horizontalPosition){

			case 0:
				gravity = Gravity.CENTER_HORIZONTAL;
				break;

			case 1:
				gravity = Gravity.LEFT;
				break;

			case 2:
				gravity = Gravity.RIGHT;
				break;

			default:

				Log.w(AdMobPlugin.LOGTAG, "Unknown horizontal position: " + horizontalPosition + "! Resolving to CENTER_HORIZONTAL...");
				gravity = Gravity.CENTER_HORIZONTAL;
		}

		switch(verticalPosition){

			case 0:
				gravity |= Gravity.CENTER_VERTICAL;
				break;

			case 1:
				gravity |= Gravity.TOP;
				break;

			case 2:
				gravity |= Gravity.BOTTOM;
				break;

			default:

				Log.w(AdMobPlugin.LOGTAG, "Unknown vertical position: " + horizontalPosition + "! Resolving to CENTER_VERTICAL...");
				gravity = Gravity.BOTTOM;
		}

		return(gravity);
	}

	@Override
	public String toString(){

		String tmp;

		// Work around SMART_BANNER bug
		try{

			tmp = this.size.toString();

		}catch(Exception error){

			tmp = ( this.size == AdSize.SMART_BANNER ? "SMART_BANNER" : "???" );
		}

		return(
			"AdMobConfiguration{" +
				"publisherId: " + this.publisherId + ", " +
				"isTesting: " + this.isTesting + ", " +
				"testDeviceIds: " + Arrays.toString( this.testDeviceIds ) + ", " +
				"guessSelfDeviceId: " + this.guessSelfDeviceId + (this.guessSelfDeviceId ? " (" + this.selfDeviceId + "), " : ", ") +
				"size: " + tmp + ", " +
				"orientation: " + this.orientation + ", " +
				"gravity: " + this.gravity +
			"}"
		);
	}
}
