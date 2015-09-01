package com.terence;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class ReminderManager{
	private Context mContext;
	private AlarmManager mAlarmManager;
	public ReminderManager(Context context){
		mContext = context;
		mAlarmManager = 
				(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	}
	
	
	public void setReminder(){
		
		long nextUpdate = 60*30*1000;
		Intent intent = new Intent(mContext, CheckNewMessageService.class);
		PendingIntent pintent = PendingIntent.getService(mContext, 0, intent, 0);
		
	    mAlarmManager.cancel(pintent); //cancel if active already
	    mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(),
	    		nextUpdate, pintent);
	}	
	
}


