package com.parallellogic.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	private DbFunctions dbFunctions;
	private SQLiteDatabase db;
	private arrayAdapter adapter;
	private List<Comment> values;
	private Button saveBtn;
	private EditText editName;
	private EditTextSeekBar editInt1;
	private EditTextSeekBar editInt2;
	private EditTextSeekBar editFloat1;
	private EditTextSeekBar editFloat2;

	private int int1min = -100;
	private int int1max = 500;
	private int int1int = 10;
	private int int1divisor = 1; //used to convert to integar, integar used in seekbar,
	
	private int int2min = -10000;
	private int int2max = 50000;
	private int int2int = 100;
	private int int2divisor = 1; //used to convert to integar, integar used in seekbar,
	
	private float float1min = -10f;
	private float float1max = 50f;
	private float float1int = 1.25f;
	private float float1divisor = 1000; //used to convert to integar, integar used in seekbar,
	private float float2min = 10f;
	private float float2max = 50f;
	private float float2int = 2.75f;
	private float float2divisor = 100000; //used to convert to integar, integar used in seekbar,

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dbFunctions = new DbFunctions(this);
		db = dbFunctions.getDb();
		Log.e("ok", "show msgs");
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
	public void setViews(){
		saveBtn = (Button) this.findViewById(R.id.save);
		editName = (EditText) this.findViewById(R.id.edit_name);
		editInt1 = (EditTextSeekBar) this.findViewById(R.id.edit_int1);
		editInt2 = (EditTextSeekBar) this.findViewById(R.id.edit_int2);
		editFloat1 = (EditTextSeekBar) this.findViewById(R.id.edit_float1);
		editFloat2 = (EditTextSeekBar) this.findViewById(R.id.edit_float2);
		
		editInt1.setMin(Math.round(int1min*int1divisor)); //mathround is used for converting to integer
		editInt1.setMax(Math.round(int1max*int1divisor)); //only integers are accepter by seekbar
		editInt1.setInterval(Math.round(int1int*int1divisor)); //can be furthuer improved to make seekbar convert digits it self
		editInt1.setdivisor(Math.round(int1divisor));
		
		editInt2.setMin(Math.round(int2min*int2divisor)); //mathround is used for converting to integer
		editInt2.setMax(Math.round(int2max*int2divisor)); //only integers are accepter by seekbar
		editInt2.setInterval(Math.round(int2int*int2divisor)); //can be furthuer improved to make seekbar convert digits it self
		editInt2.setdivisor(Math.round(int2divisor));
		
		editFloat1.setMin(Math.round(float1min*float1divisor)); //mathround is used for converting to integer
		editFloat1.setMax(Math.round(float1max*float1divisor)); //only integers are accepter by seekbar
		editFloat1.setInterval(Math.round(float1int*float1divisor)); //can be furthuer improved to make seekbar convert digits it self
		editFloat1.setdivisor(Math.round(float1divisor));
		
		editFloat2.setMin(Math.round(float2min*float2divisor)); //mathround is used for converting to integer
		editFloat2.setMax(Math.round(float2max*float2divisor));
		editFloat2.setInterval(Math.round(float2int*float2divisor));
		editFloat2.setdivisor(Math.round(float2divisor));
		
		saveBtn.setOnClickListener(this);
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
		switch (v.getId()){
			case R.id.save:
				updateList();
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
}
