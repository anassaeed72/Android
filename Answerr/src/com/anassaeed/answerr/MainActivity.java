package com.anassaeed.answerr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.lang.reflect.Method;

import com.android.internal.telephony.*;

public class MainActivity extends Activity {
	 private ITelephony telephonyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelephonyManager telephonyManager =  
                (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);  
   
  PhoneStateListener callStateListener = new PhoneStateListener() {  
  public void onCallStateChanged(int state, String incomingNumber)   
  {  
        if(state==TelephonyManager.CALL_STATE_RINGING){
           getBaseContext().startService(new Intent(getBaseContext(), PhoneAnswerIntentService.class));  
        }   
  }  
  };  
  telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);  
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
