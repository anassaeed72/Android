package com.anassaeed.simple;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends Activity {
	public static final String tag = "Simple";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i(tag,"app started");
		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
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

	 BroadcastReceiver mybroadcast = new BroadcastReceiver() {

	        //When Event is published, onReceive method is called
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            // TODO Auto-generated method stub
	                Log.i("[BroadcastReceiver]", "MyReceiver");

	                if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
	                    Log.i("[BroadcastReceiver]", "Screen ON");
	                }
	                else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
	                    Log.i("[BroadcastReceiver]", "Screen OFF");
	                }

	        }
	    };

}
