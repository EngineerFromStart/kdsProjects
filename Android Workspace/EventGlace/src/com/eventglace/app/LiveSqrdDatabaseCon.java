package com.eventglace.app;

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
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;

/**
 *<p>
 *The parent class that instantiates this class has to implement LiveSqrdDatabaseInterface.class in order to read useful information such as Database responses
 *<p>
 *This has to be passed the parent class that implements the Interface
 *<p>
 *@author      Karandeep Singh <@EngineerFromStart.com>
 *@version     1.0         (Current Version)      
 *@since       2010-11-06   (Last Editted)
 */
    public class LiveSqrdDatabaseCon extends Activity {
        //[collection, request, id, query]
        public String url = "";
        public String baseUrl = "";
        public String tok = "";
        public String query = "";
        public String ver = "";
        public String apiUrl ="";
        public boolean authenticateUserWithInstance = false;
        public boolean authenticateUserClient = false;
        public boolean authenticateUserProfile = false;
        public boolean responseFlag;
        public Context c;
        public PopupWindow wvWindow;
        public WebView webView;
    	public boolean loadingFinished = true;
    	public boolean redirect = false;
    	public LiveSqrdDatabaseInterface lsqrdInterface;
    	public int tempCounter = 0;
    	public String requestType = "POST";

    	/**
         * Used to instantiate the Class
         * <p>
         * The parent class that instantiates this class has to implement LiveSqrdDatabaseInterface.class in order to read useful information such as Database responses
         * <p>
         * This has to be passed the parent class that implements the Interface
         * <p>
         * @param uri the base uri to be used (ex: website url)
         * @param version the version of the Database
         * @param type the allowed token
         * @param enclosingClass the class that created this database api
         */
        public LiveSqrdDatabaseCon(String uri, String version, String token, Class<?> enclosingClass){
            url = uri;
            tok = token;
            ver = version;
            baseUrl = url + "api/" + ver+"/";
            Object test;
            test = "testing";
            Log.e("type", test.getClass().getName());
            try {
				lsqrdInterface = (LiveSqrdDatabaseInterface) enclosingClass.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        /**
         * Used to Communicate with the DB, creates the URL for the api and gets the JSON Object in return.
         * Then parses the JSON Object as needed.
         * <p>
         * @param collection The collectiont to access
         * @param request the type of request (read, create, update, delete)
         * @param query the parameters used to access or data to add into the DB
         * @param model The model to update
         * @param httpType the request method for the URL connection
         * @param identity The id used for access
         * @param options Additional options to modify the DB (ex: skip, limit)
         */
        public void callApi(String collection, String request, String query, String model, String httpType,
                String identity, String options){
            apiUrl = baseUrl + collection;
            requestType = httpType;
            if (identity == null){
                identity = "";
            }
            
            if (!authenticateUserClient && collection.equals("client")){
            	return;//null
            	//or delegate an error or tell user to login (webVIew)
            }else if(!authenticateUserProfile && collection.equals("profile")){
            	return;//null
            	//or delegate an error or tell user to login (webView)
            }
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if (request.equals("delete")){
            	Log.e("delete", "yes");
                params.add(new BasicNameValuePair("data","{\"token\":\""+tok+"\",\"request\":\""+request+"\",\"id\":\""+identity+"\",\"query\":{"+query+"}}"));
            }else if (request.equals("read")){
            	Log.e("read", "yes");
                if (options == null)
                    params.add(new BasicNameValuePair("data","{\"token\":\""+tok+"\",\"request\":\""+request+"\",\"id\":\""+identity+"\",\"query\":{"+query+"},\"limit\":"+String.valueOf(0)+",\"skip\":"+String.valueOf(0)+"}"));
                else
                    params.add(new BasicNameValuePair("data","{\"token\":\""+tok+"\",\"request\":\""+request+"\",\"id\":\""+identity+"\",\"query\":{"+query+"},"+options+",\"autoAll\":false}"));
            }else if (request.equals("update")){
            	Log.e("update", "yes");            	
                params.add(new BasicNameValuePair("data","{\"token\":\""+tok+"\",\"request\":\""+request+"\",\"id\":\""+identity+"\",\"query\":{"+query+"},\"model\":"+model+"}"));
            }else if (request.equals("create")){
            	Log.e("create", "yes");
                params.add(new BasicNameValuePair("data","{\"token\":\""+tok+"\",\"request\":\""+request+"\",\"model\":"+model+"}"));
            }
            JSONObject jObj = null;
            jObj = getJSONFromUrl(params);
            //if these are allowed, get their JSON object, else get result key from json object
            if (authenticateUserWithInstance || authenticateUserClient || authenticateUserProfile){
                parseUserData(jObj);
            }else{
                parseData(jObj, "result");
            }
        }
        /**
         * Methods that parses Instance, client or profile data (no result section or key is passed in)
         * Can also be use to just parse and JSON object
         * <p>
         * This has to be running after the system calls in Android (Runnable)
         * <p>
         * @param view the view where WebView should be added
         * @param context the context to be used to add the WebView
         * @param type the method to be used by the WebView to authenticate (facebook, twitter, eg.) 
         */
        public void parseUserData(JSONObject jObj){
        	JSONArray jArr;
        	JSONObject jObject;
			try {
				jArr = jObj.getJSONArray("result");
				jObject = jArr.getJSONObject(0);
				if (authenticateUserWithInstance){
					if (jObject.has("client") && !jObject.getString("client").equals("")){
						authenticateUserClient = true;//allow client access
					}else{
						authenticateUserClient = false;//disallow client access
					}
					if (jObject.has("profile") && !jObject.getString("profile").equals("")){
						authenticateUserProfile = true;//allow profile access
					}else{
						authenticateUserProfile = false;//disallow profile access
					}
	            	authenticateUserWithInstance = false;
	            	//not asking for instance in future, have to login again
	            	//but data is passes for storage anyways if needed (to get client and profile ids)
	               	//closeWebView();//close webview connection
           		}
           		//have to fix this, client is set to true above this
           		else if(authenticateUserClient){
					if (jObject.has("profile") && !jObject.getString("profile").equals("")){
						authenticateUserProfile = true;
					}
				}
				parseData(jObject,"");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        //handle data
        public void parseData(JSONObject jObj, String key){

        	//if parse by key, send that key only
        	if (!key.isEmpty()){
	        	try {
	            	if (jObj.get(key) instanceof String){
	            		String jType = new String();
	            	}else if(jObj.get(key) instanceof Integer){
	            		int jType = jObj.getInt(key);
	            	}else if(jObj.get(key) instanceof JSONArray){
	                	JSONArray jType = jObj.getJSONArray(key);
	                    lsqrdInterface.recievedData(jType);
	            	}else if(jObj.get(key) instanceof JSONObject){
	            		JSONObject jType = new JSONObject();
	            	}else if(jObj.get(key) instanceof Double){
	            		double jType = jObj.getDouble(key);
	            	}else if(jObj.get(key) instanceof Long){
	            		long jType = jObj.getLong(key);
	            	}else if(jObj.get(key) instanceof Boolean){
	            		boolean jType = jObj.getBoolean(key);
	            	}
	        	}catch (Exception e){
	        		
	        	}
        	}else{
                lsqrdInterface.recievedData(jObj);
        	}
        }
    //************Functions users should call*****************
        /**
         * Methods that adds and uses WebView to aauthernticate user using the method provided
         * <p>
         * This has to be running after the system calls in Android (Runnable)
         * <p>
         * @param view the view where WebView should be added
         * @param context the context to be used to add the WebView
         * @param type the method to be used by the WebView to authenticate (facebook, twitter, eg.) 
         */
        @SuppressLint("SetJavaScriptEnabled")
		public void logInWithWebView(View view, Context context, String type){
        	c = context;
        	final String authenticateType = type;
        	LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);  
            View layout = layoutInflater.inflate(R.layout.webview_login, null);  
            PopupWindow popupWindow = new PopupWindow(
                    layout, 
                       LayoutParams.WRAP_CONTENT,  
                       		LayoutParams.WRAP_CONTENT,
                       			true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            
        	webView = (WebView) layout.findViewById(R.id.liveSqrdWebView);
			popupWindow.showAtLocation(view,Gravity.CENTER, 0, 0);
        	
        	webView.getSettings().setJavaScriptEnabled(true);
        	webView.addJavascriptInterface(new CustomJavaScriptInterface(webView.getContext()), "Android");
        	webView.setWebViewClient(new WebViewClient(){
        		@Override
        		   public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
        		       if (!loadingFinished) {
        		          redirect = true;
        		       }

        		   loadingFinished = false;
        		   webView.loadUrl(urlNewString);
        		   return true;
        		   }
        		   @Override
        		   public void onPageStarted(WebView view, String url, Bitmap favicon) {
        		        loadingFinished = false;
        		        //SHOW LOADING IF IT ISNT ALREADY VISIBLE  
        		    }
        		   @Override
        		   public void onPageFinished(WebView view, String url) {
        		       if(!redirect){
        		          loadingFinished = true;
        		       }
        		       if(loadingFinished && !redirect){
        		    	   if (!url.contains(authenticateType)){
        		    		  Log.e("url", url);
            		    	  //view.loadUrl("javascript:Android.infoReady(window.___);");
        		    	   }
        		       } else{
        		          redirect = false; 
        		       }

        		    }
        	});
        	webView.loadUrl(url+"auth/" + type + "/");
        }
        private class CustomJavaScriptInterface {
            Context mContext;

            /** Instantiate the interface and set the context */	
            CustomJavaScriptInterface(Context c) {
                mContext = c;
            } 
            @JavascriptInterface
            public void infoReady(String instance, boolean logged) {
            	if (instance.equals("undefined")){
            		tempCounter = tempCounter + 1;
            		//webView.loadUrl("javascript:Android.infoReady(window.___);");
                	Log.e("instance not found: " + String.valueOf(tempCounter), instance);
            	}
            	else{
                	Log.e("instance found", instance);
                	loadUserInstance(instance, logged);
                	//closeWebView();
            	}
            }
        }
		public void loadUserInstance(String instance, boolean logged){
            String session = instance;
            boolean loggedIn = logged;
            if (!session.equals("") && session != null && loggedIn != false){
                authenticateUserWithInstance = true;
                readWithID("instance", session);
            }else{
            	// TODO Auto-generated catch block
            }
        }
		public void closeWebView(){
			if (wvWindow.isShowing()){
				wvWindow.dismiss();
			}
		}
		public void logout(){
			// TODO Auto-generated catch block
			//clear shared preferences and stuff
			webView.loadUrl(url+"logmeout");
			authenticateUserWithInstance = false;
			authenticateUserClient = false;
			authenticateUserProfile = false;
		}
		public void loadUserClient(String ID){
            if (!ID.equals("") && ID != null){
                authenticateUserClient = true;
                this.readWithID("client", ID);
            }else{
            	//do something
            }
		}
		public void loadUserProfile(String ID){
            if (!ID.equals("") && ID != null){
                authenticateUserClient = true;
                this.readWithID("profile", ID);
            }else{
            	//do something
            }
		}
        
		public void deleteWithID(String collection, String ID){
            this.callApi(collection, "delete", "", "", "POST", ID, null);
        }
        public void deleteWithQuery(String collection, String query){
            this.callApi(collection, "delete", query, "", "POST", "", null);
        }
        public void readWithMapping(String collection, HashMap<?, ?> jsonMap){
            query = new JSONObject(jsonMap).toString();
            this.readWithQuery(collection, query);
        }
        public void readWithQuery(String collection, String query){
            this.callApi(collection, "read", query, null, "POST", null, null);
        }
        public void readWithID(String collection, String ID){
            this.callApi(collection, "read", "", "instance", "POST", ID, "\"limit\":1");
        }
        public void readWithQueryOptions(String collection, int limit, int skip, String sort){
            this.callApi(collection, "read", query, "", "POST", "", "\"limit\":"+String.valueOf(limit)+",\"skip\":"+String.valueOf(skip)+"\"sort\":"+sort);
        }
        public void readAll(String collection, String query){
            this.callApi(collection, "read", "", null, "POST", null, null);
        }
        public void createWithMapping(String collection, HashMap<?, ?> jsonMap){
            query = new JSONObject(jsonMap).toString();
            this.createWithJSON(collection, query);
        }
        public void createWithJSON(String collection, String jObj){
            this.callApi(collection, "create", "", jObj, "POST", "", null);
        }
        public void updateWithMapping(String collection, HashMap<?, ?> jsonMap){
            query = new JSONObject(jsonMap).toString();
            this.readWithQuery(collection, query);
        }
        public void updateWithQuery(String collection, String query, String key, String value){
            this.callApi(collection, "update", query, "{\""+key+"\":\""+value+"\"}", "POST", "", null);
        }
        public void updateWithID(String collection, String ID, String key, String value){
            this.callApi(collection, "update", "", "{\""+key+"\":\""+value+"\"}", "POST", ID, null);
        }
        public void updateAll(String collection, String model){
            this.callApi(collection, "update", "", model, "POST", "", null);
        }

    //*************Functions used to fetch data***********
    //perform error checks here
        private JSONObject getJSONFromUrl(List<NameValuePair> params){
            JSONObject jObj = null;
            try {
                //try enabling a http connection and type
                StrictMode.ThreadPolicy policy = new StrictMode.
                        ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                URL urls = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) urls.openConnection();
                connection.setRequestMethod(requestType);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //perform POST request with the params you need
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                query = getQuery(params);
                writer.write(query);
                writer.close();
                os.close();
                jObj = readStream(connection.getInputStream());
            }
            catch (Exception e) {

				// TODO Auto-generated catch block
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

				// TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Put it in JSon object
            try {
                jObj = new JSONObject(jSon);
            }
            catch (JSONException e){

				// TODO Auto-generated catch block
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

