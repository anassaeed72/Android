package com.anassaeed.blocker;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract.PhoneLookup;
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
		 Log.v(TAG, "Receving....");
		  TelephonyManager telephony = (TelephonyManager) 
		  context.getSystemService(Context.TELEPHONY_SERVICE);  
		  PhoneStateListener customPhoneListener = new PhoneStateListener();
		    TelephonyManager telephony1 = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

	    telephony1.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);


	    Bundle bundle = intent.getExtras();
	     number= bundle.getString("incoming_number");
	     if ( number == null){
	    	 return;
	     }
		  try {
		   Class c = Class.forName(telephony.getClass().getName());
		   Method m = c.getDeclaredMethod("getITelephony");
		   m.setAccessible(true);
		   telephonyService = (ITelephony) m.invoke(telephony);
		   try {

		   Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
				   String[] mPhoneNumberProjection = { PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
				   Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
				      if (cur.moveToFirst()) {
				 		 Log.v(TAG, "Contact Exists...");
				         return;
				      }
				   } catch(Exception e ){
					   e.printStackTrace();
				   }
		   telephonyService.endCall();
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
	    }
}
