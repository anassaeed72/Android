package com.anassaeed.autoreplyer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SetMessage extends Activity   {
	Button button;
	EditText editText;
	RelativeLayout relativeLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.setmessage);
		}catch (Exception e ) { 
			e.printStackTrace();
		}
		executeDelayed();
		FullScreencall();
		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);	
		relativeLayout.setOnTouchListener(new OnSwipeTouchListener(getBaseContext()) {
		    public void onSwipeTop() {
		    }
		    public void onSwipeRight() {
		    	Intent intent = new Intent(SetMessage.this, MainActivity.class);
            	startActivity(intent);
            	overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		    }
		    public void onSwipeLeft() {
		        Intent intent = new Intent(SetMessage.this, MainActivity.class);
            	startActivity(intent);
            	overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		    }
		    public void onSwipeBottom() {
		    }

		    public boolean onTouch(View v, MotionEvent event) {
		    	return gestureDetector.onTouchEvent(event);
		    }
		   
		   
		});
		
		button = (Button) findViewById(R.id.btnSaveMessage);
		button.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				editText = (EditText) findViewById(R.id.message);
				String message = editText.getText().toString();				
				SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);
            	SharedPreferences.Editor  shfEditorObject=shfObject.edit();
            	shfEditorObject.putString(MainActivity.messageToReply,message);
            	shfEditorObject.commit();
            	MainActivity.messageCurrent = message;
            	Toast.makeText(SetMessage.this, "Message Saved", Toast.LENGTH_SHORT).show();
            	Intent intent = new Intent(SetMessage.this, MainActivity.class);
            	startActivity(intent);
            	overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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
		SetMessage.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    return true;
	} 
}
