package com.wingman.beta;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		handleIntent(intent);
        return START_STICKY;
    }	
	public void handleIntent(Intent intent){
		Log.e("updateServcice", "running");
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		
	    int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

	    final int N = appWidgetIds.length;

	    // Perform this loop procedure for each App Widget that belongs to this provider
	    for (int i=0; i<N; i++) {
	    	int appWidgetId = appWidgetIds[i];

	    RemoteViews views = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.appwidget);
	    
	    Intent getContact = new Intent(this.getApplicationContext(), contactPicker.class);
	    PendingIntent pickContact = PendingIntent.getActivity(this.getApplicationContext(), 0, getContact, PendingIntent.FLAG_UPDATE_CURRENT);
	    views.setOnClickPendingIntent(R.id.contactView, pickContact);
	    
	    //depending on the button, launch that contact activity
	    //LIST VIEWS
	        Intent fillMsgAdapter = new Intent(this.getApplicationContext(), fillMsgService.class);
	        fillMsgAdapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
	        fillMsgAdapter.setData(Uri.parse(fillMsgAdapter.toUri(Intent.URI_INTENT_SCHEME)));
	        views.setRemoteAdapter(R.id.msgList, fillMsgAdapter);
			
	        Intent fillOptionAdapter = new Intent(this.getApplicationContext(), fillOptionService.class);
	        fillOptionAdapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
	        fillOptionAdapter.setData(Uri.parse(fillOptionAdapter.toUri(Intent.URI_INTENT_SCHEME)));
	        views.setRemoteAdapter(R.id.optionList, fillOptionAdapter);
	        
	        Intent msgTemplate = new Intent(this.getApplicationContext(), WingmanAppWidgetProvider.class);
	        msgTemplate.setAction(WingmanAppWidgetProvider.ACTION_MSG_CHANGED);
	        msgTemplate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
	        msgTemplate.setData(Uri.parse(msgTemplate.toUri(Intent.URI_INTENT_SCHEME)));//makes it unique
			PendingIntent fillMsgIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, msgTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setPendingIntentTemplate(R.id.msgList, fillMsgIntent);
			
			Intent optionTemplate = new Intent(this.getApplicationContext(), WingmanAppWidgetProvider.class);
			optionTemplate.setAction(WingmanAppWidgetProvider.ACTION_OPTION_CLICKED);
			optionTemplate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			optionTemplate.setData(Uri.parse(optionTemplate.toUri(Intent.URI_INTENT_SCHEME)));//makes it unique
			PendingIntent fillOptionIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, optionTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setPendingIntentTemplate(R.id.optionList, fillOptionIntent);
	        
	        appWidgetManager.updateAppWidget(appWidgetId, views);
	    }
	}
}
