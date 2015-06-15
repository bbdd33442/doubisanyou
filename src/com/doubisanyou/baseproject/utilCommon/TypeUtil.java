package com.doubisanyou.baseproject.utilCommon;

import com.doubisanyou.appcenter.activity.SearchSecondActivity;

public class TypeUtil {
	
	public static String returnType(int type){
		switch(type){
		case SearchSecondActivity.REDTEA:
			return "红茶";
		case SearchSecondActivity.GREENTEA:
			return "绿茶";
		case SearchSecondActivity.BLACKTEA:
			return "黑茶";
		case SearchSecondActivity.WHITETEA:
			return "白茶";
		case SearchSecondActivity.YELLOWTEA:
			return "黄茶";
		case SearchSecondActivity.CYANTEA:
			return "青茶";
		case SearchSecondActivity.HEALTHTEA:
			return "保健茶";
		case SearchSecondActivity.FLOWERTEA:
			return "黄茶";
		default:
			return "未知类型";
		}
		
	}
}
