package com.wingman.beta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class msgWriter extends Activity implements OnClickListener{
	public Button btnCancel;
	public Button btnSet;
	public EditText textBox;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_writer);
		setClickListeners();
		textBox = (EditText) this.findViewById(R.id.editMsg);
	}
	private void setClickListeners() {
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnSet = (Button) findViewById(R.id.btnSet);
		btnCancel.setOnClickListener(this);
		btnSet.setOnClickListener(this);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
	    if(keyCode == KeyEvent.KEYCODE_BACK){
	    	this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.btnSet:
			String msg = String.valueOf(textBox.getText());
			String newMsg = msg.replace(" ", "");
			if (!msg.equals("") && !newMsg.equals("")){
				Intent intent = new Intent();
				intent.setAction(WingmanAppWidgetProvider.ACTION_MSG_CHANGED);
				intent.putExtra("type", "textView");
				intent.putExtra("msg", msg);
				getApplicationContext().sendBroadcast(intent);
				this.finish();
			}
			else {
				Toast.makeText(getApplicationContext(), "Enter a Message or Cancel", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnCancel:
			this.finish();
			break;
		}
		
	}
}
