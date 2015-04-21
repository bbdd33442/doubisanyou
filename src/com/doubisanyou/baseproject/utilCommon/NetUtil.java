package com.doubisanyou.baseproject.utilCommon;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetUtil {
	
	/**
	 * 判断网络是否连接
	 * @param conn
	 * @return
	 */
	public static boolean checkNet(Context conn) {

		try {
			ConnectivityManager cm = (ConnectivityManager) conn
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				NetworkInfo info = cm.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return false;
	}
}
