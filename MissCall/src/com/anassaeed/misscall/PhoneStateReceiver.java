package com.anassaeed.misscall;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;



import com.android.internal.telephony.ITelephony;

public class PhoneStateReceiver extends BroadcastReceiver {
	private static final String TAG = "Phone call";
	 private ITelephony telephonyService;
	 @Override
	    public void onReceive(Context context, Intent intent) {
		 if( MainActivity.calling == false){
	    		return;
	    	} 
	    	
		  TelephonyManager telephony = (TelephonyManager) 
		  context.getSystemService(Context.TELEPHONY_SERVICE);  
		  try {
		   Class c = Class.forName(telephony.getClass().getName());
		   Method m = c.getDeclaredMethod("getITelephony");
		   m.setAccessible(true);
		   telephonyService = (ITelephony) m.invoke(telephony);
		   //telephonyService.silenceRinger();
		   Thread.sleep(MainActivity.wait);
		   telephonyService.endCall();
		   MainActivity.times--;
		   MainActivity.calling = false;
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
	    }
}
