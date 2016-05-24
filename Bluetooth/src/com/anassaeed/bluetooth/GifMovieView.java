package com.anassaeed.bluetooth;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

public class GifMovieView extends View{
	private Movie mMovie;
	private InputStream mStream;
	long mMoviestart;
    public GifMovieView(Context context, InputStream stream) {
        super(context);

        mStream = stream;
        mMovie = Movie.decodeStream(mStream);        
        
        
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
       canvas.drawColor(Color.TRANSPARENT);
       Log.i("test",""+canvas.getHeight() + "   " + canvas.getWidth());
       super.onDraw(canvas);
       final long now = SystemClock.uptimeMillis();
       if (mMoviestart == 0) {
          mMoviestart = now;
       }
       final int relTime = (int)((now - mMoviestart) % mMovie.duration());
       mMovie.setTime(relTime);
//       mMovie.
       mMovie.draw(canvas, 0, 0);
       this.invalidate();
    }
}
