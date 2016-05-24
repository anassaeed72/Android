package com.anassaeed.contactssms;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class MainActivity extends Activity {
	private static final String tag = "contactSMS";
	EditText edittext;
	Button button;
	private static  SmsManager sms;
	private static String message;
	Cursor cursor;
	String phoneNumber;
	String _ID = ContactsContract.Contacts._ID;
	String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
	Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
	String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
	ContentResolver contentResolver;
	private static final String SENT = "SMS_SENT";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		FullScreencall();
		executeDelayed();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.button1);
        sms = SmsManager.getDefault();
        contentResolver = getContentResolver();        
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edittext = (EditText) findViewById(R.id.editText1);
				message = edittext.getText().toString();
				Toast.makeText(getBaseContext(), "Starting sending messges", Toast.LENGTH_SHORT);
				getContacts();
				fetchContacts();
			}
		});
		
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
	public void getContacts(){
		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;		
		cursor = contentResolver.query(CONTENT_URI, null,null, null, null);	
	}
	public void fetchContacts() {		
		Log.i(tag, "Fetch contact called");
		// Loop for every contact in the phone
		if ( cursor.isClosed()){
			return;
		}
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
				if (hasPhoneNumber > 0) {
					// Query and loop for every phone number of the contact
					Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
					if (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
						sendSMS();
					}
					phoneCursor.close();
				} else {
					try{
						fetchContacts();
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			} else {
				cursor.close();
				makeAlert("Done", "All Messages sent");
			}
		}
	}
	private void sendSMS()
    {        
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);       
        Log.i(tag, "Message is "+message + " number is "+phoneNumber);
        try{
        	sms.sendTextMessage(phoneNumber, null, message, sentPI, null);        
        }catch (Exception e) {
        	e.printStackTrace();
        }
        reciever();
//        fetchContacts();
    }
	public void reciever(){
		 registerReceiver(new BroadcastReceiver(){
	            @Override
	            public void onReceive(Context arg0, Intent arg1) {
	                switch (getResultCode())
	                {
	                    case Activity.RESULT_OK:
	                    	try{
	                    		Log.i(tag, "message sent");
	                			fetchContacts();
	                		}catch (Exception e ){
	                			e.printStackTrace();
	                		}
	                        break;
	                   
	                }
	            }
	        }, new IntentFilter(SENT));
		
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

		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    return true;
	}
	 public void makeAlert(String title,String text){
			 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

		        alertDialogBuilder.setTitle(title)
					.setMessage(text)
					.setCancelable(false)				
					.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
							executeDelayed();
							FullScreencall();
					}
					}); 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
		 }
}
