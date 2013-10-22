package easyecard.nfc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class RecieveNFCMessage extends Activity {

	private NfcAdapter mAdapter;
	private IntentFilter[] mFilters;
	private PendingIntent mIntent;
	private String techListsArray[][];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recieve_nfcmessage);
		
		//get the default nfc adapter
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mAdapter == null) {
	        Toast.makeText(this, "NFC is not avaiable!", Toast.LENGTH_LONG).show();
	        finish();
	        return;
        }
		
		mIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		

		IntentFilter nfcIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
        	nfcIntent.addDataType("*/*");  
        }
        catch (MalformedMimeTypeException e){
        	throw new RuntimeException("fail", e);
        }
        
        mFilters = new IntentFilter[] { nfcIntent };
        
        techListsArray = new String[][] { new String[] { NfcF.class.getName() } }; 
	}

	public void onPause() {
	    super.onPause();
	    mAdapter.disableForegroundDispatch(this);
	}
	
	public void onResume() {
	    super.onResume();
	    mAdapter.enableForegroundDispatch(this, mIntent, mFilters, techListsArray);
	}
	
	//used to process that data on new intent
	@Override
    protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);  
		TextView textBox = (TextView) findViewById(R.id.textV);
		textBox.setText("Intent ran but no message");
			
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
        	NdefMessage[] messages = null;
            
        	Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        	if (rawMsgs != null){
            	messages = new NdefMessage[rawMsgs.length];
            	for (int i = 0; i < rawMsgs.length; i++) {
                    messages[i] = (NdefMessage) rawMsgs[i];
                }
            }
        	 else if (rawMsgs == null){
	            textBox.setText("Nothing is present in message");
	        }
        	if(messages[0] != null) {
                String result="";
                byte[] payload = messages[0].getRecords()[0].getPayload();
               for (int b = 0; b<payload.length; b++) { // skip SOH
                    result += (char) payload[b];
                }
                
                //Intent startDownload = new Intent(this, downloadstart.class);
                //startDownload.putExtra(EXTRA_MESSAGE, DownUri);
                //startActivity(startDownload);
               
               textBox.setText(result);
            }
            
        }
        else textBox.setText("Error");
    }
}
