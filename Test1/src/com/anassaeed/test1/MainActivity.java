package com.anassaeed.test1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
	private static final String TAG = "CameraDemo";
	Camera camera;
	Preview preview;
	Button buttonClick;
	public static int second = 1000;
	public static int minute = 60;
	public static int delayInMinutes = 1;
	private Handler pictureTakerHandler = new Handler();
	public static final String databaseName = "databaseName";
	public static final String delayTimeString = "dekayTimeString";
	public static final String usernameDB = "usernameDB";
	public static final String passwordDB = "passwordDB";
	public static String picAddress = "";
	private static final Mail mail = new Mail();
	ReentrantLock lock;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		try{
			preview = new Preview(this);
			((FrameLayout) findViewById(R.id.preview)).addView(preview);
		} catch (Exception e){
			e.printStackTrace();
		}

		buttonClick = (Button) findViewById(R.id.buttonClick);
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				 pictureTakerHandlerFunction();
			}
		});
		lock = new ReentrantLock();
		Log.i(TAG, "onCreate'd");
	}
	public void pictureTakerHandlerFunction() {
		Log.i(TAG, "Taking picture");
    	preview.camera.takePicture(shutterCallback, rawCallback,
				jpegCallback);
    
//		final Runnable r = new Runnable() {
//		    public void run() {
//		    	
//		        pictureTakerHandler.postDelayed(this, 10);
//		    }
//		};
//
//		pictureTakerHandler.postDelayed(r, delayInMinutes*minute*second);
	}
	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			Log.i(TAG, "onShutter'd");
		}
	};
	

	/** Handles data for raw picture*/ 
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.i(TAG, "onPictureTaken - raw");
		}
	};
	

	/** Handles data for jpeg picture*/ 
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			String photoFile = null;
			try {
				File folder= new File(Environment.getExternalStorageDirectory(), "/Anas/Pics");
				if (!folder.exists()) {
			        folder.mkdir();
			    }
				
				long milliSeconds = System.currentTimeMillis();
				 SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
		 		   Calendar calendar = Calendar.getInstance();
		 		   calendar.setTimeInMillis(milliSeconds);
		 		   String finalDateString = formatter.format(calendar.getTime());
				outStream = new FileOutputStream(String.format(
						"/sdcard/%d.jpg", System.currentTimeMillis()));
				outStream.write(data);
				outStream.close();
				Log.i(TAG, "onPictureTaken - wrote bytes: " + data.length);
				
				
				photoFile = folder +"/Picture_" + finalDateString + ".jpg";
				Log.i(TAG, photoFile);

			    File pictureFile = new File(photoFile);
			  
				   pictureFile.createNewFile();
			   
			    try {
			      FileOutputStream fos = new FileOutputStream(pictureFile);
			      fos.write(data);
			      fos.close();
			      
			    } catch (Exception error) {
			    	error.printStackTrace();
			    }
			    
			    
			    
			    
			    
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			Log.i(TAG, "onPictureTaken - Done");
			sendMail(photoFile);
		}
	};
	public void sendMail(final String filePath){
		Thread thread = new Thread(){
			@Override
			public void run(){
				Log.i(TAG, "Sending email with attachment");
				mail.setSubject("test");
		    	mail.setUsername("anassaeed72@gmail.com");
		    	mail.setPassword("ElonGates()(0");
		    	File file = new File (filePath);
		    	if ( !file.exists()){
		    		return;
		    	}
		    	try {
					mail.addAttachment(filePath);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		    	String _to[] = new String [1];
		    	_to[0] = "anassaeed72@gmail.com";
		    	
		    	mail.setTo(_to);
		    	try {
					Log.i(TAG, "Sending message");
					mail.send();
					Log.i(TAG, "Sending message");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	Log.i(TAG, "Sent email with attachement");
			}
		};
		Preview.camera.startPreview();
		thread.start();
		
	}

}
