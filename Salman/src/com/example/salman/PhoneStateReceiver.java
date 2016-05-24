package com.example.salman;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneStateReceiver extends BroadcastReceiver {
	Context context = null;
	private static final String TAG = "Phone call";
	 private ITelephony telephonyService;
	 @Override
	    public void onReceive(Context context, Intent intent) {
	    	
		 Log.v(TAG, "Receving....");
		  TelephonyManager telephony = (TelephonyManager) 
		  context.getSystemService(Context.TELEPHONY_SERVICE);  
		  try {
		   Class c = Class.forName(telephony.getClass().getName());
		   Method m = c.getDeclaredMethod("getITelephony");
		   m.setAccessible(true);
		   telephonyService = (ITelephony) m.invoke(telephony);
		   //telephonyService.silenceRinger();
		   Thread.sleep(8000);
		   telephonyService.endCall();
		   System.exit(0);
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
	    }


//    public static String TAG="PhoneStateReceiver";
  //  @Override
//    public void onReceive(Context context, Intent intent) {
//    	
//    	
//        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) { 
//            // Outgoing call
//            String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//            Log.d(TAG,"PhoneStateReceiver **Outgoing call " + outgoingNumber);
//
//            setResultData(null); // Kills the outgoing call
//
//        }
//    }

//    public boolean killCall(Context context) {
//        try {
//            // Get the boring old TelephonyManager
//            TelephonyManager telephonyManager =
//                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//            // Get the getITelephony() method
//            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
//            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
//
//            // Ignore that the method is supposed to be private
//            methodGetITelephony.setAccessible(true);
//
//            // Invoke getITelephony() to get the ITelephony interface
//            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
//
//            // Get the endCall method from ITelephony
//            Class telephonyInterfaceClass =  
//                    Class.forName(telephonyInterface.getClass().getName());
//            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
//
//            // Invoke endCall()
//            methodEndCall.invoke(telephonyInterface);
//
//        } catch (Exception ex) { // Many things can go wrong with reflection calls
//            Log.d(TAG,"PhoneStateReceiver **" + ex.toString());
//            return false;
//        }
//        return true;
//    }

}