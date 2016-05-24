package com.anassaeed.angle;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {
  private SensorManager sensorManager;

  int ofset = 0;
  int lastAngle = 0;
  double xLast = 0;
  double yLast = 0;
  double zLast = 0;
  double noise = .01;
  double zeroAngle = .31603461503982544;
  double ninetyAngle = 10.122684478759766;
  double t = ninetyAngle - zeroAngle;
  double stepSize = t/90;
  static boolean intialized = false;
  Button button;

/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    button = (Button) findViewById(R.id.ofset);
    button.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ofset = lastAngle;
		}
	});
  
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      getAccelerometer(event);
    }

  }

  private void getAccelerometer(SensorEvent event) {
	  float[] values = event.values;
	    // Movement
	    double x = values[0];
	    float y = values[1];
	    float z = values[2];
	    
	  if (intialized == false){
		  intialized = true;
		  xLast = x;
		  yLast = y;
		  zLast = z;
	  } else {
		  double deltaX = Math.abs(xLast - x);
		  double deltaY = Math.abs(yLast - y);
		  double deltaZ = Math.abs(zLast - z);
		  if (deltaX < noise) deltaX = (double)0.0;
		  if (deltaY < noise) deltaY = (double)0.0;
		  if (deltaZ < noise) deltaZ = (double)0.0;
		  xLast = x;
		  yLast = y;
		  zLast = z;
	  }
   
	  int angle = 0;
	  while (x > stepSize){
		  x  = x - stepSize;
		  angle = angle + 1;
	  }
	  angle = angle -2;
	  
	  lastAngle  = angle;
	  angle = angle - ofset;
	  if ( angle < 0){
		  angle = -1*angle;
	  }
	  TextView textview = (TextView) findViewById(R.id.textView);
	  if ( String.valueOf(angle).length() == 1){
		  textview.setText("   "+ String.valueOf(angle));
	  } else {
		  textview.setText("  "+ String.valueOf(angle));

	  }

  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  @Override
  protected void onResume() {
    super.onResume();
    // register this class as a listener for the orientation and
    // accelerometer sensors
    sensorManager.registerListener(this,
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause() {
    // unregister listener
    super.onPause();
    sensorManager.unregisterListener(this);
  }
}