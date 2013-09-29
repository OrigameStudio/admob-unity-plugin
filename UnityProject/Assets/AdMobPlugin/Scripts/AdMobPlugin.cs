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


using UnityEngine;
using System.Collections;


public class AdMobPlugin : MonoBehaviour{

	public string				publisherId			= "YOUR_PUBLISHER_ID";
	public bool					isTesting			= true;
	public string[]				testDeviceIds		= {"TEST_DEVICE_ID"};
	public bool					guessSelfDeviceId	= true;
	public AdSize				size				= AdSize.BANNER;
	public AdOrientation		orientation			= AdOrientation.HORIZONTAL;
	public AdHorizontalPosition	horizontalPosition	= AdHorizontalPosition.CENTER_HORIZONTAL;
	public AdVerticalPosition	verticalPosition	= AdVerticalPosition.BOTTOM;
	public float				refreshInterval		= 30;
	public bool					loadOnStart			= true;
	public bool					setTargetOnStart	= false;
	public bool					loadOnReconfigure	= true;
	public AdMobTarget			target				= new AdMobTarget();

	private bool visible = true;

	void Start(){

		this.Initialize();

		if(this.loadOnStart){

			this.Load();
		}

		if(this.setTargetOnStart){

			this.SetTarget();
		}

		this.StartCoroutine( this.Refresh() );
	}

#if UNITY_ANDROID && !UNITY_EDITOR

	private AndroidJavaObject plugin;

	private void Initialize(){

		AndroidJavaClass pluginClass = new AndroidJavaClass("com.guillermonkey.unity.admob.AdMobPlugin");

		this.plugin	= pluginClass.CallStatic<AndroidJavaObject>(
			"getInstance",
			this.publisherId,
			this.isTesting,
			this.testDeviceIds,
			this.guessSelfDeviceId,
			(int)this.size,
			(int)this.orientation,
			(int)this.horizontalPosition,
			(int)this.verticalPosition
		);
	}

	public void Reconfigure(){

		this.plugin.Call(
			"reconfigure",
			this.publisherId,
			this.isTesting,
			this.testDeviceIds,
			this.guessSelfDeviceId,
			(int)this.size,
			(int)this.orientation,
			(int)this.horizontalPosition,
			(int)this.verticalPosition
		);

		if(this.loadOnReconfigure){

			this.Load();
		}
	}

	public void SetTarget(){

		this.plugin.Call(
			"setTarget",
			(int)this.target.gender,
			this.target.birthday.year,
			(int)this.target.birthday.month,
			this.target.birthday.day,
			this.target.keywords,
			this.target.location.latitude,
			this.target.location.longitude,
			this.target.location.altitude
		);
	}

	public void Load(){

		this.plugin.Call("load");

		this.Show();
	}

	public void Show(){

		this.plugin.Call("show");

		this.visible = true;
	}

	public void Hide(){

		this.plugin.Call("hide");

		this.visible = false;
	}

	public string GetLastError(){

		return( this.plugin.Call<string>("getLastError") );
	}

	public int GetReceived(){

		return( this.plugin.Call<int>("getReceived") );
	}

	void OnDestroy(){

		this.Hide();
	}

#else

	private int received;

	private void Initialize(){

		if(!this.isTesting){

			Debug.LogWarning("AdMobPlugin is NOT in test mode. Please make sure you do not request invalid impressions while testing your application.");
		}
	}

	public void Reconfigure(){

		print("AdMobPlugin.Reconfigure()");
	}

	public void SetTarget(){

		print("AdMobPlugin.SetTarget()");
	}

	public void Load(){

		print("AdMobPlugin.Load()");

		this.received++;
	}

	public void Show(){

		print("AdMobPlugin.Show()");

		this.visible = true;
	}

	public void Hide(){

		print("AdMobPlugin.Hide()");

		this.visible = false;
	}

	public string GetLastError(){

		//print("AdMobPlugin.GetLastError()");

		return(null);
	}

	public int GetReceived(){

		//print("AdMobPlugin.GetReceived()");

		return(this.received);
	}

#endif

	public bool IsVisible(){

		return(this.visible);
	}

	private IEnumerator Refresh(){

		while(true){

			if(this.refreshInterval > 0){

				yield return new WaitForSeconds(this.refreshInterval < 30 ? 30 : this.refreshInterval);

				this.Load();
			}
		}
	}
}

/*
 * helper classes and enums
 */

public enum AdSize{ BANNER, IAB_MRECT, IAB_BANNER, IAB_LEADERBOARD, SMART_BANNER };
public enum AdOrientation{ HORIZONTAL, VERTICAL };
public enum AdHorizontalPosition{ CENTER_HORIZONTAL, LEFT, RIGHT };
public enum AdVerticalPosition{ CENTER_VERTICAL, TOP, BOTTOM };
public enum AdGender{ UNKNOWN, MALE, FEMALE };
public enum AdMonth{ JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER };

[System.Serializable]
public class AdDateTime{

	public int			year;
	public AdMonth		month;
	public int			day;
}

[System.Serializable]
public class AdLocation{

	public double		latitude	= double.NaN;
	public double		longitude	= double.NaN;
	public double		altitude	= double.NaN;
}

[System.Serializable]
public class AdMobTarget{

	public AdGender		gender		= AdGender.UNKNOWN;
	public AdDateTime	birthday	= new AdDateTime();
	public string[]		keywords	= new string[0];
	public AdLocation	location	= new AdLocation();
}
