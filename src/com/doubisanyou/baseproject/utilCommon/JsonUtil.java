package com.doubisanyou.baseproject.utilCommon;

import com.google.gson.Gson;

public class JsonUtil {
		
	public static String ObjectToJson(Object o){
		Gson g = new Gson();
		String result = g.toJson(o);
		return result;
	}
	
	public static Object JsonToObject(String result,Class clazz){
		Gson g = new Gson();
		Object c = g.fromJson(result, clazz);
		return c;
	}
}
