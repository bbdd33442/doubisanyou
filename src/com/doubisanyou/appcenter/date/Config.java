package com.doubisanyou.appcenter.date;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;

public class Config {
	
	public static final String DIR_NAME = null;

	public static String CHARSET_UTF8 = "utf-8";

	public static String TOKEN="token";
	
	public static String PROJECT_ID="com.doubisanyou.tea";
	
	public static String SERVICE_URL="http://192.168.1.101:8080/api/test.jsp";
	
	public static final String  AppCenters_Path  = "doubisanyou";              // 应用中心路径
    
	public static final String  AppCenters_ImagePath  = "image";   
	
	public static final Map<String,Bitmap> BITMAPCATCH = new HashMap<String,Bitmap>();
	
	public static String getToken(Context context){
		return context.getSharedPreferences(PROJECT_ID, Context.MODE_PRIVATE).getString(TOKEN, null);
	}
	
	public static void setToken(Context context,String token){
		Editor ed = context.getSharedPreferences(PROJECT_ID, Context.MODE_PRIVATE).edit();
		ed.putString(TOKEN, token);
		ed.commit();
	}
}
