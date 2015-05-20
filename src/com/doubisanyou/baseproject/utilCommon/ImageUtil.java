package com.doubisanyou.baseproject.utilCommon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.doubisanyou.appcenter.date.Config;

/**
 * 图片处理公用类
 * @author zlh
 *
 */
public class ImageUtil {
	
	public static Bitmap getBitmapFromResources(Activity act, int resId) {
		Resources res = act.getResources();
		return BitmapFactory.decodeResource(res, resId);
	}

	// byte[] → Bitmap
	public static Bitmap convertBytes2Bimap(byte[] b) {
		if (b.length == 0) {
			return null;
		}
		return BitmapFactory.decodeByteArray(b, 0, b.length);
	}

	// Bitmap → byte[]
	public static byte[] convertBitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// 1)Drawable → Bitmap
	public static Bitmap convertDrawable2BitmapByCanvas(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	// 2)Drawable → Bitmap
	public static Bitmap convertDrawable2BitmapSimple(Drawable drawable) {
		BitmapDrawable bd = (BitmapDrawable) drawable;
		return bd.getBitmap();
	}

	// Bitmap → Drawable
	public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
		return bd;
	}
	
    //保存图片到指定路径
	public static void saveBitmap(Bitmap bm,String name) {
		/*	String imageName ="";
		try {
			imageName=Base64.encodeToString(name.trim().getBytes("utf-8"), Base64.DEFAULT).replace("\n", "");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		File f = new File((Environment.getExternalStorageDirectory()+"/"+
				Config.AppCenters_Path + "/"
						+ Config.AppCenters_ImagePath+"/"+name).trim());		
		if (f.exists()) {
		    f.delete();
		    try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
		FileOutputStream out = new FileOutputStream(f);
		bm.compress(Bitmap.CompressFormat.PNG, 90, out);
		out.flush();
		out.close();
		} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		} 
	
	
	   public static Bitmap readImage(String fileName) { 
			
			File f = new File((Environment.getExternalStorageDirectory()+"/"+
					Config.AppCenters_Path + "/"
							+ Config.AppCenters_ImagePath+"/"+fileName).trim());
			if(!f.exists()){
				return null;
			}
	        Bitmap bm=null;
	        try { 

	            FileInputStream fis = new FileInputStream(f); 

	            
	            bm= BitmapFactory.decodeStream(fis);
	          

	            fis.close(); 

	        } catch (FileNotFoundException e) { 

	            e.printStackTrace(); 
	            return null;

	        } catch (IOException e) { 

	            e.printStackTrace(); 

	        } 
	        System.out.println("从本地获取图片！");
	        return bm; 

	    } 
}
