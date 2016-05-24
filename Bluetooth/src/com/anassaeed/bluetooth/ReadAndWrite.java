package com.anassaeed.bluetooth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
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

public class ReadAndWrite extends Activity {
	public static final String tag = "BluetoothApp";
	MediaPlayer mySound = null;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    String mTitle;
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {

	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.read);
	        Log.i("tag","in read and write");
	        ReadAndWrite.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	        executeDelayed();
	        FullScreencall();

	        mySound = MediaPlayer.create(ReadAndWrite.this, R.raw.thunder );
	        Log.i(tag, "Read and write called");
	       
	       /* Button button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	            	try {
						MainActivity.microcontrollerSocket.close();
		            	Toast.makeText(ReadAndWrite.this,"Disconnect from Device", Toast.LENGTH_SHORT).show();

					} catch (IOException e) {
						// TODO Auto-generated catch block
		            	Toast.makeText(ReadAndWrite.this,"Could not close connection", Toast.LENGTH_SHORT).show();

						e.printStackTrace();
					}
	            }
	         });*/
	       try{
	        Thread thread = new Thread(){
	        	@Override
	        	public void run(){
	        		readFromStream(true);
	        	}
	        };
//	        playSound();
	        thread.start();
	        } catch(Exception e){
	        	e.printStackTrace();
	        }
	       
	    // for the nav bar
	    	 mTitle = (String) getTitle();
	    	 
	         // Getting reference to the DrawerLayout
	         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	  
	         mDrawerList = (ListView) findViewById(R.id.drawer_list);
	         String[] arrayForReadAndWrite = {"Send Number","Disconnect"};

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
	             arrayForReadAndWrite
	         );
	  

	         mDrawerList.setAdapter(adapter);
	 
	         mDrawerList.setOnItemClickListener(new OnItemClickListener() {
	             @Override
	             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            	 if ( position == 0 ){
	            		 Intent intent = new Intent(ReadAndWrite.this, SendNumber.class);
	         			startActivity(intent);
	         			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	            	 } else if (position == 1 ){
	            		 try {
							MainActivity.microcontrollerSocket.close();
			            	Toast.makeText(ReadAndWrite.this,"Disconnect from Device", Toast.LENGTH_SHORT).show();
			            	 Intent intent = new Intent(ReadAndWrite.this, MainActivity.class);
			         			startActivity(intent);
			         			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		 
	            	 }
	            	 
	                   
	  
	                     // Closing the drawer
	                     mDrawerLayout.closeDrawer(mDrawerList);
	             }
	         });
	    }
	 
	 public void readFromStream(boolean infinite){  	
	    	int readMessage = 0;
	    	byte[] buffer = new byte[1024];
	    	while(infinite){
	    		try {
	    			// Read from the InputStream
	    			readMessage =  MainActivity.inputStream.read(buffer);
	    			if ( readMessage == 1){
	    				makeAlert("Alert","Child is not on seat for 5 minutes");
	    			}	else if ( readMessage == 2 ) {
	    				makeAlert("Alert","Child is not on seat for 10 minutes");
	    				dialogshow();
	    			}	else if ( readMessage == 3 ) {
	    				makeAlert("Alert","Child is not on seat for 15 minutes");
	    			}	else if ( readMessage == 4 ) {
	    				makeAlert("Alert","Child is not on seat for 20 minutes");
	    			}	else if ( readMessage == 5 ) {
	    				makeAlert("Alert","Child is not on seat for 25 minutes");
	    			}	else if ( readMessage == 6 ) {
	    				makeAlert("Alert","Child is not on seat for 30 minutes");
	    			}	else if ( readMessage == 7 ) {
	    				 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReadAndWrite.this);
	    			        alertDialogBuilder.setTitle("Alert")
	    						.setMessage("Number is not set.Please give a valid number")
	    						.setCancelable(false)				
	    						.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
	    						public void onClick(DialogInterface dialog,int id) {
	    								dialog.cancel();
	    								Intent intent = new Intent(ReadAndWrite.this, SendNumber.class);
	    								startActivity(intent);
	    								overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	    						}
	    						}); 
	    					// create alert dialog
	    					AlertDialog alertDialog = alertDialogBuilder.create();
	    					alertDialog.show();
	    				playSound();
	    			}	else if ( readMessage == 8 ) {
	    				makeAlert("Alert","Battery of controller is dying.Please considering replacement");
	    				playSound();
	    			}	else if ( readMessage == 9 ) {
	    				playSound();
	    				 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReadAndWrite.this);

	    			        alertDialogBuilder.setTitle("Alert")
	    						.setMessage("Controller is shutting down")
	    						.setCancelable(false)				
	    						.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
	    						public void onClick(DialogInterface dialog,int id) {
	    								dialog.cancel();
	    								Intent intent = new Intent(ReadAndWrite.this, MainActivity.class);
	    								startActivity(intent);
	    								overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	    						}
	    						}); 
	    					// create alert dialog
	    					AlertDialog alertDialog = alertDialogBuilder.create();
	    					alertDialog.show();
	    			}	else if ( readMessage == 10 ) {
	    				makeAlert("Alert","Child is not on seat");
	    				playSound();
	    			} 
	    			Log.i(tag, "Data read form bluetooth Stream" + readMessage);
	    		} catch (IOException e) {
	    			Log.i(tag, "exception in reading " + e.getMessage().toString());
	    			Intent intent = new Intent(ReadAndWrite.this, BluetoothDisconnect.class);
					startActivity(intent);
					overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	    			/*
	    			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReadAndWrite.this);

			        alertDialogBuilder.setTitle("Alert")
						.setMessage("Bluetooth connection was lost")
						.setCancelable(false)				
						.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
								Intent intent = new Intent(ReadAndWrite.this, MainActivity.class);
								startActivity(intent);
								overridePendingTransition(R.anim.fadein, R.anim.fadeout);
						}
						}); 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();*/
	    			break;
	    		}
	    	}
	    	
	    }
	 public void makeAlert(String title,String text){
		 playSound();
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReadAndWrite.this);

	        alertDialogBuilder.setTitle(title)
				.setMessage(text)
				.setCancelable(false)				
				.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
				}
				}); 
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	 }
	 void playSound(){
		 mySound.start();
	 }
	 void stopPlayingSound(){
		mySound.stop();
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
						Intent intent = new Intent(ReadAndWrite.this, MainActivity.class);
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
							Intent AlarmIntent = new Intent(ReadAndWrite.this, AlarmFiveMinutes.class);
						    AlarmManager AlmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
						    PendingIntent Sender = PendingIntent.getBroadcast(ReadAndWrite.this, 0, AlarmIntent, 0);    
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
		    		return;
		    	}
	    		if ( bytes == null){
	    			Log.i(tag, "bytes is null");
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

			ReadAndWrite.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		    	   
		    return true;
		} 
		
		
	    @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        mDrawerToggle.syncState();
	    }
	 
	    /** Handling the touch event of app icon */
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }
	 
	    /** Called whenever we call invalidateOptionsMenu() */
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
}
