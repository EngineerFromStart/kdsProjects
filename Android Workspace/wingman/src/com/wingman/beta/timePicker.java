package com.wingman.beta;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

public class timePicker extends Activity implements OnClickListener{
	public TimePicker timePicker;
	public Button btnCancel;
	public Calendar c = Calendar.getInstance();
	public Button btnSet;
	public Button btnNow;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_picker);
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		setCurrentTimeOnView();
		setClickListeners();
	}
	private void setClickListeners() {
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnSet = (Button) findViewById(R.id.btnSet);
		btnNow = (Button) findViewById(R.id.btnNow);
		btnCancel.setOnClickListener(this);
		btnSet.setOnClickListener(this);
		btnNow.setOnClickListener(this);
	}
	public void setCurrentTimeOnView(){
		c = Calendar.getInstance();
		timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()){
		case R.id.btnCancel:
			this.finish();
			break;
		case R.id.btnSet:
			Intent intent = new Intent(WingmanAppWidgetProvider.ACTION_TIME_CHANGED);
			timePicker.clearFocus();
			if (c.get(Calendar.HOUR_OF_DAY) == timePicker.getCurrentHour() && c.get(Calendar.MINUTE) == timePicker.getCurrentMinute()){
				intent.putExtra("hour", -1);
			}else{
				intent.putExtra("hour", timePicker.getCurrentHour());
//				intent.putExtra("millis", timePicker);
				intent.putExtra("min", timePicker.getCurrentMinute());
			}
			getApplicationContext().sendBroadcast(intent);
			this.finish();
			break;
		case R.id.btnNow:
			c = Calendar.getInstance();
			timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
	    if(keyCode == KeyEvent.KEYCODE_BACK){
	    	this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
