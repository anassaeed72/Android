package com.anassaeed.logswriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	int number;
	int type ;
	int date ;
	int duration;
	String tag = "LogWriter";
	Button button;
	ProgressDialog progressDialog;
	private Handler progressBarHandler = new Handler();
	private Handler progressBarTextHandler = new Handler();
	File folder;
	File allCallsFile;
	FileOutputStream allCallsOutputStream = null;
	OutputStreamWriter allCallsOutputStreamWriter = null;
	File DialledCallsFile;
	FileOutputStream DialledCallsOutputStream = null;
	OutputStreamWriter DialledCallsOutputStreamWriter = null;
	File ReceivedCallsFile;
	FileOutputStream ReceivedCallsOutputStream = null;
	OutputStreamWriter ReceivedCallsOutputStreamWriter = null;
	File MissedCallsFile;
	FileOutputStream MissedCallsOutputStream = null;
	OutputStreamWriter MissedCallsOutputStreamWriter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FullScreencall();
	    executeDelayed();
		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			
		
		Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,null, null, null);
		number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		folder = new File(Environment.getExternalStorageDirectory(), "/Anas/Calls");
		if (!folder.exists()) {
	        folder.mkdir();
	    }
		button = (Button) findViewById(R.id.btnWriteLogs);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				writeAllLogs();
				writeDialledLogs();
				writeReceivedLogs();
				writeMissedLogs();
				makeAlert("Alert", "All Logs were written");
			}
		});
	}

	public void writeAllLogs(){
		Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,null, null, null);
		allCallsIntialize();
		while (managedCursor.moveToNext()) {
			String phNum = managedCursor.getString(number);
			String callTypeCode = managedCursor.getString(type);
			String strcallDate = managedCursor.getString(date);
			Date callDate = new Date(Long.valueOf(strcallDate));
			String callDuration = managedCursor.getString(duration);
			String callType = null;
			int callcode = Integer.parseInt(callTypeCode);
			switch (callcode) {
				case CallLog.Calls.OUTGOING_TYPE:
					callType = "Outgoing";
			    break;
				case CallLog.Calls.INCOMING_TYPE:
					callType = "Incoming";
			    break;
				case CallLog.Calls.MISSED_TYPE:
					callType = "Missed";
			    break;
			   }
			String toWrite;
			String contactName = getContactName(phNum);
			SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd KK:mm:ss aa yyyy");
			String formatedDate = formatter.format(callDate);
			if ( contactName.equals("null")){
				toWrite = "Call From "+phNum+"\n";
			} else {
				toWrite = "Call From "+contactName + " ("+phNum + ")\n";
			}
			toWrite += "Type: "+callType + "\n";
			toWrite += "Date:" +formatedDate + "\n";
			if  (!callType.equals("Missed")){
				toWrite += "Duration: " + callDuration + "\n";
			}
			try {
				allCallsOutputStreamWriter.append(toWrite);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		allCallsDeIntialize();
	}
	public void writeDialledLogs(){
		Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,null, null, null);
		DialledCallsIntialize();
		try {
			DialledCallsOutputStreamWriter.append("The Dialled Call Logs are the following: \n\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (managedCursor.moveToNext()) {
			String phNum = managedCursor.getString(number);
			String callTypeCode = managedCursor.getString(type);
			String strcallDate = managedCursor.getString(date);
			Date callDate = new Date(Long.valueOf(strcallDate));
			String callDuration = managedCursor.getString(duration);
			String callType = null;
			int callcode = Integer.parseInt(callTypeCode);
			switch (callcode) {
				case CallLog.Calls.OUTGOING_TYPE:
					callType = "Outgoing";
			    break;
				case CallLog.Calls.INCOMING_TYPE:
					continue;
			    
				case CallLog.Calls.MISSED_TYPE:
					continue;
			   }
			String toWrite;
			String contactName = getContactName(phNum);
			SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd KK:mm:ss aa yyyy");
			String formatedDate = formatter.format(callDate);
			if ( contactName.equals("null")){
				toWrite = "Call From "+phNum+"\n";
			} else {
				toWrite = "Call From "+contactName + " ("+phNum + ")\n";
			}
			toWrite += "Type: "+callType + "\n";
			toWrite += "Date:" +formatedDate + "\n";
			if  (!callType.equals("Missed")){
				toWrite += "Duration: " + callDuration + "\n";
			}
			try {
				DialledCallsOutputStreamWriter.append(toWrite);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DialledCallsDeIntialize();
	}
	
	public void writeReceivedLogs(){
		Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,null, null, null);
		ReceivedCallsIntialize();
		try {
			ReceivedCallsOutputStreamWriter.append("The Recieved Call Logs are the following: \n\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (managedCursor.moveToNext()) {
			String phNum = managedCursor.getString(number);
			String callTypeCode = managedCursor.getString(type);
			String strcallDate = managedCursor.getString(date);
			Date callDate = new Date(Long.valueOf(strcallDate));
			String callDuration = managedCursor.getString(duration);
			String callType = null;
			int callcode = Integer.parseInt(callTypeCode);
			switch (callcode) {
				case CallLog.Calls.OUTGOING_TYPE:
					continue;
				case CallLog.Calls.INCOMING_TYPE:
					callType = "Incoming";
			    break;
				case CallLog.Calls.MISSED_TYPE:
					continue;
			   }
			String toWrite;
			String contactName = getContactName(phNum);
			SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd KK:mm:ss aa yyyy");
			String formatedDate = formatter.format(callDate);
			if ( contactName.equals("null")){
				toWrite = "Call From "+phNum+"\n";
			} else {
				toWrite = "Call From "+contactName + " ("+phNum + ")\n";
			}
			toWrite += "Type: "+callType + "\n";
			toWrite += "Date:" +formatedDate + "\n";
			if  (!callType.equals("Missed")){
				toWrite += "Duration: " + callDuration + "\n";
			}
			try {
				ReceivedCallsOutputStreamWriter.append(toWrite);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ReceivedCallsDeIntialize();
	}
	public void writeMissedLogs(){
		Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,null, null, null);
		MissedCallsIntialize();
		try {
			MissedCallsOutputStreamWriter.append("The Missed Call Logs are the following:\n\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (managedCursor.moveToNext()) {
			String phNum = managedCursor.getString(number);
			String callTypeCode = managedCursor.getString(type);
			String strcallDate = managedCursor.getString(date);
			Date callDate = new Date(Long.valueOf(strcallDate));
			String callDuration = managedCursor.getString(duration);
			String callType = null;
			int callcode = Integer.parseInt(callTypeCode);
			switch (callcode) {
				case CallLog.Calls.OUTGOING_TYPE:
					continue;
				case CallLog.Calls.INCOMING_TYPE:
					continue;			    
				case CallLog.Calls.MISSED_TYPE:
					callType = "Missed";
					break;
			   }
			String toWrite;
			String contactName = getContactName(phNum);
			SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd KK:mm:ss aa yyyy");
			String formatedDate = formatter.format(callDate);
			if ( contactName.equals("null")){
				toWrite = "Call From "+phNum+"\n";
			} else {
				toWrite = "Call From "+contactName + " ("+phNum + ")\n";
			}
			toWrite += "Type: "+callType + "\n";
			toWrite += "Date:" +formatedDate + "\n";
			if  (!callType.equals("Missed")){
				toWrite += "Duration: " + callDuration + "\n";
			}
			try {
				MissedCallsOutputStreamWriter.append(toWrite);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		MissedCallsDeIntialize();
	}
	
	

	public void DialledCallsIntialize(){
		DialledCallsFile = new File(folder+ "/Dialled Calls.txt");
	    try {
	    	DialledCallsFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    DialledCallsOutputStream = null;
	    try {
	    	DialledCallsOutputStream = new FileOutputStream(DialledCallsFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    DialledCallsOutputStreamWriter = new OutputStreamWriter(DialledCallsOutputStream);
	    try {
	    	DialledCallsOutputStreamWriter.append("Dialled the Calls are the following:\n\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
	public void DialledCallsDeIntialize(){
		try {
			DialledCallsOutputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DialledCallsOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	  
	
	public void MissedCallsIntialize(){
		MissedCallsFile = new File(folder+ "/Missed Calls.txt");
	    try {
	    	MissedCallsFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    MissedCallsOutputStream = null;
	    try {
	    	MissedCallsOutputStream = new FileOutputStream(MissedCallsFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    MissedCallsOutputStreamWriter = new OutputStreamWriter(MissedCallsOutputStream);
	    try {
	    	MissedCallsOutputStreamWriter.append("Missed the Calls are the following:\n\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
	public void MissedCallsDeIntialize(){
		try {
			MissedCallsOutputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			MissedCallsOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void ReceivedCallsIntialize(){
		ReceivedCallsFile = new File(folder+ "/Received Calls.txt");
	    try {
	    	ReceivedCallsFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    ReceivedCallsOutputStream = null;
	    try {
	    	ReceivedCallsOutputStream = new FileOutputStream(ReceivedCallsFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    ReceivedCallsOutputStreamWriter = new OutputStreamWriter(ReceivedCallsOutputStream);
	    try {
	    	ReceivedCallsOutputStreamWriter.append("Received the Calls are the following:\n\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
	public void ReceivedCallsDeIntialize(){
		try {
			ReceivedCallsOutputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ReceivedCallsOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	private String getContactName(String num) {

		    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(num));
		    String[] projection = new String[] {ContactsContract.Contacts.DISPLAY_NAME};

		    CursorLoader cursorLoader = new CursorLoader(MainActivity.this, uri, projection, null, null,null);

		    Cursor c = cursorLoader.loadInBackground();

		    try {
		        if (!c.moveToFirst())
		            return "null";

		        return c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
		    } catch (Exception e)
		    {
		       
		        return "null";
		    }
		    finally {
		        if (c != null)
		            c.close();
		    }
		}
	public void allCallsIntialize(){
		allCallsFile = new File(folder+ "/All Calls.txt");
	    try {
			allCallsFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    allCallsOutputStream = null;
	    try {
			allCallsOutputStream = new FileOutputStream(allCallsFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    allCallsOutputStreamWriter = new OutputStreamWriter(allCallsOutputStream);
	    try {
			allCallsOutputStreamWriter.append("All the Calls are the following:\n\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	    	    
	}
	public void allCallsDeIntialize(){
		try {
			allCallsOutputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			allCallsOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    return true;
	} 
}
