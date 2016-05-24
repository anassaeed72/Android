package com.anassaeed.massemail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
	private Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new OnClickListener() {
        	 
    		@Override
    		public void onClick(View v) {
    		
    				Mail mail = new Mail();
    		        
    				EditText username = (EditText)  findViewById(R.id.username);
       		        mail.setUsername(username.getText().toString());
       		        username.setText("");
       		        
       		        EditText password = (EditText) findViewById(R.id.password);
       		        mail.setPassword(password.getText().toString());
       		        password.setText("");
       		        
    		        EditText subject = (EditText)findViewById(R.id.subject);
    		        mail.setSubject(subject.getText().toString());
    		        subject.setText("");
    		        
    		        EditText body = (EditText) findViewById(R.id.body);
    		        mail.setBody(body.getText().toString());
    		        body.setText("");
    		        
    		     
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
    		      		
    		      		
    		      	ArrayList<String> recipetents = new ArrayList<String>();
    		      	String emailAddress = null;
    		      	while(true){
    		      		try {
							emailAddress = myReader.readLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    		      		if ( emailAddress == null){
    		      			break;
    		      		}    	

    		      		recipetents.add(emailAddress);
    		      		
    		      	}
    		      	try {
						myReader.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}		      	
    		      	try{
    		        String[] to = new String[recipetents.size()];
    		        for ( int i = 0;i < recipetents.size();i++){
    		    	   to[i] = recipetents.get(i);
    		        }
    		        mail.setTo(to);
    		      	} catch(Exception e){
    		      		e.printStackTrace();
    		      	}
    		        try {
    		        	Toast.makeText(getBaseContext(), "Starting sending emails", Toast.LENGTH_SHORT).show();
    					mail.send();
    					makeAlert("Alert", "All emails were succesfully sent");
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					Toast.makeText(getBaseContext(), "Emails were not sent", Toast.LENGTH_SHORT).show();
    					e.printStackTrace();
    				}
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
