package easyecard.nfc;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


public class ShowProfiles extends Activity {

	public static String url = "https://dev-voting.circles.io/api/v1/";

	public DatabaseCon jParser = new DatabaseCon(url,"123456");
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profiles);
      
        new myAsync().execute("loader","read","{}","{}","","");
    
    }
    private class myAsync extends AsyncTask<String, Integer, JSONObject>{
        protected JSONObject doInBackground(String... params){
        	JSONObject jObj = null;
        	if (params[1] == "read"){
        		jObj = jParser.read(params[0], params[1], params[2], params[3], params[4], params[5]);
        	}else if(params[1] == "delete"){
        		
        	}else if(params[1] == "update"){
        		
        	}else if(params[1] == "write"){
        		
        	}
        	
    		Log.d("JSON Objecy", jObj.toString());
    		return jObj;
    	}   	
        protected void onPostExecute(JSONObject jObj){
        	//remove void & and parse the jObj
        }
    }
    
}
