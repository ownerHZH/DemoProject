package com.zgan.yckz.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class YCKZ_SQLHelper extends SQLiteOpenHelper {
	// Êý¾Ý¿â°æ±¾
	private static final int VERSION = 1;

	String creat_list = "create table if not exists table_SubDev"
			+ "(_ID integer primary key autoincrement,_SubDvId text,_MAC text,_Port text,"
			+ "_ProductNo text,_DeviceNo text,_DeviceName text,_Status text,"
			+ "_RegTime text,_JobStatus text)";
	String creat_SubDevList = "create table if not exists SubDevList(" +
			" _ID integer primary key autoincrement," +
			"_MAC text,_DeviceName text)";
	String creat_AlarmDevList = "create table if not exists AlarmDevList(" +
			" _ID integer primary key autoincrement," +
			"_AlarmTime text)";

	public YCKZ_SQLHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	public YCKZ_SQLHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public YCKZ_SQLHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub	
		db.execSQL(creat_SubDevList);
		db.execSQL(creat_list);
		db.execSQL(creat_AlarmDevList);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
