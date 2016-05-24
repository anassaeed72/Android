package com.anassaeed.sa_filereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 File myFile = new File("/storage/emulated/0/Anas/email.txt");

	        FileInputStream fIn = null;
	        BufferedReader myReader = null;
	      		try {
	      			fIn = new FileInputStream(myFile);
	      			   myReader = new BufferedReader(new InputStreamReader(fIn));

	      		} catch (FileNotFoundException e1) {
	      			e1.printStackTrace();
	      			 Toast.makeText(getApplicationContext(), "File Not found",
	  				         Toast.LENGTH_LONG).show();
	      			 return;
	      		} 
	      		
	      		
	      
	      	String emailAddress = "abc";
	      	while( emailAddress != null){
	      		try {
					emailAddress = myReader.readLine();
					if( emailAddress == null){
						continue;						
					}
					Log.i("tag",emailAddress);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	      		 		      		
	      	}
	      	try {
				myReader.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
