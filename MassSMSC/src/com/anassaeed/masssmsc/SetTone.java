package com.anassaeed.masssmsc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SetTone  extends Activity {
	private  ListView listView ;
	private MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settone);
        SetTone.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        executeDelayed();
        FullScreencall();
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listSetTone);
        
        // Defined Array values to show in ListView
        String[] values = new String[] { "Airplane Ding", "Door Bell", "Elevator Ding", "Knocking",  "Thunder",  "Two Tone Doorbell"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter); 
        
        // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {            
               int itemPosition = position;               
               String  itemValue    = (String) listView.getItemAtPosition(position);
               makeAlert("Alert", itemValue, itemPosition);
                  
                
              }

         }); 
      
       
        
    }
        
    public void makeAlert(String title,final String text,final int position){
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SetTone.this);
	        alertDialogBuilder.setTitle(title)
				.setMessage(text)
				.setCancelable(false)
				.setPositiveButton("Play",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						if ( position == 0 ){
							player = MediaPlayer.create(SetTone.this, R.raw.air_plane_ding );
							player.start();
						}else if (position == 1 ){
							player = MediaPlayer.create(SetTone.this, R.raw.doorbell   );
							player.start();
						} else if (position == 2 ) {
							player = MediaPlayer.create(SetTone.this, R.raw.elevator_ding   );
							player.start();
						} else if ( position == 3 ) {
							player = MediaPlayer.create(SetTone.this, R.raw.knocking_on_door   );
							player.start();
						} else if ( position == 4 ) {
							player = MediaPlayer.create(SetTone.this, R.raw.thunder   );
							player.start();
						} else if ( position == 5 ) {
							player = MediaPlayer.create(SetTone.this, R.raw.two_tone_doorbell);
							player.start();
						}
						
				}
				})
				.setNegativeButton("Set Tone",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						SharedPreferences  shfObject = getSharedPreferences(MainActivity.databaseName, Context.MODE_PRIVATE);
		            	SharedPreferences.Editor  shfEditorObject=shfObject.edit();
		            	shfEditorObject.putInt(MainActivity.toneSwitcher,position);
		            	if ( position == 0 ){
							MainActivity.mediaplayer = MediaPlayer.create(SetTone.this, R.raw.air_plane_ding );
						}else if (position == 1 ){
							MainActivity.mediaplayer  = MediaPlayer.create(SetTone.this, R.raw.doorbell   );
						} else if (position == 2 ) {
							MainActivity.mediaplayer  = MediaPlayer.create(SetTone.this, R.raw.elevator_ding   );
						} else if ( position == 3 ) {
							MainActivity.mediaplayer  = MediaPlayer.create(SetTone.this, R.raw.knocking_on_door   );
						} else if ( position == 4 ) {
							MainActivity.mediaplayer  = MediaPlayer.create(SetTone.this, R.raw.thunder   );
						} else if ( position == 5 ) {
							MainActivity.mediaplayer  = MediaPlayer.create(SetTone.this, R.raw.two_tone_doorbell);
						}
						Toast.makeText(getBaseContext(), "Tone: " +text + " saved", Toast.LENGTH_SHORT).show();
		            	shfEditorObject.commit();
				}
				}); 
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
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
    @SuppressLint("InlinedApi")
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
	@Override
	 public boolean onTouchEvent(MotionEvent event)
	 {     
		SetTone.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		float x1  = 0;
		float x2 = 0;
	    switch(event.getAction())
	    {
	    	case MotionEvent.ACTION_DOWN:
	    		x1 = event.getX();                         
	    		break;         
	    	case MotionEvent.ACTION_UP:
	    		x2 = event.getX();
	    		float deltaX = x2 - x1;
	    		if (Math.abs(deltaX) > MainActivity.SWIPE_MIN_DISTANCE)
	    		{
	    			Intent intent = new Intent(SetTone.this, MainActivity.class);
	    			startActivity(intent);
         	   		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	    		}
	    		else
	    		{
	               // consider as something else - a screen tap for example
	    		}                          
	    		break;   
	     }           
	     return super.onTouchEvent(event);       
	 } 
}
