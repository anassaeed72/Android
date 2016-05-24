package com.anassaeed.mercedes;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

public class MyWallpaperService extends WallpaperService {

	@Override
	public Engine onCreateEngine() {
		return new MyWallpaperEngine();
	}

	private class MyWallpaperEngine extends Engine {
		private final Handler handler = new Handler();
		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				try{
					draw();
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("tag","counter value is  "+ MainActivity.counter);
				}
			}

		};
		
		public MyWallpaperEngine() {
			handler.post(drawRunner);
		}

		
		@SuppressLint("NewApi") private void draw() {
			SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				try{
					canvas = holder.lockCanvas();
				} catch (Exception e ) {
					e.printStackTrace();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					draw();
				}
 					Resources res = getResources();
					MainActivity.counter++;
					MainActivity.counter = MainActivity.counter%(MainActivity.total + MainActivity.ofset);
					if ( MainActivity.counter == 0 ){
						MainActivity.ofset = (int)Math.round(Math.random());
						MainActivity.counter = MainActivity.ofset;
					}
					MainActivity.bitmap = BitmapFactory.decodeResource(res, getResources().getIdentifier("m" + String.valueOf(MainActivity.counter), "drawable","com.anassaeed.mercedes"));
					if ( MainActivity.bitmap == null ) {
						return;
					}
					canvas.drawBitmap(MainActivity.bitmap, 0, 0, null);
			} finally {
				try{
					if (canvas != null){
						holder.unlockCanvasAndPost(canvas);
					}
				} catch (Exception e ) {
					e.printStackTrace();
					if ( canvas == null){
						Log.i("tag","canvas is null");
					}
					if ( holder == null) {
						Log.i("rag","holder is null");
					}
					if ( MainActivity.bitmap == null ) {
						Log.i("Tag", "bitmap is null");
					}
					Log.i("tag","counter value is  "+ MainActivity.counter);
					draw();
				}
			}
			handler.removeCallbacks(drawRunner);
			
				handler.postDelayed(drawRunner, 1000*60*30);
			
		}

	}

}