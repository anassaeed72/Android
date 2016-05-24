package com.anassaeed.ami;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
static int callCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (callCount == 0){
        	makeCall();
        	callCount = 10;
        }
        Log.i("Ami","ON Create called");
        Button call;
        call  =(Button) findViewById(R.id.btnCall);
        call.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				makeCall();
			}
		});
//       
        
    }

    public void makeCall(){
    	Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
    	phoneCallIntent.setData(Uri.parse("tel:03244909160"));
    	try{
    		startActivity(phoneCallIntent);
    		return;
    	} catch(Exception e ){
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
