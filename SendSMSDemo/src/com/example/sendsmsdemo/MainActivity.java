package com.example.sendsmsdemo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


 public class MainActivity extends Activity {
	char single(int in){
	    if (in == 0) {
	        return '0';
	    }
	    if (in == 1) {
	        return '1';
	    }
	    if (in == 2) {
	        return '2';
	    }
	    if (in == 3) {
	        return '3';
	    }
	    if (in == 4) {
	        return '4';
	    }
	    if (in == 5) {
	        return '5';
	    }
	    if (in == 6) {
	        return '6';
	    }
	    if (in == 7) {
	        return '7';
	    }
	    if (in == 8) {
	        return '8';
	    }
	    return '9';
	}
	public String reverser ( String in ) {
	    String output = null;
	    for (int i = in.length()-1; i>=0; i--) {
	        output = output + in.charAt(i);
	    }
	    return output;
	}
	String int_string(int input){
		String output ="";
	    int num = 10;
	    if (input == 0) {
	        return "0";
	    }
	    while (input >0) {
	        int dummy = input%num;
	        input = input/num;
//	        num = num*10;
	        output = output + single(dummy);
	    }
	    return reverser(output);
	}
   Button sendBtn;
   Button writeBtn;
   Button numGenBtn;
   String series = "0333";
   int numbersDone = 0;
   @SuppressLint("NewApi") @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      writeBtn = (Button) findViewById(R.id.btnWriteFile);
      sendBtn = (Button) findViewById(R.id.btnSendSMS);
      numGenBtn = (Button) findViewById(R.id.btnNumGen);
      float alpha = Float.parseFloat("0.5");
      writeBtn.setBackgroundColor(Color.BLUE);
//      writeBtn.setAlpha(alpha);
      sendBtn.setBackgroundColor(Color.RED);
      //sendBtn.setAlpha(alpha);
      numGenBtn.setBackgroundColor(Color.GRAY);
      numGenBtn.setAlpha(alpha);
      
      sendBtn.setOnClickListener(new View.OnClickListener() {
         public void onClick(View view) {
        	 try {
 				startSending();
 			} catch (InterruptedException e) {
 				e.printStackTrace();
 			}
         }
      });
      writeBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View view) {
         	 readSMS();
          }
       });
      numGenBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View view) {
          	 try {
				genNumbers();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }
        });
   }
   private void startSending() throws InterruptedException{
	   Toast.makeText(getBaseContext(), "Generic failure", 
               Toast.LENGTH_SHORT).show();
	    String path = Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES).toString();
	    int loops = 0;
	 // for reading from a file
	      try{
	    	  File writerFile = new File("/storage/emulated/0/Anas/output.txt");
	       	  writerFile.createNewFile();
	    	  File myFile = new File("/storage/emulated/0/Anas/input.txt");
	    	     FileInputStream fIn = new FileInputStream(myFile);
	    	     BufferedReader myReader = new BufferedReader(
	    	             new InputStreamReader(fIn));
	    	      String number;
	    	     while (true) {
	    		   	  ArrayList<Thread> myThreads = new ArrayList<Thread>();
	    		   	  int counter = 0;
	    	    	 for ( int i = 0;i < 11;i++){
	    	    		 number = myReader.readLine();
	    	    		 if ( number  == null  || number.equals("Anas")){
	    	    			 break;
	    	    		 }
		    	    	 final String dummy = number;
		    	    	 Thread thread = new Thread(new Runnable() {
		    	    		 public void run(){
			    		         sendSMS(dummy,"");
		    	    			 }
		    	    	 });
		    	    	 counter++;
		    	    	 myThreads.add(thread);
	    	    	 }
	    	    	 for ( int i = 0; i < counter;i++){
	    	   	    	  myThreads.get(i).start();
	    	   	    	  myThreads.get(i).join();
	    	   	      }
	    	     Thread.sleep(60000);
	    	    	 loops++;
	    	    	 if ( loops > 5 ){
	    	    		 int waitLevel = loops/10;
	    	    		 Thread.sleep(waitLevel*60000);
	    	    	 }
	    	    	 if (loops  == 50 ){
	    	    		 Thread.sleep(20*60000);
	    	    	 }
	    	    	 number = myReader.readLine();
	    	    	 if ( number == null || number.equals("Saeed")){
	    	    		 break;
	    	    	 }
	    	    	 // for sending the sms
	    	    	 
	    	    	 }
	      }catch(IOException e){  
	      }
	     
	     
System.exit(0);
	     
	      

  }
   @Override
  
   public boolean onCreateOptionsMenu(Menu menu) {

      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   //---sends an SMS message to another device---
   private void sendSMS(String phoneNumber, String message)
   {        
       String SENT = "SMS_SENT";
       String DELIVERED = "SMS_DELIVERED";

       PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
           new Intent(SENT), 0);

       PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
           new Intent(DELIVERED), 0);

       //---when the SMS has been sent---
       registerReceiver(new BroadcastReceiver(){
           @Override
           public void onReceive(Context arg0, Intent arg1) {
               switch (getResultCode())
               {
                   case Activity.RESULT_OK:
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

       //---when the SMS has been delivered---
       registerReceiver(new BroadcastReceiver(){
           @Override
           public void onReceive(Context arg0, Intent arg1) {
               switch (getResultCode())
               {
                   case Activity.RESULT_OK:
                	  // writeNumberToFile("SMS Delivered");
//                       Toast.makeText(getBaseContext(), "SMS delivered", 
//                               Toast.LENGTH_SHORT).show();
                       break;
                   case Activity.RESULT_CANCELED:
                       Toast.makeText(getBaseContext(), "SMS not delivered", 
                               Toast.LENGTH_SHORT).show();
                       break;                        
               }
           }
       }, new IntentFilter(DELIVERED));        

       SmsManager sms = SmsManager.getDefault();
       sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
   } 
   public void writeNumberToFile(String number){
	   if ( number.equals("03234089556")){
		   Toast.makeText(getBaseContext(), "Equals", 
                   Toast.LENGTH_SHORT).show();
	   }
		 try {
	   	  File myFile = new File("/storage/emulated/0/Anas/output.txt");
	   	    FileOutputStream fOut = new FileOutputStream(myFile,true);
	   	    OutputStreamWriter myOutWriter = 
	   	                            new OutputStreamWriter(fOut);
	           myOutWriter.write(number + "\n");
	           myOutWriter.close();
	     }
	     catch (IOException e) {
	   	  Toast.makeText(getApplicationContext(),"File not wriiten",
					  Toast.LENGTH_LONG).show();  
	   	  } 
	}
   public void readSMS(){
	    Uri uriSMSURI = Uri.parse("content://sms/sent");
	   Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);
	   while (cur.moveToNext()) {
           String address = cur.getString(cur.getColumnIndex("status"));
           if ( address.equals("0")){
        	   address = cur.getString(cur.getColumnIndex("address"));
        	   writeNumberToFile(address);
           }
	   }
	   
	   Uri uriSMSURIR = Uri.parse("content://sms/inbox");
	   Cursor curr = getContentResolver().query(uriSMSURIR, null, null, null, null);
	   while (curr.moveToNext()) {
        	   String address = curr.getString(curr.getColumnIndex("address"));
        	   writeNumberToFile(address);
	   }
	   
	   Uri allCalls = Uri.parse("content://call_log/calls");
	   Cursor curCalls = getContentResolver().query(allCalls, null, null, null, null);
	   while ( curCalls.moveToNext()){
		   String num= curCalls.getString(curCalls.getColumnIndex(CallLog.Calls.NUMBER));// for  number
		   writeNumberToFile(num);
	   }
   }
   public void genNumbers() throws IOException{
	    int numbersToWrite = 100;
	    File myFile = new File("/storage/emulated/0/Anas/data.txt");
	     FileInputStream fIn = new FileInputStream(myFile);
	     BufferedReader myReader = new BufferedReader(
	             new InputStreamReader(fIn));
	    String intialString;
	    intialString = myReader.readLine(); 
	    int intial = Integer.parseInt(intialString);
	    intial = intial + 100;
	    String totalNumbersDoneString;
	    totalNumbersDoneString = myReader.readLine();
	    int totalNumberDone = Integer.parseInt(totalNumbersDoneString);
	    totalNumberDone = totalNumberDone + 100;
	    File writerFile = new File("/storage/emulated/0/Anas/input.txt");
	   	FileOutputStream dummy = new FileOutputStream(writerFile);
	    for (int i = 0;i < numbersToWrite;i++){
	    	StringBuilder toWrite = new StringBuilder();
	    	toWrite.append("0333");
	    	toWrite.append(intial);
	    	intial = intial + 1;
	    	try {
	  	   	    FileOutputStream fOut = new FileOutputStream(writerFile,true);
	  	   	    OutputStreamWriter myOutWriter = 
	  	   	                            new OutputStreamWriter(fOut);
	  	           myOutWriter.write(toWrite + "\n");
	  	           myOutWriter.close();
	  	     }
	  	     catch (IOException e) {
	  	   	  Toast.makeText(getApplicationContext(),"File not wriiten",
	  					  Toast.LENGTH_LONG).show();  
	  	   	  } 
	    }
	    FileOutputStream fOut = new FileOutputStream(writerFile,true);
	   	    OutputStreamWriter myOutWriter = 
	   	                            new OutputStreamWriter(fOut);
	   	myOutWriter.write("Anas\n");
	   	myOutWriter.close(); 
	    File dataFile = new File("/storage/emulated/0/Anas/data.txt");
	   	FileOutputStream dataFileStream = new FileOutputStream(dataFile);
	   	OutputStreamWriter dataStreamWriter = new OutputStreamWriter(dataFileStream);
	   	dataStreamWriter.write(intial + "\n");
	   	dataStreamWriter.write(totalNumberDone + "\n");
	   	dataStreamWriter.close();

   }
   String formating(int input){
	    String temp = int_string(input);
	    StringBuilder output  = new StringBuilder();
	    output.append("0333");
	    for ( int i = temp.length();i < 7;i++ ) {
	    	output.append("0");
	    }
	    output.append(temp);
	    return output.toString();
	}
}