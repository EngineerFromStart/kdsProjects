package com.wingman.beta;

import android.provider.BaseColumns;

public class WidgetReaderContract {
	public WidgetReaderContract() {
		//so no one can instantiate this class, this is empty
	}

	public static abstract class WidgetCurrents implements BaseColumns {
		public static final String TABLE_NAME = "current";
        public static String[] getColumns(){
        	String[] columns = {"service", "time", "date", "message", "contacts", "repeat"};
        	return columns;
        }
	}
	public static abstract class WidgetTaskList implements BaseColumns {
		public static final String TABLE_NAME = "tasklist";
        public static String[] getColumns(){
        	String[] columns = {"service", "time", "date", "message", "contacts", "repeat"};
        	return columns;
        }
	}
	public static abstract class WidgetDefaults implements BaseColumns {
		public static final String TABLE_NAME = "defaults";
		public static String[] getColumns(){
			String[] columns = {"service", "time", "date", "message", "repeat"};
			return columns;
        }
	}
	public static abstract class WidgetAccounts implements BaseColumns {
		public static final String TABLE_NAME = "accounts";
        public static String[] getColumns(){
        	String[] columns = {"account", "username", "password"};
        	return columns;
        }
	}
	public static abstract class WidgetTaskRepeats implements BaseColumns {
		public static final String TABLE_NAME = "repeats";
        public static String[] getColumns(){
        	String[] columns = {"taskId", "format"};
        	return columns;
        }
	}
	public static abstract class WidgetMessages implements BaseColumns {
		public static final String TABLE_NAME = "messages";
        public static String[] getColumns(){
        	String[] columns = {"messages"};
        	return columns;
        }
	}
}