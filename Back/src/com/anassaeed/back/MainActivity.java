package com.anassaeed.back;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;

import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	static MediaPlayer test;
	static boolean playing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelephonyManager telephonyManager =  
                (TelephonyManager)getSystemService(getBaseContext().TELEPHONY_SERVICE);  
   
  PhoneStateListener callStateListener = new PhoneStateListener() {  
  public void onCallStateChanged(int state, String incomingNumber)   
  {  
        if(state==TelephonyManager.CALL_STATE_RINGING){  
                  Toast.makeText(getApplicationContext(),"Phone Is Riging",  
                                                                   Toast.LENGTH_LONG).show();  
        }  
          if(state==TelephonyManager.CALL_STATE_OFFHOOK){  
        	  Toast.makeText(getApplicationContext(),"Phone is Currently in A call",   
                      Toast.LENGTH_LONG).show(); 
              try{
            	  File myFileWrite = new File("/storage/emulated/0/Anas/temp.txt");
  		   	    FileOutputStream fOut = new FileOutputStream(myFileWrite,true);
  		   	    OutputStreamWriter myOutWriter = 
  		   	                            new OutputStreamWriter(fOut);
  		           myOutWriter.write("anasOffhookanasoffhook");
            	
            	  boolean condition = false;            	  Method getFgState = null;
            	  Object cm = null;
            	  try {
            	//	  ReflectedPhoneFactory.makeDefaultPhone(getBaseContext());
            		  Object phoneproxy = ReflectedPhoneFactory.getDefaultPhone(getBaseContext());
            	   Class cmDesc = Class.forName("com.android.internal.telephony.CallManager");
            	    Method getCM = cmDesc.getMethod("getInstance");
            		  getFgState = cmDesc.getMethod("getActiveFgCallState");
            	    cm = getCM.invoke(null);
            	  } catch (Exception e) {
            	    e.printStackTrace();
            	  }
//            	  Thread.sleep(3000);
            	//  makeDefaultPhones(getBaseContext());
            //	  for ( int i =0 ; i < 10 ;i++){

            	  Object state1 = getFgState.invoke(cm);
            	  myOutWriter.write(state1.toString());

            	  
            	  //            	 while(true){
            	  Thread.sleep(4000);

            	  state1 = getFgState.invoke(cm);
            	  myOutWriter.write(state1.toString());
            	  myOutWriter.write("anas");
            	  
            	  if (state1.toString().equals("ACTIVE")) {
            	    // If the previous state wasn't "ACTIVE" then the
            	    // call has been established.
              	    myOutWriter.write("active");
              	    System.exit(0);

            	  }
            	  if ( condition){
            		//  break;
            	  }
            	//  }
            	  myOutWriter.close();
              } catch(Exception e ) {
            	  e.printStackTrace();
              }              
          }  
                            
          if(state==TelephonyManager.CALL_STATE_IDLE){  
              Toast.makeText(getApplicationContext(),"phone is neither ringing nor in a call",  
                                                                                           Toast.LENGTH_LONG).show();  
           
				File myFileWrite = new File("/storage/emulated/0/Anas/temp.txt");
				FileOutputStream fOut = null;
				try {
				fOut = new FileOutputStream(myFileWrite,true);
				OutputStreamWriter myOutWriter = 
				  new OutputStreamWriter(fOut);
				myOutWriter.write("anasRingingt");
				
				myOutWriter.close();
				} catch (IOException e) {
				// TODO Auto-generated catch block
e.printStackTrace();
}
          }  
  }  
  };  
  telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);  

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
