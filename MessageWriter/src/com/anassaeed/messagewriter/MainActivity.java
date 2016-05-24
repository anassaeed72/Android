package com.anassaeed.messagewriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button button;
	private String tag = "MessageWriter";
	private ProgressDialog progressBar;
	private Handler progressBarHandler = new Handler();
	private Handler progressBarTextHandler = new Handler();
	private int progressBarStatus = 0;
	File folder;
	File allMessagesFile;
	FileOutputStream allMessagesOutputStream = null;
	OutputStreamWriter allMessagesOutputStreamWriter = null;
	File sentMessagesFile;
	FileOutputStream sentMessagesOutputStream = null;
	OutputStreamWriter sentMessagesOutputStreamWriter = null;
	File recievedMessagesFile;
	FileOutputStream recievedMessagesOutputStream = null;
	OutputStreamWriter recievedMessagesOutputStreamWriter  = null;
	ReentrantLock lock;
	boolean test = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.activity_main);
		} catch (Exception e ) {
			e.printStackTrace();
		}
		Log.i(tag, "On create");
		try{
		FullScreencall();
        executeDelayed();
		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		} catch (Exception e ) {
			e.printStackTrace();
		}
		folder = new File(Environment.getExternalStorageDirectory(), "/Anas/Messages");
		if (!folder.exists()) {
	        folder.mkdir();
	    }
		lock  = new ReentrantLock();
		button = (Button) findViewById(R.id.btnWrite);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

				Log.i(tag, "Button clicked");				
				recievedMessages(v);
				MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		    		
			}
		});
	}
	
	public void allMessagesIntialize(){
		allMessagesFile = new File(folder+ "/All Messages.txt");
	    try {
			allMessagesFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    allMessagesOutputStream = null;
	    try {
			allMessagesOutputStream = new FileOutputStream(allMessagesFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    allMessagesOutputStreamWriter = new OutputStreamWriter(allMessagesOutputStream);
	    try {
			allMessagesOutputStreamWriter.append("All the messages are the following:\n\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	    	    
	}
	public void allMessagesDeIntialize(){
		try {
			allMessagesOutputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			allMessagesOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void allMesssagesOneTime(Cursor c){
	       String smsToWrite = "";

		try{
 		   String body = c.getString(c.getColumnIndexOrThrow("body")).toString();
 		   String address = c.getString(c.getColumnIndexOrThrow("address")).toString();
 		  
 		   String contactName = getContactName(getBaseContext(), address);
 		   
 		   long milliSeconds = Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")).toString());
 		   SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
 		   Calendar calendar = Calendar.getInstance();
 		   calendar.setTimeInMillis(milliSeconds);
 		   String finalDateString = formatter.format(calendar.getTime());
 		   
 		   if ( contactName == null){
 			   smsToWrite = "Message From "+address+"\n";
 		   } else{
 			   smsToWrite = "Message from "+contactName+ " ("+address+")\n";
 		   }
 		   smsToWrite+= "Message at "+ finalDateString +"\n";
 		   smsToWrite+= "Body : "+body+"\n\n";
 		  
 		  allMessagesOutputStreamWriter.append(smsToWrite);
 	   } catch (Exception e) {
 		   e.printStackTrace();
 	   }
	}
	
	public void sentMessagesIntialize(){
		sentMessagesFile = new File(folder+ "/Sent Messages.txt");
	    try {
	    	sentMessagesFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    sentMessagesOutputStream = null;
	    try {
	    	sentMessagesOutputStream = new FileOutputStream(sentMessagesFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    sentMessagesOutputStreamWriter = new OutputStreamWriter(sentMessagesOutputStream);
	    try {
	    	sentMessagesOutputStreamWriter.append("Sent the messages are the following:\n\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
	public void sentMessagesDeIntialize(){
		try {
			sentMessagesOutputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sentMessagesOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sentMesssagesOneTime(Cursor c){
	       String smsToWrite = "";

		try{
		   String body = c.getString(c.getColumnIndexOrThrow("body")).toString();
		   String address = c.getString(c.getColumnIndexOrThrow("address")).toString();
		  
		   String contactName = getContactName(getBaseContext(), address);
		   
		   long milliSeconds = Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")).toString());
		   SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		   Calendar calendar = Calendar.getInstance();
		   calendar.setTimeInMillis(milliSeconds);
		   String finalDateString = formatter.format(calendar.getTime());
		   
		   if ( contactName == null){
			   smsToWrite = "Message From "+address+"\n";
		   } else{
			   smsToWrite = "Message from "+contactName+ " ("+address+")\n";
		   }
		   smsToWrite+= "Message at "+ finalDateString +"\n";
		   smsToWrite+= "Body : "+body+"\n\n";
		  
		   sentMessagesOutputStreamWriter.append(smsToWrite);
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	}
	
	public void  recievedMessagesIntialize(){
		 recievedMessagesFile = new File(folder+ "/Received Messages.txt");
	    try {
	    	 recievedMessagesFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    recievedMessagesOutputStream = null;
	    try {
	    	 recievedMessagesOutputStream = new FileOutputStream(recievedMessagesFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    recievedMessagesOutputStreamWriter = new OutputStreamWriter( recievedMessagesOutputStream);
	    try {
	    	 recievedMessagesOutputStreamWriter.append("The recieved messages are the following:\n\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	    	    
	}
	public void  recievedMessagesDeIntialize(){
		try {
			 recievedMessagesOutputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			 recievedMessagesOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void  recievedMesssagesOneTime(Cursor c){
	       String smsToWrite = "";

		try{
 		   String body = c.getString(c.getColumnIndexOrThrow("body")).toString();
 		   String address = c.getString(c.getColumnIndexOrThrow("address")).toString();
 		  
 		   String contactName = getContactName(getBaseContext(), address);
 		   
 		   long milliSeconds = Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")).toString());
 		   SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
 		   Calendar calendar = Calendar.getInstance();
 		   calendar.setTimeInMillis(milliSeconds);
 		   String finalDateString = formatter.format(calendar.getTime());
 		   
 		   if ( contactName == null){
 			   smsToWrite = "Message From "+address+"\n";
 		   } else{
 			   smsToWrite = "Message from "+contactName+ " ("+address+")\n";
 		   }
 		   smsToWrite+= "Message at "+ finalDateString +"\n";
 		   smsToWrite+= "Body : "+body+"\n\n";
 		  
 		  recievedMessagesOutputStreamWriter.append(smsToWrite);
 	   } catch (Exception e) {
 		   e.printStackTrace();
 	   }
	}
	

	public void recievedMessages(final View v){
		Log.i(tag, "Recieved message writer called");

		progressBar = new ProgressDialog(v.getContext());
	 	progressBar.setCancelable(true);
	 	progressBar.setMessage("Saving Received Messages ...");
	 	progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	 	progressBar.setProgress(0);
	 	progressBar.setMax(100);
	 	progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress));
//	 	progressBar.set
	 	try{
	 		progressBar.show();
	 	} catch (Exception e ) {
	 		e.printStackTrace();
	 	}
	 	progressBarStatus = 0;
		
	      new Thread(new Runnable() {
			  public void run() {
				  lock.lock();
				  recievedMessagesIntialize();
				  progressBar.setMessage("Saving Received Messages ...");
				  Uri uri = Uri.parse("content://sms/inbox");
			      Cursor c= getContentResolver().query(uri, null, null ,null,null);
			      startManagingCursor(c);
			      double totalCount = c.getCount();
			      Log.i(tag, "total "+totalCount);
			      for(int i=0; i <c.getCount(); i++) {
			    	  recievedMesssagesOneTime(c);
			    	  c.moveToNext();
			    	  double current = i;
			    	  double progreesTemp = current/totalCount;
			    	  progreesTemp = progreesTemp*100;
			    	  progressBarStatus = (int) progreesTemp;
			    	  Log.i(tag, "Current index "+current);
			    	  progressBarHandler.post(new Runnable() {
			    		  public void run() {
			    			  progressBar.setProgress(progressBarStatus);
			    		  }
			    	  });
				  }
			      Log.i(tag, "Loop ended");
			      c.close();
				  recievedMessagesDeIntialize();
				  try{
					  progressBarTextHandler.post(new Runnable() {
							public void run() {
								  progressBar.setMessage("Saving Sent Messages ...");
								  progressBar.setProgress(0);
							}
						  });
				  sentMessagesIntialize();
				  uri = Uri.parse("content://sms/sent");
				  c= getContentResolver().query(uri, null, null ,null,null);
				  startManagingCursor(c);
				  totalCount = c.getCount();
				  } catch(Exception e ){
					  e.printStackTrace();
				  }
				  for(int i=0; i <c.getCount(); i++) {
				      sentMesssagesOneTime(c);
					  c.moveToNext();
					  double current = i;
			    	  double progreesTemp = current/totalCount;
			    	  progreesTemp = progreesTemp*100;
			    	  progressBarStatus = (int) progreesTemp;
			    	  if ( i == c.getCount()){
			    		  progressBarStatus = 100;
			    	  }
					  progressBarHandler.post(new Runnable() {
						public void run() {
						  progressBar.setProgress(progressBarStatus);
						}
					  });
				  }
				  c.close();
				  sentMessagesDeIntialize();
				 	
				  progressBarTextHandler.post(new Runnable() {
						public void run() {
							  progressBar.setMessage("Saving All Messages ...");
							  progressBar.setProgress(0);
						}
					  });
				  allMessagesIntialize();
				  uri = Uri.parse("content://sms");
				  c= getContentResolver().query(uri, null, null ,null,null);
				  startManagingCursor(c);
				  totalCount = c.getCount();
				  for(int i=0; i <c.getCount(); i++) {
					  allMesssagesOneTime(c);
					  c.moveToNext();
					  double current = i;
					  double progreesTemp = current/totalCount;
					  progreesTemp = progreesTemp*100;
					  progressBarStatus = (int) progreesTemp;
					  if ( i == c.getCount()){
						  progressBarStatus = 100;
					  }
					  progressBarHandler.post(new Runnable() {
						public void run() {
						  progressBar.setProgress(progressBarStatus);
						}
					  });
				  }
				  c.close();
				  allMessagesDeIntialize();
					      
				 	
 				  try {
					Thread.sleep(2000);
				  } catch (InterruptedException e) {
					e.printStackTrace();
				  }
 				  progressBar.dismiss();
				  
				  Log.i(tag, "Deintialization done releasing lock");
				  lock.unlock();
				  
			  }
		      }).start();
	}
	public void sentMessages(View v){
		progressBar = new ProgressDialog(v.getContext());
	 	progressBar.setCancelable(true);
	 	progressBar.setMessage("Saving Sent Messages ...");
	 	progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	 	progressBar.setProgress(0);
	 	progressBar.setMax(100);
	 	progressBar.show();
	 	progressBarStatus = 0;

	      new Thread(new Runnable() {
			  public void run() {
				  lock.lock();
				  sentMessagesIntialize();
				  Uri uri = Uri.parse("content://sms/sent");
			      Cursor c= getContentResolver().query(uri, null, null ,null,null);
			      startManagingCursor(c);
			      double totalCount = c.getCount();
			      for(int i=0; i <c.getCount(); i++) {
			    	  
			    			  
				  // process some tasks
			      sentMesssagesOneTime(c);
				  c.moveToNext();
				  double current = i;
		    	  double progreesTemp = current/totalCount;
		    	  progreesTemp = progreesTemp*100;
		    	  progressBarStatus = (int) progreesTemp;
		    	  if ( i == c.getCount()){
		    		  progressBarStatus = 100;
		    	  }
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
 					progressBar.dismiss();
				       c.close();
				       sentMessagesDeIntialize();
				       lock.unlock();
				}
			  }
		      }).start();
	}
	public void allMessages(View v){
		progressBar = new ProgressDialog(v.getContext());
	 	progressBar.setCancelable(true);
	 	progressBar.setMessage("Saving All Messages ...");
	 	progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	 	progressBar.setProgress(0);
	 	progressBar.setMax(100);
	 	progressBar.show();
	 	progressBarStatus = 0;
	      new Thread(new Runnable() {
			  public void run() {
				  lock.lock();
				  allMessagesIntialize();
				  Uri uri = Uri.parse("content://sms");
			      Cursor c= getContentResolver().query(uri, null, null ,null,null);
			      startManagingCursor(c);
			      double totalCount = c.getCount();
			      for(int i=0; i <c.getCount(); i++) {
			    	  
			    			  
				  // process some tasks
			      allMesssagesOneTime(c);
				  c.moveToNext();
				  double current = i;
		    	  double progreesTemp = current/totalCount;
		    	  progreesTemp = progreesTemp*100;
		    	  progressBarStatus = (int) progreesTemp;
		    	  if ( i == c.getCount()){
		    		  progressBarStatus = 100;
		    	  }
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
 					progressBar.dismiss();
				       c.close();
						allMessagesDeIntialize();	
						lock.unlock();
				}
			  }
		      }).start();
	}
	public String getContactName(Context context, String phoneNumber) {
	    ContentResolver cr = context.getContentResolver();
	    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
	            Uri.encode(phoneNumber));
	    Cursor cursor = cr.query(uri,
	            new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
	    if (cursor == null) {
	        return null;
	    }
	    String contactName = null;
	    if (cursor.moveToFirst()) {
	        contactName = cursor.getString(cursor
	                .getColumnIndex(PhoneLookup.DISPLAY_NAME));
	    }
	    if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	    }
	    return contactName;
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
	    	   Log.i(tag, "on touch event");
	    return true;
	} 
}
