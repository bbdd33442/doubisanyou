package com.doubisanyou.baseproject.utilsResource;

import java.io.IOException;
import java.io.InputStream;  
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ResourceManager {

	/*
	 * 从Assets中读取图片
	 */
	public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}
	
	   /**
	 * 以最省内存的方式读取本地资源的图片
	   * @param context
	   * @param resId
	   * @return
	   */  
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
	
    public static Bitmap readBitMap(Context context, String pathName) {
    	 return readBitMap(context, pathName, 8);
	} 
	
	   /**
	 * 以最省内存的方式读取本地资源的图片
	   * @param context
	   * @param resId
	   * @return
	   */  
	public static Bitmap readBitMap(Context context, String pathName,int size) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true; 
		opt.inSampleSize = size; 
		// 获取资源图片
		return	BitmapFactory.decodeFile(pathName, opt);
		//return BitmapFactory.decodeStream(is, null, opt);
	}
	
	
	public static Bitmap readBigBitMap(Context context, String pathName) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;  
		// 获取资源图片
		return	BitmapFactory.decodeFile(pathName, opt);
		//return BitmapFactory.decodeStream(is, null, opt);
	}
	 
	  
}
