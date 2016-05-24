package com.anassaeed.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
 
@SuppressWarnings("deprecation")
public class SmsReceiver extends BroadcastReceiver
{
//	private static String number = ;
    @Override
    public void onReceive(Context context, Intent intent) 
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
                str += "SMS from " + msgs[i].getOriginatingAddress();                     
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "\n";        
            }
            //---display the new SMS message---
            Log.i("BluetoothApp", str);
            if (msgs[0].getOriginatingAddress().equals(MainActivity.recieveNumber)){
            	if ( msgs[0].getMessageBody().toString().equals("1")){
            		Log.i("BluetoothApp", "Entered the if conition of equal number and content");
            		try{
            			
            			Intent intentOfRedAlert = new Intent(context, RedAlert.class);
            			intentOfRedAlert.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            			context.startActivity(intentOfRedAlert);
            		} catch(Exception e){
            			e.printStackTrace();
            		}
//            		context.o
//        			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            	}
            }
        }                         
    }
}