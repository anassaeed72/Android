package com.anassaeed.locker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Screen extends BroadcastReceiver {

	private MainActivity caller = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("Screen", "Inside the receiver");
		// TODO Auto-generated method stub
		String timesPhoneAccessed = null;
  	  File myFile = new File("/storage/emulated/0/Anas/User.txt");

		  FileInputStream fIn = null;
		try {
			fIn = new FileInputStream(myFile);
			  BufferedReader myReader = new BufferedReader(
		 	             new InputStreamReader(fIn));
			timesPhoneAccessed = myReader.readLine();

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int temp = Integer.parseInt(timesPhoneAccessed);
		temp++;
		timesPhoneAccessed = String.valueOf(temp);
		try{
		caller = new MainActivity();
//		caller.showValueToast(timesPhoneAccessed);
		 Toast.makeText(context,"Phone Accessed "+ timesPhoneAccessed + " times", 
	               Toast.LENGTH_LONG).show();
		//caller.showValueToast(test1);
		} catch(Exception e){
			e.printStackTrace();
		}
		try {
		   	  File myFileWrite = new File("/storage/emulated/0/Anas/User.txt");
		   	    FileOutputStream fOut = new FileOutputStream(myFile);
		   	    OutputStreamWriter myOutWriter = 
		   	                            new OutputStreamWriter(fOut);
		           myOutWriter.write(timesPhoneAccessed);
		           myOutWriter.close();
		     }
		     catch (IOException e) {
		   	 
		   	  } 		
	}

}
