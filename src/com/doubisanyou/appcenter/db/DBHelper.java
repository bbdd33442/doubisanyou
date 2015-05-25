package com.doubisanyou.appcenter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	static final String DATABASE_NAME="Tea.db";
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		
	}

	
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table TEA_SAY (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				   "tea_say_id VARCHAR,tea_say_publisher_id VARCHAR,tea_say_publisher_name VARCHAR," +
				   "tea_say_content VARCHAR,tea_say_publish_date VARCHAR,tea_say_time VARCHAR," +
				   "tea_say_publisher_avatar VARCHAR,tea_say_images VARCHAR)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	
}
