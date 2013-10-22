package com.parallellogic.test;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbFunctions {
	DbHelper dbHelper;
	public DbFunctions(Context context) throws SQLException{
		this.dbHelper = new DbHelper(context);
	}
	public SQLiteDatabase getDb(){
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
		return db;
	}
	public void closeDb(){
		dbHelper.close();
	}
    public long addToDb(SQLiteDatabase db, String tableName, String[] columns, String[] data){
    	ContentValues values = new ContentValues();
    	for (int x = 0; x < columns.length; x++){
        	values.put(columns[x], data[x]);
    	}
    	Log.e("values", values.toString());
    	long rowId = db.insert(tableName, null, values);
    	return rowId; 
    }

    public void deleteFromDb(SQLiteDatabase db, String tableName, String selection, String[] selectionArgs){
    	db.delete(tableName, selection, selectionArgs);
    }
    //return values in all the rows for a single column
    public static ArrayList<String> readSingleColumn(Cursor c, String columnName){
    	c.moveToFirst();
    	ArrayList <String> values = new ArrayList<String>();
    	while(c.moveToNext()){
    		int columnId = c.getInt(c.getColumnIndexOrThrow(columnName));
    		String value = c.getString(columnId);
    		values.add(value);
    	}
    	return values;
    }
    //return values from all columns for a signle row (position passed in)
    public static ArrayList <String> readSingleRow(Cursor c, int position, String[] projection){
    	c.moveToPosition(position);
    	ArrayList <String> values = new ArrayList<String>();
    	for (int x = 0; x < projection.length; x++){
        	int columnId = c.getInt(c.getColumnIndexOrThrow(projection[x]));
        	String value = c.getString(columnId);
        	values.add(value);
    	}
    	return values;
    }
    public static int updateDb(Context context,String tableName, String[] projection, String[] data, String selection, String[] selectionArgs) {
    	// New value for one column
    	DbHelper dbHelper = new DbHelper(context);
    	SQLiteDatabase db = dbHelper.getReadableDatabase();

    	ContentValues values = new ContentValues();
    	for (int x = 0; x < projection.length; x++){
        	values.put(projection[x], data[x]);
    	}

    	int count = db.update(
    	    tableName,
    	    values,
    	    selection,
    	    selectionArgs);
    	return count;
    }
}
