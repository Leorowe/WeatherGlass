package com.leo.rowe;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

			import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;

public class Weather4WallpaperActivity extends Activity {
    /** Called when the activity is first created. */
	  Handler h;
	 Runnable r;
	 private Calendar c=null;
	 private final static int Min=60;//version 0.2   Hour-Min 2011.11.21
	 SharedPreferences conf;
	 SharedPreferences.Editor editor;
	 int latitude;
	 int longitude;
	 URL url = null;
	 private AdView adView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //umeng api
        MobclickAgent.onError(this);
//        MobclickAgent.update(this);
        UMFeedbackService.enableNewReplyNotification(this, NotificationType.NotificationBar);
        MobclickAgent.updateOnlineConfig(this);
//        MobclickAgent.update(this);//version 0.3 update online
        
        												
        //google AdMob
        
        // Create the adView
        adView = new AdView(this, AdSize.BANNER, "a14f02bd94f1288");

        // Lookup your LinearLayout assuming it��s been given
        // the attribute android:id="@+id/mainLayout"
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);

        // Add the adView to it
        layout.addView(adView);

        // Initiate a generic request to load it with an ad
        adView.loadAd(new AdRequest());
        
        
        //test model
        // Initiate a generic request to load it with an ad
        AdRequest request = new AdRequest();
       // request.addTestDevice(AdRequest.TEST_EMULATOR);
        request.addTestDevice(""); 

        adView.loadAd(request);        
        
        
        //end google AdMob
		
        
        
        
        conf=getSharedPreferences("conf", MODE_PRIVATE);      
        Button stopHandlerBt=(Button)findViewById(R.id.StopHandler);
        stopHandlerBt.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				//Version 0.4 11.25 �޸������¿�����޷�ȡ��̬��ֽ���л�				
				editor=conf.edit();
				editor.putBoolean("refresh", false);
				editor.commit();
				//
                Intent intent = new Intent(Weather4WallpaperActivity.this,alarmReceiver.class);  
                PendingIntent pi = PendingIntent.getBroadcast(Weather4WallpaperActivity.this, 0, intent, 0);  
                AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);  
                am.cancel(pi);  
                Toast.makeText(Weather4WallpaperActivity.this,R.string.stopChange, Toast.LENGTH_SHORT).show();
				
			}
		});
        
        Button startBt=(Button)findViewById(R.id.changeWallpaper);
        startBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(judgeNetwork())
					Toast.makeText(Weather4WallpaperActivity.this, R.string.changeWallpaper,
							Toast.LENGTH_SHORT).show();		
				Intent intent1=new Intent(Weather4WallpaperActivity.this,alarmReceiver.class);
		                  // TODO Auto-generated method stub  
		                  c.setTimeInMillis(System.currentTimeMillis());  
		                  PendingIntent pi = PendingIntent.getBroadcast(Weather4WallpaperActivity.this, 0, intent1, 0);  
		                  AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);  
		                  am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
		                am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), (5*Min*1000), pi);//�ظ����� 
		                // am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), (1*Min*1000), pi);//�ظ����� 
		               //Version 0.4 11.25 �޸������¿�����޷�ȡ��̬��ֽ���л�				
		 				editor=conf.edit();
		 				editor.putBoolean("refresh", true);
		 				editor.commit();
		 				//
		                  System.out.println("alarm have been set! Refresh Time" +conf.getInt("refreshtime", 1));
			}
		});
        
        Button getBt=(Button)findViewById(R.id.getlocation);
        getBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(judgeNetwork()){
					final EditText cityName=new EditText(Weather4WallpaperActivity.this);
			        android.content.DialogInterface.OnClickListener cal = new DialogInterface.OnClickListener() {			
					 
			    		@Override
			    		public void onClick(DialogInterface dialog, int which) {
			    			switch (which) {
			    			case Dialog.BUTTON_NEGATIVE:
			    				Toast.makeText(Weather4WallpaperActivity.this, R.string.nolocate,
			    						Toast.LENGTH_SHORT).show();		  
			    				break;
			    			case Dialog.BUTTON_POSITIVE: 							    				
			    				try {
			    					String city=cityName.getText().toString();
			    					url = new URL("http://www.google.com/ig/api?weather="+URLEncoder.encode(city));//version 0.2:���ӶԿո��ת�壬�޸������пո�ĳ�����new york)�޷���feed������
			    				} catch (MalformedURLException e1) {
			    					// TODO Auto-generated catch block
			    					e1.printStackTrace();
			    				}
			    				XMLPullHandler xpp=new XMLPullHandler(url);
			    				String weatherCondition=xpp.getWeather();
			    				if(weatherCondition!=null&&(weatherCondition.length()!=0)){
			    					System.out.println(weatherCondition);
			    					editor=conf.edit();
			    					editor.putString("city", cityName.getText().toString());
			    					editor.putString("way", "city");
			    					editor.commit();
			    					Toast.makeText(Weather4WallpaperActivity.this, "now "+cityName.getText().toString()+" weather is  "+weatherCondition,
				    						Toast.LENGTH_SHORT).show();
			    				}
			    				else
			    					Toast.makeText(Weather4WallpaperActivity.this,R.string.nolocate,
				    						Toast.LENGTH_SHORT).show();
		    			    	break;
			    			}
			    		}
			    	};
			        new AlertDialog.Builder(Weather4WallpaperActivity.this)
			            .setTitle(R.string.cityNameDialog).setView(cityName)
			        	.setPositiveButton(R.string.submit, cal)
			            .setNegativeButton(R.string.cancel, cal).show();	
			    	
				}
			}
		});
        
        Button aboutMe=(Button)findViewById(R.id.setRefreshTime);//About Me button
        aboutMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 android.content.DialogInterface.OnClickListener aboutMe = new DialogInterface.OnClickListener() {			
					 
			    		@Override
			    		public void onClick(DialogInterface dialog, int which) {
			    			switch (which) {
			    			case Dialog.BUTTON_NEGATIVE:
			    				break;
			    			}
			    		}
			    	};
			    	
				TextView aboutmeText=new TextView(Weather4WallpaperActivity.this);
		    	aboutmeText.setText(R.string.AboutContent1);//���Ӱ汾��ʶ
        	   new AlertDialog.Builder(Weather4WallpaperActivity.this)
	            .setTitle(R.string.AboutMeDialogTitle).setView(aboutmeText)
	            .setNegativeButton(R.string.cancel, aboutMe).show();			
			}
		});    
}
    public void onStart(){
    	super.onStart();
    	c=Calendar.getInstance();
    }
    public boolean judgeNetwork(){
    	ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);   
    	NetworkInfo info = cwjManager.getActiveNetworkInfo();   
    	if (info != null && info.isAvailable()){   
    	//do  
    		return true;
    	}   
    	else  
    	{  
    		System.out.println("there is no network!");
    		Toast.makeText(Weather4WallpaperActivity.this, R.string.openNetwork, Toast.LENGTH_SHORT).show();
    		return false;
    	}  
    }
    
    public void onResume(){
    	super.onResume();
    	 MobclickAgent.onResume(this);  
    	
    }
    public void onPause(){
    	super.onPause();
    	MobclickAgent.onPause(this);  
    }
    public void onDestory(){
    	super.onDestroy();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, 0, 0, R.string.Feedback); 
    menu.add(0,1,0,R.string.Exit);
    menu.add(0,2,9,R.string.AboutMeDialogTitle);
    return true;
    }
     
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
           case 0: 
        	   	UMFeedbackService.openUmengFeedbackSDK(this);
    			return true;
           case 1:
        	    this.finish();
        	    return true;
           case 2:
        	   android.content.DialogInterface.OnClickListener aboutMe = new DialogInterface.OnClickListener() {			
					 
		    		@Override
		    		public void onClick(DialogInterface dialog, int which) {
		    			switch (which) {
		    			case Dialog.BUTTON_NEGATIVE:
		    				break;
		    			}
		    		}
		    	};
		    	
		    	TextView aboutmeText=new TextView(Weather4WallpaperActivity.this);
		    	aboutmeText.setText(R.string.AboutContent1);//���Ӱ汾��ʶ
        	   new AlertDialog.Builder(Weather4WallpaperActivity.this)
	            .setTitle(R.string.AboutMeDialogTitle).setView(aboutmeText)
	            .setNegativeButton(R.string.cancel, aboutMe).show();			
           default:
        	   return true;
     }
     }

}