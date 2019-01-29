package com.example.nicolaebogdan.smartcity;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.example.nicolaebogdan.smartcity.common.SmartCityPreferences;

public class SmartCityApp extends Application {

	private static final String APP_TAG = "FoodWellApp";
	private static final String FOODWELL_SHARED_PREFS_KEY = "SKIN_SIGHT_SHARED_PREFS_KEY";

	protected static SmartCityApp currentApplication;
	private SmartCityPreferences smartCityPreferences;
	private boolean isDebug;

	public SmartCityApp(){
		currentApplication = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		smartCityPreferences = new SmartCityPrefImpl(getSharedPreferences(FOODWELL_SHARED_PREFS_KEY, Context.MODE_PRIVATE));
		isDebug = BuildConfig.BUILD_TYPE.equals("debug");
	}

	public static SmartCityApp getCurrentApplication() {
		return currentApplication;
	}

	public static String getStringResource(@StringRes int resId){
		return currentApplication.getString(resId);
	}

	public static String getStringResource(@StringRes int resId, Object... args){
		return currentApplication.getString(resId, args);
	}

	public static int getColorResource(@ColorRes int colorId){
		return currentApplication.getColor(colorId);
	}

	public static Drawable getDrawableResource(@DrawableRes int resId){
		return currentApplication.getDrawable(resId);
	}

	public static void notifyWithToast(String message, int duration){
		Toast.makeText(getCurrentApplication(), message, duration).show();
	}

	public static void notifyDebugWithToast(String message, int duration){
		if (currentApplication.isDebug)
			Toast.makeText(getCurrentApplication(), message, duration).show();
	}

	public SmartCityPreferences getSmartCityPreferences() {
		return smartCityPreferences;
	}


}
