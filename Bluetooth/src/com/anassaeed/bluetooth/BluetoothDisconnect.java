package com.anassaeed.bluetooth;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;



public class BluetoothDisconnect  extends Activity{
	String tag = "BluetoothApp";

	@Override
		protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.bluetoothdisconnect);
	        BluetoothDisconnect.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	        executeDelayed();
	        FullScreencall();
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BluetoothDisconnect.this);

	        alertDialogBuilder.setTitle("Alert")
				.setMessage("Bluetooth connection was lost ")
				.setCancelable(false)				
				.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						Intent intent = new Intent(BluetoothDisconnect.this, MainActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.fadein, R.anim.fadeout);
				}
				}); 

			try{
				dialogshow();
			}catch(Exception e){
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
	    public void dialogshow(){
	    	final CharSequence[] items={"No Problem","Remind After two minutes","Big Issue"};
	    	final AlertDialog.Builder builder=new AlertDialog.Builder(this);
	    	builder.setTitle("Alert");
	    	// = builder.create();

	    	try{
	     	builder.setSingleChoiceItems(items,-1, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub				
					if("No Problem".equals(items[which]))
					{
						Log.i(tag, "one");
						MainActivity.which = 1;
						MainActivity.alertDialog.cancel();
						Intent intent = new Intent(BluetoothDisconnect.this, MainActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.fadein, R.anim.fadeout);
						return;
					}
					else if("Remind After two minutes".equals(items[which]))
					{
						Log.i(tag, "two");
						MainActivity.which = 2;
						try{
							Log.i(tag, " calling");
							Intent AlarmIntent = new Intent(BluetoothDisconnect.this, AlarmFiveMinutes.class);
						    AlarmManager AlmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
						    PendingIntent Sender = PendingIntent.getBroadcast(BluetoothDisconnect.this, 0, AlarmIntent, 0);    
						    AlmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*60*2, Sender);
					    	
				    		} catch (Exception e){
								e.printStackTrace();
							}
						MainActivity.alertDialog.cancel();
//						builder.d
						return;	
					}
					else if("Big Issue".equals(items[which]))
					{
						MainActivity.which = 3;
						byte[] bytesToSend = null;
						String toSend = "1";
						try {
							bytesToSend = toSend.getBytes("UTF-32");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						writeNumberToStream(bytesToSend);
						return;
					}
				}
			});
//	     	 alertDialog = builder.create();
//	     	alertDialog.show();
	     	MainActivity.alertDialog = builder.show();
	    	} catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	    public void writeNumberToStream(byte[] bytes){	    	
	    	try {
	    		if (MainActivity.outputStream == null){
		    		Log.i(tag, "outputstream is null");
		    		Toast.makeText(BluetoothDisconnect.this,"Could not send number",Toast.LENGTH_SHORT).show();
		    		Intent intent = new Intent(BluetoothDisconnect.this, MainActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		    		return;
		    	}
	    		if ( bytes == null){
	    			Log.i(tag, "bytes is null");
	    			Toast.makeText(BluetoothDisconnect.this,"Could not send number",Toast.LENGTH_SHORT).show();
		    		Intent intent = new Intent(BluetoothDisconnect.this, MainActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	    			return;
	    		}
				MainActivity.outputStream.write(bytes);
				Log.i(tag, "Data Written to bluetooth Stream"+bytes);
			} catch (Exception e) {
				Log.i(tag, "number not written");
				e.printStackTrace();
			}
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
			BluetoothDisconnect.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		    	   
		    return true;
		} 
}
