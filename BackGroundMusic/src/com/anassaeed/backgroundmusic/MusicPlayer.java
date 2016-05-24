package com.anassaeed.backgroundmusic;

import android.app.Activity;
import android.media.MediaPlayer;

public class MusicPlayer extends Activity{
    
	MediaPlayer mySound =MediaPlayer.create(MusicPlayer.this, R.raw.a );
	void playSong(){
		mySound.start();
	}
	void stopPlaying(){
		mySound.stop();
	}
}
