package com.anassaeed.misscall;



import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
	public String number = "";
	public static boolean calling = false;
	public static int times = 0;
	public static int wait = 0;
	public EditText numberInput;
	public EditText timesInput;
	public EditText waitInput;
	public Button button;
	public boolean makecalls = false;
	Button btnSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numberInput = (EditText) findViewById(R.id.phone_number);
        timesInput = (EditText) findViewById(R.id.times_to_call);
        waitInput = (EditText) findViewById(R.id.wait_times);
        button = (Button) findViewById(R.id.btnCall);
       
        TelephonyManager telephonyManager =  
                (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);  
        PhoneStateListener callStateListener = new PhoneStateListener() {  
            public void onCallStateChanged(int state, String incomingNumber)   
            {  
                 
                                      
                    if(state==TelephonyManager.CALL_STATE_IDLE && makecalls == true){  
                       if ( times > 0){
                    	   times--;
                    	   makeCall();
                       } else {
                    	   makecalls = false;
                       }
                    
                    }  
            }  
            };  
            telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);  
        button.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		number = numberInput.getText().toString(); 
				times  = Integer.parseInt(timesInput.getText().toString());
				wait  = Integer.parseInt(waitInput.getText().toString());
				makecalls = true;
				makeCall();
			}
		});
       
        btnSelect = (Button) findViewById(R.id.btnSelectContact);
        btnSelect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			        startActivityForResult(intent, PICK_CONTACT);
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
    public void makeCall(){
    	Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
    	phoneCallIntent.setData(Uri.parse("tel:"+number));
    	calling = true;
    	startActivity(phoneCallIntent);
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
