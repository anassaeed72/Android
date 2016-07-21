package com.anassaeed.timeconverter;

import java.util.Calendar;

import android.support.v7.app.ActionBarActivity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;


public class MainActivity extends ActionBarActivity {
	private TimePicker timePicker1;
	private TextView timePst;
	private TextView timeEST;
	   private Calendar calendar;
	   private String format = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePst = (TextView) findViewById(R.id.textViewPST);
        timeEST = (TextView) findViewById(R.id.textViewEST);

    }
   
    public void setTime(View view) {
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTimeEST((hour+1)%24, min);
        showTimePST((hour+10)%24, min);
     }
    public void showTimePST(int hour, int min) {
        if (hour == 0) {
           hour += 12;
           format = "AM";
        }
        else if (hour == 12) {
           format = "PM";
        } else if (hour > 12) {
           hour -= 12;
           format = "PM";
        } else {
           format = "AM";
        }
        timePst.setText(new StringBuilder().append("Time PST: ").append(hour).append(" : ").append(min)
        .append(" ").append(format).append(" (Parents)"));
     }

    public void showTimeEST(int hour, int min) {
        if (hour == 0) {
           hour += 12;
           format = "AM";
        }
        else if (hour == 12) {
           format = "PM";
        } else if (hour > 12) {
           hour -= 12;
           format = "PM";
        } else {
           format = "AM";
        }
        timeEST.setText(new StringBuilder().append("Time EST: ").append(hour).append(" : ").append(min)
                .append(" ").append(format).append(" (Salman)"));
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
}
