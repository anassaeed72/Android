package com.anassaeed.simple;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadCastReciever extends BroadcastReceiver {

@Override
public void onReceive(Context context, Intent intent) {
	Log.i(MainActivity.tag,"in braodcast receiver");
    if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
    	Log.i(MainActivity.tag,"off");
        //DO HERE
    } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
        //DO HERE
    	Log.i(MainActivity.tag,"On");
    }
}
}
