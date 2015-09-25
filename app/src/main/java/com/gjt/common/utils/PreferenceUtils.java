package com.gjt.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.List;

/**
 * xml值保存和获取
 * 
 * @author Administrator
 * 
 */
public class PreferenceUtils {
	

	private static final String PROCESS_PRE_NAME = "myhome";
	/**
	 *   ---清空该xml所有值
	 * @param context
	 * @param p
	 */
	public static void clearPreference(Context context, SharedPreferences p) {
		Editor editor = p.edit();
		editor.clear();
		editor.commit();
	}
	/**
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getPrefBoolean(Context context, String key, boolean defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}
	/**
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static float getPrefFloat(Context context, String key, float defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getFloat(key, defaultValue);
	}
	/**
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getPrefInt(Context context,String key,int defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}
	/**
	 *   ---判断是否已保存该值
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static long getPrefLong(Context context, String key, long defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getLong(key, defaultValue);
	}
	/**
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getPrefString(Context context, String key, String defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

	public static boolean hasKey(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
	}
	
	public static void remove(Context context, String key) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().remove(key).commit();
	}
	
	/**
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setPrefBoolean(Context context, String key, boolean value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putBoolean(key, value).commit();
	}

	public static void setPrefFloat(Context context, String key, float value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putFloat(key, value).commit();
	}
	
	public static void setPrefInt(Context context, String key, int value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putInt(key, value).commit();
	}

	public static void setPrefString(Context context, String key, String value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putString(key, value).commit();
	}

	public static void setSettingLong(Context context, String key, long value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putLong(key, value).commit();
	}

	public static void setProcessString(Context context, String key, String value){
		SharedPreferences sp = context.getSharedPreferences(PROCESS_PRE_NAME, Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
		sp.edit().putString(key, value).commit();
	}

	public static String getProcessPreString(Context context, String key, String defValue){
		SharedPreferences sp = context.getSharedPreferences(PROCESS_PRE_NAME, Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
		return sp.getString(key,defValue);
	}

	public static void clearProcessPre(Context context){
		SharedPreferences sp = context.getSharedPreferences(PROCESS_PRE_NAME, Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
		sp.edit().clear().commit();
	}
	
	/**
	 * List转换 String
	 * @param entity :需要转换的List
	 * @return String转换后的字符串
	 */
	public static <T> void setSettingEntity(Context context, String key, T entity){
		
		// 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 然后将得到的字符数据装载到ObjectOutputStream
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			// writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
			objectOutputStream.writeObject(entity);
			// 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
			String ListString = new String(android.util.Base64.encode(byteArrayOutputStream.toByteArray(), android.util.Base64.DEFAULT));
			setPrefString(context, key, ListString);
			// 关闭objectOutputStream
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
     
    /** 
     * String转换List 
     * @param <T>
     * @param value :需要转换的文本
     * @return 
     * @return List<?> 
     */ 
	public static <T> T getSettingEntity(Context context, String key ,String value) {

		String ListString=getPrefString(context, key, value);
		if(ListString == null){
			return null;
		}
		byte[] mobileBytes = android.util.Base64.decode(ListString.getBytes(), android.util.Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			T entity = (T) objectInputStream.readObject();
			objectInputStream.close();
			return entity;
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    /**
	 * List转换 String
	 * @param context :需要转换的List
	 * @return String转换后的字符串
	 */
	public static void setSettingList(Context context, String key, List<?> List){
		
		// 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 然后将得到的字符数据装载到ObjectOutputStream
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			// writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
			objectOutputStream.writeObject(List);
			// 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
			String ListString = new String(android.util.Base64.encode(byteArrayOutputStream.toByteArray(), android.util.Base64.DEFAULT));
			setPrefString(context, key, ListString);
			// 关闭objectOutputStream
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
     
    /** 
     * String转换List 
     * @param <E>
     * @param value :需要转换的文本
     * @return List<?> 
     */ 
	public static <E> List<?> getSettingList(Context context, String key ,String value) {

		String ListString=getPrefString(context, key, value);
		if (ListString.equals(value)) {
			return new ArrayList<E>();
		}
		byte[] mobileBytes = android.util.Base64.decode(ListString.getBytes(), android.util.Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			List<?> WeatherList = (List<?>) objectInputStream.readObject();
			objectInputStream.close();
			return WeatherList;
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<E>();
	}
}
