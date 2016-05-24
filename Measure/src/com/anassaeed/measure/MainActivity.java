package com.anassaeed.measure;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static String databaseName = "Database";
	public static String firstTime = "FirstTime";
	public static String length = "Length";
	public static int value = 0;
	Button button;
	private static int percentage = 0;
	private static int offset = 0;
	public static String tag = "Measure";
	TextView textview;
	SeekBar seekbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        FullScreencall();
        executeDelayed();
		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        final SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);      
        boolean intial = shfObject.getBoolean(MainActivity.firstTime,false);
        if ( intial == false ){
        	makeAlert("Alert", "Please set phone dimensions");
        	
        }
        textview = (TextView) findViewById(R.id.lengthTextView);
        button = (Button) findViewById(R.id.btnAddValue);
        button.setOnClickListener(new OnClickListener() {

			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String lenthValue= shfObject.getString(MainActivity.length, "0");
				int lengthValueInt = Integer.parseInt(lenthValue);
				value+=lengthValueInt;
				textview.setText(String.valueOf(value));
				offset = 0;
        		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

			}
		});
        
        seekbar = (SeekBar) findViewById(R.id.seekBar1);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        @Override
        	public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
            	percentage = progresValue;
            	String lenthValue= shfObject.getString(MainActivity.length, "0");
           		int lengthValueInt = Integer.parseInt(lenthValue);
           		double tempValue = lengthValueInt*percentage;
           		tempValue = tempValue/100;
           		offset = (int) tempValue;
           		textview.setText(String.valueOf(value+offset));
        		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        	}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
    		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
    		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			
		}       
        	});
             

	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    	   Log.i(tag, "on touch event");
	    return true;
	} 
	 public void makeAlert(String title,String text){
		
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

	        alertDialogBuilder.setTitle(title)
				.setMessage(text)
				.setCancelable(false)				
				.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						Intent intent = new Intent(MainActivity.this, Dimensions.class);
				        
						final SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);      				   
						SharedPreferences.Editor  shfEditorObject=shfObject.edit();
			        	shfEditorObject.putBoolean(MainActivity.firstTime,true);
			        	shfEditorObject.commit();
		            	startActivity(intent);
						overridePendingTransition(R.anim.fadein, R.anim.fadeout);
				}
				}); 
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(MainActivity.this, Dimensions.class);
        	startActivity(intent);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        	return true;
		}
		if ( id == R.id.action_back){
	        final SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);      
			String lenthValue= shfObject.getString(MainActivity.length, "0");
       		int lengthValueInt = Integer.parseInt(lenthValue);
			value = value - lengthValueInt;
			textview.setText(String.valueOf(value));
		}
		if ( id == R.id.action_intial){
			
			value = 0;
			textview.setText(String.valueOf(value));
		}
		return super.onOptionsItemSelected(item);
	}
}
