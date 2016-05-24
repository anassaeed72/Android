package com.anassaeed.massemails;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
	private Button btnSend;
	public String tag = "MassEmailS";
	public static String databaseName = "DB";
	public static String usernameDB = "usernameDB";
	public static String passwordDB = "passwordDB";
	public static String firstTime = "firstTime";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);
    	Boolean intial = shfObject.getBoolean(MainActivity.firstTime,false);
    	SharedPreferences.Editor  shfEditorObject=shfObject.edit();
    	if (intial == false ){
        	shfEditorObject.putBoolean(MainActivity.firstTime,true);
        	shfEditorObject.commit();
        	Intent intent = new Intent(MainActivity.this, SetAccount.class);
        	startActivity(intent);
    	}

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new OnClickListener() {
        	 
    		@Override
    		public void onClick(View v) {	
    			Toast.makeText(getBaseContext(), "Starting sending emails",Toast.LENGTH_SHORT);
    			String stringNull = "null";
    			String usernameFromDB = shfObject.getString(MainActivity.usernameDB, stringNull);
    			String passwordFromDB = shfObject.getString(MainActivity.passwordDB, stringNull);
    			if ( usernameFromDB.equals(stringNull) || passwordFromDB.equals(stringNull) ){
    				Toast.makeText(getBaseContext(), "Account not set up properly.Please set account", Toast.LENGTH_LONG).show();
    				Intent intent = new Intent(MainActivity.this, SetAccount.class);
    				startActivity(intent);
    			}
    			
    			Mail mail = new Mail();   
    			
    			mail.setUsername(usernameFromDB);
    			mail.setPassword(passwordFromDB);
    		    EditText subject = (EditText)findViewById(R.id.subject);
    		    mail.setSubject(subject.getText().toString());
    		    subject.setText("");
    		        
    		    EditText body = (EditText) findViewById(R.id.body);
    		    mail.setBody(body.getText().toString());
    		    body.setText("");
    		    Log.i(tag, "after setting things");
    		     
    		        // for the recipetents part
    		        
    		        File myFile = new File("/storage/emulated/0/Anas/email.txt");

    		        FileInputStream fIn = null;
    		        BufferedReader myReader = null;
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
    		      		
    		      		
    		      	String emailAddress = null;
    		      	Toast.makeText(getBaseContext(), "Starting sending emails", Toast.LENGTH_SHORT).show();
    		      	Log.i(tag, "starting to read addresses");
    		      	while(true){
    		      		try {
							emailAddress = myReader.readLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
    		      		if ( emailAddress == null){
    		      			break;
    		      		}    	

    		      		String []recipetents = new String[1];
    		      		recipetents[0] = emailAddress;
        		        mail.setTo(recipetents);
        		        try {
							mail.send();
						} catch (Exception e) {
							makeAlert("Error ...","Could not send emails. Aborting");
							e.printStackTrace();
							return;
						}
    		      	}
    		      	try {
						myReader.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}		      	
    		      
    		        	
    					makeAlert("Alert", "All emails were succesfully sent");
    				
    		}
     
    	});
       
    }

    public void makeAlert(String title,String text){
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

	        alertDialogBuilder.setTitle(title)
				.setMessage(text)
				.setCancelable(false)				
				.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
				}
				}); 
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
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
        	Log.i(tag, "Action settings called");
        	try{
        	Intent intent = new Intent(MainActivity.this, SetAccount.class);
        	startActivity(intent);
        	} catch(Exception e){
        		e.printStackTrace();
        	}
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
