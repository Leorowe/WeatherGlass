package com.leo.rowe;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Timer;

import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class changeWallpaperService extends Service{

	WallpaperManager wm;
	Calendar c;
	XMLPullHandler xmlPullHandler;
	String weather;
	URL url;
	int wallpaperAddress;
	SharedPreferences conf;
	SharedPreferences.Editor editor;
	int hour;
	int longitude=0,
		latitude=0;

	
	 Bundle b=new Bundle() ;
	
	public void onCreate(){
		super.onCreate();
			
		System.out.println("Service have been launched!");
		wm=WallpaperManager.getInstance(this);
		conf=getSharedPreferences("conf", MODE_PRIVATE); //initial conf.xml
		// Put elements to the map 
		
			//fog
			b.putInt("Fog", R.drawable.fog);
			b.putInt("Smoke",R.drawable.fog);//Version 0.6 1.4.2012
			b.putInt("Haze",R.drawable.fog);//Version 0.7 1.8.2012
		
			//sun
			b.putInt("Clear", R.drawable.sun);
			
			//cloud
			b.putInt("Mostly Cloudy", R.drawable.overcast);
			b.putInt("Mostly Sunny",R.drawable.msun);
			b.putInt("Partly Sunny",R.drawable.psun);
			b.putInt("Partly Cloudy",R.drawable.partlycloud);
			b.putInt("Cloudy", R.drawable.overcast);
			b.putInt("Overcast", R.drawable.oc);	//version 0.3
			
			//snow
			b.putInt("Snow",R.drawable.snow);
			b.putInt("Light snow",R.drawable.snow);
	}
	public void onStart(Intent intent,int startId){
		
		super.onStart(intent, startId);
		c=Calendar.getInstance();	//version 0.2  ������ˢ�µ����⣬ԭ��д��onCreate�
		hour=c.get(Calendar.HOUR_OF_DAY);
		System.out.println("hour:"+hour);
		System.out.println("begin change wallpaper");
		//write in SharePointFile
		
	if(hour>=17||hour<=5){
		//rain
		b.putInt("Rain",R.drawable.rainnight);
		b.putInt("Light rain",R.drawable.rainnight);
		b.putInt("Drizzle", R.drawable.rainnight);//Version 0.2 11.12.2011
		b.putInt("Thunderstorm",R.drawable.rainnight);//Version 0.6 add ThunderRain
	}else{
		//rain
		b.putInt("Rain",R.drawable.rain);
		b.putInt("Light rain",R.drawable.rain8);
		b.putInt("Drizzle", R.drawable.rain);//Version 0.2 11.12.2011
		b.putInt("Thunderstorm",R.drawable.thunderrain);//Version 0.6 add ThunderRain
	}
		

	 if(judgeNetwork()){	
		//try to get the weather which in user location
		try {
		 UMFeedbackService.enableNewReplyNotification(this, NotificationType.NotificationBar);
				url = new URL("http://www.google.com/ig/api?hl=en&weather="+conf.getString("city", "beijing"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		xmlPullHandler=new XMLPullHandler(url);
		weather=xmlPullHandler.getWeather();
		if(b.get(weather)!=null){
			if(!(weather.equals("Rain")||weather.equals("Light rain")||weather.equals("Drizzle")||weather.equals("Thunderstorm"))){
				//0.7 fix bug:wrong way to compare with String type  should use method .equals,instead of ==
				if(hour==17){	
					try {
						wm.setResource(R.drawable.nightfall);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (hour==18){
					try {
						
						wm.setResource(R.drawable.night18);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (hour==19){
					try {
						
						wm.setResource(R.drawable.n19);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (hour>=20&&hour<23){//version 0.4 �޸�bug�����Ĵ�����||��Ϊ&&
					try {
						
						wm.setResource(R.drawable.n20);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(hour>=23||hour<=5){
					try {
						wm.setResource(R.drawable.midnight);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					wallpaperAddress = b.getInt(weather);

					try {
						wm.setResource(wallpaperAddress);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
		
			//	if (hour > 5 && hour < 17) {
					wallpaperAddress = b.getInt(weather);

					try {
						wm.setResource(wallpaperAddress);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			//	}// if(hour>5&&hour<17)
			}
			
		}
		
	}
		
		
		
}
		
	
	
	public void onDestory(){
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	 public boolean judgeNetwork(){
	    	ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);   
	    	NetworkInfo info = cwjManager.getActiveNetworkInfo();   
	    	if (info != null && info.isAvailable()){   
	    		return true;
	    	}   
	    	else  
	    	{  
	    		Toast.makeText(changeWallpaperService.this, R.string.openNetwork, Toast.LENGTH_SHORT).show();
	    		System.out.println("there is no network!");
	    		return false;
	    	}  
	    }
	

}
