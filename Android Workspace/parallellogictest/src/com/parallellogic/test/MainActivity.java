package com.parallellogic.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements OnClickListener,
		OnLongClickListener, OnSeekBarChangeListener{
	private DbFunctions dbFunctions;
	private SQLiteDatabase db;
	private arrayAdapter adapter;
	private List<Comment> values;
	private SeekBar slider;
	private Button saveBtn;
	private EditText editName;
	private EditText editInt1;
	private EditText editInt2;
	private EditText editFloat1;
	private EditText editFloat2;
	
	
	//int and float params
	private int int1min = 0;
	private int int1max = 1000;
	private int int1int = 100;
	private int int2min = 50;
	private int int2max = 500;
	private int int2int = 25;
	private int intDivisor = 1;
	private float float1min = -10f;
	private float float1max = 50f;
	private float float1int = 1.25f;
	private float float1divisor = 1000; //used to convert to integar, integar used in seekbar,
	private float float2min = 10f;
	private float float2max = 50f;
	private float float2int = 2.75f;
	private float float2divisor = 100000; //used to convert to integar, integar used in seekbar,
	
	//settings set for seekbar
	private int min;
	private int max;
	private int interval;
	private int divisor;
	private EditText editView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dbFunctions = new DbFunctions(this);
		db = dbFunctions.getDb();
		
		values = getAllColumnts();
		adapter = new arrayAdapter(this,R.layout.list_item,values);
		ListView listView = (ListView) findViewById(R.id.myList);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				Comment item = (Comment) parent.getItemAtPosition(position);
				Log.e("pos", String.valueOf(position));
				Log.e("index", String.valueOf(item.index));
				dbFunctions.deleteFromDb(db, DbContract.Comments.TABLE_NAME, 
						"_id" +"="+ item.index , null);
		        adapter.remove(item);
		        adapter.notifyDataSetChanged();
		    };
		});
		setViews();
	}

	private void setViews() {
		saveBtn = (Button) findViewById(R.id.save);
		editName = (EditText) findViewById(R.id.edit_name);
		editInt1 = (EditText) findViewById(R.id.edit_int1);
		editInt2 = (EditText) findViewById(R.id.edit_int2);
		editFloat1 = (EditText) findViewById(R.id.edit_float1);
		editFloat2 = (EditText) findViewById(R.id.edit_float2);

		loseFocus(editName);
		loseFocus(editInt1);
		loseFocus(editInt2);
		loseFocus(editFloat1);
		loseFocus(editFloat2);

		saveBtn.setOnClickListener(this);
		editName.setOnClickListener(this);
		editInt1.setOnClickListener(this);
		editInt2.setOnClickListener(this);
		editFloat1.setOnClickListener(this);
		editFloat2.setOnClickListener(this);
		
		saveBtn.setOnLongClickListener(this);
		editName.setOnLongClickListener(this);
		editInt1.setOnLongClickListener(this);
		editInt2.setOnLongClickListener(this);
		editFloat1.setOnLongClickListener(this);
		editFloat2.setOnLongClickListener(this);
	}

	private List<Comment> getAllColumnts() {
		values = new ArrayList<Comment>();
		Cursor cursor = db.query(DbContract.Comments.TABLE_NAME, DbContract.Comments.getColumns(), 
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Comment comment = cursorToComment(cursor);
			values.add(comment);
			cursor.moveToNext();
		}
		return values;
	}
	private Comment cursorToComment(Cursor cursor){
		Comment comment = new Comment();
		comment.setIndex(cursor.getLong(0));//index
		comment.setName(cursor.getString(1));//name
		comment.setInt1(cursor.getInt(2));//int
		comment.setInt2(cursor.getInt(3));//int
		comment.setFloat1(cursor.getFloat(4));//float
		comment.setFloat2(cursor.getFloat(5));//float
		return comment;
	}
	@Override
	public void onClick(View v) {
		loseFocus(v);
		switch (v.getId()){
			case R.id.save:
				updateList();
				break;
			case R.id.edit_name:
				Log.e("ok11", "gets here");
				hideSeekBar();
				requestFocus(v);
			case R.id.edit_int1:
				showSeekBar(v, int1min, int1max, int1int, intDivisor);
				break;
			case R.id.edit_int2:
				showSeekBar(v, int2min, int2max, int2int, intDivisor);
				break;
			case R.id.edit_float1:
				int Float1Min = Math.round(float1min*float1divisor);
				int Float1Max = Math.round(float1max*float1divisor);
				int Float1Int = Math.round(float1int*float1divisor);
				showSeekBar(v, Float1Min, Float1Max, Float1Int, (int)float1divisor);
				break;
			case R.id.edit_float2:
				int Float2Min = Math.round(float2min*float2divisor);
				int Float2Max = Math.round(float2max*float2divisor);
				int Float2Int = Math.round(float2int*float2divisor);
				showSeekBar(v, Float2Min, Float2Max, Float2Int, (int)float2divisor);
				break;
		}
	}
	private void updateList() {
		String[] data = {editName.getText().toString(),
				editInt1.getText().toString(),
				editInt2.getText().toString(),
				editFloat1.getText().toString(),
				editFloat2.getText().toString()};
		boolean good = checkInputs(data);
		if (good){
			addToList(data);
		}
	}
	public boolean checkInputs(String[] data){
		for (int x = 0; x <data.length;x++){
			if (data[x].equals("") || data[x] == null){
				Toast.makeText(getApplicationContext(), "Please Enter All values", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}
	public void addToList(String[] data){
    	long rowId = dbFunctions.addToDb(db, DbContract.Comments.TABLE_NAME, DbContract.Comments.getColumnsNoIndex(), data);
		Cursor cursor = db.query(DbContract.Comments.TABLE_NAME, DbContract.Comments.getColumns(), "_id" +"="+ rowId, null, null, null , null);
		cursor.moveToFirst();//the query returns -1 by default;
		Comment newComment = cursorToComment(cursor);
		cursor.close();
		adapter.add(newComment);
		adapter.notifyDataSetChanged();
	}
	//parameters used or affected by seekbar
	private void showSeekBar(View v, int Min, int Max, int Interval, int Divisor) {
		slider = (SeekBar) findViewById(R.id.editSeekBar);
		min = Min;
		max = Max;
		divisor = Divisor;
		interval = Interval;
		editView = (EditText) v;
		slider.setVisibility(View.VISIBLE);
		slider.setMax(max);
		if (editView.getText().toString().equals("")){
			slider.setProgress(0);
		}else{
			Log.e("progress", String.valueOf(Integer.valueOf(editView.getText().toString())*Divisor));
			slider.setProgress(Integer.valueOf(editView.getText().toString())*Divisor);
		}
		slider.setOnSeekBarChangeListener(this);
	}
	private void hideSeekBar(){
		Log.e("ok", "gets here");
		if (slider != null){
			slider.setVisibility(View.GONE);
		}
	}
	private void loseFocus(View v) {
		v.setFocusable(false);
		v.setFocusableInTouchMode(false);
	}
	@Override
	public boolean onLongClick(View v){
		requestFocus(v);
		return false;
	}
	private void requestFocus(View v){
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		InputMethodManager IMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		IMM.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
		if (IMM.isActive(v)){
			hideSeekBar();
		}
	}
	//implemented by seekbar
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean isUser){
		progress = Math.round(progress/interval)*interval;
		seekBar.setProgress(progress);
		Log.e("progress in seekBar",String.valueOf(progress));
		editView.setText(String.valueOf(progress/divisor));
	}
	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		
	}
}
