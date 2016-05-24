package com.anassaeed.outgoingblocker;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class IncomingPhoneStateReceiver extends BroadcastReceiver {
	Context context = null;
	private static final String TAG = "Phone call";
	
	 private ITelephony telephonyService;
	 @Override
	    public void onReceive(Context context, Intent intent) {
	    	String number = null;
		  TelephonyManager telephony = (TelephonyManager) 
		  context.getSystemService(Context.TELEPHONY_SERVICE);  
		  PhoneStateListener customPhoneListener = new PhoneStateListener();
		    TelephonyManager telephony1 = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

	    telephony1.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);


	    Bundle bundle = intent.getExtras();
	     number= bundle.getString("incoming_number");
	     boolean check = false;
	     if ( number == null){
	    	 check = true;
	    	 //return;
	     }
		  try {
		   Class c = Class.forName(telephony.getClass().getName());
		   Method m = c.getDeclaredMethod("getITelephony");
		   m.setAccessible(true);
		   telephonyService = (ITelephony) m.invoke(telephony);
		   if ( check == true){
			   telephonyService.endCall();
		   }
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
	    }
}
