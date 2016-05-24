package com.anassaeed.bluetooth;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SendNumber extends Activity{
	public EditText phoneNumberEditText;
	private final String tag = "BluetoothApp";
	private Button btnSend;
	private Button btnSelectContact;
	private static final int PICK_CONTACT = 3;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.sendnumber);
        SendNumber.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        executeDelayed();
        FullScreencall();
        Log.i(tag, "send number called");
	    btnSend = (Button) findViewById(R.id.btnSendNumber);
	    btnSend.setOnClickListener(new OnClickListener() {
	  	  public void onClick(View v) {
	  		phoneNumberEditText = (EditText) findViewById(R.id.edittext);
			String phoneNumberString = phoneNumberEditText.getText().toString();
			byte[] bytesToSend = null;
			try {
				bytesToSend = phoneNumberString.getBytes("UTF-32");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writeNumberToStream(bytesToSend);
			try{
			Intent intent = new Intent(SendNumber.this, ReadAndWrite.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			} catch(Exception e){
				e.printStackTrace();
			}
			
	  	  }
	  	});
	    btnSelectContact = (Button) findViewById(R.id.btnSelectContact);
	    btnSelectContact.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Log.i(tag, "here");
				 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			        startActivityForResult(intent, PICK_CONTACT);
			      
			}
		});   
    }
	
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);
     Log.i(tag, "result called");
        switch(reqCode){
           case (PICK_CONTACT):
             if (resultCode == Activity.RESULT_OK){
                 Uri contactData = data.getData();
                 @SuppressWarnings("deprecation")
				Cursor c = managedQuery(contactData, null, null, null, null);
                 if (c.moveToFirst()){
                	 if (c.moveToFirst()){
                         // other data is available for the Contact.  I have decided
                         //    to only get the name of the Contact.
                         String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                         Toast.makeText(getApplicationContext(),"Contact Select "+  name, Toast.LENGTH_SHORT).show();
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
               	        	EditText temp = (EditText) findViewById(R.id.edittext);
               	        	temp.setText(dataS);
               	        	Log.i(tag, dataS);
               	        } 
               	        cursor.close();
                    	 }
                   }
              	       
                 }
             }
        }
    }

	 public void writeNumberToStream(byte[] bytes){	    	
	    	try {
	    		if (MainActivity.outputStream == null){
	    			Toast.makeText(SendNumber.this, "Number not sent",Toast.LENGTH_SHORT).show();
		    		Log.i(tag, "outputstream is null");
		    		return;
		    	}
	    		if ( bytes == null){
	    			Toast.makeText(SendNumber.this, "Number not sent",Toast.LENGTH_SHORT).show();
	    			Log.i(tag, "bytes is null");
	    			return;
	    		}
				MainActivity.outputStream.write(bytes);
				Log.i(tag, "Data Written to bluetooth Stream"+bytes);
				Toast.makeText(SendNumber.this,"Number was sent successfully ",Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Log.i(tag, "number not written");
				Toast.makeText(SendNumber.this,"Number was not send successfully please try again",Toast.LENGTH_SHORT).show();				
				Intent intent = new Intent(SendNumber.this, MainActivity.class);
				startActivity(intent);
				e.printStackTrace();
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
    public void FullScreencall() {

	    if(Build.VERSION.SDK_INT < 19){ //19 or above api
	        View v = this.getWindow().getDecorView();
	        v.setSystemUiVisibility(View.GONE);
	    } else {
	            //for lower api versions.
	        View decorView = getWindow().getDecorView(); 
	        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
	        decorView.setSystemUiVisibility(uiOptions);
	    }
	}
	private void executeDelayed() {
	    Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	        @Override
	        public void run() {
	            // execute after 500ms
	            FullScreencall();
	        }
	    }, 500);
	}
	@Override
	protected void onResume() {
	    super.onResume();
	    executeDelayed();
	}	
	public boolean onTouchEvent(MotionEvent event) {
		SendNumber.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    	   
	    return true;
	} 
}
