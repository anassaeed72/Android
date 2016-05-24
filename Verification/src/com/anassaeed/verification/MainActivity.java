package com.anassaeed.verification;

import java.util.UUID;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	LinearLayout linearlayout;
	int color;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        color = Color.GRAY;
        MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        executeDelayed();
        FullScreencall();
        String brandTemp = Build.BRAND.toString();
		String boardTemp = Build.BOARD.toString();
        String displayTemp = Build.DISPLAY.toString();
        String serialTemp = Build.SERIAL.toString();
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);        
        final String tmDeviceTemp, tmSerialTemp, androidIdTemp;
        tmDeviceTemp = "" + tm.getDeviceId();
        tmSerialTemp = "" + tm.getSimSerialNumber();
        androidIdTemp = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidIdTemp.hashCode(), ((long)tmDeviceTemp.hashCode() << 32) | tmSerialTemp.hashCode());
        String uuidString = deviceUuid.toString();
        
        makeTextView(brandTemp);
        makeTextView(boardTemp);
        makeTextView(displayTemp);
        makeTextView(serialTemp);
        makeTextView(tmDeviceTemp);
        makeTextView(tmSerialTemp);
        makeTextView(androidIdTemp);
        makeTextView(uuidString);
        
	}

	
	 private void makeTextView(String text){
	    	
	    	TextView textView = new TextView(getBaseContext());  
	        textView.setTextColor(color);
	        textView.setText(text);
	        textView.setTextSize(20);
	        linearlayout.addView(textView);
	        TextView textView1 = new TextView(getBaseContext());  
	        textView1.setTextColor(color);
	        textView1.setText("  ");
	        textView1.setTextSize(20);
	        linearlayout.addView(textView1);
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
		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    return true;
	} 
}
