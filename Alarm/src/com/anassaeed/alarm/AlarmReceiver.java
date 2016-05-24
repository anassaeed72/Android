package com.anassaeed.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String tag = "Alarm";
		// TODO Auto-generated method stub
		if ( intent.getAction().equals("playAlarm")){
			
			try{
				Toast.makeText(context, "Timer Over", Toast.LENGTH_LONG).show();
				Log.i(tag," Playing sound");
		    	MediaPlayer mySound = MediaPlayer.create(context, R.raw.king );
		    	mySound.start();
		    	} catch ( Exception e ) {
		    		Log.i(tag,"exception");
		    		e.printStackTrace();
		    	}
		}
		
	}

}
