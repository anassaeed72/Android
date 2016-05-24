package com.anassaeed.flashlight;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;


public class MainActivity extends Activity {

	 
	   static Camera camera;
	   static Parameters params;
	    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getCamera();
        try{
        params.setFlashMode(Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
        } catch(Exception e ){
        	e.printStackTrace();
        }
    }
 // getting camera parameters
 private void getCamera() {
     if (camera == null) {
         try {
             camera = Camera.open();
             params = camera.getParameters();
         } catch (RuntimeException e) {
             Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
         }
     }
 }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    // TODO Auto-generated method stub
    	int eventaction=event.getAction();

        switch(eventaction) {
  
          case MotionEvent.ACTION_DOWN:
        	  params = camera.getParameters();
              params.setFlashMode(Parameters.FLASH_MODE_OFF);
              camera.setParameters(params);
              camera.stopPreview();
        	  Intent intent = new Intent(MainActivity.this, TurnFlashLightOff.class);
        	  startActivity(intent);
              break;
     
          default:
              break;
        }
    return super.onTouchEvent(event);
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
