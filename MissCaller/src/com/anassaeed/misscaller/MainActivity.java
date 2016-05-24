package com.anassaeed.misscaller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static boolean calling = false;
	public static final int waitTime = 5000;
	public static final String tag = "Misscaller";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 TelephonyManager telephonyManager =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);  
		 PhoneStateListener callStateListener = new PhoneStateListener();
	  telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);  
		makeCallsFromFile("/storage/emulated/0/Anas/callNumbers.txt");
	}

	public void makeCallsFromFile(String filename){
		File file = new File(filename);
		BufferedReader myReader = null;
 		try {
 			FileInputStream fIn = new FileInputStream(file);
 			 myReader = new BufferedReader(new InputStreamReader(fIn));
 		} catch (FileNotFoundException e1) {
 			e1.printStackTrace();
 			 Toast.makeText(getApplicationContext(), "File Not found",
				         Toast.LENGTH_LONG).show();
 			 return;
 		} 
 		String oneNumber=  "";
 		try{
 			if((oneNumber = myReader.readLine())!= null){
 				makeCall(oneNumber);
// 				return;
// 				while(calling == true){
// 					try{
// 						Thread.sleep(1000);
// 					}catch (Exception e) {
// 						e.printStackTrace();
// 					}
// 				}
 			}
 		}catch (Exception e ) {
 			e.printStackTrace();
 		}
	}
	public void makeCall(String number){
		Log.i(tag, number);
    	Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
    	phoneCallIntent.setData(Uri.parse("tel:"+number));
    	calling = true;
    	startActivity(phoneCallIntent);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
