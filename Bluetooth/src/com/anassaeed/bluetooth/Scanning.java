package com.anassaeed.bluetooth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import android.widget.Toast;

public class Scanning extends Activity{
	boolean anyDevice = false;
	BroadcastReceiver mReceiver;
	ProgressDialog progressBar;

	public static BluetoothDevice device = null;
	  
		@Override
		
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.scanning);
	        Scanning.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	        executeDelayed();
	        FullScreencall();
	        
	        progressBar = new ProgressDialog(Scanning.this);
			progressBar.setCancelable(true);
			progressBar.setMessage("Scanning for Bluetooth Devices ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBar.show();
			
	        try{
	        mReceiver = new BroadcastReceiver() {
			    public void onReceive(Context context, Intent intent) {	    	
			    	
			    	String action = intent.getAction();
			    
			        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			        	
			    		unregisterReceiver(mReceiver);

			          	if ( anyDevice == false){
				          	Toast.makeText(getBaseContext(), "Finished Scanning and no devices were found", Toast.LENGTH_SHORT).show();
				        	progressBar.dismiss();
			          		Intent intentmain = new Intent(Scanning.this, MainActivity.class);
			          		startActivity(intentmain);
			    			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			          	} else {
				          	Toast.makeText(getBaseContext(), "Finished Scanning ", Toast.LENGTH_SHORT).show();

			          	}
			        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			    		unregisterReceiver(mReceiver);
			    		if ( anyDevice == false){
			    			anyDevice = true;
			    		}
			        	device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			        	progressBar.dismiss();
			        	Intent intentmain = new Intent(Scanning.this, ListOfDevices.class);
		          		startActivity(intentmain);
		    			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			            
			        }
			    }
			   };
	        IntentFilter filter = new IntentFilter();
	 	   filter.addAction(BluetoothDevice.ACTION_FOUND);
	 	   filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
	 	   filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);;
	 	   registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	 	   
	        } catch(Exception e){
	        	e.printStackTrace();
	        }
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
			Scanning.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		    	   
		    return true;
		} 
}
