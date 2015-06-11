package com.doubisanyou.baseproject.utilCommon;

import com.google.gson.Gson;

public class JsonUtil {
		
	public static String ObjectToJson(Object o){
		Gson g = new Gson();
		String result = g.toJson(o);
		return result;
	}
	
}
