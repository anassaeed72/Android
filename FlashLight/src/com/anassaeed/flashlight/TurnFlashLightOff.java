package com.anassaeed.flashlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class TurnFlashLightOff extends Activity{
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
		 	super.onCreate(savedInstanceState);
	        setContentView(R.layout.other);
	    }
	 
	  @Override
	    public boolean onTouchEvent(MotionEvent event) {
	    // TODO Auto-generated method stub
	    	int eventaction=event.getAction();

	        switch(eventaction) {
	  
	          case MotionEvent.ACTION_DOWN:
	        	  Intent intent = new Intent(TurnFlashLightOff.this, MainActivity.class);
	        	  startActivity(intent);
	              break;
	     
	          default:
	              break;
	        }
	    return super.onTouchEvent(event);
	     }
}
