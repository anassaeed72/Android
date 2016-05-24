package com.anassaeed.bouncingballsw;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
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
        draw();
      }

    };
    private List<MyPoint> circles;
    private Paint paint = new Paint();
    private int width;
    int height;
    private boolean visible = true;
    private int tolerance = 50;
    private int colorArray[] = {-16776961, -16711681, -12303292, -7829368 , -16711936, -3355444, -65281,  -65536, -1, -256};
    public MyWallpaperEngine() {
      SharedPreferences prefs = PreferenceManager
          .getDefaultSharedPreferences(MyWallpaperService.this);
      MainActivity.maxNumber = Integer
          .valueOf(prefs.getString("numberOfCircles", "4"));
      circles = new ArrayList<MyPoint>();
      paint.setAntiAlias(true);
      paint.setColor(Color.BLUE);
      paint.setStyle(Paint.Style.FILL_AND_STROKE);
      paint.setStrokeJoin(Paint.Join.ROUND);
      paint.setStrokeWidth(10f);
      
      handler.post(drawRunner);
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
      this.visible = visible;
      if (visible) {
        handler.post(drawRunner);
      } else {
        handler.removeCallbacks(drawRunner);
      }
    }

   	@Override
	public void onSurfaceDestroyed(SurfaceHolder holder) {
	  super.onSurfaceDestroyed(holder);
	  this.visible = false;
	  handler.removeCallbacks(drawRunner);
	}
	
	@Override
	public void onSurfaceChanged(SurfaceHolder holder, int format,
	    int width, int height) {
	  this.width = width;
	  this.height = height;
	  super.onSurfaceChanged(holder, format, width, height);
	  intialize();
	}
	public void intialize(){
		for ( int i = 0;i < MainActivity.maxNumber;i++){
			  // getting the variables from their places
			  int xTemp =  (int) (width * Math.random());
			  int yTemp = (int) (height * Math.random());
			
			  MyPoint mypoint = new MyPoint("text", xTemp, yTemp);
			  circles.add(mypoint);

		  }
	      
	}
	
	private void draw() {
	  SurfaceHolder holder = getSurfaceHolder();
	  Canvas canvas = null;
	  
	  try{
		  canvas = holder.lockCanvas();
		  if (canvas != null) {

			  List<MyPoint> circlesTemp = new ArrayList<MyPoint>();
			  for ( int i = 0;i < MainActivity.maxNumber;i++){
				  // getting the variables from their places
				  int xTemp = circles.get(i).x;
				  int yTemp = circles.get(i).y;
				  int xVelocityTemp = circles.get(i).xVelocity;
				  int yVelocityTemp = circles.get(i).yVelocity;
				  int xFinal = xTemp + xVelocityTemp;
				  int yFinal = yTemp + yVelocityTemp;
				  
				  // for the x calculations 
				  if ( xFinal > (width -tolerance)){
					  xFinal -=tolerance/2;
					  xVelocityTemp = -xVelocityTemp;
				  } else if (xFinal < tolerance ){
					  xFinal+=tolerance/2;
					  xVelocityTemp = -xVelocityTemp;
				  }
				  
				  // for the y calculations
				  if (yFinal >(height - tolerance)){
					  yFinal -=tolerance/2;
					  yVelocityTemp = -yVelocityTemp;
				  } else if ( yFinal < tolerance){
					  yFinal+=tolerance/2;
					  yVelocityTemp = -yVelocityTemp;
				  }
				  MyPoint mypoint = new MyPoint("text", xFinal, yFinal);
				  mypoint.xVelocity = xVelocityTemp;
				  mypoint.yVelocity = yVelocityTemp;
				  circlesTemp.add(mypoint);
			  }
			  circles.clear();
			  circles = circlesTemp;
		      drawCircles(canvas, circles);
		  } 
	  }catch (Exception e) {
		  e.printStackTrace();
	  } finally {
	    if (canvas != null)
	      holder.unlockCanvasAndPost(canvas);
	  }
	  handler.removeCallbacks(drawRunner);
	  if (visible) {
	    handler.postDelayed(drawRunner, 10);
	  }
	
	
	}
	
	// Surface view requires that all elements are drawn completely
	private void drawCircles(Canvas canvas, List<MyPoint> circles) {
			canvas.drawBitmap(MainActivity.bitmap, 0, 0, null);

	  int index = 0;
	  for (MyPoint point : circles) {
		  index = index %colorArray.length;
		  paint.setColor(colorArray[index]);
		  index++;
	    canvas.drawCircle(point.x, point.y, 50.0f, paint);
	  }
	}
} 
  }