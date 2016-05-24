package com.anassaeed.sa_notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		makeNotification("Title", "Subject", "Body");
	}
	void makeNotification(String title, String subject, String body){
		 NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
         Notification notify=new Notification(R.drawable.ic_launcher,title,System.currentTimeMillis());
         PendingIntent pending= PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
         
         notify.setLatestEventInfo(getApplicationContext(),subject,body,pending);
         notif.notify(0, notify);
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
