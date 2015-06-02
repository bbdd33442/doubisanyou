package com.doubisanyou.appcenter.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doubisanyou.appcenter.bean.TeaSay;
import com.doubisanyou.baseproject.utilCommon.StringAndDataUtil;

public class TeaSayDBManager {

	DBHelper help;

	SQLiteDatabase db;

	public TeaSayDBManager(Context context) {
		help = new DBHelper(context);
		db = help.getWritableDatabase();
	}

	public void addTeaSay(TeaSay ts) {
		String[] s=null;
		String images="";
		if (ts.tea_say_images.size() > 0&&!StringAndDataUtil.isNullOrEmpty(ts.tea_say_images.get(0))) {
			s = ts.tea_say_images.toArray(new String[ts.tea_say_images.size()]);  
			images= StringAndDataUtil.List2String(s);
		}
		db.beginTransaction();
		db.execSQL("insert into TEA_SAY values(null,?,?,?,?,?,?,?,?)",
				new Object[] { ts.tea_say_id, ts.tea_say_publisher_id,
						ts.tea_say_publisher_name, ts.tea_say_content,
						ts.tea_say_publish_date, ts.tea_say_time,
						ts.tea_say_publisher_avatar, images });
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public List<TeaSay> getTeaSayList() {
		TeaSay ts = null;
		List<TeaSay> tss = new ArrayList<TeaSay>();
		Cursor c = null;
		c = db.rawQuery(
				"select * from TEA_SAY t order by tea_say_time DESC", null);
		while (c.moveToNext()) {
			ts = new TeaSay();
			ts.tea_say_content = c.getString(c
					.getColumnIndex("tea_say_content"));
			ts.tea_say_id = c.getString(c.getColumnIndex("tea_say_id"));
			ts.tea_say_publish_date = c.getString(c
					.getColumnIndex("tea_say_publish_date"));
			ts.tea_say_publisher_avatar = c.getString(c
					.getColumnIndex("tea_say_publisher_avatar"));
			ts.tea_say_publisher_name = c.getString(c
					.getColumnIndex("tea_say_publisher_name"));
			ts.tea_say_time = c.getString(c.getColumnIndex("tea_say_time"));
			ts.tea_say_publisher_id = c.getString(c
					.getColumnIndex("tea_say_publisher_id"));
			ts.tea_say_images.addAll(Arrays.asList(c.getString(
					c.getColumnIndex("tea_say_images")).split(",")));
			tss.add(ts);
		}
		c.close();
		return tss;
	}

	public void delteTeaSayById(String id) {
		db.delete("TEA_SAY", "tea_say_time=?", new String[]{id});
	}

	/*
	 * public AppUpdate findApp(String app_id){ AppUpdate app = null; Cursor c =
	 * db.rawQuery("SELECT * FROM APP_INFO t where t.app_id=? ",new
	 * String[]{app_id}); while (c.moveToNext()) { app=new AppUpdate();
	 * app._id=c.getInt(c.getColumnIndex("_id"));
	 * app.app_id=c.getString(c.getColumnIndex("app_id"));
	 * app.app_info_id=c.getString(c.getColumnIndex("app_info_id"));
	 * app.app_detail_id=c.getString(c.getColumnIndex("app_detail_id"));
	 * app.app_name=c.getString(c.getColumnIndex("app_name"));
	 * app.app_size=c.getString(c.getColumnIndex("app_size"));
	 * app.app_icon=c.getString(c.getColumnIndex("app_icon"));
	 * app.app_org=c.getString(c.getColumnIndex("app_org"));
	 * app.app_install_package
	 * =c.getString(c.getColumnIndex("app_install_package"));
	 * app.app_usually_used=c.getString(c.getColumnIndex("app_usually_used"));
	 * app.app_package_name=c.getString(c.getColumnIndex("app_package_name"));
	 * app.app_start_path=c.getString(c.getColumnIndex("app_start_path"));
	 * 
	 * } c.close();
	 * 
	 * return app; } //插入APP数据 如果APP已经存在那么更新数据 public void
	 * addUpdateApp(AppUpdate app){ if(findApp(app.app_id)!=null){ upDate(app);
	 * return; } db.beginTransaction();
	 * db.execSQL("insert into APP_INFO values(null,?,?,?,?,?,?,?,?,?,0,?)",new
	 * Object[]{
	 * app.app_id,app.app_info_id,app.app_detail_id,app.app_name,app.app_size,
	 * app.app_icon,app.app_org,app.app_install_package,app.app_package_name,
	 * app.app_start_path }); db.setTransactionSuccessful();
	 * db.endTransaction();
	 * 
	 * }
	 * 
	 * public List<AppUpdate> getUpdateAppList() { AppUpdate app = null;
	 * 
	 * List<AppUpdate> apps=new ArrayList<AppUpdate>(); Cursor c=null;
	 * c=db.rawQuery("select * from APP_INFO t",null); while(c.moveToNext()){
	 * app=new AppUpdate(); app._id=c.getInt(c.getColumnIndex("_id"));
	 * app.app_id=c.getString(c.getColumnIndex("app_id"));
	 * app.app_info_id=c.getString(c.getColumnIndex("app_info_id"));
	 * app.app_detail_id=c.getString(c.getColumnIndex("app_detail_id"));
	 * apps.add(app); } c.close(); return apps; }
	 * 
	 * public int upDate(AppUpdate app) { ContentValues cv = new
	 * ContentValues(); cv.put("app_id",app.app_id);
	 * cv.put("app_info_id",app.app_info_id);
	 * cv.put("app_detail_id",app.app_detail_id); cv.put("app_name",
	 * app.app_name); cv.put("app_size", app.app_size); cv.put("app_icon",
	 * app.app_icon); cv.put("app_org", app.app_org);
	 * cv.put("app_install_package",app.app_install_package);
	 * cv.put("app_package_name", app.app_package_name);
	 * cv.put("app_start_path", app.app_start_path);
	 * 
	 * int i =db.update("APP_INFO", cv, "app_id=?", new String[]{app.app_id});
	 * 
	 * return i; }
	 * 
	 * public void delete(String app_id) { db.delete("APP_INFO", "app_id=?", new
	 * String[]{app_id});
	 * 
	 * }
	 */

}
