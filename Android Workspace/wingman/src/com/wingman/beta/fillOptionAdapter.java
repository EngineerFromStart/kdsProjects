package com.wingman.beta;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class fillOptionAdapter implements RemoteViewsFactory{
	private Context ctxt;
	private String[] items={"Now","Today","Settings","Add"};
	public fillOptionAdapter(Context context, Intent intent){
		ctxt = context;
		ctxt.getApplicationContext().registerReceiver(mMessageReceiver,
			      new IntentFilter(WingmanAppWidgetProvider.ACTION_UPDATE_OPTIONS));
		 
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
		RemoteViews option_row = new RemoteViews(ctxt.getPackageName(),R.layout.widget_msg_option_list);
		Intent optionType = new Intent();
		if (position == 0){
			option_row.setTextViewText(R.id.timeText, items[position]);
			option_row.setViewVisibility(R.id.dateText, View.GONE);
			option_row.setViewVisibility(R.id.advBtn, View.GONE);
			option_row.setViewVisibility(R.id.addBtn, View.GONE);
			optionType.putExtra("type", "time");
		}else if (position == 1){
			option_row.setTextViewText(R.id.dateText, items[position]);
			option_row.setViewVisibility(R.id.advBtn, View.GONE);
			option_row.setViewVisibility(R.id.addBtn, View.GONE);
			option_row.setViewVisibility(R.id.timeText, View.GONE);
			optionType.putExtra("type", "date");
		}else if (position == 2){
			option_row.setTextViewText(R.id.advBtn, items[position]);
			option_row.setViewVisibility(R.id.dateText, View.GONE);
			option_row.setViewVisibility(R.id.addBtn, View.GONE);
			option_row.setViewVisibility(R.id.timeText, View.GONE);
			optionType.putExtra("type", "adv");
		}else if (position == 3){
			option_row.setTextViewText(R.id.addBtn, items[position]);
			option_row.setViewVisibility(R.id.advBtn, View.GONE);
			option_row.setViewVisibility(R.id.dateText, View.GONE);
			option_row.setViewVisibility(R.id.timeText, View.GONE);
			optionType.putExtra("type", "add");
		}
		option_row.setOnClickFillInIntent(R.id.optionListView, optionType);
		return option_row;
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
			Log.e("intent received", "change values in option");
			if (intent.getStringExtra("type").equals("date")){
				items[1] = intent.getStringExtra("data");
			}else if (intent.getStringExtra("type").equals("time")){
				items[0] = intent.getStringExtra("data");
			}
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.optionList);
		}
	};
}
