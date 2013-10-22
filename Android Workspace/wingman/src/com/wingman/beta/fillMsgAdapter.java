package com.wingman.beta;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class fillMsgAdapter implements RemoteViewsFactory {
	private Context ctxt;
	private int curPosition;
	private String[] items={"","Call you back soon","In a meeting!"};
	public fillMsgAdapter(Context context, Intent intent){
		ctxt = context;
		ctxt.getApplicationContext().registerReceiver(mMessageReceiver,
			      new IntentFilter(WingmanAppWidgetProvider.ACTION_UPDATE_MSGS));
	}
	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		curPosition = position;
		Log.e("item", String.valueOf(position));
		RemoteViews row = new RemoteViews(ctxt.getPackageName(), R.layout.widget_msg_list_view);
		Intent getMessage = new Intent();
		if (curPosition == 0){
			row.setTextViewText(R.id.msgListEditView, items[position]);
			row.setViewVisibility(R.id.msgListTextView, View.GONE);

			getMessage.putExtra("type", "editView");
		}else{
			row.setTextViewText(R.id.msgListTextView, items[position]);
			row.setViewVisibility(R.id.msgListEditView, View.GONE);
			
			getMessage.putExtra("type", "textView");
			getMessage.putExtra("msg", items[position]);
		}
        row.setOnClickFillInIntent(R.id.msgListView, getMessage);
		return row;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
		
	}

	@Override
	public void onDataSetChanged() {
		
	}

	@Override
	public void onDestroy() {
		ctxt.unregisterReceiver(mMessageReceiver);
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
			if (intent.getStringExtra("type").equals("msg")){
				items[0] = intent.getStringExtra("data");
			}
			Log.e("items in adapter", items[0]);
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.msgList);
		}
	};
}
