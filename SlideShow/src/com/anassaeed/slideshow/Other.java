package com.anassaeed.slideshow;


import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class Other extends Activity {
	RelativeLayout relativeLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		FullScreencall();
		executeDelayed();
		String fileName = "a"+String.valueOf(MainActivity.counter++%MainActivity.total);
		int resID = getResources().getIdentifier(fileName, "drawable", "com.anassaeed.slideshow");      
		Drawable drawable = getResources().getDrawable(resID);
		if ( drawable == null ) {
			Intent intent = new Intent(Other.this, Other.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		}
		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		relativeLayout.setBackground(drawable);
		SetAlarm(Other.this);

	}
	public void SetAlarm(Context context)
    {
		try {
            
			  Calendar cal = Calendar.getInstance();
		        cal.add(Calendar.MINUTE, MainActivity.wait);
		 
		        //Create a new PendingIntent and add it to the AlarmManager
		        Intent intent = new Intent(this, Other.class);
		        overridePendingTransition(R.anim.fadein,R.anim.fadeout);

		        PendingIntent pendingIntent = PendingIntent.getActivity(this,
		            12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		        AlarmManager am =
		            (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
		        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		                pendingIntent);
             
          } catch (Exception e) {}
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
	   @SuppressLint("InlinedApi")
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
			Other.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		    	   
		    return true;
		} 
}
