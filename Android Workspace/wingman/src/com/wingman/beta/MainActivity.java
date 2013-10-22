package com.wingman.beta;

import android.app.Activity;
import android.util.Log;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("app started", "started");
	}
}
