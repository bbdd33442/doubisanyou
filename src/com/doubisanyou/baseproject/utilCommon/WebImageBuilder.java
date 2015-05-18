package com.doubisanyou.baseproject.utilCommon;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.doubisanyou.appcenter.date.Config;

public class WebImageBuilder{

	/**
	 * ͨ通过URL获取图片
	 * @param url
	 * @return
	 */
	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap =null;
		
		String[] data = url.split(";");
		
		String imageName = data[2];
		bitmap=ImageUtil.readImage(imageName);
		if(bitmap!=null){
    		return bitmap;
    	}
		
		bitmap = Config.BITMAPCATCH.get(imageName);
    	if(bitmap!=null){
    		return bitmap;
    	}
		try {
			myFileUrl = new URL(data[1]);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			
			conn.setRequestMethod("POST");
		
			conn.setDoOutput(true);// 是否输入参数
			conn.setRequestProperty("Content-Type","text/plain;charset=utf-8"); 
		    
		    DataOutputStream out = new DataOutputStream(conn    
	                .getOutputStream());    
	        
	        out.writeBytes(data[0]); 
		    
			InputStream is = conn.getInputStream();
			
			bitmap = BitmapFactory.decodeStream(is);
		/*	if(bitmap==null){
				bitmap=BitmapFactory.decodeResource(null, R.drawable.non_load);
			}*/
			is.close();
			Config.BITMAPCATCH.put(imageName, bitmap);
			ImageUtil.saveBitmap(bitmap,imageName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
