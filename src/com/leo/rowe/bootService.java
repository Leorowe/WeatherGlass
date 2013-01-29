package com.leo.rowe;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class bootService extends Service {

	private Calendar c=null;
	private int Min=60;
	SharedPreferences conf;
	SharedPreferences.Editor editor;
	
	public void onCreate(){
		c=Calendar.getInstance();
		System.out.println("bootService onCreate()!");
	    conf=getSharedPreferences("conf", MODE_PRIVATE);
		
	}
	public void onStart(Intent intent,int startId){
		super.onStart(intent, startId);
		System.out.println("bootService onStart!");
		Intent intent1=new Intent(bootService.this,alarmReceiver.class);
                  // TODO Auto-generated method stub  
					
                  c.setTimeInMillis(System.currentTimeMillis());  
                  PendingIntent pi = PendingIntent.getBroadcast(bootService.this, 0, intent1, 0);  
                  AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);  
                  //@todo conf 
                  if(conf.getBoolean("refresh", true)){
                  am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
                 // am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), (15*Min*1000), pi);
                  am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), (5*Min*1000), pi);
                  }
                  else{
                	 am.cancel(pi);
                  }
	}
	
	public void onDestory(){
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
