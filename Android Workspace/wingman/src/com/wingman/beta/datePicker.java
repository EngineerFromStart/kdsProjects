package com.wingman.beta;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

public class datePicker extends Activity implements OnClickListener{
	public DatePicker datePicker;
	public Button btnCancel;
	public Button btnSet;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_picker);
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		datePicker.setMinDate(System.currentTimeMillis()-1000);//min date must be less
		setCurrentTimeOnView();
		setClickListeners();
	}
	private void setClickListeners() {
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnSet = (Button) findViewById(R.id.btnSet);
		btnCancel.setOnClickListener(this);
		btnSet.setOnClickListener(this);
	}
	public void setCurrentTimeOnView(){
		Calendar c = Calendar.getInstance();
		int yy = c.get(Calendar.YEAR);
		int mm = c.get(Calendar.MONTH);
		int dd = c.get(Calendar.DAY_OF_MONTH);
		datePicker.updateDate(yy, mm, dd);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.btnCancel:
			this.finish();
			break;
		case R.id.btnSet:
			Intent intent = new Intent(WingmanAppWidgetProvider.ACTION_DATE_CHANGED);
			intent.putExtra("day", datePicker.getDayOfMonth());
			intent.putExtra("month", datePicker.getMonth());
			intent.putExtra("year", datePicker.getYear());
			getApplicationContext().sendBroadcast(intent);
			this.finish();
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
