package com.anassaeed.bluetooth;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Intent;



public class AlarmFiveMinutes extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		String tag = "Alarm";
			Log.i(tag, "called");
			try{
				Toast.makeText(context, "Timer Over", Toast.LENGTH_LONG).show();
				Log.i(tag," Playing sound");
		    	MainActivity.mySound.start();
		    	Thread.sleep(5000);

		    	 Intent i = new Intent();
		         i.setClass(context,RedAlert.class);
		         i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		         context.startActivity(i);
				
		    	} catch ( Exception e ) {
		    		Log.i(tag,"exception");
		    		e.printStackTrace();
		    	}
		}
		


}
