package com.anassaeed.attendance;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;


public class CustomScrollView extends ScrollView {
	private float xDistance, yDistance, lastX, lastY;
    View.OnTouchListener gestureListener;

	public CustomScrollView(Context context, AttributeSet attrs) {
	      super(context, attrs);

	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	    switch (ev.getAction()) {
	        
	        default:
	            final float curX = ev.getX();
	            final float curY = ev.getY();
	            xDistance += Math.abs(curX - lastX);
	            yDistance += Math.abs(curY - lastY);
	            lastX = curX;
	            lastY = curY;
	            Log.i("Tag","xdis "+ xDistance + " ydis "+ yDistance + "lastx  "+lastX + " lasty "+lastY);
	            if(xDistance > yDistance){
	            	xDistance = yDistance = 0f;
		            lastX = ev.getX();
		            lastY = ev.getY();
	            	Intent intent = new Intent(getContext(), TransitionActivity.class);
	            	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
             	   	getContext().startActivity(intent);
             	   return true;
	            } else {
	            	xDistance = yDistance = 0f;
		            lastX = ev.getX();
		            lastY = ev.getY();
	            }
	    }

	    return super.onInterceptTouchEvent(ev);
	}
}