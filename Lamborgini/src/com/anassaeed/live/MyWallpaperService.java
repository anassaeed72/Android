package com.anassaeed.live;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class MyWallpaperService extends WallpaperService {
	SurfaceHolder holder;
	Resources res;
	@Override
	public Engine onCreateEngine() {
		return new MyWallpaperEngine();
	}

	private class MyWallpaperEngine extends Engine {			
		public MyWallpaperEngine() {
			holder = getSurfaceHolder();
			res = getResources();
			 registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
		}


	}

	BroadcastReceiver mybroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
        			Canvas canvas = null;
        			try {
        				try{
        					canvas = holder.lockCanvas();
        				} catch (Exception e ) {
        					e.printStackTrace();				
        				}
         					MainActivity.counter++;
         					MainActivity.counter = MainActivity.counter%MainActivity.total;
        					MainActivity.bitmap = BitmapFactory.decodeResource(res, getResources().getIdentifier("a" + String.valueOf(MainActivity.counter), "drawable","com.anassaeed.live"));
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
        				}
        			}		
        		
                }
        }
    };

}