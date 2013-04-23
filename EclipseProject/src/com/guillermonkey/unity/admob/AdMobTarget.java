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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.ads.AdRequest.Gender;


class AdMobTarget{

	public final Gender			gender;
	public final Date			birthday;
	public final Location		location;
	public final Set<String>	keywords;

	AdMobTarget(int gender, int birthYear, int birthMonth, int birthDay, String[] keywords, double latitude, double longitude, double altitude){

		this.gender		= AdMobTarget.parseGender(gender);
		this.birthday	= AdMobTarget.parseBirthday(birthYear, birthMonth, birthDay);
		this.keywords	= AdMobTarget.parseKeywords(keywords);
		this.location	= AdMobTarget.parseLocation(latitude, longitude, altitude);
	}

	private static Gender parseGender(int gender){

		Log.d(AdMobPlugin.LOGTAG, "Parsing gender (" + gender + ")...");

		switch(gender){

			case 1:
				return(Gender.MALE);

			case 2:
				return(Gender.FEMALE);
		}

		Log.w(AdMobPlugin.LOGTAG, "Unknown gender: " + gender + "! Resolving to UNKNOWN...");

		return(Gender.UNKNOWN);
	}

	private static Date parseBirthday(int year, int month, int day){

		Date birthday = null;

		Log.d(AdMobPlugin.LOGTAG, "Parsing birthday (year: " + year + ", month: " + month + ", day: " + day + ")...");

		try{

			birthday = new GregorianCalendar(year, month, day).getTime();

		}catch(Exception error){

			Log.e(AdMobPlugin.LOGTAG, "Could not parse birthday (year: " + year + ", month: " + month + ", day: " + day + "): " + error);
		}

		return(birthday);
	}

	private static Set<String> parseKeywords(String[] keywords){

		Set<String> set = new HashSet<String>();

		Log.d(AdMobPlugin.LOGTAG, "Parsing keywords (" + Arrays.toString(keywords) + ")...");

		for(String keyword : keywords){

			if(keyword != null && !set.contains(keyword)){

				Log.d(AdMobPlugin.LOGTAG, "Parsed keyword: " + keyword);

				set.add(keyword);
			}
		}

		return(set);
	}

	private static Location parseLocation(double latitude, double longitude, double altitude){

		Location location = null;

		Log.d(AdMobPlugin.LOGTAG, "Parsing location (lat: " + latitude + ", lon: " + longitude + ", alt: " + altitude + ")...");

		try{
			if(latitude != Double.NaN || longitude != Double.NaN || altitude != Double.NaN){

				location = new Location(LocationManager.PASSIVE_PROVIDER);

				if(latitude != Double.NaN){

					location.setLatitude(latitude);
				}

				if(longitude != Double.NaN){

					location.setLongitude(longitude);
				}

				if(altitude != Double.NaN){

					location.setAltitude(altitude);
				}
			}
		}catch(Exception error){

			Log.e(AdMobPlugin.LOGTAG, "Could not parse location (lat: " + latitude + ", lon: " + longitude + ", alt: " + altitude + "): " + error);
		}

		return(location);
	}

	@Override
	public String toString(){
		return(
			"AdMobTarget{" +
				"birthday: " + birthday + ", " +
				"gender: " + gender + ", " +
				"location: " + location + ", " +
				"keywords: " + keywords +
			"}"
		);
	}
}
