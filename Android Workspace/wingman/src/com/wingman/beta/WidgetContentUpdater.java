package com.wingman.beta;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class WidgetContentUpdater {
	public static String getContactName(Context context, String number){
		String name = null;
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null){
			if (cursor.moveToFirst()){
				name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
			}else{
				name = number;
			}
		}
		return name;
	}
	public static String getTime(int hourInt, int minInt){
		String min = "00";
        if (minInt < 10){
            min = "0"+String.valueOf(minInt);
        }else{
        	min = String.valueOf(minInt);
        }
        String time = "12:00 AM";
        if (hourInt <= -1){
        	time = "Now";
        }else if (hourInt >= 0 && hourInt < 1){
            time = "12:"+min+" AM";
        }else if (hourInt >= 1 && hourInt < 12){
        	String hour = String.valueOf(hourInt);
            time = hour+":"+min+" AM";
        }else if (hourInt >= 12 && hourInt < 13){
            time = "12:"+min+" PM";
        }else if (hourInt >= 13 && hourInt < 24){
        	String hour = String.valueOf(hourInt-12);
            time = hour+":"+min+" PM";
        }else{
        	time = "Now";
        }
		return time;
	}
	public static String getContactNumber(){
		String number = null;
		return number;
	}
}
