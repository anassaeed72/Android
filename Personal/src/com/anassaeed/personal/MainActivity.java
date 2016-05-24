package com.anassaeed.personal;


import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {
	Button button;
	public static int counter = -1;
	public static Bitmap bitmap;
	public static final int total = 11;
	public static int ofset = 0;
	public static final String tag = "LMerceedes";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		FullScreencall();
		executeDelayed();
		button = (Button) findViewById(R.id.btnStart);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				test(view);
				
			}
		});
    }

    public void test(View view) {
    	 Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
	        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, MyWallpaperService.class));
	        startActivity(intent);
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
	public boolean onTouchEvent(MotionEvent event) {
		MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	    return true;
	} 
}
