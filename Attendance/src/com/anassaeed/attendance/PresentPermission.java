package com.anassaeed.attendance;
import android.annotation.SuppressLint;
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
import android.widget.CheckBox;

public class PresentPermission extends Activity {
	CheckBox checkbox;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presentpermission);
		checkbox = (CheckBox) findViewById(R.id.checkbox);
		SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);  
		boolean presentState = shfObject.getBoolean(MainActivity.permisionPresentAbsentStudents, true);
		Log.i("tag","on create state is "+presentState);
		if (presentState){
			checkbox.setText("Present and Absent");
			checkbox.setChecked(true);
		}else {
			checkbox.setText("Absent Only");
			checkbox.setChecked(false);
		}
		
		checkbox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( checkbox.isChecked()){
					SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);
	            	SharedPreferences.Editor  shfEditorObject=shfObject.edit();
	            	shfEditorObject.putBoolean(MainActivity.permisionPresentAbsentStudents,true);
	            	shfEditorObject.commit();
	            	MainActivity.permissionToSendPresntStudentsMessages = true;
	            	checkbox.setText("Present and Absent");
	    			checkbox.setChecked(true);
	    			Log.i("Tag","presnet and absent");
				}else {
					SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);
	            	SharedPreferences.Editor  shfEditorObject=shfObject.edit();
	            	shfEditorObject.putBoolean(MainActivity.permisionPresentAbsentStudents,false);
	            	shfEditorObject.commit();

					MainActivity.permissionToSendPresntStudentsMessages = false;
					checkbox.setText("Absent Only");
					checkbox.setChecked(false);
					Log.i("Tag","absent only");
				}
			}
		});
	}
	
	@Override
	 public boolean onTouchEvent(MotionEvent event)
	 {     
		PresentPermission.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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
	    			Intent intent = new Intent(PresentPermission.this, MainActivity.class);
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