package com.anassaeed.back;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class MyPhoneStateListener extends BroadcastReceiver{

	public void onReceive(Context context, Intent intent) {
//		MediaPlayer mySound =MediaPlayer.create(context, R.raw.a );
	
		AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	//	mAudioManager.setSpeakerphoneOn(true);
		mAudioManager.setMicrophoneMute(false);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
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
        if(state.equals(TelephonyManager.EXTRA_STATE_IDLE) || state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
        	if( MainActivity.playing == true ){
        		MainActivity.playing = false;
        		MainActivity.test.stop();
        		MainActivity.test.release();
            	return;

        	}
        }
        
      //  MainActivity.playing = true;
        if ( state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
        	MainActivity.test = MediaPlayer.create(context, R.raw.a );
        	if ( MainActivity.test.isPlaying() == false ) {
        		MainActivity.test.start();
        		MainActivity.playing = true;
        	}
        }
	}

}
