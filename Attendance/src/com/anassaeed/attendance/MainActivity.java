package com.anassaeed.attendance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;

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
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private static String number;
	private static String name;
	private static String present;
	private static String classSection;
	private static String number2;
	private static boolean secondNumber; 
	private static String SENT = "SMS_SENT";
	private ArrayList<PendingIntent> sentPI;
	private static  File folder;
	private static  File myFile;
	private static FileInputStream myFileInputStream = null;
	private static BufferedReader myBufferedReader = null;
	private static BufferedReader myBufferedReaderCount = null;
	private static boolean allDone  =false;
	private static int numberOfParts = 0;
	Button button;
	private static boolean permissionToSend = true;
	private static int totalNumberOfMessages = 0;
	private ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private static Handler progressBarHandler =null;
	private static boolean permissionAfterMessageSent = false;
	private static int numberOfMessagesSent = 0;
	public static MediaPlayer mediaplayer;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public static final String databaseName = "databaseName"; 
	public static final String toneSwitcher = "toneSwitcher";
	public static final int SWIPE_MIN_DISTANCE = 120;
	private static ArrayList<String> arrayList;
	private static final String brand = "Huawei";
	private static final String board = "P7-L10";
	private static final String display = "P7-L10V100R001C00B158";
	private static final String buildSerialNumber = "7N2NEF145J016776";
	private static final String device_id  = "357456042532120";
	private static final String phoneSerialNumber = "8941007106700755098F";
	private static final String androidId = "f211bb2b1834dcf8";
	private static final String uuid = "ffffffff-8822-c075-6b94-44e97dfe89b5";
	public static boolean permissionToSendPresntStudentsMessages = true;
	public static final String permisionPresentAbsentStudents = "permisionPresentAbsentStudents";
	private  boolean verifyPhoneState(){
		String brandTemp = Build.BRAND.toString();
        String boardTemp = Build.BOARD.toString();
        String displayTemp = Build.DISPLAY.toString();
        String serialTemp = Build.SERIAL.toString();
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);        
        final String tmDeviceTemp, tmSerialTemp, androidIdTemp;
        tmDeviceTemp = "" + tm.getDeviceId();
        tmSerialTemp = "" + tm.getSimSerialNumber();
        androidIdTemp = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidIdTemp.hashCode(), ((long)tmDeviceTemp.hashCode() << 32) | tmSerialTemp.hashCode());
        String uuidString = deviceUuid.toString();
        if (!brandTemp.equals(brand)){
        	return false;
        }
        if (!boardTemp.equals(board)){
        	return false;
        }
        if (!displayTemp.equals(display)){
        	return false;
        }
        
        
        if (!serialTemp.equals(buildSerialNumber)){
        	return false;
        }
        if (!tmDeviceTemp.equals(device_id)){
        	return false;
        }
        
        if ( !tmSerialTemp.equals(phoneSerialNumber)){
        	return false;
        }
        if ( !androidIdTemp.equals(androidId)){
        	return false;
        }
       
        if (!uuidString.equals(uuid)){
        	return false;
        }       
        return true;
	}

	private static void flushVariables(){
		numberOfParts = 0;
		number = "";
		name = "";
		present  = "";
		classSection = "";
		number2 = "";
		secondNumber = false;
	}
	private static void intialiaze(){
		folder = new File(Environment.getExternalStorageDirectory(), "/Attendance");
		if (!folder.exists()) {
	        folder.mkdir();
	    }
		try{
			myFile = new File(folder+ "/Attendance.csv");
		
		} catch (Exception e ){
			e.printStackTrace();
		}
		try {
			myFileInputStream = new FileInputStream(myFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		 
		myBufferedReader = new BufferedReader( new InputStreamReader(myFileInputStream));
		myBufferedReaderCount = new BufferedReader( new InputStreamReader(myFileInputStream));
		
		allDone = false;
		numberOfParts = 0;
		number = "";
		name = "";
		present  = "";
		classSection = "";
		permissionToSend = true;
		permissionAfterMessageSent = true;
		arrayList = new ArrayList<String>();
		progressBarHandler= new Handler();
	}
	public static boolean checkForReaders(){
		if ( folder == null){
			return false;
		}
		if ( myFile == null ) {
			return false;
		}
		if ( myFileInputStream == null ) {
			return false;
		}
		if ( myBufferedReader == null ) {
			return false;
		}
		return true;
	}
	public void stringSplit(String input){
		int counter = 0;
		boolean first  =false;
		boolean seconNumberSetting  = false;
		int index = 0;
		for ( index = 0;index  < input.length();index ++){
			if ( input.charAt(index ) == ','){
				counter++;
				continue;
			}
			if ( counter == 4 ){
				break;
			}
			if ( counter == 2){
				number = number + input.charAt(index );
			}
			if ( counter == 0){
				name  = name + input.charAt(index );
			}
			if ( counter == 1 ) {
				 classSection = classSection + input.charAt(index );
			}	
			if ( counter == 3  && first == false && input.charAt(index ) != ',') {
				seconNumberSetting = true;
			}
			if ( seconNumberSetting && counter == 3) {
				number2+=input.charAt(index );
				secondNumber = true;
			}
			
		}
		if ( counter > 3){
			if ( input.charAt(index) =='1'){
				present = "present";
			} else if ( input.charAt(index ) =='0') {
				present = "absent";
			}
		} else {
			present = "present";
		}
	}
	public String genMessage(){
		String output = "";
		output = "Test Message Salam Your child " + name + " in " + classSection + " is " + present + " Regards Misber School";
		return output;
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (verifyPhoneState() == false){
        	Toast.makeText(getBaseContext(), "Phone not verified", Toast.LENGTH_LONG).show();
        	System.exit(0);
        }
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
        permissionToSendPresntStudentsMessages = shfObject.getBoolean(MainActivity.permisionPresentAbsentStudents, true);

        button = (Button) findViewById(R.id.btnSend);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if (verifyPhoneState() == false){
                	Toast.makeText(getBaseContext(), "Phone not verified", Toast.LENGTH_LONG).show();
                	System.exit(0);
                }
            	intialiaze();
            	if ( checkForReaders() == false ) {
            		makeAlert("Alert", "Could not send messages as input was not correct");
            		return;
            	}
                countLines();
                if ( totalNumberOfMessages == 0 ){
                	makeAlert("Alert", "No student record found. PLease check the file");
                	
                }else {
                	myBroadcastReciever();
                	try{
                		sendSMSMessage(view);
                	}catch (Exception e ) {
                		e.printStackTrace();
                	}
                }
             }
          });
        
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
 
        String[] arrayForReadAndWrite = {"How To use","Set Tone", "Info", "Trouble Shooting","Present Absent","Format","Contact"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( getBaseContext(), R.layout.drawer_list_item , arrayForReadAndWrite);
 
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   if ( position == 0 ) {
                	   Intent intent = new Intent(MainActivity.this, HowToUse.class);
                	   startActivity(intent);
                	   overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                   }
                   else if ( position == 1 ) {
                	   Intent intent = new Intent(MainActivity.this, SetTone.class);
                	   startActivity(intent);
                	   overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                   }
                   else if ( position == 2 ) {
                	   Intent intent = new Intent(MainActivity.this, Info.class);
                	   startActivity(intent);
                	   overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                   }
                   else if ( position == 3 ) {
                	   Intent intent = new Intent(MainActivity.this, TroubleShooting.class);
                	   startActivity(intent);
                	   overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                   }
                   else if ( position == 4 ) {
                	   Intent intent = new Intent(MainActivity.this, PresentPermission.class);
                	   startActivity(intent);
                	   overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                   }
                   
                   else if ( position == 5 ) {
                	   Intent intent = new Intent(MainActivity.this, Format.class);
                	   startActivity(intent);
                	   overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                   }
                   
                   else if ( position == 6 ) {
                	   Intent intent = new Intent(MainActivity.this, Contact.class);
                	   startActivity(intent);
                	   overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                   }
                   
                    // Closing the drawer
                    mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
    }
    public static void deintlialize(){
    	try {
			myFileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	try {
			myBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	mediaplayer.start();
    	
    }
    public void myBroadcastReciever(){
    	 //---when the SMS has been sent---
         final BroadcastReceiver receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                      
                        numberOfParts--;
                        if ( numberOfParts == 0) {

                      		  try{
                      			  if ( allDone == true){
                      				  permissionToSend = false;
                      			  } else {
                      				permissionAfterMessageSent = true;
                      			  }
                      		  } catch (Exception e ) {
                      			  e.printStackTrace();
                      		  }

                      	  
                        }
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        permissionToSend = false;
                        makeAlert("Alert", "Generic failure");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        makeAlert("Alert", "No Service");
                        permissionToSend = false;
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        makeAlert("Alert", "Null PDU");
                        permissionToSend = false;
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        makeAlert("Alert", "Radio off");
                        permissionToSend = false;
                        break;
                }
            }
        };
        this.registerReceiver(receiver,  new IntentFilter(SENT));
    }
  
    public void  sendOneMessage(){
    	if ( permissionToSend == false ) {
    		return;
    	}
    	flushVariables();
    	String oneLine ="";
    	try{
    		oneLine= arrayList.get(numberOfMessagesSent);
    	}catch (Exception e ) {
    		e.printStackTrace();
    	}
    	try{
    		stringSplit(oneLine);
    	}catch (Exception e ){
    		e.printStackTrace();
    	}
    	if ( permissionToSendPresntStudentsMessages == false  && present.equals("present")) {
    		if ( numberOfMessagesSent == totalNumberOfMessages){
    			return;
    		}
    		numberOfMessagesSent++;
    		sendOneMessage();
    	}
    	if ( secondNumber == false && number.equals("")){
    		if ( numberOfMessagesSent == totalNumberOfMessages ) {
    			return;
    		}
    		numberOfMessagesSent++;
    		sendOneMessage();
    	}
    	SmsManager smsManager = SmsManager.getDefault();
    	
    	ArrayList<String> parts = smsManager.divideMessage(genMessage());
        numberOfParts = parts.size();
        sentPI =new ArrayList<PendingIntent>();
        
        if ( secondNumber ) {
        	numberOfParts = numberOfParts*2;
        }
        for (int j = 0; j < numberOfParts; j++) {
       	 sentPI.add( PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0));
        }
        Log.i("tag","The message is "+ genMessage());
        
        smsManager.sendMultipartTextMessage(number, null, parts, sentPI, null);
        if ( secondNumber){
        	smsManager.sendMultipartTextMessage(number2, null, parts, sentPI, null);
        }
	    permissionAfterMessageSent = false;
    }

    protected void sendSMSMessage(final View v) {
        
      
        	    	progressBar = new ProgressDialog(v.getContext());
        			progressBar.setCancelable(false);
        			progressBar.setMessage("Sending Messages ...");
        			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        			progressBar.setProgress(0);
        			progressBar.setMax(100);
        			try{
        				progressBar.show();
        			}catch (Exception e ) {
        				e.printStackTrace();
        			}
        			progressBarStatus = 0;		                	
        	
        
		
			new Thread(new Runnable() {
				  public void run() {			
					while (progressBarStatus < 100) {
						if ( permissionToSend == false ) {	
							progressBar.dismiss();
							progressBar.cancel();
			                deintlialize();
							progressBar.dismiss();
							break;
						}
						//progressBarStatus = 100;
						if ( progressBarStatus== 100 ) {
							continue;
						}
						while ( permissionAfterMessageSent == false) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
						sendOneMessage();
						numberOfMessagesSent++;
						double tempnumberOfMessagesSent = numberOfMessagesSent;
						double temptotalNumberOfMessages = totalNumberOfMessages;
						double tempprogressBarStatus = tempnumberOfMessagesSent/temptotalNumberOfMessages;
						tempprogressBarStatus = tempprogressBarStatus*100;
						progressBarStatus = (int)  tempprogressBarStatus;
					
						if (numberOfMessagesSent == totalNumberOfMessages ) {
							progressBarStatus = 100;
						} 
						  // Update the progress bar
						  progressBarHandler.post(new Runnable() {
							public void run() {
							  progressBar.setProgress(progressBarStatus);
							}
						  });
						}
	 
					if (progressBarStatus >= 100) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	 
						// close the progress bar dialog
						progressBar.dismiss();
						progressBar.cancel();
		                deintlialize();
		                
		                try{
		                	MainActivity.this.runOnUiThread(new Runnable() {
		                	    public void run() {
				                	makeAlertFullScreenActivator("Alert", "All Messages Sent");				                	
		                	    }
		                	});
		                } catch (Exception e ) {
		                	e.printStackTrace();
		                }
					}
				  }
			       }).start();	 		          		                
	 
     }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
				}
				}); 
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	 }
    
    public void makeAlertFullScreenActivator(String title,String text){
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
   
    public static  void countLines()  {
  
    	try {
    		String temp = myBufferedReaderCount.readLine();
			while(temp != null){
				arrayList.add(temp);
				temp = myBufferedReaderCount.readLine();
				totalNumberOfMessages++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
 
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
	@SuppressLint("InlinedApi")
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
