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
using System.Collections.Generic;


[ExecuteInEditMode]
[RequireComponent( typeof(AdMobPlugin) )]
public class AdMobPluginMockup : MonoBehaviour{

	public bool				executeInEditMode	= true;
	public AdMockupStyle	style				= AdMockupStyle.DARK;
	public string[]			texts				= AdMobPluginMockup.quotes;
	public Texture[]		icons				= new Texture[0];
	public Texture[]		actions				= new Texture[0];
	public Texture			darkBackground		= null;
	public Texture			lightBackground		= null;
	public AdMockup			currentAd			= null;

	private AdMobPlugin plugin = null;
	private int currentAdId = -1;

	void Start(){

		this.plugin		= this.GetComponent<AdMobPlugin>();

		this.GenerateRandomAd();
	}

	void Update(){

		int received = this.plugin.GetReceived();

		if(this.currentAdId != received){

			currentAdId = received;

			this.GenerateRandomAd();
		}
	}

    void OnGUI(){

		if( this.plugin.IsVisible() && Application.isEditor && (Application.isPlaying || this.executeInEditMode) ){

			if(this.currentAd == null){

				this.GenerateRandomAd();
			}

			this.DrawAd();
		}

	}

	private Rect GetAdRect(AdSize size, AdHorizontalPosition horizontalPosition, AdVerticalPosition verticalPosition){

		float x = 0, y = 0, width = 0, height = 0;

		switch(size){

			case AdSize.BANNER:
				width	= 320;
				height	= 50;
				break;

			case AdSize.IAB_BANNER:
				width	= 300;
				height	= 250;
				break;

			case AdSize.IAB_LEADERBOARD:
				width	= 486;
				height	= 60;
				break;

			case AdSize.IAB_MRECT:
				width	= 728;
				height	= 90;
				break;

			case AdSize.SMART_BANNER:
				width	= Screen.width;
				height	= width / 6.4f;
				break;
		}

		if(width > Screen.width){

			width = Screen.width;
		}

		if(height > Screen.height){

			height = Screen.height;
		}

		switch(horizontalPosition){

			case AdHorizontalPosition.CENTER_HORIZONTAL:
				x = (Screen.width / 2) - (width / 2);
				break;

			case AdHorizontalPosition.LEFT:
				x = 0;
				break;

			case AdHorizontalPosition.RIGHT:
				x = Screen.width - width;
				break;
		}

		switch(verticalPosition){

			case AdVerticalPosition.CENTER_VERTICAL:
				y = (Screen.height / 2) - (height / 2);
				break;

			case AdVerticalPosition.TOP:
				y = 0;
				break;

			case AdVerticalPosition.BOTTOM:
				y = Screen.height - height;
				break;
		}

		return( new Rect(x, y, width, height) );
	}

    private void DrawAd(){

		Rect	backgroundRect	= this.GetAdRect(this.plugin.size, this.plugin.horizontalPosition, this.plugin.verticalPosition);
		Rect	iconRect		= new Rect(backgroundRect.x + 4, backgroundRect.y + 4, 38, 38);
		Rect	actionRect		= new Rect(backgroundRect.x + backgroundRect.width - 34, backgroundRect.y + 4, 30, 30);
		Rect	textRect		= new Rect(backgroundRect.x + 4 + 38 + 4, backgroundRect.y + 4, backgroundRect.width - 4 - 38 - 4 - 4 - 30 - 4, backgroundRect.height - 8);
		Texture	background		= this.GetBackground();
		Color	textColor		= this.GetTextColor();

		if(background != null){

			GUI.DrawTexture(backgroundRect, background);

		}else{

			GUI.Box(backgroundRect, (this.currentAd.text == null ? "AD MOCK-UP" : null) );
		}

		if(this.currentAd.icon != null){

			GUI.DrawTexture(iconRect, this.currentAd.icon);
		}

		if(this.currentAd.action != null){

			GUI.DrawTexture(actionRect, this.currentAd.action);
		}

		if(this.currentAd.text != null){

			GUIStyle textStyle = new GUIStyle();

			textStyle.normal.textColor	= Color.black;
			textStyle.fontStyle			= FontStyle.Bold;
			textStyle.wordWrap			= true;
			textStyle.alignment			= TextAnchor.MiddleCenter;
			textStyle.normal.textColor	= textColor;

			GUI.Label(textRect, this.currentAd.text, textStyle);
		}
	}

	private T GetRandomElement<T>(T[] array) where T : class{

		int index, length;

		length = ( array == null ? 0 : array.Length );

		index = Random.Range(0, length);

		return(length == 0 ? null : array[index]);
	}

	private void GenerateRandomAd(){

		this.currentAd = new AdMockup{
			icon	= this.GetRandomElement(this.icons),
			action	= this.GetRandomElement(this.actions),
			text	= this.GetRandomElement(this.texts)
		};
	}

	private Texture GetBackground(){

		return( this.style == AdMockupStyle.DARK ? this.darkBackground : this.lightBackground );
	}

	private Color GetTextColor(){

		return(this.style == AdMockupStyle.DARK ? Color.white : Color.black);
	}

	private static string[] quotes = new string[]{

		"Bugs that go away by themselves come back by themselves.",
		"When in doubt, use brute force.",
		"Deleted code is debugged code.",
		"Premature optimization is the root of all evil.",
		"Simplicity is the ultimate sophistication.",
		"With diligence it is possible to make anything run slowly.",
		"Simplicity carried to the extreme becomes elegance.",
		"The best is the enemy of the good.",
		"A data structure is just a stupid programming language.",
		"Software gets slower faster than hardware gets faster.",
		"If it doesn't work, it doesn't matter how fast it doesn't work.",
		"If it works, it's obsolete.",
		"The common language of programmers is Profanity.",
		"There is no place like 127.0.0.1.",
		"The code is 100% complete, it just doesn't work yet.",
		"Programming is hard, let's go shopping."
	};
}


/*
 * helper classes and enums
 */

public class AdMockup{

	public Texture	icon;
	public Texture	action;
	public string	text;
}

public enum AdMockupStyle{ DARK, LIGHT };
