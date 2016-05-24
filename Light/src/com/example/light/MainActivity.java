package com.example.light;


import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	public void lights(){
		while ( true ){
			Intent batteryStatus = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			float batteryPercentage = level / (float)scale; 

			if (batteryPercentage > .90){
				Blue();
			} else if( batteryPercentage > .60 ) {
				Cyan();
			}
			else if( batteryPercentage > .30 ) {
				White();
			} else {
				Red1();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        lights();
        Button lights;
        lights = (Button) findViewById(R.id.btnLights);
        lights.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	lights();
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
    public void Red1(){
    	NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	Notification notif = new NotificationCompat.Builder(getApplicationContext())
        .setAutoCancel(false)          
        .setLights(0xff0000, 10000, 1)
        .setPriority(Notification.PRIORITY_MAX)
        .build();
    	nm.notify(0,notif);
    }
  
    public void Blue(){
    	NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	Notification notif = new NotificationCompat.Builder(getApplicationContext())
        .setAutoCancel(false)          
        .setLights(Color.BLUE, 10000, 1)
        .setPriority(Notification.PRIORITY_MAX)
        .build();
    	nm.notify(0,notif);
    	
    }
    public void White(){
    	NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	Notification notif = new NotificationCompat.Builder(getApplicationContext())
        .setAutoCancel(false)          
        .setLights(Color.WHITE, 10000, 1)
        .setPriority(Notification.PRIORITY_MAX)
        .build();
    	nm.notify(0,notif);
    }
    public void Cyan(){
    	NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	Notification notif = new NotificationCompat.Builder(getApplicationContext())
        .setAutoCancel(false)        
        .setLights(Color.CYAN, 10000, 1)
        .setPriority(Notification.PRIORITY_MAX)
        .build();
    	nm.notify(0,notif);
    }
}
