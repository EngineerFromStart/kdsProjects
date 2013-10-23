package com.parallellogic.test;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class EditTextSeekBar extends EditText implements OnClickListener,
		OnLongClickListener, OnSeekBarChangeListener{

	public Context context;
	
	public int min = 0;
	public int max = 100;
	public int interval = 1;
	public int divisor = 1;
	
	public Dialog dialog;
	public SeekBar slider;
	
	public EditTextSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(!isInEditMode())
			init(context);
	}
	public EditTextSeekBar(Context context) {
		super(context);
		if(!isInEditMode())
			init(context);
		
	}
	public EditTextSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(!isInEditMode())
			init(context);
	}
	private void init(Context context) {
		this.context = context;
		loseFocus(this);
		this.setOnClickListener(this);
		this.setOnLongClickListener(this);
		Log.e("ok1", "got here");
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog);
		dialog.setTitle("Set size!");
		dialog.setCancelable(true);
		slider = (SeekBar) dialog.findViewById(R.id.slider);
	}
	@Override
	public void onClick(View v){
		loseFocus(v);
		showSeekBarDialog(this, this.min, this.max, this.interval, this.divisor);
	}
	public void setMax(int max){
		this.max = max;
	}
	public void setMin(int min){
		this.min = min;
	}
	public void setInterval(int interval){
		this.interval = interval;
	}
	public void setdivisor(int divisor){
		this.divisor = divisor;
	}
	private void showSeekBarDialog(View v, int Min, int Max, int Interval, int Divisor) {
		slider.setMax(max);		
		if (this.getText().toString().equals("")){
			slider.setProgress(0);
		}else{
			slider.setProgress(Integer.valueOf(this.getText().toString())*divisor);
		}
		slider.setOnSeekBarChangeListener(this);
		dialog.show();
	}
	private void loseFocus(View v) {
		Log.e("ok", "get hers");
		v.setFocusable(false);
		v.setFocusableInTouchMode(false);
	}
	@Override
	public boolean onLongClick(View v) {
		requestFocus(v);
		return false;
	}
	private void requestFocus(View v){
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		InputMethodManager IMM = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		IMM.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
		if (IMM.isActive(v)){
			if (dialog != null && dialog.isShowing()){
				dialog.cancel();
			}
		}
		
	}
	@Override
	public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
		progress = Math.round(progress/interval)*interval;
		seekbar.setProgress(progress);
		this.setText(String.valueOf(progress/divisor));
	}
	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}
}
