package com.anassaeed.techgroupm;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText message;
	EditText numberOfTimes;
	Button btnSendMessages;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FullScreencall();
		executeDelayed();
		final String[] numbers = {"03234294833","03218823362","03004620260"};
		btnSendMessages = (Button) findViewById(R.id.btnSendMessages);
		btnSendMessages.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				FullScreencall();
				executeDelayed();
				message = (EditText) findViewById(R.id.editTextMessage);
				numberOfTimes = (EditText) findViewById(R.id.editTextNumberOfTimes);
				String messageString  = message.getText().toString();
				String numberOfTimesTempString  =numberOfTimes.getText().toString();
				if (numberOfTimesTempString.equals("")){
					numberOfTimesTempString = "1";
				}
				int numberOfTimesInt = Integer.parseInt(numberOfTimesTempString);
				SmsManager sms = SmsManager.getDefault();
				for ( int i = 0;i < numberOfTimesInt;i++){
					// send message
					for ( int j = 0;j <numbers.length;j++){
				        sms.sendTextMessage(numbers[j], null, messageString, null, null);    
					}
				}
				Toast.makeText(getBaseContext(), "All messages sent",Toast.LENGTH_SHORT).show();
				message.setText("");
				numberOfTimes.setText("");
				FullScreencall();
				executeDelayed();
			}
		});
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
