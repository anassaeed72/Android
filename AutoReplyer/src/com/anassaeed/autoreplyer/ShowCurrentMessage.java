package com.anassaeed.autoreplyer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShowCurrentMessage extends Activity{
	TextView textview;
	RelativeLayout relativeLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showcurrentmessage);
		Log.i("Tag","show message called");
		textview = (TextView) findViewById(R.id.textView2);
		textview.setText(MainActivity.messageCurrent);
		
		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);	
		relativeLayout.setOnTouchListener(new OnSwipeTouchListener(getBaseContext()) {
		    public void onSwipeTop() {
		    }
		    public void onSwipeRight() {
		    	Intent intent = new Intent(ShowCurrentMessage.this, MainActivity.class);
            	startActivity(intent);
            	overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		    }
		    public void onSwipeLeft() {
		        Intent intent = new Intent(ShowCurrentMessage.this, MainActivity.class);
            	startActivity(intent);
            	overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		    }
		    public void onSwipeBottom() {
		    }

		    public boolean onTouch(View v, MotionEvent event) {
		    	return gestureDetector.onTouchEvent(event);
		    }
		   
		   
		});
		
	}
	
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
		ShowCurrentMessage.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    return true;
	} 

}
