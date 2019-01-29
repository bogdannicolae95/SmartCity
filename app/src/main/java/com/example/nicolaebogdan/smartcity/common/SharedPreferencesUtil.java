package com.example.nicolaebogdan.smartcity.common;

import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public interface SharedPreferencesUtil {

	//get values

	/*
	return (!sharedPreferencesStore.contains(key))?null:
		actually means that in case the key does not exist than a null will be returned and not a dummy default value
		the actual business implementation must resolve its fallback default value

	 */

	static String getString(SharedPreferences sharedPreferencesStore, String key, String defValue) {
		return sharedPreferencesStore.getString(key, defValue);
	}
	static String getString(SharedPreferences sharedPreferencesStore, String key) {
		return getString(sharedPreferencesStore, key, null);
	}

	static Integer getInt(SharedPreferences sharedPreferencesStore, String key, Integer defValue) {
		return sharedPreferencesStore.getInt(key, defValue);
	}
	static Integer getInt(SharedPreferences sharedPreferencesStore, String key) {
		return getInt(sharedPreferencesStore, key, 0);
	}

	static Boolean getBoolean(SharedPreferences sharedPreferencesStore, String key, Boolean defValue) {
		return sharedPreferencesStore.getBoolean(key, defValue);
	}
	static Boolean getBoolean(SharedPreferences sharedPreferencesStore, String key) {
		return getBoolean(sharedPreferencesStore, key, false);
	}

	static Float getFloat(SharedPreferences sharedPreferencesStore, String key, Float defValue) {
		return sharedPreferencesStore.getFloat(key, defValue);
	}
	static Float getFloat(SharedPreferences sharedPreferencesStore, String key) {
		return getFloat(sharedPreferencesStore, key, (float) 0);
	}

	static Long getLong(SharedPreferences sharedPreferencesStore, String key, Long defValue) {
		return sharedPreferencesStore.getLong(key, defValue);
	}
	static Long getLong(SharedPreferences sharedPreferencesStore, String key) {
		return getLong(sharedPreferencesStore, key, (long) 0);
	}

	static Set<String> getStringSet(SharedPreferences sharedPreferencesStore, String key, Set<String> defValues) {
		return sharedPreferencesStore.getStringSet(key, defValues);
	}
	static Set<String> getStringSet(SharedPreferences sharedPreferencesStore, String key) {
		return getStringSet(sharedPreferencesStore, key, new HashSet<String>());
	}

	//write values

	static void putValueInSharedPref(SharedPreferences sharedPreferencesStore, String key, boolean booleanValue) {
		SharedPreferences.Editor editor = sharedPreferencesStore.edit();
		editor.putBoolean(key, booleanValue);
		editor.commit();
	}

	static void putValueInSharedPref(SharedPreferences sharedPreferencesStore, String key, int intValue) {
		SharedPreferences.Editor editor = sharedPreferencesStore.edit();
		editor.putInt(key, intValue);
		editor.commit();
	}

	static void putValueInSharedPref(SharedPreferences sharedPreferencesStore, String key, float floatValue) {
		SharedPreferences.Editor editor = sharedPreferencesStore.edit();
		editor.putFloat(key, floatValue);
		editor.commit();
	}

	static void putValueInSharedPref(SharedPreferences sharedPreferencesStore, String key, String stringValue) {
		SharedPreferences.Editor editor = sharedPreferencesStore.edit();
		editor.putString(key, stringValue);
		editor.commit();
	}

	//clean values

	static void removeKeys(SharedPreferences sharedPreferencesStore, String... keys) {
		SharedPreferences.Editor editor = sharedPreferencesStore.edit();
		for (String key : keys)
			editor.remove(key);
		editor.commit();
	}

	static void clearAll(SharedPreferences sharedPreferencesStore) {
		SharedPreferences.Editor editor = sharedPreferencesStore.edit();
		editor.clear();
		editor.commit();
	}

}

