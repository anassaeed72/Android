package com.anassaeed.slideshow;


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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	public static int counter = 0;
	public static final int total = 11;
	public static int wait = 1;
	RelativeLayout relativeLayout;
	EditText editText;
	Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		FullScreencall();
		executeDelayed();
		editText = (EditText) findViewById(R.id.edittext);
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FullScreencall();
				executeDelayed();
				String value = editText.getText().toString();
				if ( value.length() == 0){
					value = "1";
				}
				wait = Integer.parseInt(value);
				Intent intent = new Intent(getBaseContext(),Other.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fadein,R.anim.fadeout);
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
		public boolean onTouchEvent(MotionEvent event) {
			MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		    	   
		    return true;
		} 
}
