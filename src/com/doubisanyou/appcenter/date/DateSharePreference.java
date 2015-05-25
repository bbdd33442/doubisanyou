package com.doubisanyou.appcenter.date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @Title 共享偏好
 * @Description 偏好设置
 * @author xy
 * @date 2015-04-11
 * @version V1.0
 */
public class DateSharePreference {

	private static DateSharePreference shareDate;
	private Context mContext;

	private DateSharePreference(Context context) {
		this.mContext = context;
	}

	public static DateSharePreference getIntance(Context context) {
		if (shareDate == null) {
			shareDate = new DateSharePreference(context);
		}
		return shareDate;
	}

	/**
	 * 放到Preference 
	 * @param key
	 * @param value
	 */
	public void putToPreference(String key, String value) {
		SharedPreferences pre = mContext.getSharedPreferences(
				mContext.getPackageName(), Context.MODE_WORLD_WRITEABLE);
		Editor editor = pre.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 放到Preference
	 * @param key
	 * @param value
	 */
	public void putToPreference(String key, int value) {
		SharedPreferences pre = mContext.getSharedPreferences(
				mContext.getPackageName(), Context.MODE_WORLD_WRITEABLE);
		Editor editor = pre.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 获取String型偏好值
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		String value = null;
		if (value == null) { 
			SharedPreferences pre = mContext.getSharedPreferences(
					mContext.getPackageName(), Context.MODE_WORLD_READABLE);
			value = pre.getString(key, null);
		}
		return value;
	}
	
	/**
	 * 获取int型偏好值
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		int value = 0;
		if (value == 0) { 
			SharedPreferences pre = mContext.getSharedPreferences(
					mContext.getPackageName(), Context.MODE_WORLD_READABLE);
			value = pre.getInt(key, 0);
		}
		return value;
	}

}
