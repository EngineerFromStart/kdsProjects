package com.eventglace.app;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements LiveSqrdDatabaseInterface {

	public LiveSqrdDatabaseCon dbConnection;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dbConnection = new LiveSqrdDatabaseCon("https://dev-eventglance.circles.io/", "v1", "123456", this.getClass());
	   	
		
		//should do this in async task so that it happens in background
		//dbConnection.deleteWithQuery("item", "\"title\":\"test\"");	   	
		//dbConnection.readWithQuery("item", "\"_id\":\"520bf1c53212bfdf61000004\"");
			//String createQuery = createItem();
		//dbConnection.createWithJSON("item", createQuery);
		//dbConnection.readWithQuery("item", "\"title\":\"New York International Fringe Festival\"");
		dbConnection.readWithQuery("item", "");   
		
		//popup window has to on a UI thread after all system calls are made (oncreate, resume, pause)
//		findViewById(R.id.mainView).post(new Runnable() {
//			   public void run() {
//					
//					//should be done with UI thread
//					dbConnection.logInWithWebView(findViewById(R.id.mainView), getBaseContext(), "facebook");
//			   }
//		 });
	}
	private String createItem() {
		JSONObject body = new JSONObject();
		try {
			body.put("startTime", "12:55 PM");
			body.put("address","187 Edgecasdfsdafombe Ave, New York 10030");
			body.put("description","this it tasdfsdfhe temp apartment i am currently living in");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LiveSqrdItems item = new LiveSqrdItems();
		item.setBody(body);
		item.setBox(new Number[]{10,20});
		item.setColor("");
		item.setDate(new Date());
		item.setDue(new Date(), 10, 19);
		item.setGeo(new Number[]{43,-73});
		item.setGroup("item");
		item.setWidth(10);
		item.setHeight(10);
		item.setX(10);
		item.setY(10);
		item.setZ(10);
		item.setItem(null);
		item.setLock(null);
		item.setPath("somewhere");
		item.setPermission(null,null);
		item.setPhoto("");
		item.setRole("");
		item.setStates(null);
		item.setTag(new String[]{});
		item.setTitle("test");
		return item.createQuery();
	}
	@Override
	protected void onResume(){
		super.onResume();
	}
	@Override
	public void recievedData(JSONArray jArray) {
		Log.e("data in main activity", jArray.length() +": " + jArray.toString());		
	}
	@Override
	public void recievedData(JSONObject jObj) {
		Log.e("data in main activity", jObj.toString());
		
	}
}
