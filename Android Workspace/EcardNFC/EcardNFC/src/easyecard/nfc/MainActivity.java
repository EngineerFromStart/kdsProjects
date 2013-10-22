package easyecard.nfc;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void sendPackage (View view){
		Intent sendData = new Intent(this, SendNFCMessage.class);
		startActivity(sendData);
	}
	
	public void recievePackage (View view)
	{
		Intent recievePackage = new Intent(this, RecieveNFCMessage.class);
		startActivity(recievePackage);
	}
	
	public void FBlogin (View view)
	{
		Intent LogInFb = new Intent(this, WebLogInOut.class);
		LogInFb.putExtra("log", "facebook");
		startActivity(LogInFb);
	}
	public void logout (View view)
	{
		Intent LogOut = new Intent(this, WebLogInOut.class);
		LogOut.putExtra("log", "logmeout");
		startActivity(LogOut);
	}
}
