package com.example.phonecalldemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import com.android.internal.telephony.*;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.lang.reflect.Method;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
public class MainActivity extends Activity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      makeCall();

      Button startBtn = (Button) findViewById(R.id.makeCall);
      startBtn.setOnClickListener(new View.OnClickListener() {
         public void onClick(View view) {
      }
       });

   }
   protected void makeCall() {
      Log.i("Make call", "");

      Intent phoneIntent = new Intent(Intent.ACTION_CALL);
      phoneIntent.setData(Uri.parse("tel:03204774017"));

      try {
    	  startActivityForResult(phoneIntent,1);
//         finish();
//         Log.i("Finished making a call...", "");
      } catch (android.content.ActivityNotFoundException ex) {
         Toast.makeText(MainActivity.this, 
         "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
      }
   }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   public void onCallStateChanged(int state, String incomingNumber) {
       switch(state) {
           case TelephonyManager.CALL_STATE_IDLE:
                   Log.d("Tony","Outgoing Call finished");
                   // Call Finished -> stop counter and store it.
                  

               break;
           case TelephonyManager.CALL_STATE_OFFHOOK:
                   Log.d("Tony","Outgoing Call Starting");
                   // Call Started -> start counter.
                   // This is not precise, because it starts when calling,
                   // we can correct it later reading from call log
               break;
       }
}
}
