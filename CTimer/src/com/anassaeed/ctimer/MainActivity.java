package com.anassaeed.ctimer;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainActivity extends Activity {
	private String tag = "Ctimer";
	private boolean call = false;
	private ScrollView scrollView;
	private LinearLayout linearLayout;
	long start = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Drawable drawable = (Drawable) getResources().getDrawable( R.drawable.anas );
        scrollView = new ScrollView(getBaseContext());
        scrollView.setBackground(drawable);
        linearLayout = new LinearLayout(getBaseContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
        
        this.setContentView(scrollView);
        TelephonyManager telephonyManager =  
                (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);  
   
  PhoneStateListener callStateListener = new PhoneStateListener() {  
  public void onCallStateChanged(int state, String incomingNumber)   
  {  
        if(state==TelephonyManager.CALL_STATE_RINGING){  
               call  = true;
               start = System.currentTimeMillis();
        }  
        if(state==TelephonyManager.CALL_STATE_OFFHOOK){  
        	if ( call  == true) {
         	   long end = System.currentTimeMillis();
         	   end = end - start;
         	   end = end /100;
         	   int time = (int )end;
         	   call = false;
         	   Log.i(tag, String.valueOf(time));
         	   TextView text=new TextView(getBaseContext());
         	   	text.setText(String.valueOf(time));
         	   	text.setVisibility(View.VISIBLE);
         	   	text.setAlpha((float) 1.0);
         	   	text.setTextColor(Color.rgb(1, 1,1));
         	   	text.setTextSize((float) 20.0);
         	   linearLayout.addView(text);
            }
        }  
                            
        if(state==TelephonyManager.CALL_STATE_IDLE){  
           if ( call  == true) {
        	   long end = System.currentTimeMillis();
        	   end = end - start;
        	   end = end /100;
        	   int time = (int )end;
        	   call = false;
        	   Log.i(tag, String.valueOf(time));
        	   TextView text=new TextView(getBaseContext());
        	   	text.setText(String.valueOf(time));
        	   	text.setVisibility(View.VISIBLE);
        	   	text.setAlpha((float) 1.0);
        	   	text.setTextColor(Color.rgb(1, 1,1));
        	   	text.setTextSize((float) 20.0);
        	   linearLayout.addView(text);
           }
        	
        }  
  }  
  };  
  telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);  
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
