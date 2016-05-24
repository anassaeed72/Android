package com.anassaeed.bluetooth;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


@SuppressLint("NewApi") public class MainActivity extends Activity {
	public static int btnIndex = 0;
	 static String tag = "BluetoothApp";
	 private static final int REQUEST_ENABLE_BT = 1;  
     public static BluetoothSocket microcontrollerSocket = null;
     public static UUID microctrollerUUID =null;
     BluetoothManager bluetoothManager = null;
	 String microcontrollerName = "Anas's MacBook Pro";
	 boolean connected = false;
	 boolean devicePresent = false;
	 public static BluetoothAdapter bluetoothAdapter = null;
	 Set<BluetoothDevice> pairedDevices = null;
	 public static InputStream inputStream = null;
     public static OutputStream outputStream = null;
     String uuidOfPhone = "";
     ParcelUuid[] microcontrollerUUID = null;
     Button btnScanning;
     public static String recieveNumber = "";
     public static final String databaseName  = "database";
     public static int which = 0;
     public static AlertDialog alertDialog = null;
     public static MediaPlayer mySound;
     public static String firstTime = "FirstTime";
     public static boolean bluetoothOnOffByUser = false;
     public static boolean bluetoothOnOffByUserResult = false;
     public static boolean clearMainActivityBraodcast = false;
     public static boolean disconnectedByUser = false;
     public static String toneSwitcher = "toneSwitcher";
     // Within which the entire activity is enclosed
     DrawerLayout mDrawerLayout;
  
     // ListView represents Navigation Drawer
     ListView mDrawerList;
  
     // ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
     ActionBarDrawerToggle mDrawerToggle;
  
     // Title of the action bar
     String mTitle="";
     
    protected void onCreate(Bundle savedInstanceState) {
    	try{
    	//ListOfDevices.arrayList = null;
	        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
            	if ( clearMainActivityBraodcast == false ){
            		clearMainActivityBraodcast = true;            	
	            	Log.i(tag, "broadcast reciever called");
	                String action = intent.getAction();
	                Log.i(tag, "the actuion is  "+action);
	                // It means the user has changed his bluetooth state.
	                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
	                	BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();    
	                	Log.i("tag","Bluetooth adapter state is   "+btAdapter.getState());                  
	                    if (btAdapter.getState() == BluetoothAdapter.STATE_ON) {
	                        // The user bluetooth is already disabled.
	                    	Log.i("tag","blueooth on in braodcast reciever");
	                    	makeDiscoverable();
	                        startScanningForDevices();
	                        return;
	                    } else {
	                    	// her bluetooth is not on and return to main
	                    	Log.i(tag, "Here as bluetooth is not turned on");
	                    	Toast.makeText(getBaseContext(), "Could not turn on bluetooth", Toast.LENGTH_SHORT).show();
//	                    	Intent intentMainActivity = new Intent(MainActivity.this, MainActivity.class);
//	             			startActivity(intentMainActivity);
//	             			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	                    }
	                }                                        
                }
            }
        };    

      
        
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)); 
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    	executeDelayed();
    	FullScreencall();
        SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);      
    	Boolean intial = shfObject.getBoolean(MainActivity.firstTime,false);
    	if ( intial == false){
    		Intent intent = new Intent(MainActivity.this, Welcome.class);
    		startActivity(intent);
    		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    	}
        int toneSelector = shfObject.getInt(MainActivity.toneSwitcher, 0);
        if ( toneSelector == 0){
        	mySound = MediaPlayer.create(MainActivity.this, R.raw.air_plane_ding );

        } else if ( toneSelector == 1 ){
        	mySound = MediaPlayer.create(MainActivity.this, R.raw.doorbell   );

        } else if ( toneSelector == 2 ) {
        	mySound  = MediaPlayer.create(MainActivity.this, R.raw.elevator_ding   );

        } else if ( toneSelector == 3 ) {
        	mySound = MediaPlayer.create(MainActivity.this, R.raw.knocking_on_door   );

        }  else if ( toneSelector == 4 ) {
            mySound= MediaPlayer.create(MainActivity.this, R.raw.thunder   );
        } else if ( toneSelector == 5 ) {
        	mySound = MediaPlayer.create(MainActivity.this, R.raw.two_tone_doorbell);

        }
    
		
        
        
    	String temp = shfObject.getString(MainActivity.recieveNumber,"not");
    	
    	if ( temp.equals("not")){
    		Toast.makeText(getBaseContext(), "Number not set",Toast.LENGTH_SHORT).show();
    		Intent intent = new Intent(MainActivity.this, SetNumber.class);
    		startActivity(intent);
    		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    	}
    			
    	Log.i(temp, temp);
        btnScanning = (Button) findViewById(R.id.btnScanning);
        btnScanning.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	try{
            		if ( bluetoothAdapter.isEnabled() == true ){
            			makeDiscoverable();
            			startScanningForDevices();
            		}else {
            			turnOnBluethooth();
            		}
                 
            	} catch(Exception e) {
            		e.printStackTrace();
            	}
            }
         });
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	// for the nav bar
    	 mTitle = (String) getTitle();
    	 
         // Getting reference to the DrawerLayout
         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
  
         mDrawerList = (ListView) findViewById(R.id.drawer_list);
  
         // Getting reference to the ActionBarDrawerToggle
         mDrawerToggle = new ActionBarDrawerToggle( this,
             mDrawerLayout,
             R.drawable.ic_drawer,
             R.string.drawer_open,
             R.string.drawer_close){
  
                 /** Called when drawer is closed */
                 public void onDrawerClosed(View view) {
                     invalidateOptionsMenu();
                 }
  
                 /** Called when a drawer is opened */
                 public void onDrawerOpened(View drawerView) {
                     invalidateOptionsMenu();
                 }
         };
  
         // Setting DrawerToggle on DrawerLayout
         mDrawerLayout.setDrawerListener(mDrawerToggle);
  
         // Creating an ArrayAdapter to add items to the listview mDrawerList
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(
             getBaseContext(),
             R.layout.drawer_list_item ,
             getResources().getStringArray(R.array.rivers)
         );
  

         mDrawerList.setAdapter(adapter);
 
         mDrawerList.setOnItemClickListener(new OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	 if ( position == 0 ){
            		 	Intent intent = new Intent(MainActivity.this, SetNumber.class);
            			startActivity(intent);
            			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            	 } else if (position == 1 ){
            		 	Intent intent = new Intent(MainActivity.this, SetTone.class);
            			startActivity(intent);
            			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            	 } 
  
                     // Closing the drawer
                     mDrawerLayout.closeDrawer(mDrawerList);
             }
         });
    }
   
    public void turnOnBluethooth(){
    	 bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
         bluetoothAdapter =   BluetoothAdapter.getDefaultAdapter();
         if (bluetoothAdapter == null){
        	 Toast.makeText(getBaseContext(), "Bluetooth is not avalible on this phone",Toast.LENGTH_SHORT).show();
         }else {
        	 if(!bluetoothAdapter.isEnabled()){
        		 Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        		 startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        		 Log.i(tag, "Bluetooth turn on");
        	 } else {
        		 Log.i(tag, "Bluetooth already on");
        	 }
         }
        
    }
    public void makeDiscoverable(){
    			Intent discoverableIntent = new
    			Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 500);
    			startActivity(discoverableIntent);
    			Log.i(tag, "Bluetooth made discoverable");
    } 
    public void startScanningForDevices(){  	
    	if ( bluetoothAdapter.isEnabled() == false ) {
    	//	return;
    	}
    	if (bluetoothAdapter.startDiscovery()){
    		Log.i(tag, "Starting scanning for devices");
    		try{
    			Intent intent = new Intent(MainActivity.this, Scanning.class);
    			startActivity(intent);
    			overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	} else {
    		Toast.makeText(getBaseContext(), "Could not scan for devices please try again",Toast.LENGTH_SHORT).show();
    	}
    }
 
    public void getUUIDOfPhone(){
    	TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    	uuidOfPhone =  tManager.getDeviceId();
    }

   
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
 
    /** Handling the touch event of app icon*/ 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
 
    /** Called whenever we call invalidateOptionsMenu()*/ 
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
 
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
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
	public boolean onTouchEvent(MotionEvent event) {
		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    	   
	    return true;
	}
}
