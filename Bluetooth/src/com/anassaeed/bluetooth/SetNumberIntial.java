package com.anassaeed.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class SetNumberIntial extends Activity {
	public static final String  sharedBoolean = "boolean";
	private static final int PICK_CONTACT = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setnumber);
        Log.i("tag","set intial called");
        SetNumberIntial.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        executeDelayed();
        FullScreencall();
        makeAlert("Alert","Please set the number from which you want to recieve the sms messages.Thank You");
        final EditText textview = (EditText) findViewById(R.id.edittext1);
        Button button = (Button) findViewById(R.id.btnSendNumber1);
        button.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View view) {
            	String temp = textview.getText().toString();
            	SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);
            	SharedPreferences.Editor  shfEditorObject=shfObject.edit();
            	shfEditorObject.putString(MainActivity.recieveNumber,temp);
            	shfEditorObject.commit();
            	Toast.makeText(SetNumberIntial.this, "Number Saved "+temp, Toast.LENGTH_SHORT).show();
            		
            		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SetNumberIntial.this);

         	        alertDialogBuilder.setTitle("Alert")
         				.setMessage("The app is configured.Have a nive time using it.Thank You")
         				.setCancelable(false)				
         				.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
         				public void onClick(DialogInterface dialog,int id) {
         						dialog.cancel();
         						Intent intent = new Intent(SetNumberIntial.this, MainActivity.class);
         	            		startActivity(intent);
         	            		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
         	            		
         				}
         				}); 
         			// create alert dialog
         			AlertDialog alertDialog = alertDialogBuilder.create();
         			alertDialog.show();
         			
         			
            }  
        });         
        
        Button btnSelectContact = (Button) findViewById(R.id.btnSelectContact1);
	    btnSelectContact.setOnClickListener(new View.OnClickListener() {
			
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
               	        	EditText temp = (EditText) findViewById(R.id.edittext1);
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

	 public void makeAlert(String title,String text){
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SetNumberIntial.this);

	        alertDialogBuilder.setTitle(title)
				.setMessage(text)
				.setCancelable(false)				
				.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
				}
				}); 
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	 }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Log.i("test","In menu");
            return true;
        }
        if (id == R.id.action_settings){
        	
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
		SetNumberIntial.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    	   
	    return true;
	} 
}
