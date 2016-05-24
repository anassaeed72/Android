package com.anassaeed.phototaker;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;



public class MainActivity extends Activity {
	public static final String tag = "PhotoTaker";
	private Camera camera;
	private int cameraId = 0;
	private Button btnCapture;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnCapture = (Button) findViewById(R.id.btnCapture);
		btnCapture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(tag, "Button Clicked");
				try{
					if ( camera == null ){
						Log.i(tag, "Camera object is null");
					}
					camera.takePicture(null, null,
								new PhotoHandler(getApplicationContext()));
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		// do we have a camera?
	    if (!getPackageManager()
	        .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
	      Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
	          .show();
	    } else {
	      cameraId = findFrontFacingCamera();
	      if (cameraId < 0) {
	        Toast.makeText(this, "No front facing camera found.",
	            Toast.LENGTH_LONG).show();
	      } else {
	        camera = Camera.open(cameraId);
	      }
	    }
	}

	private int findFrontFacingCamera() {
	    int cameraId = -1;
	    // Search for the front facing camera
	    int numberOfCameras = Camera.getNumberOfCameras();
	    for (int i = 0; i < numberOfCameras; i++) {
	      CameraInfo info = new CameraInfo();
	      Camera.getCameraInfo(i, info);
	      if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
	        Log.i(tag, "Camera found id is "+ i);
	        cameraId = i;
	        break;
	      }
	    }
	    return cameraId;
	  }

	  @Override
	  protected void onPause() {
	    if (camera != null) {
	    	Log.i(tag, "Releasing camera");
	      camera.release();
	      camera = null;
	    }
	    super.onPause();
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
