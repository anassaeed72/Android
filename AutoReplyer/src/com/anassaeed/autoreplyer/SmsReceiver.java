package com.anassaeed.autoreplyer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
 
@SuppressWarnings("deprecation")
public class SmsReceiver extends BroadcastReceiver
{
//	private static String number = ;
    @Override
    public void onReceive(Context context, Intent intent) 
    {
    	if ( MainActivity.toggleSwitch){
	    	//---get the SMS message passed in---
	        Bundle bundle = intent.getExtras();        
	        SmsMessage[] msgs = null;
	        String str = "";      
	        String phoneNumber = "";
	    	//---retrieve the SMS message received---
	        Object[] pdus = (Object[]) bundle.get("pdus");
	        msgs = new SmsMessage[pdus.length];            
	        for (int i=0; i<msgs.length; i++){
	            msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
	            phoneNumber = msgs[i].getOriginatingAddress();                     
	               
	        }
	        Log.i("Tag","the phone number is "+phoneNumber);
	 	   	SmsManager smsManager = SmsManager.getDefault();
	 	   	
	 	   	smsManager.sendTextMessage(phoneNumber, null, MainActivity.messageCurrent, null, null);
    	}                                        
    }
}