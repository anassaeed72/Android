package com.anassaeed.bluetooth;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListOfDevices extends Activity {
	final String tag  = MainActivity.tag;
	private ArrayList<BluetoothDevice> arrayList = null;
	LinearLayout linearLayout;
	BroadcastReceiver mReceiver = null;
	static boolean avalibleDevicesTag = false;
    ListView listView ;
    private ArrayList<String> arrayListNames;
	ProgressDialog progressBar;

	protected void onCreate(Bundle savedInstanceState) {
		try{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.listofdevices);
		} catch(Exception e ) {
			e.printStackTrace();
		}
		Log.i(tag, "list called");

        ListOfDevices.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        executeDelayed();
        FullScreencall();
		avalibleDevicesTag = false;
		arrayList = null;
	    arrayListNames = null;
	    
	            
	    linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
	    arrayList = new ArrayList<BluetoothDevice>();
	    arrayListNames = new ArrayList<String>();
		listView = (ListView) findViewById(R.id.list);		
		  // Assign adapter to ListView
        

	    Log.i(tag, "list execution");
	    mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {	    	
	    	
	    	String action = intent.getAction();
	    
	        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
	        	 
	          	Log.i(tag, "Scanning started");
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	        	
	           	Log.i(tag, "Scanning Finished");
	          	if ( avalibleDevicesTag == false){
		          	Toast.makeText(getBaseContext(), "Finished scanning and no devices found", Toast.LENGTH_SHORT).show();

	          		Intent intentmain = new Intent(ListOfDevices.this, MainActivity.class);
	          		startActivity(intentmain);
	    			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	          	}
	          	else {
		          	Toast.makeText(getBaseContext(), "Finished Scanning", Toast.LENGTH_SHORT).show();

	          	}
	        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        
	     
	        	if (avalibleDevicesTag == false ){		    		
		    		avalibleDevicesTag = true;
		    	}
	            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            arrayList.add(device);
	            arrayListNames.add(device.getName());
	 		   	ArrayAdapter<String >adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, android.R.id.text1, arrayListNames);
	 			  // Assign adapter to ListView
	 	          listView.setAdapter(adapter); 
	 	          
		           
	            
	        }
	    }
	   };
	   // Register the BroadcastReceiver
	   IntentFilter filter = new IntentFilter();
	   filter.addAction(BluetoothDevice.ACTION_FOUND);
	   filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
	   filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);;
	   registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy 
	   Log.i(tag, "Reciever registereed");
	  
	   if ( Scanning.device != null){
		   avalibleDevicesTag = true;
		   Log.i(tag, "Scanning device is not null and being added");
		   arrayList.add(Scanning.device);
		   arrayListNames.add(Scanning.device.getName());
		   Log.i(tag, "the name is "+Scanning.device.getName());
		   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		              android.R.layout.simple_list_item_1, android.R.id.text1, arrayListNames);
			  // Assign adapter to ListView
	          listView.setAdapter(adapter); 
	          		  
	       }
	   
	            listView.setOnItemClickListener(new OnItemClickListener() {
	 
	                  @Override
	                  public void onItemClick(AdapterView<?> parent, View view,
	                     int position, long id) {	                   
	                	  // ListView Clicked item index
	                	  int itemPosition     = position;	                  
	                	  addDevice(itemPosition);
	                  }
	    
	             }); 
	   
	    }
	public void ProgressBar(){
		ProgressDialog progressBar = null;
		try{
			progressBar = new ProgressDialog(ListOfDevices.this);
		} catch (Exception e ){
			e.printStackTrace();
		}
		if ( progressBar == null )
		{
			Log.i(tag, "bar is null");
		}
		
		progressBar.setCancelable(true);
		progressBar.setMessage("Connecting to device ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	
		progressBar.show();
	}
	@SuppressLint("NewApi")
	public void addDevice(int position){
		ProgressBar();
		unregisterReceiver(mReceiver);
		BluetoothDevice device = null;
		Log.i(tag, "here");
		try{
		 device= arrayList.get(position);
		 Log.i(tag, "the device index is "+ position);
		} catch(Exception e){
			e.printStackTrace();
		}
		try{
			try{
				if ( device.getBondState() != 12){
					Log.i(tag, "here temp");
					Method method = device.getClass().getMethod("createBond", (Class[]) null);
					method.invoke(device, (Object[]) null);
				}
			} catch(Exception e ){
				e.printStackTrace();
				Intent intent123 = new Intent(ListOfDevices.this, MainActivity.class);
   	   			arrayList = null;
   	   			Log.i(tag, "Return to main pair" );
   	   			startActivity(intent123);
   				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			}
			while(device.getBondState() != 12){}
			if (device.getUuids() == null){
				Log.i(tag, "device uuid is null");
				return;
			} else {
				ParcelUuid[] uuidsLocal  = device.getUuids();
				for (ParcelUuid uuid: uuidsLocal) {
					MainActivity.microctrollerUUID = uuid.getUuid();
				    Log.i(tag, "UUID: " + uuid.getUuid().toString());
				    break;
				}
			}
			try{
				MainActivity.microcontrollerSocket = device.createRfcommSocketToServiceRecord(MainActivity.microctrollerUUID);
           		MainActivity.bluetoothAdapter.cancelDiscovery();
			} catch(Exception e ){
				e.printStackTrace();
				Intent intent123 = new Intent(ListOfDevices.this, MainActivity.class);
   	   			arrayList = null;
   	   			arrayListNames = null;
   	   			Log.i(tag, "Return to main pair" );
//   	   			progressBar.dismiss();
   	   			startActivity(intent123);
   				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			}
           AlertDialog alertDialog1 = null;

		try{
        	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
  	         alertDialogBuilder.setMessage("Connecting");
  	         alertDialog1 = alertDialogBuilder.create();
  	         alertDialog1.show();
  	         Thread.sleep(100);

        	   MainActivity.microcontrollerSocket.connect();

           } catch(Exception e ){
        	   e.printStackTrace();
        	   
   				Intent intent123 = new Intent(ListOfDevices.this, MainActivity.class);
   	   			alertDialog1.dismiss();
//   	   			progressBar.dismiss();
   	   			arrayListNames = null;
   	   			arrayList = null;
   	   			Log.i(tag, "return to main from connect" );
   	   			startActivity(intent123);
   				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
           }

           if (MainActivity.microcontrollerSocket.isConnected()){
        	   Log.i(tag, "Socket is connected");
	        	MainActivity.outputStream = MainActivity.microcontrollerSocket.getOutputStream();
	        	MainActivity.inputStream = MainActivity.microcontrollerSocket.getInputStream();

           } else {
        	   Log.i(tag, "Socket is not connected");
        	   Toast.makeText(ListOfDevices.this,"Could not connect with device",Toast.LENGTH_SHORT).show();
        	   return;
           }
           try{
   			Intent intent123 = new Intent(ListOfDevices.this, ReadAndWrite.class);
   			alertDialog1.dismiss();
   			arrayList = null;
   			arrayListNames = null;
//	   		progressBar.dismiss();
   			Log.i(tag, "Read and Write intent started here" );
   			startActivity(intent123);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			}catch(Exception e){
   				e.printStackTrace();
   			}
			Log.i(tag, "Device added"+ device.getName()+" " + MainActivity.microctrollerUUID);
		} catch (Exception e) {
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
		ListOfDevices.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    	   
	    return true;
	} 
}
