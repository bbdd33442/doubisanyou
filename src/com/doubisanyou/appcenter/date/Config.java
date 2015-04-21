package com.doubisanyou.appcenter.date;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Config {
	
	public static final String DIR_NAME = null;

	public static String CHARSET_UTF8 = "utf-8";

	public static String TOKEN="token";
	
	public static String PROJECT_ID="com.xy.secret";
	
	public static String SERVICE_URL="http://192.168.1.101:8080/api/test.jsp";
	
	public static String getToken(Context context){
		return context.getSharedPreferences(PROJECT_ID, Context.MODE_PRIVATE).getString(TOKEN, null);
	}
	
	public static void setToken(Context context,String token){
		Editor ed = context.getSharedPreferences(PROJECT_ID, Context.MODE_PRIVATE).edit();
		ed.putString(TOKEN, token);
		ed.commit();
	}
}
