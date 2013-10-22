package easyecard.nfc;

import android.app.Activity;
import android.os.Bundle;

public class StartDownload extends Activity {
	
	//private DownloadManager mgr = null;
	//private long lastDownload = -1L;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_download);
		
		
		/*mgr=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
		registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		registerReceiver(onNotificationClick, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
	*/
		
	}

	/*public void startDownload(String result){
		Uri uri=Uri.parse("https://dl.dropbox.com/s/w5fktejwroi0lbt/lab.docx?token_hash=AAG4gfwezSP4PMBRbaAQ0OTbcD6fBEj2FXsKpVuEVDg4cg&dl=1");
		
		Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
			.mkdirs();
		
		lastDownload=mgr.enqueue(new DownloadManager.Request(uri)
				.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
				.setAllowedOverRoaming(false)
				.setTitle("Test")
				.setDescription("what ever useful, really?")
				.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"temp_Conrol.doc"));
	}
	
	BroadcastReceiver onComplete = new BroadcastReceiver(){
		public void onReceive(Context ctxt, Intent intent){
		 
		}
	};
	
	BroadcastReceiver onNotificationClick = new BroadcastReceiver(){
		public void onReceive(Context ctxt, Intent intent){
			
		}
	};*/
	/*public void onDestroy(){
		super.onDestroy();
		
		unregisterReceiver(onComplete);
		unregisterReceiver(onNotificationClick);
	}*/
}
