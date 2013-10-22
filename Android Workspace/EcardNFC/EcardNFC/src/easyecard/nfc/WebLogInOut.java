package easyecard.nfc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebLogInOut extends Activity {
	public static String uri = "https://www.subculture.fm/";
	
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle intent = getIntent().getExtras();
		String logInOut = intent.getString("log");
		setContentView(R.layout.activity_webview);
		WebView webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new WebAppInterface(this), "Circles");
		String url = "";
		if (logInOut.equals("facebook")){
			url = uri+"auth/"+logInOut;	
		}
		else if (logInOut.equals("logmeout")){
			url = uri+logInOut;	
			
		}
		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient(){
			int loaded = 0;
			@Override
			public boolean shouldOverrideUrlLoading(WebView View, String Url){
				View.loadUrl(Url);
				return false;
			}
			@Override
			public void onPageFinished(WebView View, String Url) {
				if (Url.startsWith("https://www.subculture.fm/")){
					Toast.makeText(getApplicationContext(), loaded + " __  Loaded:" + Url, Toast.LENGTH_SHORT).show();
					if (loaded == 2) {
						View.loadUrl("javascript:var x = window.___; Circles.showToast(x);");
						//WebLogInOut.this.finish();
					}
					loaded++;
					
				}
			}
		});
		
	}
	public class WebAppInterface {
	    Context mContext;
	    
	    // Instantiate the interface and set the context 
	    WebAppInterface(Context c) {
	        mContext = c;
	        
	    }
	    // Show a toast from the web page 
	    @JavascriptInterface
	    public void showToast(String toast) {
	        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	    }
	}
}
