package com.anassaeed.autoreplyer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	public static boolean toggleSwitch = true;
	public static final String databaseName = "databaseName";
	public static final String messageToReply = "messageToReply";
	public static String messageCurrent = "";
	Button button;
	TextView textView;
	
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	public static final int SWIPE_MIN_DISTANCE = 120;
	public static final int SWIPE_MAX_OFF_PATH = 250;
	public static final int SWIPE_THRESHOLD_VELOCITY = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
 	   	SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);  
    	messageCurrent = shfObject.getString(MainActivity.messageToReply,"Message");

    	textView = (TextView) findViewById(R.id.textView1);
		button = (Button) findViewById(R.id.btnToggle);
		button.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if ( toggleSwitch == true ) {
					toggleSwitch = false;
					Toast.makeText(getBaseContext(), "Auto replyer turned off", Toast.LENGTH_SHORT).show();
					button.setText("Turn On");
					textView.setText("Please press below to turn on auto reply");
				} else if ( toggleSwitch == false ) {
					toggleSwitch = true;
					Toast.makeText(getBaseContext(), "Auto replyer turned on", Toast.LENGTH_SHORT).show();
					button.setText("Turn Off");
					textView.setText("Please press below to turn off auto reply");
				}
				MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
				executeDelayed();
				FullScreencall();
			}
		});
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); 
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout,R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close){
                /** Called when drawer is closed */
                public void onDrawerClosed(View view) {
                    invalidateOptionsMenu();
                }
                /** Called when a drawer is opened */
                public void onDrawerOpened(View drawerView) {                   
                    invalidateOptionsMenu();
                }
        };
 
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
        String[] arrayForReadAndWrite = {"See Current Message","Set Message"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( getBaseContext(), R.layout.drawer_list_item , arrayForReadAndWrite);
 
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {                 
                    mDrawerLayout.closeDrawer(mDrawerList);
                    if ( position == 0 ) {
                    	Intent intent = new Intent(MainActivity.this, ShowCurrentMessage.class);
                    	startActivity(intent);
                    	overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                    else if ( position == 1 ) {
                    	Intent intent = new Intent(MainActivity.this, SetMessage.class);
                    	startActivity(intent);
                    	overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
            }
        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	public boolean onTouchEvent(MotionEvent event) {
		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    return true;
	} 
	
	  @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        mDrawerToggle.syncState();
	    }
	 
	    /** Handling the touch event of app icon */
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }
	 
	    /** Called whenever we call invalidateOptionsMenu() */
	    @Override
	    public boolean onPrepareOptionsMenu(Menu menu) {
	        // If the drawer is open, hide action items related to the content view
	        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	 
	        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
	        return super.onPrepareOptionsMenu(menu);
	    }
	 
}
