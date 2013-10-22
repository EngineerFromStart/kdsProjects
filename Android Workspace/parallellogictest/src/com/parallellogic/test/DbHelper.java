package com.parallellogic.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//File used to manage DB, creata, update and remove tables

public class DbHelper extends SQLiteOpenHelper{
		private static final String SQL_DELETE_CURRENT =
			    "DROP TABLE IF EXISTS " + DbContract.Comments.TABLE_NAME;
		public static final int DATABASE_VERSION = 1;
	    public static final String DATABASE_NAME = "Comments.db";
	    
		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			//create defualt tables
			String defaultString = createString(DbContract.Comments.TABLE_NAME, DbContract.Comments.getColumns());
			db.execSQL(defaultString);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DELETE_CURRENT);
			onCreate(db);
		}
		public String createString(String tableName, String[] columns){
			String sqlString = "CREATE TABLE " +tableName+" (";
			sqlString += columns[0] + " integer primary key autoincrement" + ",";
			for (int x = 1; x < columns.length; x++){
				Log.e("x", String.valueOf(x));
				if (x < columns.length-1)
					sqlString += columns[x] + " text not null" + ",";
				else
					sqlString += columns[x] + " text not null" +");";
			}
			return sqlString;
		}
}
