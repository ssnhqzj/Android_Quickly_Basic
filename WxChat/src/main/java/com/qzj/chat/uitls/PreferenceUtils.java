package com.qzj.chat.uitls;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences值保存和获取
 * @author qzj
 */
public class PreferenceUtils {
	private static final String PROCESS_PRE_NAME = "qzj_chat";

	private static PreferenceUtils instance;
	private static SharedPreferences preferences;

	public static PreferenceUtils getInstance(Context context){
		if (instance == null){
			instance = new PreferenceUtils();
			preferences = context.getSharedPreferences(PROCESS_PRE_NAME,
					Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
		}
		return instance;
	}

	public void clear() {
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	public void putIntValue(String key, int value){
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public int getIntValue(String key, int defValue){
		return preferences.getInt(key, defValue);
	}

	public void putStringValue(String key, String value){
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getStringValue(String key, String defValue){
		return preferences.getString(key,defValue);
	}
}
