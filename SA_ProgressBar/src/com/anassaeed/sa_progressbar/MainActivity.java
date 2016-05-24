package com.anassaeed.sa_progressbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	int work = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// prepare for a progress bar dialog
					progressBar = new ProgressDialog(this);
					progressBar.setCancelable(true);
					progressBar.setMessage("File downloading ...");
					progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressBar.setProgress(0);
					progressBar.setMax(100);
					try{
						progressBar.show();
					} catch (Exception e){
						e.printStackTrace();
					}
					//reset progress bar status
					progressBarStatus = 0;
						
					//reset filesize
					work = 0;
					new Thread(new Runnable() {
						  public void run() {
							while (progressBarStatus < 100) {

							  // process some tasks
							  progressBarStatus = doSomeTasks();

							  // your computer is too fast, sleep 1 second
							  try {
								Thread.sleep(1000);
							  } catch (InterruptedException e) {
								e.printStackTrace();
							  }

							  // Update the progress bar
							  progressBarHandler.post(new Runnable() {
								public void run() {
								  progressBar.setProgress(progressBarStatus);
								}
							  });
							}

							// ok, file is downloaded,
							if (progressBarStatus >= 100) {

								// sleep 2 seconds, so that you can see the 100%
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								// close the progress bar dialog
								progressBar.dismiss();
							}
						  }
					       }).start();
					

	}
	
	public int doSomeTasks() {
		return work++;
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
