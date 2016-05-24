package com.anassaeed.repeater;

import java.sql.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private static final int PICK_CONTACT = 3;

	public EditText number;
	Button btn;
	Button btnSelect;
	boolean calling = false;
	TextView textView = null;
	String tag ="Repeater";
	public void call(String number){
		 Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:"+number));
			startActivity(callIntent);
	}
	public boolean checkCondition(String callNumber){
		@SuppressWarnings("deprecation")
		Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int counter = 0;
		managedCursor.moveToLast();
		while (true) {
			if (managedCursor == null){
				return false;
			}
			Log.i(tag,"Looped: "+ String.valueOf(++counter));
			String phNumber = managedCursor.getString(number);
			String callType = managedCursor.getString(type);
			String callDuration = managedCursor.getString(duration);
			String callDate = managedCursor.getString(date);
			Date callDayTime = new Date(Long.valueOf(callDate));

			String dir = null; 
			int dircode = Integer.parseInt(callType); 
			
			 switch (dircode) { 
				case CallLog.Calls.OUTGOING_TYPE: dir = "OUTGOING";
				if (phNumber.equals(callNumber)){
					if (callDuration.equals("0")){
						Log.i(tag, "All conditions met");
						return true;
					} else {
						Log.i(tag, "Duration not zero :" + callDuration);
					}
				}
				return false;
//				break;
			}
			 managedCursor.moveToPrevious();
		}
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textview_call);
       // getCallDetails();
        number = (EditText) findViewById(R.id.phone_number);
        btn = (Button) findViewById(R.id.btnCall);
        btnSelect = (Button) findViewById(R.id.btnSelectContact);
        btnSelect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			        startActivityForResult(intent, PICK_CONTACT);
			}
		});
//        Toast.makeText(getBaseContext(),"03004138722",Toast.LENGTH_LONG).show();
        btn.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
				 final String numberReal = number.getText().toString(); 
				 call(numberReal);
				 
			     TelephonyManager telephonyManager = (TelephonyManager)getSystemService(getBaseContext().TELEPHONY_SERVICE);  
		     PhoneStateListener callStateListener = new PhoneStateListener() {  
			    	 public void onCallStateChanged(int state, String incomingNumber)   
			    	 {  
			    		 if( state == TelephonyManager.CALL_STATE_OFFHOOK ) {  
			    			 calling = true;
			    		 }  
			    		 if( state==TelephonyManager.CALL_STATE_IDLE && calling  == true ){
			    			 try {
			 					Thread.sleep(1000);
			 				} catch (InterruptedException e) {
			 					// TODO Auto-generated catch block
			 					e.printStackTrace();
			 				}
			    			 if (checkCondition(numberReal)  == true) {
			    				 calling = false;
			    				 Log.i(tag, "Calling again");
			    				 call(numberReal);
			    			 }
			    		 }  else {
			    			 Log.i(tag, "call has been recieved so exiting");
			    		 }
			    	 }
			  };  
			  telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);
	  
			}
		});
  
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);
     
        switch(reqCode){
           case (PICK_CONTACT):
             if (resultCode == Activity.RESULT_OK){
                 Uri contactData = data.getData();
                 Cursor c = managedQuery(contactData, null, null, null, null);
     
                 if (c.moveToFirst()){
                	 if (c.moveToFirst()){
                         // other data is available for the Contact.  I have decided
                         //    to only get the name of the Contact.
                         String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                         Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                     }
                     if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
              		    //Query phone here.  Covered next
                         ContentResolver cr = getContentResolver();
                         String id = c.getString(
                                 c.getColumnIndex(ContactsContract.Contacts._ID));
                    	 if (Integer.parseInt(c.getString(
                                 c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                              Cursor cursor = cr.query(
               		    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
               		    null, 
               		    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
               		    new String[]{id}, null);
               	        while (cursor.moveToNext()) {
               		    // Do something with phones
               	        	String dataS = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
               	        	TextView temp = (TextView) findViewById(R.id.phone_number);
               	        	temp.setText(dataS);
               	        	
               	        } 
               	        cursor.close();
                    	 }
                   }
              	       
                 }
             }
        }
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

   
