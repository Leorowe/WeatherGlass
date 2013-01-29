package com.leo.rowe;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class bootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent i=new Intent(context,bootService.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(i);
	}

}
