package easyecard.nfc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;

public class DatabaseCon{
//[collection, request, id, query]
	String url = "";
	String tok = "";
	
	public DatabaseCon(String uri, String token){
		url = uri;
		tok = token;
	}
	
	//get JSON objects with these post request parameters
	public JSONObject read(String collection, String request, String id, String query, String limit, String skip){
		JSONObject jObj = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("data", "{\"token\":\""+tok+"\",\"request\":\""+request+"\",\"id\":\""+id+"\",\"query\":"+query+",\"limit\":\""+limit+"\",\"skip\":\""+skip+"\"}"));
	    jObj = getJSONFromUrl(params, collection);
		return jObj;
	}
		
	public JSONObject getJSONFromUrl(List<NameValuePair> params, String col){
		JSONObject jObj = null;
		try {
			//try enabling a http connection and type
	        StrictMode.ThreadPolicy policy = new StrictMode.
	                ThreadPolicy.Builder().permitAll().build();
	                StrictMode.setThreadPolicy(policy); 
	        URL urls = new URL(url+col);
	        HttpURLConnection connection = (HttpURLConnection) urls.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setDoInput(true);
	        connection.setDoOutput(true); 
	        
	       
	        //perform POST request with the params you need
	        OutputStream os = connection.getOutputStream();
	        BufferedWriter writer = new BufferedWriter(
	                new OutputStreamWriter(os, "UTF-8"));
	        writer.write(getQuery(params));
	        writer.close();
	        os.close();
	        jObj = readStream(connection.getInputStream());
		}
	    catch (Exception e) {
	    	//  e.printStackTrace();
	  	}
		return jObj;
	}
	private JSONObject readStream(InputStream IS){
		String jSon = "";
		JSONObject jObj = null;
	        try {
	       		BufferedReader reader = new BufferedReader(new InputStreamReader(IS));
	           	StringBuilder sb = new StringBuilder();
	           	String line = null;
	           	while ((line = reader.readLine()) != null) {
	           		sb.append(line + "\n");
	           	}
	           	IS.close();
	           	reader.close();
	        	jSon = sb.toString();
	        }
	        catch (IOException e) {
                e.printStackTrace();
	        }
	        //Put it in JSon object
	        try {
	          	jObj = new JSONObject(jSon);
	        }
	        catch (JSONException e){
	        
	        }
	        return jObj;
	} 

	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;
	
	    for (NameValuePair pair : params)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");
	
	        result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
	    }
	    return result.toString();
	}
}
