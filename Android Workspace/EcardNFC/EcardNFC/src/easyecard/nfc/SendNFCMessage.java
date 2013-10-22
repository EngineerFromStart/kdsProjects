package easyecard.nfc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class SendNFCMessage extends Activity {
	private NfcAdapter mAdapter;
	private NdefMessage mMessage;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_send_nfcmessage);
		
		//get the nfc adapter
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        
        //create records of information
        //NdefRecord picture = NdefRecord.createMime("image/jpeg", "mimeData".getBytes());
        NdefRecord name = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(), "name".getBytes(), "Karandeep".getBytes());
        NdefRecord company = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(), "company".getBytes(), "Mobile-MedTech".getBytes());
                
        //put record in the message and send
        mMessage = new NdefMessage(
        		new NdefRecord[] {name, company}
        );
        if (mAdapter != null) {
        	mAdapter.setNdefPushMessage(mMessage, this);
        } else {
        	Toast.makeText(this, "NFC is not avaiable!", Toast.LENGTH_LONG).show();
	        finish();
	        return;
        	//add error statement here
        }
	}
	@Override
	//creates the options menu, menu is defined under menu/send_nfcmessage.xml
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.send_nfcmessage, menu);
		return true;
		
	}
	
	//called when menu item is clicked and start a function
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if (id == R.id.profile){
				viewProfiles();
				return true;
		}	
		else {
			return super.onOptionsItemSelected(item);
		}
		
	}
	//start profile activity
	public void viewProfiles(){
		Intent showProfiles = new Intent(this, ShowProfiles.class);
		startActivity(showProfiles);
	}
}
