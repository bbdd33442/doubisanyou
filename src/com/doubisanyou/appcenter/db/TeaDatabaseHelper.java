package com.doubisanyou.appcenter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Blook
 * @Description 数据库帮助类
 */
public class TeaDatabaseHelper extends SQLiteOpenHelper {

	final String CREATE_TABLE_TEA_CONTACT_SQL = "CREATE TABLE tea_contact(_id integer primary key autoincrement, teco_nickname, teco_jid, teco_backname, teco_gxqm )";
	final String CREATE_TABLE_TEA_MESSAGE_SQL = "CREATE TABLE tea_message(_id integer primary key autoincrement, teme_content, teme_type, teme_from, teme_to, teme_time, teme_is_read, teme_tech_id)";
	final String CREATE_TABLE_TEA_CHATROOM_SQL = "CREATE TABLE tea_chatroom(_id integer primary key autoincrement, tech_type, tech_update_time)";
	final String CREATE_TABLE_TEA_TECO_TECH_SQL = "CREATE TABLE tea_teco_tech(_id integer primary key autoincrement, tett_teco_id, tett_tech_id)";

	// final String CREATE_TABLE_TEA_TECO_TEME_SQL =
	// "CREATE TABLE tea_teco_teme(tett_id integer primary key autoincrement, teco_id, teme_id)";

	public TeaDatabaseHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_TEA_CONTACT_SQL);
		db.execSQL(CREATE_TABLE_TEA_CHATROOM_SQL);
		db.execSQL(CREATE_TABLE_TEA_MESSAGE_SQL);
		db.execSQL(CREATE_TABLE_TEA_TECO_TECH_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("DatabaseHelper", "upgrate: " + oldVersion + "--->" + newVersion);
	}

}
