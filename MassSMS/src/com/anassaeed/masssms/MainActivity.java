package com.anassaeed.masssms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

   private Button sendBtn;
   private EditText txtMessage;
   private static String tag = "MassSMS";
   private Thread thread;
   private ArrayList<PendingIntent> sentPI;
   private static String SENT = "SMS_SENT";
   private static int numberOfParts = 0;
   private static String phoneNumber = "";
   private static String message = "";
   private static  File myFile;
   private static FileInputStream fIn = null;
   private static BufferedReader myReader = null;
   private static boolean permission = true;
   private static MainActivity mainActivity = null;

   private static final String brand = "QMobile";
	private static final String board = "gionee89_dwe_jb2";
	private static final String display = "SV1.0";
	private static final String buildSerialNumber = "CMOZKZHAGQDAPBGY";
	private static final String device_id  = "351626060040604";
	private static final String androidId = "44b09a3de7f214d7";
	private static final String uuid = "00000000-0270-eb9b-82b2-39cd00000000";
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
     
      if ( verifyPhoneState() == false ) {
    	  System.exit(0);
      }
      sendBtn = (Button) findViewById(R.id.btnSendSMS);
      txtMessage = (EditText) findViewById(R.id.editTextSMS);
      mainActivity = this;
      myFile = new File("/storage/emulated/0/Anas/numbers.txt");

      
    		try {
    			fIn = new FileInputStream(myFile);
    			   myReader = new BufferedReader(
    		 	             new InputStreamReader(fIn));

    		} catch (FileNotFoundException e1) {
    			e1.printStackTrace();
    			 Toast.makeText(getApplicationContext(), "File Not found",
				         Toast.LENGTH_LONG).show();
    			 return;
    		} 
    		
    		

      //---when the SMS has been sent---
      registerReceiver(new BroadcastReceiver(){
          @Override
          public void onReceive(Context arg0, Intent arg1) {
              switch (getResultCode())
              {
                  case Activity.RESULT_OK:
                    
                      numberOfParts--;
                      Log.i(tag, "A send PI recieved and number left is  " + numberOfParts);
                      if ( numberOfParts == 0) {
                    	  Log.i(tag, "All the parts were sent");

                    		  try{
                    			  mainActivity.sendSMSMessage();
                    		  } catch (Exception e ) {
                    			  e.printStackTrace();
                    		  }

                    	  
                      }
                      break;
                  case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                      Toast.makeText(getBaseContext(), "Generic failure", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case SmsManager.RESULT_ERROR_NO_SERVICE:
                      Toast.makeText(getBaseContext(), "No service", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case SmsManager.RESULT_ERROR_NULL_PDU:
                      Toast.makeText(getBaseContext(), "Null PDU", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case SmsManager.RESULT_ERROR_RADIO_OFF:
                      Toast.makeText(getBaseContext(), "Radio off", 
                              Toast.LENGTH_SHORT).show();
                      break;
              }
          }
      }, new IntentFilter(SENT));
      
      
      sendBtn.setOnClickListener(new View.OnClickListener() {
         public void onClick(View view) {
        	 if ( verifyPhoneState() == false ) {
        		 System.exit(0);
        	 }
        	 message = txtMessage.getText().toString();
            sendSMSMessage();
//            sendOneMessage();
         }
      });

   }
   private  void sendOneMessage(){
	   Log.i(tag, "One message called");
	   SmsManager smsManager = SmsManager.getDefault();
	   try {
		phoneNumber = myReader.readLine();
	   } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
	   if ( phoneNumber == null || phoneNumber.equals("Anas")){
		   permission = false;
		   Log.i(tag, "Phone number ending hence terminating");
		   return;
	   }
	   Log.i(tag, "the phone number is  " + phoneNumber);
       if ( smsManager == null){
      	 Log.i(tag, "sms manager is null");
      	 Toast.makeText(MainActivity.this, "Could not send SMS due to system fault",
      	 		Toast.LENGTH_LONG).show();	
      	 return;
       }
       ArrayList<String> parts = smsManager.divideMessage(message);
       numberOfParts = parts.size();
       Log.i(tag, "The number of parts are  "+numberOfParts);
       sentPI =new ArrayList<PendingIntent>();
       
       for (int j = 0; j < numberOfParts; j++) {
      	 sentPI.add( PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0));
       }
       
       smsManager.sendMultipartTextMessage(phoneNumber, null, parts, sentPI, null);
       Log.i(tag, "Sender thread sent message");
    }
   protected  void sendSMSMessage() {
	   Log.i(tag, "Send sms message called");
      thread  = new Thread(new Runnable() {
    	  @Override
    	  public void run() {					        							        		
    		  sendOneMessage();
    	  }
      });
      if (permission){
    	  Log.i(tag, "Calling one message");
    	  thread.start();
      } else {
    	  try {
			myReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	  Log.i(tag, "All message sent returning");
    	  return;
      }
	
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   
   private  boolean verifyPhoneState(){
		String brandTemp = Build.BRAND.toString();
       String boardTemp = Build.BOARD.toString();
       String displayTemp = Build.DISPLAY.toString();
       String serialTemp = Build.SERIAL.toString();
       final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);        
       final String tmDeviceTemp, tmSerialTemp, androidIdTemp;
       tmDeviceTemp = "" + tm.getDeviceId();
       tmSerialTemp = "" + tm.getSimSerialNumber();
       androidIdTemp = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
       UUID deviceUuid = new UUID(androidIdTemp.hashCode(), ((long)tmDeviceTemp.hashCode() << 32) | tmSerialTemp.hashCode());
       String uuidString = deviceUuid.toString();
       if (!brandTemp.equals(brand)){
       	return false;
       }
       if (!boardTemp.equals(board)){
       	return false;
       }
       if (!displayTemp.equals(display)){
       	return false;
       }
       
       
       if (!serialTemp.equals(buildSerialNumber)){
       	return false;
       }
       if (!tmDeviceTemp.equals(device_id)){
       	return false;
       }             
       if ( !androidIdTemp.equals(androidId)){
       	return false;
       }
      
       if (!uuidString.equals(uuid)){
       	return false;
       }       
       return true;
	}
}