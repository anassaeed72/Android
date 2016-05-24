package com.example.sa_filewriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {


	File folder;
	File file;
	FileOutputStream outputStream = null;
	OutputStreamWriter outputStreamWriter = null;
;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		folder = new File(Environment.getExternalStorageDirectory(), "/Anas/Test");
		if (!folder.exists()) {
	        folder.mkdir();
	    }
		write();
	}

	public void write(){
		fileWritingInitlialize();
		try {
			outputStreamWriter.append("Test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileWritingDeIntialize();
	}
	

	public void fileWritingInitlialize(){
		file = new File(folder+ "/file.txt");
	    try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    outputStream = null;
	    try {
			outputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    outputStreamWriter = new OutputStreamWriter(outputStream);
	    try {
			outputStreamWriter.append("data");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	    	    
	}
	public void fileWritingDeIntialize(){
		try {
			outputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outputStream.close();
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

}

