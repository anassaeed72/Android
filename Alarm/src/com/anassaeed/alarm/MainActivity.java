package com.anassaeed.alarm;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.anassaeed.*;

public class MainActivity extends Activity {
	String tag = "Alarm";
	int hours = 0;
	int minutes = 0;
	int seconds = 0;
	EditText hourInput;// = (EditText) findViewById(R.id.hourText);
	EditText minuteInput;// = (EditText) findViewById(R.id.minuteText);
	EditText secondInput;// = (EditText) findViewById(R.id.secondText);
	Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hourInput = (EditText) findViewById(R.id.hourText);
        minuteInput = (EditText) findViewById(R.id.minuteText);
        secondInput = (EditText) findViewById(R.id.secondText);
        btn = (Button) findViewById(R.id.btnSetAlarm);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String hourString = hourInput.getText().toString();
				String minuteString = minuteInput.getText().toString();
				String secondString  = secondInput.getText().toString();
				try{
				if ( hourString.length()!= 0){
					hours = Integer.parseInt(hourString);
				}
				if ( minuteString.length() != 0 ) {
					minutes = Integer.parseInt(minuteString);
				}
				if ( secondString.length() != 0 ) {
					seconds = Integer.parseInt(secondString);
				}
				}catch(Exception e ) {
					Toast.makeText(getBaseContext(), hourString, Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				Thread thread = new Thread(){
					@Override
					public void run(){
						alarmWaiter();
					}
				};
				thread.start();
				
			}
		});

    }

  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    void alarmWaiter(){
    	Log.i(tag, "Alarm called");
    	Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
    	intent.setAction("playAlarm");
    	PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent,0);
    	
    	Calendar calendar=Calendar.getInstance();  
    	calendar.setTimeInMillis(System.currentTimeMillis());  

    	calendar.add(Calendar.SECOND, seconds);  
    	calendar.add(Calendar.MINUTE, minutes);
    	calendar.add(Calendar.HOUR, hours);
    	AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);  
    	alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);	
    	try{
    		runOnUiThread(new Runnable() {
    			public void run() {
    				if ( hours == 0 && minutes == 0  && seconds == 0){
        				Toast.makeText(getApplicationContext(), "Timer Set For -∞", Toast.LENGTH_LONG).show();
        				
    				} else if ( hours == 0 && minutes == 0 && seconds != 0 ){
    					Toast.makeText(getApplicationContext(), "Timer Set " + seconds + " seconds", Toast.LENGTH_LONG).show();
    				}
    				else if ( hours == 0 && minutes != 0 && seconds == 0 ){
    					Toast.makeText(getApplicationContext(), "Timer Set " + minutes + " minutes", Toast.LENGTH_LONG).show();
    				}
    				else if ( hours == 0 && minutes != 0 && seconds != 0 ){
    					Toast.makeText(getApplicationContext(), "Timer Set " + minutes + " minutes and  " + seconds + " seconds", Toast.LENGTH_LONG).show();
    				}
    				else if ( hours != 0 && minutes == 0 && seconds ==  0 ){
    					Toast.makeText(getApplicationContext(), "Timer Set " + hours + " hours", Toast.LENGTH_LONG).show();
    				}
    				else if ( hours != 0 && minutes == 0 && seconds != 0 ){
    					Toast.makeText(getApplicationContext(), "Timer Set "+hours + "hours and " + seconds + " seconds", Toast.LENGTH_LONG).show();
    				}
    				else if ( hours != 0 && minutes != 0 && seconds == 0 ){
    					Toast.makeText(getApplicationContext(), "Timer Set "+hours +" hours and "  + minutes + " minutes", Toast.LENGTH_LONG).show();
    				}
    				else if ( hours != 0 && minutes != 0 && seconds != 0 ){
        				Toast.makeText(getApplicationContext(), "Timer Set " + hours+ " hours, " + minutes +" minutes and " + seconds + " seconds", Toast.LENGTH_LONG).show();
    				}
    			}
    		});
    	}catch(Exception e ) { 
    		e.printStackTrace();
    	}
    	
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
