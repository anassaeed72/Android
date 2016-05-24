package com.anassaeed.sa_callender;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
public class IncomingCallReceiver extends BroadcastReceiver
{
	Context context = null;
	private static final String TAG = "Phone call";
	 private ITelephony telephonyService;
    @Override
    public void onReceive(Context context, Intent intent) 
    {
    	TelephonyManager telephony = (TelephonyManager) 
    			  context.getSystemService(Context.TELEPHONY_SERVICE);  
    			  try {
    			   Class c = Class.forName(telephony.getClass().getName());
    			   Method m = c.getDeclaredMethod("getITelephony");
    			   m.setAccessible(true);
    			   telephonyService = (ITelephony) m.invoke(telephony);
    			   //telephonyService.silenceRinger();
    			   telephonyService.endCall();
    			  } catch (Exception e) {
    			   e.printStackTrace();
    			  }
    	
                             
	}
}