package com.eventglace.app;

import org.json.JSONArray;
import org.json.JSONObject;

public interface LiveSqrdDatabaseInterface {
	public void recievedData(JSONArray jArray);
	public void recievedData(JSONObject jObj);
	
}
