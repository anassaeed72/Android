package com.anassaeed.info;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
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
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        color = Color.BLUE;
        MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        executeDelayed();
        FullScreencall();
        TextView textView = new TextView(getBaseContext());  
        textView.setTextColor(color);
        textView.setText("Phone Info");
        textView.setTextSize(16);
        linearlayout.addView(textView);        
       
        String brand = Build.BRAND.toString();
        makeTextView("Brand", brand);

        String board = Build.BOARD;
        makeTextView("Board", board);
        
        String cpu_abi = Build.CPU_ABI;
        makeTextView("CPU ABI",cpu_abi);
        
        String cpu_abui2 = Build.CPU_ABI2;
        makeTextView("CPU ABI 2",cpu_abui2);
        
        String device = Build.DEVICE;
        makeTextView("Device", device);
        
        String display = Build.DISPLAY;
        makeTextView("Display", display);
        
        String fingerprint = Build.FINGERPRINT;
        makeTextView("Finger print", fingerprint);
        
        String hardware = Build.HARDWARE;
        makeTextView("Hardware", hardware);
        
        String host = Build.HOST;
        makeTextView("Host", host);
        
        String id = Build.ID;
        makeTextView("ID", id);
        
        String manufacturer = Build.MANUFACTURER;
        makeTextView("Manufacturer", manufacturer);
        
        String model = Build.MODEL;
        makeTextView("Model", model);
        
        String product = Build.PRODUCT;
        makeTextView("Product", product);
		
        String radio = Build.RADIO;
        makeTextView("Radio", radio);
        
        String serial = Build.SERIAL;
        makeTextView("Build Serial #", serial);
        
        String tags = Build.TAGS;
        makeTextView("Tags", tags);
        
        String type = Build.TYPE;
        makeTextView("Type", type);
        
        String user = Build.USER;
        makeTextView("User", user);
        
        String android_id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);
        makeTextView("Android Id", android_id);
        
        String buildVersionRelease = Build.VERSION.RELEASE;
        makeTextView("Android Version", buildVersionRelease);
        
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        makeTextView("Device ID", tmDevice);
        tmSerial = "" + tm.getSimSerialNumber();
        makeTextView("Phone Serial #", tmSerial);
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        makeTextView("Android ID", androidId);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        makeTextView("UUID", deviceId);
           
        
    }

    private void makeTextView( String title, String text){
    	
    	TextView textView = new TextView(getBaseContext());  
        textView.setTextColor(color);
        textView.setText(title+" : "+text);
        textView.setTextSize(16);
        linearlayout.addView(textView);
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
