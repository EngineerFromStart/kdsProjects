package com.parallellogic.test;

import android.provider.BaseColumns;

public class DbContract {
	public DbContract() {
		//so no one can instantiate this class, this is empty
	}
	public static abstract class Comments implements BaseColumns {
		public static final String TABLE_NAME = "comments";
        public static String[] getColumns(){
        	String[] columns = {"_id", "name", "int1", "int2", "float1", "float2"};
        	return columns;
        }
        public static String[] getColumnsNoIndex(){
        	String[] columns = {"name", "int1", "int2", "float1", "float2"};
        	return columns;
        }
	}
}