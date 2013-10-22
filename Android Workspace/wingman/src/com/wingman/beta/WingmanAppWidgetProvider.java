package com.wingman.beta;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.RemoteViews;

public class WingmanAppWidgetProvider extends AppWidgetProvider {
	public static final String ACTION_TIME_CHANGED = "com.wingman.beta.timeUpdate";
	public static final String ACTION_DATE_CHANGED = "com.wingman.beta.dateUpdate";
	public static final String ACTION_MSG_CHANGED = "com.wingman.beta.msgUpdate";
	public static final String ACTION_OPTION_CLICKED = "com.wingman.beta.optionClicked";
	public static final String ACTION_UPDATE_OPTIONS = "com.wingman.beta.updateOptionList";
	public static final String ACTION_UPDATE_MSGS = "com.wingman.beta.updateMsgList";
	public static final String ACTION_SEND_MESSAGE = "com.wingman.beta.sendMsg";
	public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	public static final String ACTION_CONTACT_CHANGED = "com.wingman.beta.sendMsg";
	
	public static final int MAX_SMS_MESSAGE_LENGTH = 160;
	public RemoteViews views;
	public void onEnabled(Context context){
	}
	//called in response to action_appwidget_update when asked to provide remote views (no update)
	//thus called when onUpdate is called, time based, or initiation
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) { 
    	Log.e("updating onUpdate", "true");
    	
    	ComponentName thisWidget = new ComponentName(context, WingmanAppWidgetProvider.class);
    	int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    	
    	Intent intent = new Intent(context, UpdateWidgetService.class);
    	intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
    	    
    	context.startService(intent);
    	
    	super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    @SuppressLint("SimpleDateFormat")
	public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
//        called when asked for update  
//        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
//        	Log.e("updating on intent", "true");
//        }
        //called after time picker
         if (intent.getAction().equals(ACTION_TIME_CHANGED)) {
            int hourInt = intent.getIntExtra("hour", 0);
            int minInt = intent.getIntExtra("min", 0);
            String time = "12:00 AM";
            time = WidgetContentUpdater.getTime(hourInt, minInt);//-1 if set to now
            
            SharedPreferences times = context.getSharedPreferences("dateTime",0);
            SharedPreferences.Editor timesEditor = times.edit();
            timesEditor.putInt("hour", hourInt);
            timesEditor.putInt("min", minInt);
            timesEditor.apply();
            
        	updatePartially(context,ACTION_UPDATE_OPTIONS,"time",time);
        }
        //called after date picker
        else if(intent.getAction().equals(ACTION_DATE_CHANGED)){
        	int year = intent.getIntExtra("year", 0);
        	int month = intent.getIntExtra("month", 0);
        	int day = intent.getIntExtra("day", 0);
        	SharedPreferences times = context.getSharedPreferences("dateTime",0);
            SharedPreferences.Editor timesEditor = times.edit();
            timesEditor.putInt("year", year);
            timesEditor.putInt("month", month);
            timesEditor.putInt("day", day);
            timesEditor.apply();
        	String date = String.valueOf(month+"/"+day+"/"+year);
        	//used in options adapter
        	updatePartially(context,ACTION_UPDATE_OPTIONS,"date",date);        	
        }
        //called when items in msg list clicked
        else if(intent.getAction().equals(ACTION_MSG_CHANGED)){
        	Log.e("intent", "intent called");
        	if (intent.getStringExtra("type").equals("editView")){
	        	Intent msgActivity = new Intent(context, msgWriter.class);
	        	msgActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	context.getApplicationContext().startActivity(msgActivity); 
	        	//when activity closes, it leads to the textView filter
        	}else if(intent.getStringExtra("type").equals("textView")){
        		String msg = intent.getStringExtra("msg");
                SharedPreferences times = context.getSharedPreferences("dateTime",0);
                SharedPreferences.Editor timesEditor = times.edit();
                timesEditor.putString("msg", msg);
                timesEditor.apply();
        		updatePartially(context,ACTION_UPDATE_MSGS,"msg",msg);  
        	}
        }
        //called when items in options list clicked
        else if(intent.getAction().equals(ACTION_OPTION_CLICKED)){
        	Log.e("msg", "clicked");
	        if(intent.getStringExtra("type").equals("add")){
	        	Log.e("msg", "should send msg");
	        	setAlarm(context);
	    	}
	        else if(intent.getStringExtra("type").equals("time")){
	        	Intent timeActivity = new Intent(context, timePicker.class);
	        	timeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	context.getApplicationContext().startActivity(timeActivity); 
	        }
	        else if(intent.getStringExtra("type").equals("date")){
	        	Intent dateActivity = new Intent(context, datePicker.class);
	        	dateActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	context.getApplicationContext().startActivity(dateActivity);
	        }
        }//called by the alarm
        else if(intent.getAction().equals(ACTION_SEND_MESSAGE)){
        	Log.e("alarm got called", "true");
        	sendMessage(context);
        }
        //called by incoming text
        else if(intent.getAction().equals(ACTION_SMS_RECEIVED )){
        	Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from = null;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        //String msgBody = msgs[i].getMessageBody();
                    }
                }catch(Exception e){
                }
            }
            SharedPreferences times = context.getSharedPreferences("dateTime",0);
            SharedPreferences.Editor timesEditor = times.edit();
            timesEditor.putString("contact", msg_from);
            timesEditor.apply();
            String name = WidgetContentUpdater.getContactName(context, msg_from);
            updateWidgetObject(context, R.id.contactView, name);
        }else if (intent.getAction().equals(ACTION_CONTACT_CHANGED)){
        	Log.e("contact changed", "true");
        	
        }
    }
    private void updateWidgetObject(Context context, int contactview, String data) {
    	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WingmanAppWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        views.setTextViewText(contactview, data);	    
    	onUpdate(context,appWidgetManager,appWidgetIds);
    	
	}
	private void setAlarm(Context context) {
    	Calendar c = Calendar.getInstance();
    	long curTime = c.getTimeInMillis();
    	SharedPreferences times = context.getSharedPreferences("dateTime",0);
    	int year = times.getInt("year", c.get((Calendar.YEAR)));
    	int month = times.getInt("month", c.get((Calendar.MONTH)));
    	int day = times.getInt("day", c.get((Calendar.DAY_OF_MONTH)));
    	int hourOfDay = times.getInt("hour", c.get((Calendar.HOUR_OF_DAY)));
    	int minute = times.getInt("min", c.get((Calendar.MINUTE)));
    	c.clear();
    	c.set(year, month, day, hourOfDay, minute);
    	long sendTime = c.getTimeInMillis();
    	long timeLeft = sendTime - curTime;
    	Log.e("timeleft", String.valueOf(timeLeft));
    	if (hourOfDay < 0 || timeLeft < 0){
    		sendMessage(context);
    	}else{
        	Intent intent = new Intent();
        	intent.setAction(ACTION_SEND_MESSAGE);
        	PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        	AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        	am.set(AlarmManager.RTC_WAKEUP, sendTime, sender);
    	}
	}
	public void sendMessage(Context context){
        SharedPreferences times = context.getSharedPreferences("dateTime",0);
    	SharedPreferences.Editor timesEditor = times.edit();
        String msg = times.getString("msg", "");
        String contact = times.getString("contact", null);
    	SmsManager smsManager = SmsManager.getDefault();
    	if (!msg.equals("") || msg == null && contact != null){
    		int length = msg.length();
    		if(length > MAX_SMS_MESSAGE_LENGTH) {
                ArrayList<String> messagelist = smsManager.divideMessage(msg);          
                smsManager.sendMultipartTextMessage(contact, null, messagelist, null, null);
            }else{
                smsManager.sendTextMessage(contact, null, msg, null, null);	
            }
    	}
    	timesEditor.clear();
    	timesEditor.apply();
    }
    public void updateWidget(Context context){
    	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WingmanAppWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
    	onUpdate(context,appWidgetManager,appWidgetIds);
    }
    public void updatePartially(Context context, String Action, String type, String data){
    	Log.e("updating partially", "true");
    	
        
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
    	ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WingmanAppWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        
        Intent intent = new Intent(context, WingmanAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(intent);
        
        Intent updateList = new Intent();
    	updateList.setAction(Action);
    	updateList.putExtra("type", type);
    	updateList.putExtra("data", data);
    	updateList.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
    	context.getApplicationContext().sendBroadcast(updateList);
    }
}
