package com.anassaeed.locker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

	public void showValueToast(String value){
		 Toast.makeText(getBaseContext(), value, 
	               Toast.LENGTH_LONG).show();
	}
	public void second(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void minute(){
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void hour(){
		for ( int i = 0; i < 60; i++){
			minute();
		}
	}
	public void autoIntial(){
		for ( int i = 0; i <11; i++){
			hour();
		}
		for ( int i = 0; i < 41;i++){
			minute();
		}
		intial(false);
		for ( int i = 0; i <12; i++){
			hour();
		}
		for ( int i = 0; i < 18;i++){
			minute();
		}
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new Close();
        registerReceiver(mReceiver, filter);
       Button intial;
       intial = (Button) findViewById(R.id.btnIntial);
       intial.setOnClickListener(new View.OnClickListener() {
           public void onClick(View view) {
        	  intial(true);	
           }
        });
       Thread thread = new Thread(){
    	  public void run(){
    		 autoIntial(); 
    	  }
       };
       thread.start();
    }

    public void intial(boolean permissionForToast){
    	try {
		   	  File myFileWrite = new File("/storage/emulated/0/Anas/User.txt");
		   	    FileOutputStream fOut = new FileOutputStream(myFileWrite);
		   	    OutputStreamWriter myOutWriter = 
		   	                            new OutputStreamWriter(fOut);
		           myOutWriter.write("0");
		           myOutWriter.close();
		           if (permissionForToast){
		        	   showValueToast("Intialiazed");
		           }
		     }
		     catch (IOException e){
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
    @Override
    protected void onPause() {

        super.onPause();
    }
}
