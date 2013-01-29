package com.leo.rowe;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class alarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent i=new Intent(context,changeWallpaperService.class);
		context.startService(i);
	}

}
