package com.anassaeed.backgroundmusic;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class MyPhoneStateReciever extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		MediaPlayer mySound =MediaPlayer.create(context, R.raw.a );

		// TODO Auto-generated method stub
		  PhoneStateListener customPhoneListener = new PhoneStateListener();
		    TelephonyManager telephony1 = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

	    telephony1.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);


	    Bundle bundle = intent.getExtras();
	    String number= bundle.getString("incoming_number");
	    if ( number != null ) {
	    	return;
	    }
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (MainActivity.test.isPlaying() == true){
        	MainActivity.test.stop();
        	MainActivity.test.release();
        }
        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
        		MainActivity.playing = false;
                MainActivity.test = MediaPlayer.create(context, R.raw.a );
                MainActivity.test.start();
                MainActivity.test.stop();
                MainActivity.test.release();
        }
        if(state.equals(TelephonyManager.EXTRA_STATE_IDLE))
        {
        	if(MainActivity.playing  == true ){
        		MainActivity.playing = false;
        		MainActivity.test.stop();
        		MainActivity.test.release();
        		return;
        	}
        }
              
         MainActivity.playing = true;
         MainActivity.test = MediaPlayer.create(context, R.raw.a );
         MainActivity.test.start();
        		

	}

}
