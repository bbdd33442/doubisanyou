
package com.doubisanyou.baseproject.utilCommon;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.doubisanyou.appcenter.date.Config;

/**
 * @Title：文件工具类
 * @Description：包换获取文件路径删除文件等对文件的操作 
 * @date 2015-04-11
 */

public class FileUtil {

	/**
	 * 得到应用的缓存目录
	 * 
	 * @param context
	 *            Context对象
	 * @return
	 */
	public static String getCacheDir(Context context) { 
		return context.getCacheDir().getAbsolutePath();
	}

	public static Boolean isVideo(String path) {

		if (path == null || "".equals(path))
			return false;
		path = path.toLowerCase();
		int length = path.length();
		if (length < 4)
			return false;
		String mimetype = "3gp,mp4";
		path = path.substring(path.length() - 3);
		return mimetype.contains(path);

	}

	public static boolean deleteFile(String path) {
		File file = new File(path);
		if (file.isFile()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 获取可用容量
	 * @return
	 */
	public static long getAvailaleSize() {

		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径

		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		// return availableBlocks * blockSize; 
		// (availableBlocks * blockSize)/1024 KIB 单位 
		return (availableBlocks * blockSize) / 1048576; // MIB单位

	}

	
	
	/**
	 *	SDcard 是否可以
	 * @return
	 */
	public static boolean sdCardState() {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}

		return false;
	}

	/**
	 * 获取SDcard文件夹路径
	 * @return
	 */
	public static String getfilePath() {
		File content = new File(
				android.os.Environment.getExternalStorageDirectory(),
				Config.DIR_NAME);

		// 目录是否存在，不存在创建
		if (!content.exists()) {
			content.mkdirs();
		}
		return   content.getAbsolutePath();

	}
	
	/**
	 * 读取源文件内容
	 * @param filename String 文件路径
	 * @throws IOException
	 * @return byte[] 文件内容
	*/
	public static byte[] readFile(String filename) throws IOException {

		 File file =new File(filename);
		 if(filename==null || filename.equals(""))
		{
		 throw new NullPointerException("无效的文件路径");
		}
		 long len = file.length();
		 byte[] bytes = new byte[(int)len];
	
		 BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(file));
		 int r = bufferedInputStream.read( bytes );
		 if (r != len)
		 throw new IOException("读取文件不正确");
		bufferedInputStream.close();

	 return bytes;

	}
}
