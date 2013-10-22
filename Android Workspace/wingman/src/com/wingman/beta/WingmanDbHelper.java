package com.wingman.beta;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//File used to manage DB, creata, update and remove tables

public class WingmanDbHelper extends SQLiteOpenHelper{
		private static final String SQL_DELETE_CURRENT =
			    "DROP TABLE IF EXISTS " + WidgetReaderContract.WidgetCurrents.TABLE_NAME;
		private static final String SQL_DELETE_DEFAULT =
			    "DROP TABLE IF EXISTS " + WidgetReaderContract.WidgetDefaults.TABLE_NAME;
		public static final int DATABASE_VERSION = 1;
	    public static final String DATABASE_NAME = "WingmanData.db";
	    
		public WingmanDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			//create defualt tables
			String defaultString = createString(WidgetReaderContract.WidgetDefaults.TABLE_NAME, WidgetReaderContract.WidgetDefaults.getColumns());
			db.execSQL(defaultString);
			String currentString = createString(WidgetReaderContract.WidgetCurrents.TABLE_NAME, WidgetReaderContract.WidgetCurrents.getColumns());
			db.execSQL(currentString);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DELETE_CURRENT);
			db.execSQL(SQL_DELETE_DEFAULT);
			onCreate(db);
		}
		public String createString(String tableName, String[] columns){
			String sqlString = "CREATE TABLE " +tableName+" (";
			sqlString += WidgetReaderContract.WidgetCurrents._ID + " integer primary key autoincrement" + ",";
			for (int x = 0; x < columns.length-1; x++){
				if (x < columns.length-1)
					sqlString += columns[x] + " text not null" + ",";
				else
					sqlString += columns[x] + " text not null" +")";
			}
			return sqlString;
		}
}
