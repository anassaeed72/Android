package com.anassaeed.masssmsc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.SmsManager;
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

public class MainActivity extends Activity {

	   private Button sendBtn;
	   private Thread thread;
	   private ArrayList<PendingIntent> sentPI;
	   private static String SENT = "SMS_SENT";
	   private static int numberOfParts = 0;
	   private static String phoneNumber = "";
	   private static String message = "";
	   private static  File myFile;
	   private static FileInputStream fIn = null;
	   private static BufferedReader myReader = null;
	   private static boolean permission = true;
	   private static MainActivity mainActivity = null;
	   private DrawerLayout mDrawerLayout;
	   private ListView mDrawerList;
	   private ActionBarDrawerToggle mDrawerToggle;
	   ProgressDialog progressBar;
	   public static MediaPlayer mediaplayer;
	   public static final String databaseName = "databaseName";
	   public static final String toneSwitcher = "toneSwitcher";
	   public static final int SWIPE_MIN_DISTANCE = 120;
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_main);
	     
	      sendBtn = (Button) findViewById(R.id.btnSendSMS);
	      mainActivity = this;
	      myFile = new File("/storage/emulated/0/Anas/numbersData.txt");
	    		try {
	    			fIn = new FileInputStream(myFile);
	    			   myReader = new BufferedReader(
	    		 	             new InputStreamReader(fIn));

	    		} catch (FileNotFoundException e1) {
	    			e1.printStackTrace();
	    			 Toast.makeText(getApplicationContext(), "File Not found",
					         Toast.LENGTH_LONG).show();
	    			 return;
	    		} 
	      //---when the SMS has been sent---
	      registerReceiver(new BroadcastReceiver(){
	          @Override
	          public void onReceive(Context arg0, Intent arg1) {
	              switch (getResultCode())
	              {
	                  case Activity.RESULT_OK:
	                    
	                      numberOfParts--;
	                      if ( numberOfParts == 0) {

	                    		  try{
	                    			  mainActivity.sendSMSMessage();
	                    		  } catch (Exception e ) {
	                    			  e.printStackTrace();
	                    		  }

	                    	  
	                      }
	                      break;
	                  case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	                      Toast.makeText(getBaseContext(), "Generic failure", 
	                              Toast.LENGTH_SHORT).show();
	                      break;
	                  case SmsManager.RESULT_ERROR_NO_SERVICE:
	                      Toast.makeText(getBaseContext(), "No service", 
	                              Toast.LENGTH_SHORT).show();
	                      break;
	                  case SmsManager.RESULT_ERROR_NULL_PDU:
	                      Toast.makeText(getBaseContext(), "Null PDU", 
	                              Toast.LENGTH_SHORT).show();
	                      break;
	                  case SmsManager.RESULT_ERROR_RADIO_OFF:
	                      Toast.makeText(getBaseContext(), "Radio off", 
	                              Toast.LENGTH_SHORT).show();
	                      break;
	              }
	          }
	      }, new IntentFilter(SENT));
	      
	      
	      sendBtn.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View view) {	        	 
	            startSendingMessages(view);
	            Toast.makeText(getBaseContext(), "Starting Sending Messages", Toast.LENGTH_SHORT).show();
	         }
	      });
	      
	      // for the nav bar
	      
	        
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); 
	        mDrawerList = (ListView) findViewById(R.id.drawer_list);
	        mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout,R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close){
	                /** Called when drawer is closed */
	                public void onDrawerClosed(View view) {
	                    invalidateOptionsMenu();
	                }
	                /** Called when a drawer is opened */
	                public void onDrawerOpened(View drawerView) {                   
	                    invalidateOptionsMenu();
	                }
	        };
	 
	        mDrawerLayout.setDrawerListener(mDrawerToggle);
	 
	        String[] arrayForReadAndWrite = {"Set Tone"};
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>( getBaseContext(), R.layout.drawer_list_item , arrayForReadAndWrite);
	 
	        mDrawerList.setAdapter(adapter);
	        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            	if ( position == 0 ) {
	            		Intent intent  = new Intent(MainActivity.this, SetTone.class);
	            		startActivity(intent);
	            		overridePendingTransition(R.anim.fadein,R.anim.fadeout);
	            	}
	                    mDrawerLayout.closeDrawer(mDrawerList);
	            }
	        });
	        
	        SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);      
	    	
	        int toneSelector = shfObject.getInt(MainActivity.toneSwitcher, 0);
	        if ( toneSelector == 0){
	        	mediaplayer = MediaPlayer.create(MainActivity.this, R.raw.air_plane_ding );
	        } else if ( toneSelector == 1 ){
	        	mediaplayer = MediaPlayer.create(MainActivity.this, R.raw.doorbell   );
	        } else if ( toneSelector == 2 ) {
	        	mediaplayer  = MediaPlayer.create(MainActivity.this, R.raw.elevator_ding   );
	        } else if ( toneSelector == 3 ) {
	        	mediaplayer = MediaPlayer.create(MainActivity.this, R.raw.knocking_on_door   );
	        }  else if ( toneSelector == 4 ) {
	        	mediaplayer= MediaPlayer.create(MainActivity.this, R.raw.thunder   );
	        } else if ( toneSelector == 5 ) {
	        	mediaplayer = MediaPlayer.create(MainActivity.this, R.raw.two_tone_doorbell);

	        }    

	   }
	   private  void sendOneMessage(){
		   SmsManager smsManager = SmsManager.getDefault();
		   try {
			phoneNumber = myReader.readLine();
		   } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
		   if ( phoneNumber == null ){
			   permission = false;
			   try{
               	MainActivity.this.runOnUiThread(new Runnable() {
               	    public void run() {
               	    	makeAlert("Alert", "All Messages Sent");	
               	    	mediaplayer.start();
               	    }
               	});
               } catch (Exception e ) {
               	e.printStackTrace();
               }
			   return;
		   }
		   
		   // for reading the message from the file
		   boolean breakMessageReaderWhileLoop = false;
		   String tempMessage = "";
		   while(breakMessageReaderWhileLoop == false ) {
			   try{
				  tempMessage += myReader.readLine();
			   }catch (Exception e ) {
				   e.printStackTrace();
			   }
			   if (tempMessage.charAt(tempMessage.length()-1) == '/' &&  tempMessage.charAt(tempMessage.length()-2) == '/'){
				breakMessageReaderWhileLoop = true;   
			   }
		   }
		   // now for parsing the temp message read
		   
		   for ( int i = 0;i < tempMessage.length()-3;i++){
			   message+=tempMessage.charAt(i);
		   }
		   
		

	       if ( smsManager == null){
	      	 Toast.makeText(MainActivity.this, "Could not send SMS due to system fault",
	      	 		Toast.LENGTH_LONG).show();	
	      	 return;
	       }
	       ArrayList<String> parts = smsManager.divideMessage(message);
	       numberOfParts = parts.size();
	       sentPI =new ArrayList<PendingIntent>();
	       
	       for (int j = 0; j < numberOfParts; j++) {
	      	 sentPI.add( PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0));
	       }
	       
	       smsManager.sendMultipartTextMessage(phoneNumber, null, parts, sentPI, null);
	    }
	   private void startSendingMessages(final View v) {
		   progressBar = new ProgressDialog(v.getContext());
 			progressBar.setCancelable(true);
 			progressBar.setMessage("Sending messages ...");
 			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
 			progressBar.setProgress(0);
 			progressBar.setMax(100);
 			progressBar.show();
 			sendSMSMessage();
	   }
	   protected  void sendSMSMessage() {
	      thread  = new Thread(new Runnable() {
	    	  @Override
	    	  public void run() {		
	    		
	    		  sendOneMessage();
	    	  }
	      });
	      if (permission){
	    	  thread.start();
	      } else {
	    	  try {
				myReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	  return;
	      }
		
	   }
	   
	   @Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.main, menu);
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
		
		  public void makeAlert(String title,String text){
			  progressBar.dismiss();
				 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

			        alertDialogBuilder.setTitle(title)
						.setMessage(text)
						.setCancelable(false)				
						.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
								executeDelayed();
								FullScreencall();
						}
						}); 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
			 }
		    
		
	}