package com.anassaeed.attendance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class Contact  extends Activity  {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);
		Contact.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		FullScreencall();
		executeDelayed();
		

    }

	
	@Override
	 public boolean onTouchEvent(MotionEvent event)
	 {     
		Contact.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		float x1  = 0;
		float x2 = 0;
	    switch(event.getAction())
	    {
	    	case MotionEvent.ACTION_DOWN:
	    		x1 = event.getX();                         
	    		break;         
	    	case MotionEvent.ACTION_UP:
	    		x2 = event.getX();
	    		float deltaX = x2 - x1;
	    		if (Math.abs(deltaX) > MainActivity.SWIPE_MIN_DISTANCE)
	    		{
	    			Intent intent = new Intent(Contact.this, MainActivity.class);
	    			startActivity(intent);
          	   		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	    		}
	    		else
	    		{
	               // consider as something else - a screen tap for example
	    		}                          
	    		break;   
	     }           
	     return super.onTouchEvent(event);       
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
	
}
