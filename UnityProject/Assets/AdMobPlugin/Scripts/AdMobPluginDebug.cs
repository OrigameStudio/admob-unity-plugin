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


[ExecuteInEditMode]
[RequireComponent( typeof(AdMobPlugin) )]
public class AdMobPluginDebug : MonoBehaviour{

	public AdMobPluginDebugRects rectangles = new AdMobPluginDebugRects();

	private AdMobPlugin plugin = null;
	private string lastError = null;

	void Start(){

		this.plugin = this.GetComponent<AdMobPlugin>();
	}

    void OnGUI(){

		bool changed = false;
		bool targeted = false;

        if(GUI.Button(this.rectangles.loadButton, "LOAD")){

			this.plugin.Load();
		}

        if(GUI.Button(this.rectangles.exitButton, "EXIT")){

			Application.Quit();
		}

        if(GUI.Button(this.rectangles.showButton, "SHOW")){

			this.plugin.Show();
		}

        if(GUI.Button(this.rectangles.hideButton, "HIDE")){

			this.plugin.Hide();
		}

        if(GUI.Button(this.rectangles.horizontalPositionButton, this.plugin.horizontalPosition.ToString())){

			changed = true;

			this.plugin.horizontalPosition++;

			if(this.plugin.horizontalPosition > AdHorizontalPosition.RIGHT){

				this.plugin.horizontalPosition = AdHorizontalPosition.CENTER_HORIZONTAL;
			}
		}

		if(GUI.Button(this.rectangles.verticalPositionButton, this.plugin.verticalPosition.ToString())){

			changed = true;

			this.plugin.verticalPosition++;

			if(this.plugin.verticalPosition > AdVerticalPosition.BOTTOM){

				this.plugin.verticalPosition = AdVerticalPosition.CENTER_VERTICAL;
			}
		}

		if(GUI.Button(this.rectangles.orientationButton, this.plugin.orientation.ToString())){

			changed = true;

			plugin.orientation++;

			if(plugin.orientation > AdOrientation.VERTICAL){

				plugin.orientation = AdOrientation.HORIZONTAL;
			}
		}

		if(GUI.Button(this.rectangles.genderButton, this.plugin.target.gender.ToString())){

			targeted = true;

			plugin.target.gender++;

			if(plugin.target.gender > AdGender.FEMALE){

				plugin.target.gender = AdGender.UNKNOWN;
			}
		}

		if(GUI.Button(this.rectangles.sizeButton, plugin.size.ToString())){

			changed = true;

			this.plugin.size++;

			if(this.plugin.size > AdSize.SMART_BANNER){

				this.plugin.size = AdSize.BANNER;
			}
		}

		if(changed){

			this.plugin.Reconfigure();
		}

		if(targeted){

			this.plugin.SetTarget();
		}

		string tmp;

		tmp = this.plugin.GetLastError();

		if(tmp != null){

			this.lastError = tmp;
		}

		if(this.lastError != null && this.lastError.Length > 0){

			if(GUI.Button(this.rectangles.lastErrorRectButton, this.lastError)){

				this.lastError = null;
			}
		}else{
			GUI.Label(this.rectangles.receivedLabel, this.plugin.GetReceived() + " ad(s) loaded so far (" + System.DateTime.Now + ")");
		}
	}
}


/*
 * helper class
 */

[System.Serializable]
public class AdMobPluginDebugRects{
	
	public Rect loadButton				= new Rect(32, 32, 64, 48);
	public Rect exitButton				= new Rect(104, 32, 64, 48);
	public Rect showButton				= new Rect(32, 88, 64, 48);
	public Rect hideButton				= new Rect(104, 88, 64, 48);
	public Rect horizontalPositionButton= new Rect(176, 32, 240, 48);
	public Rect verticalPositionButton	= new Rect(176, 88, 240, 48);
	public Rect orientationButton		= new Rect(176, 144, 240, 48);
	public Rect genderButton			= new Rect(32, 144, 136, 48);
	public Rect sizeButton				= new Rect(176, 200, 240, 50);
	public Rect lastErrorRectButton		= new Rect(32, 256, 384, 50);
	public Rect receivedLabel			= new Rect(32, 256, 384, 50);
}
